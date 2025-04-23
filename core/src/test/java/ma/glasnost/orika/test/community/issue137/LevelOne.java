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

package ma.glasnost.orika.test.community.issue137;

import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: tgruenheit Date: 06.12.13 Time: 19:34 To change this template
 * use File | Settings | File Templates.
 */
public class LevelOne {

  private Set<LevelTwo> levelTwos;

  public Set<LevelTwo> getLevelTwos() {
    return levelTwos;
  }

  public void setLevelTwos(Set<LevelTwo> levelTwos) {
    this.levelTwos = levelTwos;
  }
}
