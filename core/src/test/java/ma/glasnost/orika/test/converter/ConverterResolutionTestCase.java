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

package ma.glasnost.orika.test.converter;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.BuiltinConverters;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * This test case is intended to assert the resolution behavior for converters with respect to the
 * order in which they are registered, and their relation to the source/destination types (w/respect
 * to class hierarchy).
 */
public class ConverterResolutionTestCase {

  @Test
  public void testResolveSingleConverter() {
    MapperFactory factory = MappingUtil.getMapperFactory();

    factory.getConverterFactory().registerConverter(new NameToStringConverter());

    Converter<?, ?> converter =
        factory
            .getConverterFactory()
            .getConverter(TypeFactory.valueOf(Name.class), TypeFactory.valueOf(String.class));

    Assert.assertEquals(NameToStringConverter.class, converter.getClass());
  }

  @Test
  public void testResolveMultipleConverters() {
    MapperFactory factory = MappingUtil.getMapperFactory();

    ConverterFactory converterFactory = factory.getConverterFactory();
    converterFactory.registerConverter(new ExtendedNameToStringConverter());
    converterFactory.registerConverter(new NameToStringConverter());

    Converter<?, ?> converter =
        converterFactory.getConverter(
            TypeFactory.valueOf(ExtendedName.class), TypeFactory.valueOf(String.class));

    Assert.assertEquals(ExtendedNameToStringConverter.class, converter.getClass());
  }

  @Test
  public void testResolveMultipleConvertersOutOfOrder() {
    MapperFactory factory = MappingUtil.getMapperFactory();

    ConverterFactory converterFactory = factory.getConverterFactory();
    converterFactory.registerConverter(new NameToStringConverter());
    converterFactory.registerConverter(new ExtendedNameToStringConverter());

    Converter<?, ?> converter =
        converterFactory.getConverter(
            TypeFactory.valueOf(ExtendedName.class), TypeFactory.valueOf(String.class));

    Assert.assertEquals(ExtendedNameToStringConverter.class, converter.getClass());
  }

  @Test
  public void testResolveOverriddenConverter() {
    PassThroughConverter cc = new PassThroughConverter(Date.class, Calendar.class);

    MapperFactory factory = MappingUtil.getMapperFactory();
    factory.getConverterFactory().registerConverter(cc);
    BuiltinConverters.register(factory.getConverterFactory());
    Assert.assertSame(
        cc,
        factory
            .getConverterFactory()
            .getConverter(TypeFactory.valueOf(Date.class), TypeFactory.valueOf(Date.class)));
  }

  public static class Name {
    public String first;
    public String last;
    public String middle;
  }

  public static class ExtendedName extends Name {
    public String title;
    public String salutation;
  }

  public static class NameToStringConverter extends CustomConverter<Name, String> {

    /*
     * (non-Javadoc)
     *
     * @see ma.glasnost.orika.Converter#convert(java.lang.Object,
     * ma.glasnost.orika.metadata.Type)
     */
    public String convert(
        Name source, Type<? extends String> destinationType, MappingContext context) {
      return source.first + " " + source.middle + " " + source.last;
    }
  }

  public static class ExtendedNameToStringConverter extends CustomConverter<ExtendedName, String> {

    /*
     * (non-Javadoc)
     *
     * @see ma.glasnost.orika.Converter#convert(java.lang.Object,
     * ma.glasnost.orika.metadata.Type)
     */
    public String convert(
        ExtendedName source, Type<? extends String> destinationType, MappingContext context) {
      return source.salutation
          + " "
          + source.first
          + " "
          + source.middle
          + " "
          + source.last
          + ", "
          + source.title;
    }
  }
}
