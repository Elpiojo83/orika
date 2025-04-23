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

package ma.glasnost.orika.test.community.issue135;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Issue135Test {

  private MapperFacade mapper;

  @Before
  public void setup() {
    MapperFactory mapperFactory = MappingUtil.getMapperFactory();
    mapperFactory
        .classMap(Domain.class, Representation.class)
        .mapNulls(true)
        .mapNullsInReverse(true)
        .field("subB", "repA.repB") // this causes NPE if repA is null
        .field("active", "repA.active")
        .field("primitive", "repA.primitive")
        .register();

    mapper = mapperFactory.getMapperFacade();
  }

  @Test
  public void testCase() {
    Domain src = new Domain();
    // throws NPE
    Representation target = mapper.map(src, Representation.class);
    Assert.assertNotNull(target);
  }
}
