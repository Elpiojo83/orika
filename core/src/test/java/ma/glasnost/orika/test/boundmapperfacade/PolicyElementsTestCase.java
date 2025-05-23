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

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.test.MappingUtil;
import ma.glasnost.orika.test.crossfeatures.PolicyElementsTestCaseClasses.*;
import ma.glasnost.orika.unenhance.UnenhanceStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PolicyElementsTestCase {

  @Test
  public void test() {
    MapperFactory factory = MappingUtil.getMapperFactory();
    configureMapperFactory(factory);

    Policy policy = new Policy();
    Set<PolicyElement> elements = new HashSet<PolicyElement>();
    elements.add(new CustomerElement());
    elements.add(new ProductElement());
    elements.add(new OtherElement());
    elements.add(new OneOtherElement());

    policy.setElements(elements);

    PolicyDTO dto = factory.getMapperFacade(Policy.class, PolicyDTO.class).map(policy);

    Assert.assertEquals(elements.size(), dto.getElements().size());
  }

  private void configureMapperFactory(MapperFactory factory) {

    factory.registerClassMap(factory.classMap(Policy.class, PolicyDTO.class).byDefault());
    factory.registerClassMap(
        factory.classMap(CustomerElement.class, CustomerElementDTO.class).byDefault());
    factory.registerClassMap(
        factory.classMap(ProductElement.class, ProductElementDTO.class).byDefault());
    factory.registerClassMap(
        factory.classMap(OtherElement.class, OtherElementDTO.class).byDefault());
    factory.registerClassMap(
        factory.classMap(OneOtherElement.class, OneOtherElementDTO.class).byDefault());
  }

  @Test
  public void testHibernateProxyLike() {
    MapperFactory factory =
        new DefaultMapperFactory.Builder()
            .unenhanceStrategy(
                new UnenhanceStrategy() {
                  @SuppressWarnings("unchecked")
                  public <T> Type<T> unenhanceType(T object, Type<T> type) {
                    if (object instanceof PolicyElementProxy)
                      return (Type<T>) ((PolicyElementProxy) object).getTargetClass();
                    return type;
                  }

                  @SuppressWarnings("unchecked")
                  public <T> T unenhanceObject(T object, Type<T> type) {
                    if (object instanceof PolicyElementProxy)
                      return (T) ((PolicyElementProxy) object).getTarget();
                    return object;
                  }
                })
            .build();
    configureMapperFactory(factory);

    Policy policy = new Policy();
    Set<PolicyElement> elements = new HashSet<PolicyElement>();
    CustomerElement target = new CustomerElement();
    target.setName("Adil");
    elements.add(new PolicyElementProxy(target));
    elements.add(new ProductElement());
    elements.add(new OtherElement());
    elements.add(new OneOtherElement());

    policy.setElements(elements);

    PolicyDTO dto = factory.getMapperFacade(Policy.class, PolicyDTO.class).map(policy);

    Assert.assertEquals(elements.size(), dto.getElements().size());

    for (PolicyElementDTO element : dto.getElements()) {
      if (element instanceof CustomerElementDTO) {
        Assert.assertEquals("Adil", ((CustomerElementDTO) element).getName());
      }
    }
  }
}
