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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Orika maps enum to enum, but not List&lt;enum&gt; to List&lt;enum&gt;.
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/71">https://code.google.com/archive/p/orika/</a>
 */
public class Issue71TestCase {

  @Test
  public void testEnumListMap() {
    final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    final MapperFacade mapper = mapperFactory.getMapperFacade();

    final A a = new A();
    final List<MyEnum> myEnumList = new ArrayList<MyEnum>();
    myEnumList.add(MyEnum.foo);
    a.setMyEnum(myEnumList);
    mapper.map(a, A2.class);
  }

  public static enum MyEnum {
    foo,
    bar
  }

  public static enum MyEnum2 {
    foo,
    bar
  }

  public static class A {
    private List<MyEnum> myEnum;

    public List<MyEnum> getMyEnum() {
      return myEnum;
    }

    public void setMyEnum(final List<MyEnum> myEnum) {
      this.myEnum = myEnum;
    }
  }

  public static class A2 {
    private List<MyEnum2> myEnum;

    public List<MyEnum2> getMyEnum() {
      return myEnum;
    }

    public void setMyEnum(final List<MyEnum2> myEnum) {
      this.myEnum = myEnum;
    }
  }
}
