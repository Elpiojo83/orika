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

package ma.glasnost.orika.test.community.collection;

import java.util.ArrayList;
import java.util.List;

public class OrderData {
  private String name;
  private List<PositionData> positions;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<PositionData> getPositions() {
    return positions;
  }

  public void setPositions(List<PositionData> positions) {
    this.positions = positions;
  }

  public void add(PositionData data) {
    if (positions == null) {
      positions = new ArrayList<PositionData>();
    }
    positions.add(data);
  }
}
