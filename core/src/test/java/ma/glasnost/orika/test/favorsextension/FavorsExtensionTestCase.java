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

package ma.glasnost.orika.test.favorsextension;

import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.GeneratedMapperBase;
import ma.glasnost.orika.metadata.MapperKey;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import static ma.glasnost.orika.metadata.TypeFactory.valueOf;

public class FavorsExtensionTestCase {

  @Test
  public void favorsExtension() throws Throwable {

    MapperFactory factory = MappingUtil.getMapperFactory();
    factory
        .classMap(Animal.class, AnimalDto.class)
        .field("category", "type")
        .field("name", "qualifier")
        .favorExtension(true)
        .register();

    MapperFacade mapper = factory.getMapperFacade();

    Bird src = new Bird();
    src.category = "falcon";
    src.name = "Falcor";
    src.wingSpanInCm = 120;

    BirdDto dest = mapper.map(src, BirdDto.class);
    Assert.assertEquals(src.category, dest.type);
    Assert.assertEquals(src.name, dest.qualifier);
    Assert.assertEquals(src.wingSpanInCm, dest.wingSpanInCm);

    Cat cat = new Cat();
    cat.category = "tiger";
    cat.name = "Tigger";
    cat.striped = true;

    CatDto dest2 = mapper.map(cat, CatDto.class);
    Assert.assertEquals(cat.category, dest2.type);
    Assert.assertEquals(cat.name, dest2.qualifier);
    Assert.assertEquals(cat.striped, dest2.striped);
  }

  @Test
  public void favorsExtensionGlobally() throws Throwable {

    MapperFactory factory = new DefaultMapperFactory.Builder().favorExtension(true).build();
    factory
        .classMap(Animal.class, AnimalDto.class)
        .field("category", "type")
        .field("name", "qualifier")
        .register();

    MapperFacade mapper = factory.getMapperFacade();

    Bird src = new Bird();
    src.category = "falcon";
    src.name = "Falcor";
    src.wingSpanInCm = 120;

    BirdDto dest = mapper.map(src, BirdDto.class);
    Assert.assertEquals(src.category, dest.type);
    Assert.assertEquals(src.name, dest.qualifier);
    Assert.assertEquals(src.wingSpanInCm, dest.wingSpanInCm);

    Cat cat = new Cat();
    cat.category = "tiger";
    cat.name = "Tigger";
    cat.striped = true;

    CatDto dest2 = mapper.map(cat, CatDto.class);
    Assert.assertEquals(cat.category, dest2.type);
    Assert.assertEquals(cat.name, dest2.qualifier);
    Assert.assertEquals(cat.striped, dest2.striped);
  }

  @Test
  public void favorsExtensionMultiLevel() throws Throwable {

    MapperFactory factory = new DefaultMapperFactory.Builder().favorExtension(true).build();

    factory
        .classMap(Animal.class, AnimalDto.class)
        .field("category", "type")
        .field("name", "qualifier")
        .register();

    factory.classMap(Reptile.class, ReptileDto.class).field("weightInKg", "weightKg").register();

    MapperFacade mapper = factory.getMapperFacade();

    Salamander src = new Salamander();
    src.category = "falcon";
    src.name = "Falcor";
    src.tailLengthInCm = 23.0f;
    src.weightInKg = 12.5f;

    SalamanderDto dest = mapper.map(src, SalamanderDto.class);
    Assert.assertEquals(src.category, dest.type);
    Assert.assertEquals(src.name, dest.qualifier);
    Assert.assertEquals(src.weightInKg, dest.weightKg, 0.1);
    Assert.assertEquals(src.tailLengthInCm, dest.tailLengthInCm, 0.1);
  }

  @Test
  public void discoverUsedMappersWithoutDuplicates() throws Throwable {

    MapperFactory factory = new DefaultMapperFactory.Builder().favorExtension(true).build();

    factory
        .classMap(Animal.class, AnimalDto.class)
        .field("category", "type")
        .field("name", "qualifier")
        .register();

    factory.classMap(Reptile.class, ReptileDto.class).field("weightInKg", "weightKg").register();

    MapperFacade mapperFacade = factory.getMapperFacade();
    /*
     * Cause Salamander<->SalamanderDto mapper to be created
     */
    mapperFacade.map(new Salamander(), SalamanderDto.class);
    Mapper<Object, Object> mapper =
        factory.lookupMapper(
            new MapperKey(valueOf(Salamander.class), valueOf(SalamanderDto.class)));

    /*
     * Proceed through the hierarchy, assuring that no mapper is seen more than once
     */
    Mapper<Object, Object>[] usedMappers = ((GeneratedMapperBase) mapper).getUsedMappers();
    for (int i = 0, len = usedMappers.length; i < len; ++i) {
      for (int j = 0; j < len; ++j) {
        if (i != j) {
          Assert.assertFalse(
              usedMappers[i] + " is a used by " + usedMappers[j],
              GeneratedMapperBase.isUsedMapperOf(usedMappers[i], usedMappers[j]));
        }
      }
    }
  }

  @Test
  public void withoutFavorsExtension() throws Throwable {

    /*
     * Note: without using 'favorsExtension', the result is that Orika
     * uses the Animal <-> AnimalDto mapping that was registered without
     * generating a dynamic mapping for the downstream classes
     */
    MapperFactory factory = MappingUtil.getMapperFactory();
    factory
        .classMap(Animal.class, AnimalDto.class)
        .field("category", "type")
        .field("name", "qualifier")
        .register();

    MapperFacade mapper = factory.getMapperFacade();

    Bird src = new Bird();
    src.category = "falcon";
    src.name = "Falcor";
    src.wingSpanInCm = 120;

    BirdDto dest = mapper.map(src, BirdDto.class);
    Assert.assertEquals(src.category, dest.type);
    Assert.assertEquals(src.name, dest.qualifier);
    Assert.assertNotEquals(src.wingSpanInCm, dest.wingSpanInCm);

    Cat cat = new Cat();
    cat.category = "tiger";
    cat.name = "Tigger";
    cat.striped = true;

    CatDto dest2 = mapper.map(cat, CatDto.class);
    Assert.assertEquals(cat.category, dest2.type);
    Assert.assertEquals(cat.name, dest2.qualifier);
    Assert.assertNotEquals(cat.striped, dest2.striped);

    /*
     * But after we explicitly register the mapping, and
     * declare that it should 'use' the Animal to AnimalDto
     * mapper, it works as expected
     */
    factory
        .classMap(Bird.class, BirdDto.class)
        .byDefault()
        .use(Animal.class, AnimalDto.class)
        .register();

    dest = mapper.map(src, BirdDto.class);
    Assert.assertEquals(src.category, dest.type);
    Assert.assertEquals(src.name, dest.qualifier);
    Assert.assertEquals(src.wingSpanInCm, dest.wingSpanInCm);
  }

  public static class Animal {
    public String category;
    public String name;
  }

  public static class Bird extends Animal {
    public int wingSpanInCm;
  }

  public static class Cat extends Animal {
    public boolean striped;
  }

  public static class Reptile extends Animal {
    public float weightInKg;
  }

  public static class Salamander extends Reptile {
    public float tailLengthInCm;
  }

  public static class AnimalDto {
    public String type;
    public String qualifier;
  }

  public static class BirdDto extends AnimalDto {
    public int wingSpanInCm;
  }

  public static class CatDto extends AnimalDto {
    public boolean striped;
  }

  public static class ReptileDto extends AnimalDto {
    public float weightKg;
  }

  public static class SalamanderDto extends ReptileDto {
    public float tailLengthInCm;
  }
}
