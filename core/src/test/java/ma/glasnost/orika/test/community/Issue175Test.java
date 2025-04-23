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

import ma.glasnost.orika.OrikaSystemProperties;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class Issue175Test {

  @Test
  public void maps_one_value_to_all_elements_in_collection_and_back() {
    System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES, "true");
    DefaultMapperFactory mapper = new DefaultMapperFactory.Builder().build();

    mapper
        .classMap(Source.class, Destination.class)
        .field("nested", "nested")
        .field("value", "nested{value}")
        .byDefault()
        .register();

    Source source = new Source();
    source.setValue("some data");
    source.setNested(
        Arrays.asList(aNestedSource("one"), aNestedSource("two"), aNestedSource("three")));

    Destination destination = mapper.getMapperFacade().map(source, Destination.class);

    assertEquals("some data", destination.getNested().get(0).getValue());
    assertEquals("one", destination.getNested().get(0).getId());
    assertEquals("some data", destination.getNested().get(1).getValue());
    assertEquals("two", destination.getNested().get(1).getId());
    assertEquals("some data", destination.getNested().get(2).getValue());
    assertEquals("three", destination.getNested().get(2).getId());

    Source newSource = mapper.getMapperFacade().map(destination, Source.class);

    assertEquals(source, newSource);
  }

  private NestedSource aNestedSource(String id) {
    NestedSource nested = new NestedSource();
    nested.setId(id);
    return nested;
  }

  public static class Source {

    private String value;
    private List<NestedSource> nested = new ArrayList<>();

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public List<NestedSource> getNested() {
      return nested;
    }

    public void setNested(List<NestedSource> nested) {
      this.nested = nested;
    }

    @Override
    public boolean equals(Object obj) {
      return Objects.equals(value, ((Source) obj).value)
          && Objects.equals(nested, ((Source) obj).nested);
    }
  }

  public static class Destination {

    private List<NestedDestination> nested;

    public List<NestedDestination> getNested() {
      return nested;
    }

    public void setNested(List<NestedDestination> nested) {
      this.nested = nested;
    }
  }

  public static class NestedSource {

    private String id;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
      return Objects.equals(id, ((NestedSource) obj).id);
    }
  }

  public static class NestedDestination {
    private String id;
    private String value;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }
}
