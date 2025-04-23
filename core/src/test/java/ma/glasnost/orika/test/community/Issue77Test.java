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
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Invalid code generated when embedded field name not a valid java variable name.
 *
 * <p>
 *
 * @see <a
 *     href="https://github.com/orika-mapper/orika/issues/77">https://github.com/orika-mapper/orika/issues</a>
 */
public class Issue77Test {

  @Test
  public void map_with_keys_containing_invalid_characters_for_a_variable_instantiation() {
    MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);

    mapperFactory.classMap(A.class, B.class).field("mapSource['foo//bar']", "targetSet").register();

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    Map<String, List<String>> mapSource = new HashMap<>();
    mapSource.put("foo//bar", asList("one", "two"));
    A source = new A();
    source.setMapSource(mapSource);

    B map1 = mapperFacade.map(source, B.class);

    Assert.assertEquals(Set.of("one", "two"), map1.getTargetSet());
  }

  public static class A {
    private Map<String, List<String>> mapSource;

    public Map<String, List<String>> getMapSource() {
      return mapSource;
    }

    public void setMapSource(Map<String, List<String>> mapSource) {
      this.mapSource = mapSource;
    }
  }

  public static class B {
    private Set<String> targetSet;

    public Set<String> getTargetSet() {
      return targetSet;
    }

    public void setTargetSet(Set<String> targetSet) {
      this.targetSet = targetSet;
    }
  }
}
