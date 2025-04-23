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

package ma.glasnost.orika.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author Eduard Drenth at VectorPrint.nl */
public class ClassHelper {

  private ClassHelper() {}

  /**
   * find the runtime class for a class parameter. Calls {@link
   * #findParameterClasses(java.lang.Class, java.lang.Class) }
   *
   * @param subclass the subclass of the generic class whose parameter class we want to know
   * @param classWithParameter the class that contains the parameter whose class we are looking for
   * @return
   */
  public static <T> Class<?> findParameterClass(
      int paramNum, Class<? extends T> subclass, Class<? extends T> classWithParameter) {
    return findParameterClasses(subclass, classWithParameter).get(paramNum);
  }

  /**
   * find the runtime classes of the parameters of a class or interface. The strategy is to visit
   * the subclass and its parent classes to find the classes of the classWithParameters
   *
   * @param subclass the subclass of the generic class whose parameter classes we want to know
   * @param classWithParameter the class that contains the parameter whose class we are looking for
   * @return a List of java class for the class parameters, or null
   */
  public static <T> List<Class<?>> findParameterClasses(
      Class<? extends T> subclass, Class<? extends T> classWithParameter) {
    List<Class<?>> parameterClasses = null;
    // conditions
    TypeVariable[] params = classWithParameter.getTypeParameters();
    if (params.length > 0) { // parameters have to be present

      parameterClasses = new ArrayList<Class<?>>(params.length);
      // to keep track of where parameters go in the class hierarchy
      Map<TypeVariable, Class<?>> varsPrevious = new HashMap<TypeVariable, Class<?>>(3);
      Map<TypeVariable, Class<?>> varsCurrent = new HashMap<TypeVariable, Class<?>>(3);
      Type parent = subclass;
      while ((parent = getGenericSuperType((Class) parent, classWithParameter)) != null) {
        if (parent instanceof ParameterizedType) {
          ParameterizedType pa = (ParameterizedType) parent;
          TypeVariable[] pars = getClass(pa.getRawType()).getTypeParameters();
          int i = 0;
          for (Type t : pa.getActualTypeArguments()) {
            /*
             * the number of getActualTypeArguments and getTypeParameters of the parent is always the same
             *
             * when t is a typevariable, we may find its class in previously processed type variables
             *
             * working this way finds the correct type, also when order of parameters changes in the hierarchy
             */
            if (t instanceof TypeVariable) {
              varsCurrent.put(
                  pars[i++], (varsPrevious.containsKey(t)) ? varsPrevious.get(t) : null);
            } else {
              varsCurrent.put(pars[i++], getClass(t));
            }
          }
          parent = pa.getRawType();
        }
        varsPrevious.clear();
        varsPrevious.putAll(varsCurrent);
        varsCurrent.clear();
      }
      for (TypeVariable tv : params) {
        parameterClasses.add(varsPrevious.get(tv));
      }
    }
    return parameterClasses;
  }

  /**
   * returns either the generic superclass or a generic interface, but only when it is assignable
   * from the typed classWithParameter argument
   *
   * @param <T>
   * @param subclass
   * @param classWithParameter
   * @return
   */
  public static <T> Type getGenericSuperType(
      Class<? extends T> subclass, Class<? extends T> classWithParameter) {
    if (subclass.getSuperclass() != null
        && classWithParameter.isAssignableFrom(subclass.getSuperclass())) {
      return subclass.getGenericSuperclass();
    } else {
      int i = 0;
      for (Class in : subclass.getInterfaces()) {
        if (classWithParameter.isAssignableFrom(in)) {
          return subclass.getGenericInterfaces()[i];
        }
        i++;
      }
      return null;
    }
  }

  /**
   * when possible return the class of the type argument, otherwise null
   *
   * @param type
   * @return
   */
  public static Class<?> getClass(Type type) {
    if (type instanceof Class) {
      return (Class<?>) type;
    } else if (type instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) type).getRawType();
    } else if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      Class<?> componentClass = getClass(componentType);
      if (componentClass != null) {
        return Array.newInstance(componentClass, 0).getClass();
      } else {
        return null;
      }
    } else if (type instanceof TypeVariable) {
      return null;
    } else if (type instanceof WildcardType) {
      return null;
    } else {
      return null;
    }
  }
}
