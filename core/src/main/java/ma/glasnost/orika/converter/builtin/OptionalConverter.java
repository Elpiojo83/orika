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

package ma.glasnost.orika.converter.builtin;

import java.util.Optional;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

/**
 * Converter which will convert one {@linkplain Optional} field into another optional field.
 *
 * @param <S> Type of the optional field to map from.
 * @param <D> Type of the optional field to map to.
 */
public class OptionalConverter<S, D> implements Converter<Optional<S>, Optional<D>> {

  private final Type<S> sourceType;
  private final Type<D> destinationType;
  private MapperFacade mapper;

  /**
   * Construct a new {@linkplain Optional} converter configured to convert an {@linkplain Optional}
   * field to another <code>Optional</code> field.
   *
   * @param sourceType Type the source {@linkplain Optional} field contains.
   * @param destinationType Type the destination {@linkplain Optional} field will contain.
   */
  public OptionalConverter(final Type<S> sourceType, final Type<D> destinationType) {
    this.sourceType = sourceType;
    this.destinationType = destinationType;
  }

  public boolean canConvert(final Type<?> sourceType, final Type<?> destinationType) {
    final Type<?> sourceComponentType = sourceType.getComponentType();
    final Type<?> destinationComponentType = destinationType.getComponentType();

    return !(sourceComponentType == null || destinationComponentType == null)
        && this.sourceType.isAssignableFrom(sourceComponentType.getRawType())
        && this.destinationType.isAssignableFrom(destinationComponentType.getRawType());
  }

  public Optional<D> convert(
      final Optional<S> optionalSource,
      final Type<? extends Optional<D>> destinationType,
      final MappingContext mappingContext) {
    if (!optionalSource.isPresent()) {
      return Optional.empty();
    }

    final S source = optionalSource.get();

    return Optional.ofNullable(
        mapper.map(source, sourceType, this.destinationType, mappingContext));
  }

  public void setMapperFacade(final MapperFacade mapper) {
    this.mapper = mapper;
  }

  public Type<Optional<S>> getAType() {
    return getOptionalTypeOf(sourceType);
  }

  public Type<Optional<D>> getBType() {
    return getOptionalTypeOf(destinationType);
  }

  @SuppressWarnings("unchecked")
  private <T> Type<Optional<T>> getOptionalTypeOf(Type<T> type) {
    return (Type) TypeFactory.valueOf(Optional.class, type);
  }
}
