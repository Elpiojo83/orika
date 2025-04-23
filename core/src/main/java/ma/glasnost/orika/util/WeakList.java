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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeakList<T> {

  private List<WeakReference<T>> items;

  public WeakList() {
    items = new ArrayList();
  }

  public void clear() {
    items.clear();
  }

  public WeakReference<T> add(int index, T element) {
    synchronized (items) {
      WeakReference<T> item = new WeakReference(element);
      items.add(index, item);
      return item;
    }
  }

  public WeakReference<T> add(T element) {
    synchronized (items) {
      WeakReference<T> wrapped = new WeakReference(element);
      items.add(wrapped);
      return wrapped;
    }
  }

  public int size() {
    removeReleased();
    return items.size();
  }

  public T getFirstValid() {
    synchronized (items) {
      int size = size();
      if (size > 0) {
        WeakReference<T> ref = (WeakReference) items.get(0);
        return ref.get();
      }
      return null;
    }
  }

  public T getLastValid() {
    synchronized (items) {
      int size = size();
      for (int i = size - 1; i >= 0; i--) {
        WeakReference<T> ref = (WeakReference) items.get(i);
        if (ref.get() == null) {
          items.remove(ref);
          ref.clear();
        } else return ref.get();
      }
      return null;
    }
  }

  public T get(int index) {
    return items.get(index).get();
  }

  public boolean has(T object) {
    if (object == null) return false;
    synchronized (items) {
      for (Iterator it = items.iterator(); it.hasNext(); ) {
        WeakReference ref = (WeakReference) it.next();
        if (ref.get() == object) {
          return true;
        }
      }
      return false;
    }
  }

  public boolean remove(T object) {
    synchronized (items) {
      for (Iterator it = items.iterator(); it.hasNext(); ) {
        WeakReference ref = (WeakReference) it.next();
        if (ref.get() == object) {
          items.remove(ref);
          ref.clear();
          return true;
        }
      }
      return false;
    }
  }

  public void removeReleased() {
    synchronized (items) {
      int size = items.size();
      for (int i = size - 1; i >= 0; i--) {
        WeakReference<T> ref = (WeakReference) items.get(i);
        if (ref.get() == null) {
          items.remove(ref);
          ref.clear();
        }
      }
    }
  }
}
