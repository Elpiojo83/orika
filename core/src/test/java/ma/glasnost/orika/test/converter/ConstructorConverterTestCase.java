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

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.ConstructorConverter;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URL;

/**
 * ConstructorConverter will converter from one type to another if there exists a constructor for
 * the destinationType with a single argument matching the type of the source.
 */
public class ConstructorConverterTestCase {

  @Test
  public void testStringBasedConstructor() {
    MapperFactory factory = MappingUtil.getMapperFactory();
    factory.getConverterFactory().registerConverter(new ConstructorConverter());
    MapperFacade mapper = factory.getMapperFacade();

    String urlString = "http://localhost:80/index.html";
    URL url = mapper.map(urlString, URL.class);
    Assert.assertNotNull(url);
    Assert.assertEquals(urlString, url.toExternalForm());
  }

  @Test
  public void testPrimitiveConstructor() {
    MapperFactory factory = MappingUtil.getMapperFactory();
    factory.getConverterFactory().registerConverter(new ConstructorConverter());
    MapperFacade mapper = factory.getMapperFacade();

    Double doubleValue = Double.valueOf("4.99");
    BigDecimal bd = mapper.map(doubleValue, BigDecimal.class);
    Assert.assertNotNull(bd);
    Assert.assertEquals(doubleValue, bd.doubleValue(), 0.0001);
  }

  @Test
  public void testRegisterdAsBuiltinConverterAndWorksWithCustomType() {
    MapperFactory factory = MappingUtil.getMapperFactory();
    MapperFacade mapper = factory.getMapperFacade();

    StringContainer strCont = mapper.map("test", StringContainer.class);
    Assert.assertNotNull(strCont);
    Assert.assertEquals("test", strCont.getString());
  }

  public static class StringContainer {
    private String string;

    public StringContainer() {
      super();
    }

    public StringContainer(String string) {
      super();
      this.string = string;
    }

    public String getString() {
      return string;
    }

    public void setString(String string) {
      this.string = string;
    }
  }
}
