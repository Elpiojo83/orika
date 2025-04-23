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

package ma.glasnost.orika.test.filters;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author: Ilya Krokhmalyov
 * @email jad7kii@gmail.com
 * @since: 5/6/14
 */
public class SourceNestedFilterTest {

  @Test
  public void test() {
    MapperFactory mapperFactory =
        new DefaultMapperFactory.Builder().captureFieldContext(true).build();

    mapperFactory
        .classMap(A_Source.class, A_Destination.class)
        .field("a1_source.id", "id")
        .register();

    A_Source a_source = new A_Source();
    a_source.a1_source = new A1_Source();
    a_source.a1_source.id = 10;

    A_Destination map = mapperFactory.getMapperFacade().map(a_source, A_Destination.class);
    Assert.assertEquals(map.getId(), Integer.valueOf(10));
  }

  public static class A_Source {
    private A1_Source a1_source;

    public A1_Source getA1_source() {
      return a1_source;
    }

    public void setA1_source(A1_Source a1_source) {
      this.a1_source = a1_source;
    }
  }

  public static class A1_Source {
    private Integer id;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }
  }

  public static class A_Destination {
    private Integer id;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }
  }
}
