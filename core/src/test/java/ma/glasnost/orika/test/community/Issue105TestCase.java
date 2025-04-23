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

package ma.glasnost.orika.test.community;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Circular reference mapping with classes that extend an abstract class fail.
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/143">https://code.google.com/archive/p/orika/</a>
 * @see <a
 *     href="https://github.com/orika-mapper/orika/issues/105">https://github.com/orika-mapper/orika/issues</a>
 */
public class Issue105TestCase {
  @Test
  public void test() {
    MapperFactory factory = new DefaultMapperFactory.Builder().build();

    factory
        .classMap(Entity.class, BaseEntity.class)
        .field("anotherEntity", "anotherBaseEntity")
        .register();
    factory
        .classMap(AnotherEntity.class, AnotherBaseEntity.class)
        .field("abstractEntity", "abstractBaseEntity")
        .register();

    MapperFacade mapperFacade = factory.getMapperFacade();

    Entity entity = new Entity();

    AnotherEntity anotherEntity = new AnotherEntity();
    anotherEntity.setAbstractEntity(entity);

    entity.setAnotherEntity(anotherEntity);

    AnotherBaseEntity anotherBaseEntity = mapperFacade.map(anotherEntity, AnotherBaseEntity.class);
    Assert.assertEquals(
        anotherEntity, ((Entity) anotherEntity.getAbstractEntity()).getAnotherEntity());
    Assert.assertEquals(
        anotherBaseEntity,
        ((BaseEntity) anotherBaseEntity.getAbstractBaseEntity()).getAnotherBaseEntity());

    BaseEntity baseEntity1 = mapperFacade.map(entity, BaseEntity.class);
    Assert.assertEquals(entity, entity.getAnotherEntity().getAbstractEntity());
    Assert.assertEquals(baseEntity1, baseEntity1.getAnotherBaseEntity().getAbstractBaseEntity());

    BaseEntity baseEntity2 = (BaseEntity) mapperFacade.map(entity, AbstractBaseEntity.class);
    Assert.assertEquals(entity, entity.getAnotherEntity().getAbstractEntity());
    Assert.assertEquals(baseEntity2, baseEntity2.getAnotherBaseEntity().getAbstractBaseEntity());
  }

  public abstract static class AbstractEntity {
    AnotherEntity anotherEntity;

    public AnotherEntity getAnotherEntity() {
      return this.anotherEntity;
    }

    public void setAnotherEntity(AnotherEntity anotherEntity) {
      this.anotherEntity = anotherEntity;
    }
  }

  public static class Entity extends AbstractEntity {}

  public static class AnotherEntity {
    AbstractEntity abstractEntity;

    public AbstractEntity getAbstractEntity() {
      return this.abstractEntity;
    }

    public void setAbstractEntity(AbstractEntity abstractEntity) {
      this.abstractEntity = abstractEntity;
    }
  }

  public static class AbstractBaseEntity {
    AnotherBaseEntity anotherBaseEntity;

    public AnotherBaseEntity getAnotherBaseEntity() {
      return this.anotherBaseEntity;
    }

    public void setAnotherBaseEntity(AnotherBaseEntity anotherBaseEntity) {
      this.anotherBaseEntity = anotherBaseEntity;
    }
  }

  public static class BaseEntity extends AbstractBaseEntity {}

  public static class AnotherBaseEntity {
    AbstractBaseEntity abstractBaseEntity;

    public AbstractBaseEntity getAbstractBaseEntity() {
      return this.abstractBaseEntity;
    }

    public void setAbstractBaseEntity(AbstractBaseEntity abstractBaseEntity) {
      this.abstractBaseEntity = abstractBaseEntity;
    }
  }
}
