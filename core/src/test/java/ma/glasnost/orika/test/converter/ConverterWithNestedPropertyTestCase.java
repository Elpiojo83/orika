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

package ma.glasnost.orika.test.converter;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Assert;
import org.junit.Test;

public class ConverterWithNestedPropertyTestCase {

  @Test
  public void testConverterWithNestedProperty() {
    MapperFactory mapperFactory = MappingUtil.getMapperFactory();

    mapperFactory
        .getConverterFactory()
        .registerConverter(
            new CustomConverter<Address, String>() {
              public String convert(
                  Address source, Type<? extends String> destinationClass, MappingContext context) {
                return source.getLine1() + " " + source.getLine2();
              }
            });

    mapperFactory
        .classMap(Order.class, OrderDTO.class)
        .fieldMap("customer.address", "shippingAddress")
        .add()
        .byDefault()
        .register();

    Address address = new Address();
    address.setLine1("5 rue Blida");
    address.setLine2("Casablanca");

    Customer customer = new Customer();
    customer.setName("Sidi Mohammed El Aatifi");
    customer.setAddress(address);

    Order order = new Order();
    order.setNumber("CPC6128");
    order.setCustomer(customer);

    MapperFacade mapperFacade = mapperFactory.getMapperFacade();
    OrderDTO orderDto = mapperFacade.map(order, OrderDTO.class);

    Assert.assertEquals(address.line1 + " " + address.line2, orderDto.getShippingAddress());
  }

  public static class Address {

    private String line1;

    private String line2;

    public String getLine1() {
      return line1;
    }

    public void setLine1(String line1) {
      this.line1 = line1;
    }

    public String getLine2() {
      return line2;
    }

    public void setLine2(String line2) {
      this.line2 = line2;
    }
  }

  public static class Customer {

    private String name;

    private Address address;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Address getAddress() {
      return address;
    }

    public void setAddress(Address address) {
      this.address = address;
    }
  }

  public static class Order {

    private String number;

    private Customer customer;

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }

    public Customer getCustomer() {
      return customer;
    }

    public void setCustomer(Customer customer) {
      this.customer = customer;
    }
  }

  public static class OrderDTO {

    private String number;
    private String customerName;
    private String shippingAddress;

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }

    public String getCustomerName() {
      return customerName;
    }

    public void setCustomerName(String customerName) {
      this.customerName = customerName;
    }

    public String getShippingAddress() {
      return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
      this.shippingAddress = shippingAddress;
    }
  }
}
