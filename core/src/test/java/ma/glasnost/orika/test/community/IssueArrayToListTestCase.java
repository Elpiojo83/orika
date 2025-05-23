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

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class IssueArrayToListTestCase {

  static {
    // System.setProperty(OrikaSystemProperties.COMPILER_STRATEGY,
    // EclipseJdtCompilerStrategy.class.getName());
  }

  @Test
  public void testStringArrayToListOfString() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    A a = new A();
    a.setStrings(new String[] {"4"});

    B b = mapperFacade.map(a, B.class);
    assertEquals(asList("4"), b.getStrings());
  }

  @Test
  public void testListOfStringToStringArray() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    B b = new B();
    b.setStrings(asList("5"));

    A a = mapperFacade.map(b, A.class);
    assertArrayEquals(new String[] {"5"}, a.getStrings());
  }

  @Test
  public void testIntArrayToListOfInteger() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    mapperFactory.classMap(A.class, B.class).field("ints", "integers").byDefault().register();

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    A a = new A();
    a.setInts(new int[] {4});

    B b = mapperFacade.map(a, B.class);
    assertNotNull(b.getIntegers());
    assertEquals(1, b.getIntegers().size());
    assertEquals(Integer.class, b.getIntegers().get(0).getClass());
    assertEquals(Integer.valueOf(4), b.getIntegers().get(0));
  }

  @Test
  public void testListOfIntegerToIntArray() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    mapperFactory.classMap(A.class, B.class).field("ints", "integers").byDefault().register();

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    B b = new B();
    b.setIntegers(asList(Integer.valueOf(6)));

    A a = mapperFacade.map(b, A.class);
    assertArrayEquals(new int[] {6}, a.getInts());
  }

  @Test
  public void testIntegerArrayToListOfInteger() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    A a = new A();
    a.setIntegers(new Integer[] {4});

    B b = mapperFacade.map(a, B.class);
    assertNotNull(b.getIntegers());
    assertEquals(1, b.getIntegers().size());
    assertEquals(Integer.class, b.getIntegers().get(0).getClass());
    assertEquals(Integer.valueOf(4), b.getIntegers().get(0));
  }

  @Test
  public void testListOfIntegerToIntegerArray() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    B b = new B();
    b.setIntegers(asList(Integer.valueOf(7)));

    A a = mapperFacade.map(b, A.class);
    assertArrayEquals(new Integer[] {7}, a.getIntegers());
  }

  public static class A {
    String[] strings;

    int[] ints;

    Integer[] integers;

    public String[] getStrings() {
      return strings;
    }

    public void setStrings(String[] strings) {
      this.strings = strings;
    }

    public int[] getInts() {
      return ints;
    }

    public void setInts(int[] ints) {
      this.ints = ints;
    }

    public Integer[] getIntegers() {
      return integers;
    }

    public void setIntegers(Integer[] integers) {
      this.integers = integers;
    }
  }

  public static class B {
    List<String> strings;

    List<Integer> integers;

    public List<String> getStrings() {
      return strings;
    }

    public void setStrings(List<String> strings) {
      this.strings = strings;
    }

    public List<Integer> getIntegers() {
      return integers;
    }

    public void setIntegers(List<Integer> integers) {
      this.integers = integers;
    }
  }
}
