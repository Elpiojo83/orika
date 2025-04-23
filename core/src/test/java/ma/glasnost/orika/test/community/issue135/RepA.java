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

package ma.glasnost.orika.test.community.issue135;

public class RepA {

  private String id;
  private int primitive;
  private Boolean active;
  private RepB repB;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RepB getRepB() {
    return repB;
  }

  public void setRepB(RepB repB) {
    this.repB = repB;
  }

  public int getPrimitive() {
    return primitive;
  }

  public void setPrimitive(int primtive) {
    this.primitive = primtive;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
