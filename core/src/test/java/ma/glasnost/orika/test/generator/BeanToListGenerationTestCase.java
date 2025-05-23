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

import java.util.List;

public class BeanToListGenerationTestCase {

  private final double DELTA = 0.00001;

  @Test
  public void testBeanToListGeneration() throws Exception {

    MapperFactory factory = MappingUtil.getMapperFactory();

    factory
        .classMap(Student.class, List.class)
        .field("grade.letter", "[0]")
        .field("grade.point", "[1]")
        .field("grade.percentage", "[2]")
        .field("name.first", "[3]")
        .field("name.last", "[4]")
        .byDefault()
        .register();

    MapperFacade mapper = factory.getMapperFacade();

    Student student = new Student();
    student.id = "1";
    student.email = "test@test.com";
    student.name = new Name();
    student.name.first = "Chuck";
    student.name.last = "Testa";
    student.grade = new Grade();
    student.grade.letter = "B-";
    student.grade.percentage = 81.5;
    student.grade.point = 2.7;

    @SuppressWarnings("unchecked")
    List<Object> result = mapper.map(student, List.class);

    int index = -1;
    Assert.assertEquals(student.grade.letter, result.get(++index));
    Assert.assertEquals(student.grade.point, result.get(++index));
    Assert.assertEquals(student.grade.percentage, result.get(++index));
    Assert.assertEquals(student.name.first, result.get(++index));
    Assert.assertEquals(student.name.last, result.get(++index));
    Assert.assertEquals(student.id, result.get(++index));
    Assert.assertEquals(student.email, result.get(++index));

    Student mapBack = mapper.map(result, Student.class);

    Assert.assertEquals(student.id, mapBack.id);
    Assert.assertEquals(student.email, mapBack.email);
    Assert.assertEquals(student.name.first, mapBack.name.first);
    Assert.assertEquals(student.name.last, mapBack.name.last);
    Assert.assertEquals(student.grade.letter, mapBack.grade.letter);
    Assert.assertEquals(student.grade.percentage, mapBack.grade.percentage, DELTA);
    Assert.assertEquals(student.grade.point, mapBack.grade.point, DELTA);
  }

  @Test
  public void nestedListElement() {
    MapperFactory factory = MappingUtil.getMapperFactory();

    factory.classMap(Person.class, PersonDto.class).field("name.first", "names[0].name").register();

    MapperFacade mapper = factory.getMapperFacade();

    Person person = new Person();
    person.name = new Name();
    person.name.first = "Chuck";
    person.name.last = "Testa";

    PersonDto result = mapper.map(person, PersonDto.class);

    Assert.assertNotNull(result.names);
    Assert.assertEquals(person.name.first, result.names.get(0).name);

    Person mapBack = mapper.map(result, Person.class);

    Assert.assertNotNull(mapBack.name.first);
    Assert.assertEquals(person.name.first, mapBack.name.first);
  }

  public static class Student {
    public Grade grade;
    public String id;
    public String email;
    public Name name;
  }

  public static class Name {
    public String first;
    public String last;
  }

  public static class NameDto {
    public String name;
  }

  public static class Grade {
    public double point;
    public double percentage;
    public String letter;
  }

  public static class Person {
    public Name name;
  }

  public static class PersonDto {
    public List<NameDto> names;
  }
}
