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

package ma.glasnost.orika.impl;

import ma.glasnost.orika.MapEntry;

import java.util.*;

public class DefaultConcreteTypeMap {

  private static final Map<Class, Class> map;

  static {
    Map<Class, Class> tmpMap = new HashMap<Class, Class>();
    tmpMap.put(Collection.class, ArrayList.class);
    tmpMap.put(List.class, ArrayList.class);
    tmpMap.put(Set.class, LinkedHashSet.class);
    tmpMap.put(Map.class, LinkedHashMap.class);
    tmpMap.put(Map.Entry.class, MapEntry.class);
    tmpMap.put(SortedMap.class, TreeMap.class);
    tmpMap.put(SortedSet.class, TreeSet.class);
    map = Collections.unmodifiableMap(tmpMap);
  }

  public static Set<Map.Entry<Class, Class>> getAll() {
    return map.entrySet();
  }

  public static Class get(Class<?> type) {
    return map.get(type);
  }
}
