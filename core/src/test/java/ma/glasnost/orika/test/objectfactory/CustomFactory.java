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

package ma.glasnost.orika.test.objectfactory;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;

import java.lang.reflect.Constructor;

public class CustomFactory<T> implements ObjectFactory<T> {
  private Class<T> type;

  public CustomFactory(Class<T> type) {
    this.type = type;
  }

  public T create(Object o, MappingContext mappingContext) {
    try {
      Constructor<T> declaredConstructor = type.getDeclaredConstructor();
      declaredConstructor.setAccessible(true);
      return declaredConstructor.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
