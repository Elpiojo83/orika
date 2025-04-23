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

package ma.glasnost.orika.test.community;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.test.MappingUtil;
import org.junit.Test;

import java.util.List;

/**
 * NPE on mapping nested field with collection.
 *
 * <p>
 *
 * @see <a
 *     href="https://code.google.com/archive/p/orika/issues/119">https://code.google.com/archive/p/orika/</a>
 */
public class Issue119TestCase {

  @Test
  public void test() {

    MapperFactory factory = MappingUtil.getMapperFactory(true);

    factory
        .classMap(Source.class, Dest.class)
        .mapNulls(false)
        .fieldAToB("id", "id")
        .fieldAToB("ref.identities{}", "references{identity}")
        .register();

    Source src = new Source();
    src.id = "myId";

    Dest dest = factory.getMapperFacade().map(src, Dest.class);
  }

  public static class Source {
    public String id;
    public SourceRef ref;
  }

  public static class SourceRef {
    public List<String> identities;
  }

  public static class Dest {
    public String id;
    public List<DestReference> references;
  }

  public static class DestReference {
    public String identity;
  }
}
