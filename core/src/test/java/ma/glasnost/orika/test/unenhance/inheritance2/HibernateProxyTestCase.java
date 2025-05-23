/*
 * Orika - simpler, better and faster Java bean mapping
 *
 *  Copyright (C) 2011-2019 Orika authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ma.glasnost.orika.test.unenhance.inheritance2;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.unenhance.HibernateUnenhanceStrategy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** @author Sergey Vasilyev */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:HibernateProxyTestCase-context.xml")
@Transactional
@DirtiesContext
public class HibernateProxyTestCase {

  private Serializable roomId;
  private Serializable peterId;
  private Serializable johnId;

  @Autowired private SessionFactory sessionFactory;

  protected Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  @Before
  public void setup() {
    RoomEntity room = new RoomEntity();

    Set<PersonEntity> tenants = new HashSet<PersonEntity>();

    OwnerEntity john = new OwnerEntity();
    john.setName("John");
    johnId = getSession().save(john);
    tenants.add(john);

    PersonEntity peter = new OwnerEntity();
    peter.setName("Peter");
    peterId = getSession().save(peter);
    tenants.add(peter);

    room.setTenants(tenants);

    roomId = getSession().save(room);

    getSession().flush();
    getSession().clear();
  }

  private MapperFacade buildMapper() {
    MapperFactory mf =
        new DefaultMapperFactory.Builder()
            .unenhanceStrategy(new HibernateUnenhanceStrategy())
            .build();
    mf.registerClassMap(
        mf.classMap(AbstractEntity.class, AbstractDto.class).byDefault().toClassMap());
    mf.registerClassMap(
        mf.classMap(OwnerEntity.class, OwnerDto.class)
            .use(PersonEntity.class, PersonDto.class)
            .byDefault()
            .toClassMap());
    mf.registerClassMap(
        mf.classMap(PersonEntity.class, PersonDto.class)
            .use(AbstractEntity.class, AbstractDto.class)
            .byDefault()
            .toClassMap());
    mf.registerClassMap(
        mf.classMap(RoomEntity.class, RoomDto.class)
            .use(AbstractEntity.class, AbstractDto.class)
            .byDefault()
            .toClassMap());
    return mf.getMapperFacade();
  }

  @Test
  public void testInheritance() {
    MapperFacade mapper = buildMapper();

    OwnerEntity john = (OwnerEntity) getSession().load(OwnerEntity.class, johnId);
    PersonEntity peter = (PersonEntity) getSession().load(PersonEntity.class, peterId);

    Assert.assertEquals("John", john.getName());
    Assert.assertEquals("Peter", peter.getName());

    RoomEntity room = (RoomEntity) getSession().load(RoomEntity.class, roomId);

    RoomDto roomDto = mapper.map(room, RoomDto.class);
    Assert.assertNotNull(roomDto);
    // Iterator<PersonDto> tenantsIter = roomDto.getTenants().iterator();
    // Assert.assertEquals("John",tenantsIter.next().getName());
    // Assert.assertEquals("Peter",tenantsIter.next().getName());
  }
}
