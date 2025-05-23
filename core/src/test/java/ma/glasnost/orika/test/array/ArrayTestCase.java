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

package ma.glasnost.orika.test.array;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ArrayTestCase {

  @Test
  public void testSimplePrimitiveArray() {
    ArrayTestCaseClasses.A source = new ArrayTestCaseClasses.A();
    byte[] buffer = new byte[] {1, 2, 3, 4};
    source.setBuffer(buffer);

    MapperFacade mapperFacade = MappingUtil.getMapperFactory().getMapperFacade();

    ArrayTestCaseClasses.B destination = mapperFacade.map(source, ArrayTestCaseClasses.B.class);

    Assert.assertArrayEquals(source.getBuffer(), destination.getBuffer());
  }

  @Test
  public void testSimplePrimitiveToWrapperArray() {
    ArrayTestCaseClasses.A source = new ArrayTestCaseClasses.A();
    byte[] buffer = new byte[] {1, 2, 3, 4};
    source.setBuffer(buffer);

    MapperFacade mapperFacade = MappingUtil.getMapperFactory().getMapperFacade();

    ArrayTestCaseClasses.C destination = mapperFacade.map(source, ArrayTestCaseClasses.C.class);

    Assert.assertArrayEquals(new Byte[] {1, 2, 3, 4}, destination.getBuffer());
  }

  @Test
  public void testArrayToList() {
    MapperFacade mapperFacade = MappingUtil.getMapperFactory().getMapperFacade();

    ArrayTestCaseClasses.A source = new ArrayTestCaseClasses.A();
    byte[] buffer = new byte[] {1, 2, 3, 4};
    source.setBuffer(buffer);

    ArrayTestCaseClasses.D destination = mapperFacade.map(source, ArrayTestCaseClasses.D.class);

    Assert.assertEquals(
        Arrays.asList((byte) 1, (byte) 2, (byte) 3, (byte) 4), destination.getBuffer());
  }

  @Test
  public void testWrapperArrayToList() {
    MapperFacade mapperFacade = MappingUtil.getMapperFactory().getMapperFacade();

    ArrayTestCaseClasses.C source = new ArrayTestCaseClasses.C();
    Byte[] buffer = new Byte[] {1, 2, 3, 4};
    source.setBuffer(buffer);

    ArrayTestCaseClasses.D destination = mapperFacade.map(source, ArrayTestCaseClasses.D.class);

    Assert.assertEquals(
        Arrays.asList((byte) 1, (byte) 2, (byte) 3, (byte) 4), destination.getBuffer());
  }

  @Test
  public void testListToArray() {
    MapperFacade mapperFacade = MappingUtil.getMapperFactory().getMapperFacade();

    ArrayTestCaseClasses.D source = new ArrayTestCaseClasses.D();
    source.setBuffer(Arrays.asList((byte) 1, (byte) 2, (byte) 3, (byte) 4));

    ArrayTestCaseClasses.A destination = mapperFacade.map(source, ArrayTestCaseClasses.A.class);

    Assert.assertArrayEquals(
        new byte[] {(byte) 1, (byte) 2, (byte) 3, (byte) 4}, destination.getBuffer());
  }

  @Test
  public void testMappingArrayOfString() {

    Product p = new Product();
    p.setTags(new String[] {"music", "sport"});

    ProductDTO productDTO =
        MappingUtil.getMapperFactory().getMapperFacade().map(p, ProductDTO.class);

    Assert.assertArrayEquals(p.getTags(), productDTO.getTags());
  }

  public static class Product {

    private String[] tags;

    public String[] getTags() {
      return tags.clone();
    }

    public void setTags(String[] tags) {
      this.tags = tags;
    }
  }

  public static class ProductDTO {

    private String[] tags;

    public String[] getTags() {
      return tags.clone();
    }

    public void setTags(String[] tags) {
      this.tags = tags;
    }
  }
}
