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
package ma.glasnost.orika.test.community.issue28;

public abstract class AbstractOrder<T extends AbstractOrder<T>> extends PositionContainer<T> {
  private static final long serialVersionUID = 3L;

  private Long number;

  public AbstractOrder() {
    this(null);
  }

  public AbstractOrder(Long number) {
    this.number = number;
  }

  public Long getId() {
    return number;
  }
}
