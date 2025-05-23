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

package ma.glasnost.orika.test.extensibility;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PooledInstancesTestCase {

  @Test
  public void testExtendedMapper() {
    MyMapperFactory factory = new MyMapperFactory.Builder().build();
    factory.registerClassMap(
        factory.classMap(SourcePoolView.class, Pooled.class).byDefault().toClassMap());
    factory.registerClassMap(
        factory.classMap(SourceObject.class, DestObject.class).byDefault().toClassMap());

    SourceObject source1 = new SourceObject();
    source1.setPooled(new SourcePoolView("A"));
    DestObject dest1 = factory.getMapperFacade().map(source1, DestObject.class);

    SourceObject source2 = new SourceObject();
    source2.setPooled(new SourcePoolView("A"));
    DestObject dest2 = factory.getMapperFacade().map(source2, DestObject.class);
    Assert.assertEquals(dest2.getPooled(), dest1.getPooled());

    SourceObject source3 = new SourceObject();
    source3.setPooled(new SourcePoolView("A"));
    DestObject dest3 = new DestObject();
    dest3.setPooled(factory.getPool().get("C"));
    factory.getMapperFacade().map(source3, dest3);
    Assert.assertEquals(dest3.getPooled(), dest1.getPooled());
  }

  public static class MyMapperFactory extends DefaultMapperFactory {
    private Map<String, Pooled> pool = new HashMap<String, Pooled>();

    /**
     * Since DefaultMapperFactory uses (some form of) the Builder pattern, we need to provide a
     * constructor which can accept an appropriate builder and pass it to the super constructor.
     *
     * @param builder
     */
    protected MyMapperFactory(Builder builder) {
      super(builder);
      pool.put("A", new Pooled("A"));
      pool.put("B", new Pooled("B"));
      pool.put("C", new Pooled("C"));
    }

    public Map<String, Pooled> getPool() {
      return pool;
    }

    public <S, D> BoundMapperFacade<S, D> getMapperFacade(
        Type<S> sourceType, Type<D> destinationType, boolean containsCycles) {
      BoundMapperFacade<S, D> ret =
          super.getMapperFacade(sourceType, destinationType, containsCycles);
      if (sourceType.getRawType().equals(SourcePoolView.class)
          && destinationType.getRawType().equals(Pooled.class)) {
        ret = new PoolingBoundMapperFacade<S, D>(ret, pool);
      }
      return ret;
    }

    public static class Builder extends MapperFactoryBuilder<MyMapperFactory, Builder> {

      protected Builder self() {
        return this;
      }

      public MyMapperFactory build() {
        return new MyMapperFactory(this);
      }
    }

    private static class PoolingBoundMapperFacade<A, B> implements BoundMapperFacade<A, B> {
      private BoundMapperFacade<A, B> wrapped;
      private Map<String, Pooled> pool;

      public PoolingBoundMapperFacade(BoundMapperFacade<A, B> wrapped, Map<String, Pooled> pool) {
        this.wrapped = wrapped;
        this.pool = pool;
      }

      @SuppressWarnings("unchecked")
      public B map(A instanceA) {
        return (B) pool.get(((SourcePoolView) instanceA).getName());
      }

      public Type<A> getAType() {
        return wrapped.getAType();
      }

      public Type<B> getBType() {
        return wrapped.getBType();
      }

      @SuppressWarnings("unchecked")
      public B map(A instanceA, MappingContext context) {
        return (B) pool.get(((SourcePoolView) instanceA).getName());
      }

      public A mapReverse(B instanceB) {
        return wrapped.mapReverse(instanceB);
      }

      public A mapReverse(B instanceB, MappingContext context) {
        return wrapped.mapReverse(instanceB, context);
      }

      @SuppressWarnings("unchecked")
      public B map(A instanceA, B instanceB) {
        return (B) pool.get(((SourcePoolView) instanceA).getName());
      }

      @SuppressWarnings("unchecked")
      public B map(A instanceA, B instanceB, MappingContext context) {
        return (B) pool.get(((SourcePoolView) instanceA).getName());
      }

      public A mapReverse(B instanceB, A instanceA) {
        return wrapped.mapReverse(instanceB, instanceA);
      }

      public A mapReverse(B instanceB, A instanceA, MappingContext context) {
        return wrapped.mapReverse(instanceB, instanceA, context);
      }

      public B newObject(A source, MappingContext context) {
        return wrapped.newObject(source, context);
      }

      public A newObjectReverse(B source, MappingContext context) {
        return wrapped.newObjectReverse(source, context);
      }
    }
  }

  public static class SourcePoolView {
    private String name;

    public SourcePoolView(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class SourceObject {
    private SourcePoolView pooled;

    public SourcePoolView getPooled() {
      return pooled;
    }

    public void setPooled(SourcePoolView pooled) {
      this.pooled = pooled;
    }
  }

  public static class Pooled {
    private String name;

    public Pooled() {}

    public Pooled(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class DestObject {
    private Pooled pooled;

    public Pooled getPooled() {
      return pooled;
    }

    public void setPooled(Pooled pooled) {
      this.pooled = pooled;
    }
  }
}
