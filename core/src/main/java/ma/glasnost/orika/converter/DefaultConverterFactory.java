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

package ma.glasnost.orika.converter;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.util.ClassUtil;
import ma.glasnost.orika.metadata.ConverterKey;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * DefaultConverterFactory is the base implementation of ConverterFactory.
 *
 * @author mattdeboer
 */
public class DefaultConverterFactory implements ConverterFactory {

  private final Map<ConverterKey, Converter<Object, Object>> converterCache;
  private final Map<String, Converter<Object, Object>> convertersMap;
  private Collection<Converter<Object, Object>> converters;
  private MapperFacade mapperFacade;

  /**
   * @param converterCache
   * @param converters
   */
  public DefaultConverterFactory(
      Map<ConverterKey, Converter<Object, Object>> converterCache,
      Set<Converter<Object, Object>> converters) {
    super();
    this.converterCache = converterCache;
    this.converters = new CopyOnWriteArrayList<>();
    this.convertersMap = new ConcurrentHashMap<>();
  }

  /**
   * Constructs a new instance of DefaultConverterFactory using a concurrent linked hash map as the
   * Converter cache, and a linked hashSet holding the converters.
   */
  public DefaultConverterFactory() {
    this(new ConcurrentHashMap<>(), new LinkedHashSet<Converter<Object, Object>>());
  }

  public synchronized void setMapperFacade(MapperFacade mapperFacade) {
    this.mapperFacade = mapperFacade;
    Set<Converter<Object, Object>> orderedConverters =
        new LinkedHashSet<Converter<Object, Object>>();
    for (Converter<Object, Object> converter : converters) {
      converter.setMapperFacade(mapperFacade);
      orderedConverters.add(converter);
    }
    converters = orderedConverters;
    for (Converter<?, ?> converter : convertersMap.values()) {
      converter.setMapperFacade(mapperFacade);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ma.glasnost.orika.converter.ConverterFactory#canConvert(java.lang.Class,
   * java.lang.Class)
   */
  public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
    return getConverter(sourceType, destinationType) != null;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ma.glasnost.orika.converter.ConverterFactory#hasConverter(java.lang.String
   * )
   */
  public boolean hasConverter(String converterId) {
    return convertersMap.containsKey(converterId);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ma.glasnost.orika.converter.ConverterFactory#getConverter(java.lang.Class
   * , java.lang.Class)
   */
  public synchronized Converter<Object, Object> getConverter(
      Type<?> sourceClass, Type<?> destinationClass) {

    // Step verify if converter exists for sourceClass and destination
    Converter<Object, Object> converter = _converter(sourceClass, destinationClass);

    if (converter != null) {
      return converter;
    }

    // Apply auto-boxing in converter lookup
    if (sourceClass.isPrimitive()) {
      sourceClass = TypeFactory.valueOf(ClassUtil.getWrapperType(sourceClass.getRawType()));
      converter = _converter(sourceClass, destinationClass);
    }
    if (converter != null) {
      return converter;
    }

    // Destination
    if (destinationClass.isPrimitive()) {
      destinationClass =
          TypeFactory.valueOf(ClassUtil.getWrapperType(destinationClass.getRawType()));
      converter = _converter(sourceClass, destinationClass);
    }
    if (converter != null) {
      return converter;
    }
    return null;
  }

  private Converter<Object, Object> _converter(Type<?> sourceClass, Type<?> destinationClass) {
    ConverterKey key = new ConverterKey(sourceClass, destinationClass);
    if (converterCache.containsKey(key)) {
      return converterCache.get(key);
    }

    for (Converter<Object, Object> converter : converters) {
      if (converter.canConvert(sourceClass, destinationClass)) {
        converterCache.put(key, converter);
        return converter;
      }
    }

    return null;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ma.glasnost.orika.converter.ConverterFactory#getConverter(java.lang.String
   * )
   */
  public Converter<Object, Object> getConverter(String converterId) {
    return convertersMap.get(converterId);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ma.glasnost.orika.converter.ConverterFactory#registerConverter(ma.glasnost
   * .orika.converter.Converter)
   */
  @SuppressWarnings({"unchecked"})
  public <S, D> void registerConverter(Converter<S, D> converter) {
    if (mapperFacade != null) {
      throw new IllegalStateException(
          "Cannot register converters after MapperFacade has been initialized");
    }
    converters.add((Converter<Object, Object>) converter);
    if (converter instanceof BidirectionalConverter
        && !converter.getAType().equals(converter.getBType())) {
      converters.add(((BidirectionalConverter<?, ?>) converter).reverse());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ma.glasnost.orika.converter.ConverterFactory#registerConverter(java.lang
   * .String, ma.glasnost.orika.converter.Converter)
   */
  @SuppressWarnings({"unchecked"})
  public <S, D> void registerConverter(String converterId, Converter<S, D> converter) {
    if (mapperFacade != null) {
      throw new IllegalStateException(
          "Cannot register converters after MapperFacade has been initialized");
    }
    convertersMap.put(converterId, (Converter<Object, Object>) converter);
  }
}
