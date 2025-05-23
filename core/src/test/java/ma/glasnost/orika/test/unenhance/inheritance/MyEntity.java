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

package ma.glasnost.orika.test.unenhance.inheritance;

import javax.persistence.*;

@Entity
public class MyEntity extends AbstractEntity {
  @Column private String myProperty;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "references")
  private MyEntity references;

  public MyEntity() {}

  public String getMyProperty() {
    return myProperty;
  }

  public void setMyProperty(String myProperty) {
    this.myProperty = myProperty;
  }

  public MyEntity getReferences() {
    return references;
  }

  public void setReferences(MyEntity references) {
    this.references = references;
  }
}
