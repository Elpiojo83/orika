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

package ma.glasnost.orika.impl.generator.specification;

import ma.glasnost.orika.MappingException;
import ma.glasnost.orika.impl.generator.SourceCodeContext;
import ma.glasnost.orika.impl.generator.VariableRef;
import ma.glasnost.orika.metadata.FieldMap;

/**
 * UnmappableEnum is a place-holder specification to catch types which cannot be mapped (and which
 * shouldn't fall through to some other specification).
 *
 * @author mattdeboer
 */
public class UnmappableEnum extends AbstractSpecification {

  public boolean appliesTo(FieldMap fieldMap) {
    return fieldMap.getBType().isEnum()
        && !fieldMap.getAType().isEnum()
        && !fieldMap.getAType().isString();
  }

  public String generateMappingCode(
      FieldMap fieldMap, VariableRef source, VariableRef destination, SourceCodeContext code) {
    throw new MappingException(
        "Encountered mapping of enum to object (or vise-versa); sourceType="
            + source.type()
            + ", destinationType="
            + destination.type());
  }
}
