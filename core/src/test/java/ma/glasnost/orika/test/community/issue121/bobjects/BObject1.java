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

package ma.glasnost.orika.test.community.issue121.bobjects;

/**
 * @author: Ilya Krokhmalyov YC14IK1
 * @since: 8/23/13
 */
public class BObject1 {
  private Integer id;
  private String name;
  private Integer key;
  private BObject2Container container;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getKey() {
    return key;
  }

  public void setKey(Integer key) {
    this.key = key;
  }

  public BObject2Container getContainer() {
    return container;
  }

  public void setContainer(BObject2Container container) {
    this.container = container;
  }

  @Override
  public String toString() {
    return "BObject1{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", key="
        + key
        + ", container="
        + container
        + '}';
  }
}
