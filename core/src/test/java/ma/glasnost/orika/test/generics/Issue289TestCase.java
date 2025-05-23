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

package ma.glasnost.orika.test.generics;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

public class Issue289TestCase {

  @Test
  public void testRecursivelyDefinedClassesWithInheritance() {
    final MapperFactory mapperFactory = MappingUtil.getMapperFactory();
    mapperFactory.registerClassMap(
        mapperFactory.classMap(Parent.class, ParentDto.class).byDefault().toClassMap());
    mapperFactory.registerClassMap(
        mapperFactory.classMap(Child.class, ChildDto.class).byDefault().toClassMap());
    mapperFactory.registerClassMap(
        mapperFactory.classMap(ChildImpl.class, ChildImplDto.class).byDefault().toClassMap());
    final MapperFacade mapperFacade = mapperFactory.getMapperFacade();

    {
      final Parent source = new Parent();
      source.setParentName("parentName");

      final ParentDto target = mapperFacade.map(source, ParentDto.class);

      Assert.assertEquals(source.getParentName(), target.getParentName());
    }

    {
      final Child source = new Child();
      source.setParentName("parentName");
      source.setChildName("childName");

      final ChildDto target = mapperFacade.map(source, ChildDto.class);

      Assert.assertEquals(source.getParentName(), target.getParentName());
      Assert.assertEquals(source.getChildName(), target.getChildName());
    }

    {
      final ChildImpl source = new ChildImpl();
      source.setParentName("parentName");
      source.setChildName("childName");
      source.setChildImpl("source");

      final ChildImplDto target = mapperFacade.map(source, ChildImplDto.class);

      Assert.assertEquals(source.getParentName(), target.getParentName());
      Assert.assertEquals(source.getChildName(), target.getChildName());
      Assert.assertEquals(source.getChildImpl(), target.getChildImpl());
    }
  }

  public static class Parent<T extends Parent<T>> {
    private String parentName;

    public String getParentName() {
      return parentName;
    }

    public void setParentName(String parentName) {
      this.parentName = parentName;
    }
  }

  public static class Child<T extends Child<T>> extends Parent<T> {
    private String childName;

    public String getChildName() {
      return childName;
    }

    public void setChildName(String childName) {
      this.childName = childName;
    }
  }

  public static class ChildImpl extends Child<ChildImpl> {
    private String childImpl;

    public String getChildImpl() {
      return childImpl;
    }

    public void setChildImpl(String childImpl) {
      this.childImpl = childImpl;
    }
  }

  public static class ParentDto {
    private String parentName;

    public String getParentName() {
      return parentName;
    }

    public void setParentName(String parentName) {
      this.parentName = parentName;
    }
  }

  public static class ChildDto extends ParentDto {
    private String childName;

    public String getChildName() {
      return childName;
    }

    public void setChildName(String childName) {
      this.childName = childName;
    }
  }

  public static class ChildImplDto extends ChildDto {
    private String childImpl;

    public String getChildImpl() {
      return childImpl;
    }

    public void setChildImpl(String childImpl) {
      this.childImpl = childImpl;
    }
  }
}
