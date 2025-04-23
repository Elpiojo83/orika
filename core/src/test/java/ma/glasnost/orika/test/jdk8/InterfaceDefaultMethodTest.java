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

package ma.glasnost.orika.test.jdk8;

import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class InterfaceDefaultMethodTest {

  @Test
  public void defaultInterfaceImplementationsTest() {
    DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    mapperFactory.classMap(A.class, B.class).byDefault().register();

    B b = mapperFactory.getMapperFacade().map(new A(), B.class);
    assertThat(b, notNullValue());
    assertThat(b.getId(), is("test"));
  }

  public static interface BaseA {

    default String getId() {
      return "test";
    }
  }

  public static class A implements BaseA {
    // inherited default methods from Interface
  }

  public static class B {
    String id;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }
}
