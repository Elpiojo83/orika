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

package ma.glasnost.orika.test.optional;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.OptionalConverter;
import ma.glasnost.orika.metadata.TypeFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OptionalTestCase {

  @Test
  public void testMappingIgnoresEmptyOptionalInDestination() {
    final String expected = "initial";

    final Source source = new Source();
    source.setS(Optional.of(expected));

    final Destination actual = getMapperFacade().map(source, Destination.class);

    assertEquals(expected, actual.getS().get());
  }

  @Test
  public void testMappingMapEmptyToEmpty() {
    final Destination actual = getMapperFacade().map(new Source(), Destination.class);

    assertFalse(actual.getS().isPresent());
  }

  private MapperFacade getMapperFacade() {
    final MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
    mapperFactory
        .getConverterFactory()
        .registerConverter(
            new OptionalConverter<>(
                TypeFactory.valueOf(String.class), TypeFactory.valueOf(String.class)));
    return mapperFactory.getMapperFacade();
  }

  public static class Source {
    private Optional<String> s = Optional.empty();

    public Optional<String> getS() {
      return s;
    }

    public void setS(final Optional<String> s) {
      this.s = s;
    }
  }

  public static class Destination {
    private Optional<String> s = Optional.empty();

    public Optional<String> getS() {
      return s;
    }

    public void setS(final Optional<String> s) {
      this.s = s;
    }
  }
}
