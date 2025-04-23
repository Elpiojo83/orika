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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapping to Map&lt;String, List&lt;String&gt;&gt; only maps keys. Map&lt;String,
 * ArrayList&lt;String&gt;&gt; works..
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/128">https://code.google.com/archive/p/orika/</a>
 */
public class Issue128TestCase {

  private A createA() {
    A a = new A();
    a.x = new HashMap<String, List<String>>();
    a.x.put("key1", new ArrayList<String>());
    a.x.get("key1").add("value1a");
    a.x.get("key1").add("value1b");
    a.x.get("key1").add("value1c");
    a.x.put("key2", new ArrayList<String>());
    a.x.get("key2").add("value2a");
    a.x.get("key2").add("value2b");
    a.x.get("key2").add("value2c");
    return a;
  }

  @Test
  public void testGenericList() {

    MapperFactory factory = MappingUtil.getMapperFactory(true);
    MapperFacade mapper = factory.getMapperFacade();

    A a = createA();

    B1 b1 = mapper.map(a, B1.class);
    Assert.assertEquals(a.x, b1.x);
  }

  @Test
  public void testConcreteList() {

    MapperFactory factory = MappingUtil.getMapperFactory(true);
    MapperFacade mapper = factory.getMapperFacade();

    A a = createA();

    B2 b2 = mapper.map(a, B2.class);
    Assert.assertEquals(a.x, b2.x);
  }

  public static class A {
    public Map<String, List<String>> x;
  }

  public static class B1 {
    public Map<String, List<String>> x;
  }

  public static class B2 {
    public Map<String, ArrayList<String>> x;
  }
}
