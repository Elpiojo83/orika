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

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingException;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Usage of .constructorA/B results in NullPointerException.
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/132">https://code.google.com/archive/p/orika/</a>
 */
public class Issue132TestCase {

  @Test(expected = MappingException.class)
  public void test() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    mapperFactory
        .classMap(Person1.class, Person2.class)
        .constructorB("fn")
        .field("firstname", "forename")
        .register();

    BoundMapperFacade<Person1, Person2> mapperFacade =
        mapperFactory.getMapperFacade(Person1.class, Person2.class);
    Person1 otto1 = new Person1("Otto");
    Person2 otto2 = mapperFacade.map(otto1);

    assertEquals(otto1.getFirstname(), otto2.getForename());
  }

  @Test
  public void testMappedNamesWithSpecifiedConstructor() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    mapperFactory
        .classMap(Person1.class, Person2.class)
        .constructorB("fn")
        .fieldAToB("firstname", "fn")
        .fieldBToA("forename", "firstname")
        .register();

    BoundMapperFacade<Person1, Person2> mapperFacade =
        mapperFactory.getMapperFacade(Person1.class, Person2.class);
    Person1 otto1 = new Person1("Otto");
    Person2 otto2 = mapperFacade.map(otto1);

    assertEquals(otto1.getFirstname(), otto2.getForename());
  }

  @Test
  public void testMappedNamesWithoutSpecifiedConstructor() {
    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    mapperFactory
        .classMap(Person1.class, Person2.class)
        .fieldAToB("firstname", "fn")
        .fieldBToA("forename", "firstname")
        .register();

    BoundMapperFacade<Person1, Person2> mapperFacade =
        mapperFactory.getMapperFacade(Person1.class, Person2.class);
    Person1 otto1 = new Person1("Otto");
    Person2 otto2 = mapperFacade.map(otto1);

    assertEquals(otto1.getFirstname(), otto2.getForename());
  }

  public static class Person1 {
    private final String firstname;

    public Person1(String firstname) {
      this.firstname = firstname;
    }

    public String getFirstname() {
      return firstname;
    }
  }

  public static class Person2 {
    private final String forename;

    public Person2(String fn) {
      this.forename = fn;
    }

    public String getForename() {
      return forename;
    }
  }
}
