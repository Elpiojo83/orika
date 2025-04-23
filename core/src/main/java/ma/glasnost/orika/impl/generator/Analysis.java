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

import java.lang.reflect.Modifier;

class Analysis {

  private Analysis() {
    throw new UnsupportedOperationException("not instantiable");
  }

  static Visibility getMostRestrictiveVisibility(Class<?> classToCheck) {
    Visibility visibility = Visibility.PUBLIC;
    Class<?> currentClass = classToCheck;
    while (currentClass != null) {
      int modifiers = currentClass.getModifiers();
      if (Modifier.isPrivate(modifiers)) {
        visibility = Visibility.PRIVATE;
      } else if (Modifier.isProtected(modifiers) && visibility != Visibility.PRIVATE) {
        visibility = Visibility.PROTECTED;
      } else if (Modifier.isPublic(modifiers)) {
        // visibility = Visibility.PUBLIC not needed because if visibiliy were anything
        // else than PUBLIC we wouldn't set it anyways
      } else if (visibility != Visibility.PRIVATE && visibility != Visibility.PROTECTED) {
        visibility = Visibility.PACKAGE;
      }
      currentClass = currentClass.getEnclosingClass();
    }
    return visibility;
  }

  enum Visibility {
    PRIVATE,
    PACKAGE,
    PROTECTED,
    PUBLIC
  }
}
