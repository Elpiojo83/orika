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

package ma.glasnost.orika.test.community.issue121.aobjects;

import java.io.Serializable;
import java.util.*;

/**
 * @author: Ilya Krokhmalyov YC14IK1
 * @since: 8/23/13
 */
public abstract class AbstractOrderedMap<K, T> implements Iterable<T>, Serializable {

  protected final Map<K, T> resultSet = new LinkedHashMap<K, T>();

  protected AbstractOrderedMap(Map<K, T> map) {
    resultSet.putAll(map);
  }

  public Iterator<T> iterator() {
    return Collections.unmodifiableCollection(resultSet.values()).iterator();
  }

  public T get(K id) {
    return resultSet.get(id);
  }

  public T getSingle() {
    if (resultSet.size() == 1) {
      return resultSet.values().iterator().next();
    } else if (resultSet.size() == 0) {
      return null;
    } else {
      throw new IllegalStateException(
          "The operation is not allowed when collection size is greater then one");
    }
  }

  public Map<K, T> getMap() {
    return Collections.unmodifiableMap(resultSet);
  }

  public int size() {
    return resultSet.size();
  }

  public boolean containsId(K id) {
    return resultSet.containsKey(id);
  }

  public Set<K> idSet() {
    return Collections.unmodifiableSet(resultSet.keySet());
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" + "resultSet=" + resultSet + '}';
  }
}
