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

package ma.glasnost.orika.test.boundmapperfacade;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author: jad7 jad7kii@gmail.com
 * @since: 11/8/14
 */
public class SortedMapSetTestCase {

  private MapperFactory mapperFactory;
  private A a;

  @Before
  public void init() {
    // System.setProperty(MappingUtil.DISABLE_DEBUG_MODE, "false");
    mapperFactory = MappingUtil.getMapperFactory(true);
    a = new A();
    SortedSet<Integer> sortedSet = new TreeSet<Integer>(Arrays.asList(5, 7, 3, 4, 1));
    SortedMap<String, String> sortedMap = new TreeMap<String, String>();
    sortedMap.put("a", "a");
    sortedMap.put("e", "e");
    sortedMap.put("b", "b");
    sortedMap.put("r", "r");
    a.setIntegerSortedSet(sortedSet);
    a.setStringStringSortedMap(sortedMap);
  }

  @Test
  public void sortedTest() {
    mapperFactory.classMap(A.class, B.class).byDefault().register();

    B b = mapperFactory.getMapperFacade(A.class, B.class).map(a);
    SortedSet<Integer> integerSortedSetResult = b.getIntegerSortedSet();
    SortedMap<String, String> stringStringSortedMapResult = b.getStringStringSortedMap();
    checkMapping(integerSortedSetResult, stringStringSortedMapResult);
  }

  @Test
  public void sortedByConstructor() {
    mapperFactory
        .classMap(A.class, C.class)
        .constructorB("integerSortedSet", "stringStringSortedMap")
        .fieldMap("integerSortedSet", "integerSortedSet")
        .exclude()
        .add()
        .fieldMap("stringStringSortedMap", "stringStringSortedMap")
        .exclude()
        .add()
        .register();

    C map = mapperFactory.getMapperFacade(A.class, C.class).map(a);
    checkMapping(map.getIntegerSortedSet(), map.getStringStringSortedMap());
  }

  private void checkMapping(
      SortedSet<Integer> integerSortedSetResult,
      SortedMap<String, String> stringStringSortedMapResult) {
    Assert.assertFalse(integerSortedSetResult == a.getIntegerSortedSet());
    Assert.assertFalse(stringStringSortedMapResult == a.getStringStringSortedMap());

    Assert.assertEquals(a.getIntegerSortedSet().size(), integerSortedSetResult.size());
    for (Iterator<Integer> aSetIterator = a.getIntegerSortedSet().iterator(),
            bSetIterator = integerSortedSetResult.iterator();
        aSetIterator.hasNext() && bSetIterator.hasNext(); ) {
      Integer aInteger = aSetIterator.next();
      Integer bInteger = bSetIterator.next();

      Assert.assertEquals(aInteger, bInteger);
    }

    Assert.assertEquals(a.getStringStringSortedMap().size(), stringStringSortedMapResult.size());
    for (Iterator<String> aMapIterator = a.getStringStringSortedMap().keySet().iterator(),
            bMapIterator = stringStringSortedMapResult.keySet().iterator();
        aMapIterator.hasNext() && bMapIterator.hasNext(); ) {
      String aString = aMapIterator.next();
      String bString = bMapIterator.next();
      Assert.assertEquals(aString, bString);
    }
  }

  public static class A {
    private SortedSet<Integer> integerSortedSet;
    private SortedMap<String, String> stringStringSortedMap;

    public SortedSet<Integer> getIntegerSortedSet() {
      return integerSortedSet;
    }

    public void setIntegerSortedSet(SortedSet<Integer> integerSortedSet) {
      this.integerSortedSet = integerSortedSet;
    }

    public SortedMap<String, String> getStringStringSortedMap() {
      return stringStringSortedMap;
    }

    public void setStringStringSortedMap(SortedMap<String, String> stringStringSortedMap) {
      this.stringStringSortedMap = stringStringSortedMap;
    }
  }

  public static class B {
    private SortedSet<Integer> integerSortedSet;
    private SortedMap<String, String> stringStringSortedMap;

    public SortedSet<Integer> getIntegerSortedSet() {
      return integerSortedSet;
    }

    public void setIntegerSortedSet(SortedSet<Integer> integerSortedSet) {
      this.integerSortedSet = integerSortedSet;
    }

    public SortedMap<String, String> getStringStringSortedMap() {
      return stringStringSortedMap;
    }

    public void setStringStringSortedMap(SortedMap<String, String> stringStringSortedMap) {
      this.stringStringSortedMap = stringStringSortedMap;
    }
  }

  public static class C {
    private SortedSet<Integer> integerSortedSet;
    private SortedMap<String, String> stringStringSortedMap;
    public C(SortedSet<Integer> integerSortedSet, SortedMap<String, String> stringStringSortedMap) {
      this.integerSortedSet = integerSortedSet;
      this.stringStringSortedMap = stringStringSortedMap;
    }

    public SortedSet<Integer> getIntegerSortedSet() {
      return integerSortedSet;
    }

    public SortedMap<String, String> getStringStringSortedMap() {
      return stringStringSortedMap;
    }
  }
}
