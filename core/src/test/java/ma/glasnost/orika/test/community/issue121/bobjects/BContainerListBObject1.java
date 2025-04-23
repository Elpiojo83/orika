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

/** This need, for example, for REST */
public class BContainerListBObject1 {

  private List<BObject1> list;

  public BContainerListBObject1() {}

  public List<BObject1> getList() {
    return list;
  }

  public void setList(List<BObject1> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return "BContainerListBObject1{" + "list=" + list + '}';
  }
}
