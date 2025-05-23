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
package ma.glasnost.orika.test.community.issue21;

import javax.persistence.*;
import java.util.Set;

/** @author Dmitriy Khomyakov */
@Entity
public class User extends BaseEntity {
  private UserGroup group;
  private String name;
  private String password;
  private Set<Authority> authorities;

  public User(String name) {
    this(name, name);
  }

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }

  public User() {}

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  public UserGroup getGroup() {
    return group;
  }

  public void setGroup(UserGroup group) {
    this.group = group;
  }

  @Column(unique = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ManyToMany
  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" + "name='" + name + '\'' + "} " + super.toString();
  }
}
