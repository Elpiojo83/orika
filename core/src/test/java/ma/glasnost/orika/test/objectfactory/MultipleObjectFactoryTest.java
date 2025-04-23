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

package ma.glasnost.orika.test.objectfactory;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultipleObjectFactoryTest {
  @Test
  public void orikaTest() {
    MapperFactory factory = new DefaultMapperFactory.Builder().build();

    factory.registerObjectFactory(
        new CustomFactory<>(Sub1.class), TypeFactory.<Sub1>valueOf(Sub1.class));
    factory.registerObjectFactory(
        new CustomFactory<>(Sub2.class), TypeFactory.<Sub2>valueOf(Sub2.class));
    factory.registerObjectFactory(
        new CustomFactory<>(Base.class), TypeFactory.<Base>valueOf(Base.class));

    MapperFacade mapperFacade = factory.getMapperFacade();
    Base mapped = mapperFacade.map(new Object(), Base.class);
    assertEquals("returned instance is not Base", Base.class, mapped.getClass());
  }

  public static class Base {}

  public static class Sub1 extends Base {}

  public static class Sub2 extends Base {}
}
