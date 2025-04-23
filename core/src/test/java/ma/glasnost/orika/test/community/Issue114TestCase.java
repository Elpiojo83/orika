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

import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import java.util.List;

/**
 * NPE on mapNulls in 1.4.3.
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/114">https://code.google.com/archive/p/orika/</a>
 */
public class Issue114TestCase {

  @Test
  public void test() {

    DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    mapperFactory
        .classMap(Class1.class, Class1Binding.class)
        .field("longs", "class2.longs")
        .byDefault()
        .register();

    Class1 class1 = new Class1();
    Class1Binding class1Binding =
        mapperFactory.getMapperFacade(Class1.class, Class1Binding.class).map(class1);

    System.out.println(class1Binding);
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static class Class1 {
    private List<Long> longs;

    public List<Long> getLongs() {
      return longs;
    }

    public void setLongs(List<Long> longs) {
      this.longs = longs;
    }
  }

  public static class Class1Binding {
    private Class2Binding class2;

    public Class2Binding getClass2() {
      return class2;
    }

    public void setClass2(Class2Binding class2) {
      this.class2 = class2;
    }

    @Override
    public String toString() {
      return "Class1Binding{" + "class2=" + class2 + '}';
    }
  }

  public static class Class2Binding {
    private List<Long> longs;

    public List<Long> getLongs() {
      return longs;
    }

    public void setLongs(List<Long> longs) {
      this.longs = longs;
    }

    @Override
    public String toString() {
      return "Class2Binding{" + "longs=" + longs + '}';
    }
  }
}
