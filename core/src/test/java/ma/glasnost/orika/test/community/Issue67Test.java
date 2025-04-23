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

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * NPE on VariableRef.isPrimitive() with map of map.
 *
 * <p>
 *
 * @see <a
 *     href="https://github.com/orika-mapper/orika/issues/67">https://github.com/orika-mapper/orika/issues</a>
 */
public class Issue67Test {

  private static final Date A_DATE = new Date();
  private MapperFactory mapperFactory;

  @Before
  public void setUp() throws Exception {
    mapperFactory = new DefaultMapperFactory.Builder().build();
    mapperFactory
        .classMap(DateToIntegerToStringMap.class, DateToIntegerToStringMap.class)
        .byDefault()
        .register();
  }

  @Test
  public void clone_a_map_of_map() {
    DateToIntegerToStringMap original = new DateToIntegerToStringMap();
    Map<Integer, String> integerStringMap = new HashMap<Integer, String>();
    integerStringMap.put(5, "five");
    Map<Date, Map<Integer, String>> dateMapMap = new HashMap<Date, Map<Integer, String>>();
    dateMapMap.put(A_DATE, integerStringMap);
    original.setDateIntegerStringMap(dateMapMap);

    DateToIntegerToStringMap copy =
        mapperFactory.getMapperFacade().map(original, DateToIntegerToStringMap.class);

    Map<Integer, String> nestedMap = copy.getDateIntegerStringMap().get(A_DATE);
    assertNotNull(nestedMap);
    assertEquals("five", nestedMap.get(5));
  }

  public static class DateToIntegerToStringMap {

    private Map<Date, Map<Integer, String>> dateIntegerStringMap;

    public Map<Date, Map<Integer, String>> getDateIntegerStringMap() {
      return dateIntegerStringMap;
    }

    public void setDateIntegerStringMap(Map<Date, Map<Integer, String>> dateIntegerStringMap) {
      this.dateIntegerStringMap = dateIntegerStringMap;
    }
  }
}
