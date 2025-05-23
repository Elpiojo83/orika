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

package ma.glasnost.orika.test.boundmapperfacade;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

public class UsedMappersTestCase {

  @Test
  public void testReuseOfMapper() {
    MapperFactory factory = MappingUtil.getMapperFactory();
    {
      ClassMapBuilder<A, C> classMapBuilder = factory.classMap(A.class, C.class);
      classMapBuilder.field("name", "nom");
      factory.registerClassMap(classMapBuilder.toClassMap());
    }

    {
      ClassMapBuilder<B, D> classMapBuilder = factory.classMap(B.class, D.class);
      classMapBuilder.field("age", "ages").use(A.class, C.class);
      factory.registerClassMap(classMapBuilder.toClassMap());
    }

    BoundMapperFacade<B, D> mapperFacade = factory.getMapperFacade(B.class, D.class);

    B source = new B();
    source.setName("Israfil");
    source.setAge(1000);

    D target = mapperFacade.map(source);

    Assert.assertEquals(source.getName(), target.getNom());
    Assert.assertEquals(source.getAge(), target.getAges());
  }

  @Test
  public void testOneCallOfFieldMapping() {
    MapperFactory factory = MappingUtil.getMapperFactory();
    {
      ClassMapBuilder<A, E> classMapBuilder = factory.classMap(A.class, E.class);
      factory.registerClassMap(classMapBuilder.byDefault().toClassMap());
    }
    {
      ClassMapBuilder<B, F> classMapBuilder = factory.classMap(B.class, F.class);
      classMapBuilder.byDefault().use(A.class, E.class);
      factory.registerClassMap(classMapBuilder.toClassMap());
    }

    BoundMapperFacade<B, F> mapperFacade = factory.getMapperFacade(B.class, F.class);

    B source = new B();
    source.setName("Israfil");
    source.setAge(1000);

    F target = mapperFacade.map(source);

    Assert.assertEquals(source.getName(), target.getName());
    Assert.assertEquals(source.getAge(), target.getAge());
    Assert.assertEquals(1, target.getNameCalls());
  }

  public abstract static class A {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class B extends A {
    private int age;

    public int getAge() {
      return age;
    }

    public void setAge(int ages) {
      this.age = ages;
    }
  }

  public abstract static class C {
    private String nom;

    public String getNom() {
      return nom;
    }

    public void setNom(String nom) {
      this.nom = nom;
    }
  }

  public static class D extends C {
    private int ages;

    public int getAges() {
      return ages;
    }

    public void setAges(int age) {
      this.ages = age;
    }
  }

  public abstract static class E {
    private String name;
    private int nameCalls;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
      nameCalls++;
    }

    public int getNameCalls() {
      return nameCalls;
    }
  }

  public static class F extends E {
    private int age;

    public int getAge() {
      return age;
    }

    public void setAge(int ages) {
      this.age = ages;
    }
  }
}
