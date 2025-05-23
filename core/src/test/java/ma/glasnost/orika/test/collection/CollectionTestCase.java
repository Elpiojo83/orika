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

package ma.glasnost.orika.test.collection;

import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CollectionTestCase {

  @Test
  public void testStringToString() {
    D source = new D();
    source.setTags(Arrays.asList("soa", "java", "rest"));

    A destination = MappingUtil.getMapperFactory().getMapperFacade().map(source, A.class);

    Assert.assertNotNull(destination.getTags());
    Assert.assertEquals(3, destination.getTags().size());

    Set<String> sourceSet = new HashSet<String>(source.getTags());
    Set<String> destSet = new HashSet<String>(destination.getTags());
    Assert.assertEquals(sourceSet, destSet);
  }

  @Test
  public void testListToSet() {
    A source = new A();
    source.setTags(new HashSet<String>(Arrays.asList("soa", "java", "rest")));

    D destination = MappingUtil.getMapperFactory().getMapperFacade().map(source, D.class);

    Assert.assertNotNull(destination.getTags());
    Assert.assertEquals(3, destination.getTags().size());

    Set<String> sourceSet = new HashSet<String>(source.getTags());
    Set<String> destSet = new HashSet<String>(destination.getTags());
    Assert.assertEquals(sourceSet, destSet);
  }

  @Test
  public void testStringToStringWithGetterOnlyCollection() {
    D source = new D();
    source.setTags(Arrays.asList("soa", "java", "rest"));

    B destination = MappingUtil.getMapperFactory().getMapperFacade().map(source, B.class);

    Assert.assertNotNull(destination.getTags());
    Assert.assertEquals(3, destination.getTags().size());

    Set<String> sourceSet = new HashSet<String>(source.getTags());
    Set<String> destSet = new HashSet<String>(destination.getTags());
    Assert.assertEquals(sourceSet, destSet);
  }

  @Test
  public void nullSourceCollection_toCollection() {
    Source source = new Source();

    Destination destination =
        MappingUtil.getMapperFactory().getMapperFacade().map(source, Destination.class);

    Assert.assertNull(destination.getNames());
  }

  @Test
  public void nullSourceCollection_toArray() {
    Source source = new Source();

    Destination2 destination =
        MappingUtil.getMapperFactory().getMapperFacade().map(source, Destination2.class);

    Assert.assertNull(destination.getNames());
  }

  @Test
  public void unmodifiableCollection() {
    Source3 source = new Source3();
    source.setNames(Arrays.asList("soa", "java", "rest"));

    Destination3 destination =
        MappingUtil.getMapperFactory().getMapperFacade().map(source, Destination3.class);

    Assert.assertNotNull(destination.getNames());
    Assert.assertEquals(3, destination.getNames().size());
  }

  public static class A {
    private Set<String> tags;

    public Set<String> getTags() {
      return tags;
    }

    public void setTags(Set<String> tags) {
      this.tags = tags;
    }
  }

  public static class D {
    private List<String> tags;

    public List<String> getTags() {
      return tags;
    }

    public void setTags(List<String> tags) {
      this.tags = tags;
    }
  }

  public static class B {

    private List<String> tags;

    // Collection as typically generated by JAXB
    public List<String> getTags() {
      if (tags == null) {
        tags = new ArrayList<String>();
      }
      return tags;
    }
  }

  public static class Name {
    public String first;
    public String last;
  }

  public static class Source {
    private List<Name> names;

    /** @return the names */
    public List<Name> getNames() {
      return names;
    }

    /** @param names the names to set */
    public void setNames(List<Name> names) {
      this.names = names;
    }
  }

  public static class Destination {
    private List<Name> names;

    /** @return the names */
    public List<Name> getNames() {
      return names;
    }

    /** @param names the names to set */
    public void setNames(List<Name> names) {
      this.names = names;
    }
  }

  public static class Destination2 {
    private Name[] names;

    /** @return the names */
    public Name[] getNames() {
      return names;
    }

    /** @param names the names to set */
    public void setNames(Name[] names) {
      this.names = names;
    }
  }

  public static class Source3 {
    private List<String> names;

    /** @return the names */
    public List<String> getNames() {
      return Collections.unmodifiableList(names);
    }

    /** @param names the names to set */
    public void setNames(List<String> names) {
      this.names = names;
    }
  }

  public static class Destination3 {
    private List<String> names;

    /** @return the names */
    public List<String> getNames() {
      return Collections.unmodifiableList(names);
    }

    /** @param names the names to set */
    public void setNames(List<String> names) {
      this.names = names;
    }
  }
}
