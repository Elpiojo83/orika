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

package ma.glasnost.orika.test.generator;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Verify that we can properly map non-assignable properties of an object, so long as they are not
 * immutable
 */
public class PropertiesWithNoSetterTestCase {

  @Test
  public void test() {

    MapperFactory factory = MappingUtil.getMapperFactory();
    MapperFacade mapper = factory.getMapperFacade();

    SomeObject source = new SomeObject();
    source.getPerson().firstName = "Joe";
    source.getPerson().lastName = "Smith";

    AnotherObject dest = mapper.map(source, AnotherObject.class);
    Assert.assertEquals(source.getPerson().firstName, dest.getPerson().firstName);
    Assert.assertEquals(source.getPerson().lastName, dest.getPerson().lastName);
    Assert.assertNotEquals(source.getId(), dest.getId());
  }

  public static class Person {
    public String firstName;
    public String lastName;
  }

  public static class SomeObject {

    private final Person person;
    private String id;

    public SomeObject() {
      this.person = new Person();
      this.id = UUID.randomUUID().toString();
    }

    public Person getPerson() {
      return person;
    }

    public String getId() {
      return id;
    }
  }

  public static class AnotherObject {

    private final Person person;
    private String id;

    public AnotherObject() {
      this.person = new Person();
      this.id = UUID.randomUUID().toString();
    }

    public Person getPerson() {
      return person;
    }

    public String getId() {
      return id;
    }
  }
}
