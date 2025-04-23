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

package ma.glasnost.orika.impl.generator;

import ma.glasnost.orika.metadata.FieldMap;

/**
 * Base Specification that contains the common methods for all Specifications. See {@link
 * Specification} and {@link AggregateSpecification}
 *
 * @author Kalyan Ayyagari kalyan01
 */
public interface BaseSpecification {
  /**
   * Tests whether this Specification applies to the specified MappedTypePair.
   *
   * @param fieldMap
   * @return true if this specification applies to the given MappedTypePair
   */
  boolean appliesTo(FieldMap fieldMap);
}
