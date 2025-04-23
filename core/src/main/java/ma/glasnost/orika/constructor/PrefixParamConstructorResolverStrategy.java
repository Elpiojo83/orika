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

package ma.glasnost.orika.constructor;

import ma.glasnost.orika.impl.util.StringUtil;

/**
 * Finds constructor with param names that follow a prefix naming convention. For instance
 * p-prefixed param names - (pName, pAge).
 */
public class PrefixParamConstructorResolverStrategy extends SimpleConstructorResolverStrategy {

  @Override
  protected String[] mapTargetParamNames(String[] parameters) {
    final String[] mappedParamNames = new String[parameters.length];
    for (int idx = 0; idx < parameters.length; idx++) {
      mappedParamNames[idx] = StringUtil.uncapitalize(parameters[idx].substring(1));
    }
    return mappedParamNames;
  }
}
