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

import java.util.List;

/**
 * @author: Ilya Krokhmalyov YC14IK1
 * @since: 8/23/13
 */
public class BObject2Container {

  private List<BObject2> list;

  public List<BObject2> getList() {
    return list;
  }

  public void setList(List<BObject2> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return "BObject2Container{" + "list=" + list + '}';
  }
}
