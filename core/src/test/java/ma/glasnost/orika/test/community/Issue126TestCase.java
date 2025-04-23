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
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Exclude() does not work if the field is not present in both classes.
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/126">https://code.google.com/archive/p/orika/</a>
 */
public class Issue126TestCase {

  @Test
  public void testExclude() {

    MapperFactory factory = MappingUtil.getMapperFactory();

    factory
        .classMap(A.class, B.class)
        .field("id", "id")
        .exclude("unmappedField")
        .byDefault()
        .register();

    A source = new A();
    source.id = "a";
    source.fieldA = "a";
    source.fieldB = "b";
    source.unmappedField = new MyField("myField");

    B dest = factory.getMapperFacade().map(source, B.class);
    Assert.assertNotNull(dest);
  }

  @Test
  public void testByDefault() {

    MapperFactory factory = MappingUtil.getMapperFactory();

    factory
        .classMap(A.class, B.class)
        // .field("id", "id")
        .byDefault()
        .register();

    A source = new A();
    source.id = "a";
    source.fieldA = "a";
    source.fieldB = "b";
    source.unmappedField = new MyField("myField");

    B dest = factory.getMapperFacade().map(source, B.class);
    Assert.assertNotNull(dest);
  }

  public static class A {
    public String id;
    public MyField unmappedField;
    public String fieldA;
    public String fieldB;
  }

  public static class MyField {
    public String value = "default";

    public MyField(String blah) {
      this.value = blah;
    }
  }

  public static class B {
    public String id;
    public String fieldA;
    public String fieldB;
  }
}
