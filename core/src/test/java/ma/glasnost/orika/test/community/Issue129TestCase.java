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

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.junit.Test;

/**
 * problem generic types when subclassing.
 *
 * <p>
 *
 * @see <a
 *     href="https://github.com/orika-mapper/orika/issues/129">https://github.com/orika-mapper/orika/issues</a>
 */
public class Issue129TestCase {

  @Test
  public void testCustomMapperOneParameter() {
    new StringConverter();
  }
}

abstract class AbstractStringConverter<T> extends BidirectionalConverter<T, String> {}

class StringConverter extends AbstractStringConverter<String> {

  @Override
  public String convertTo(
      String source, Type<String> destinationType, MappingContext mappingContext) {
    throw new UnsupportedOperationException(
        "Not supported yet."); // To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String convertFrom(
      String source, Type<String> destinationType, MappingContext mappingContext) {
    throw new UnsupportedOperationException(
        "Not supported yet."); // To change body of generated methods, choose Tools | Templates.
  }
}
