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

import java.util.Set;

/** @author Dmitriy Khomyakov */
public class UserDto extends BaseDto {

  private String name;
  // private String password;
  private UserGroupDto group;

  private Set<AuthorityDto> authorities;

  public UserDto() {}

  public UserDto(String name, UserGroupDto group) {
    this.name = name;
    this.group = group;
  }

  public Set<AuthorityDto> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<AuthorityDto> authorities) {
    this.authorities = authorities;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserGroupDto getGroup() {
    return group;
  }

  public void setGroup(UserGroupDto group) {
    this.group = group;
  }

  @Override
  public String toString() {
    return "UserDto{"
        + "name='"
        + name
        + '\''
        +
        // ", password='" + password + '\'' +
        "} "
        + super.toString();
  }
}
