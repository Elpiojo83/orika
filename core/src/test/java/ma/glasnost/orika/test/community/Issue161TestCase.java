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

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

/**
 * Unable to map class with self-referencing generics and Comparable return type.
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/161">https://code.google.com/archive/p/orika/</a>
 */
public class Issue161TestCase {

  @Test
  public void mapSelfReferencingGenericType() {
    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    factory.classMap(Foo.class, String.class).byDefault().register();
  }

  public static class Optional<T> {}

  public static class Range<C extends Comparable<C>> {}

  public static class SelfReferencingGenericType<T extends SelfReferencingGenericType<T>> {
    public Optional<Range<String>> getOptionalStringRange() {
      return null;
    }
  }

  public static class Foo extends SelfReferencingGenericType<Foo> {}
}
