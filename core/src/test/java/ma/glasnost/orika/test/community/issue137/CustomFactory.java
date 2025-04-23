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

package ma.glasnost.orika.test.community.issue137;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;

import java.lang.reflect.Constructor;

/**
 * Created with IntelliJ IDEA. User: tgruenheit Date: 06.12.13 Time: 20:13 To change this template
 * use File | Settings | File Templates.
 */
public class CustomFactory<T> implements ObjectFactory<T> {

  public T create(Object o, MappingContext mappingContext) {

    // FIXME: While converting second LevelTwo object, resolvedDestinationType is LevelThree
    @SuppressWarnings("unchecked")
    Class<T> rawType = (Class<T>) mappingContext.getResolvedDestinationType().getRawType();

    try {

      Constructor<T> declaredConstructor = rawType.getDeclaredConstructor();
      declaredConstructor.setAccessible(true);

      return declaredConstructor.newInstance();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
