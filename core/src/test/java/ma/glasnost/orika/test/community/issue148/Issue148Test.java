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

package ma.glasnost.orika.test.community.issue148;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

public class Issue148Test {

  private MapperFacade mapper;

  @Before
  public void setup() {
    MapperFactory mapperFactory = MappingUtil.getMapperFactory();
    mapperFactory
        .classMap(Categorized.class, TagSet.class)
        .field("categories", "tags")
        .byDefault()
        .register();
    mapperFactory.classMap(Category.class, ItemTag.class).byDefault().register();
    mapperFactory.classMap(Product.class, Item.class).byDefault().register();

    mapper = mapperFactory.getMapperFacade();
  }

  @Test
  public void testCase() {
    Product product = new Product();
    Category category = new Category();
    category.setName("test");
    product.setCategories(Collections.singleton(category));
    Item b = mapper.map(product, Item.class);

    Assert.assertNotNull(b.getTags());
    Assert.assertEquals("test", b.getTags().iterator().next().getName());
  }

  public interface Categorized {
    Collection<Category> getCategories();

    void setCategories(Collection<Category> categories);
  }

  public interface TagSet<T> {
    Collection<T> getTags();

    void setTags(Collection<T> tags);
  }

  public static class Product implements Categorized {
    private Collection<Category> categories;
    private String test;

    @Override
    public Collection<Category> getCategories() {
      return categories;
    }

    @Override
    public void setCategories(Collection<Category> categories) {
      this.categories = categories;
    }

    public String getTest() {
      return test;
    }

    public void setTest(String test) {
      this.test = test;
    }
  }

  public static class ItemTag {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class Item implements TagSet<ItemTag> {
    private Collection<ItemTag> tags;
    private String test;

    @Override
    public Collection<ItemTag> getTags() {
      return tags;
    }

    @Override
    public void setTags(Collection<ItemTag> tags) {
      this.tags = tags;
    }

    public String getTest() {
      return test;
    }

    public void setTest(String test) {
      this.test = test;
    }
  }

  public class Category {

    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
