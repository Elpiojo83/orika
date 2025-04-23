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

class Edge<T> {
  public final Node<T> from;
  public final Node<T> to;

  public Edge(Node<T> from, Node<T> to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public boolean equals(Object obj) {
    Edge<?> e = (Edge<?>) obj;
    return e.from == from && e.to == to;
  }

  @Override
  public int hashCode() {
    return to.hashCode() ^ from.hashCode();
  }

  @Override
  public String toString() {
    return from.toString() + " -> " + to.toString();
  }
}
