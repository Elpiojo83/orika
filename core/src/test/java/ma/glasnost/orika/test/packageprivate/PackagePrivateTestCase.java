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

package ma.glasnost.orika.test.packageprivate;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import ma.glasnost.orika.test.packageprivate.otherpackage.SomePublicDto;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PackagePrivateTestCase {

  @Test
  public void testMappingPackagePrivateToPublic() {
    SomePrivateEntity source = new SomePrivateEntity();
    source.setField("test value");

    final SomePublicDto actual = getMapperFacade().map(source, SomePublicDto.class);

    assertEquals(source.getField(), actual.getField());
  }

  @Test
  public void testMappingPublicToPackagePrivate() {
    SomePublicDto source = new SomePublicDto();
    source.setField("test value");

    final SomePrivateEntity actual = getMapperFacade().map(source, SomePrivateEntity.class);

    assertEquals(source.getField(), actual.getField());
  }

  @Test
  public void testMappingPackagePrivateToPackagePrivate() {
    SomePrivateEntity source = new SomePrivateEntity();
    source.setField("test value");

    final SimilarEntity actual = getMapperFacade().map(source, SimilarEntity.class);

    assertEquals(source.getField(), actual.getField());
  }

  @Test
  public void testGeneratedObjectFactory() {
    SimilarEntityCustomConstructor source = new SimilarEntityCustomConstructor("test value");

    final SimilarEntityCustomConstructor actual =
        getMapperFacade().map(source, SimilarEntityCustomConstructor.class);

    assertEquals(source.getField(), actual.getField());
  }

  @Test
  public void testMappingToNestedProtected() throws Exception {
    SomePublicDto source = new SomePublicDto();
    source.setField("test value");

    final SomeParentClass.SomeProtectedClass actual =
        getMapperFacade().map(source, SomeParentClass.SomeProtectedClass.class);

    assertEquals(source.getField(), actual.getField());
  }

  @Test
  public void testMappingFromNestedProtected() throws Exception {
    SomeParentClass.SomeProtectedClass source = new SomeParentClass.SomeProtectedClass();
    source.setField("test value");

    final SomePublicDto actual = getMapperFacade().map(source, SomePublicDto.class);

    assertEquals(source.getField(), actual.getField());
  }

  @Test
  public void testPackagePrivateNestedEntities() {
    NestedEntity source = new NestedEntity();
    source.setField("test value");

    final NestedEntity actual = getMapperFacade().map(source, NestedEntity.class);

    assertEquals(source.getField(), actual.getField());
  }

  private MapperFacade getMapperFacade() {
    final MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
    mapperFactory.classMap(SomePrivateEntity.class, SomePublicDto.class);
    mapperFactory.classMap(SomePrivateEntity.class, SimilarEntity.class);
    mapperFactory.classMap(SomeParentClass.SomeProtectedClass.class, SomePublicDto.class);
    return mapperFactory.getMapperFacade();
  }

  static class NestedEntity {
    private String field;

    public String getField() {
      return field;
    }

    public void setField(String field) {
      this.field = field;
    }
  }
}
