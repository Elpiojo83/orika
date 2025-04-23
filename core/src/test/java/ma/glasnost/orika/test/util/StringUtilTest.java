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

package ma.glasnost.orika.test.util;

import ma.glasnost.orika.impl.util.StringUtil;
import org.junit.Test;

import static ma.glasnost.orika.impl.util.StringUtil.toValidVariableName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilTest {
  private static final String FOO_UNCAP = "foo";
  private static final String FOO_CAP = "Foo";

  @Test
  public void to_valid_variable_name_replaces_all_invalid_characters_with_underscore() {
    assertEquals("foo_bar_baz", toValidVariableName("foo/bar baz"));
  }

  @Test
  public void to_valid_variable_name_add_underscore__when_string_start_with_number() {
    assertEquals("_42", toValidVariableName("42"));
  }

  /** Remark: Copied from commons-lang3 v3.5 */
  @Test
  public void testUnCapitalize() {
    assertNull(StringUtil.uncapitalize(null));

    assertEquals("uncapitalize(String) failed", FOO_UNCAP, StringUtil.uncapitalize(FOO_CAP));
    assertEquals("uncapitalize(string) failed", FOO_UNCAP, StringUtil.uncapitalize(FOO_UNCAP));
    assertEquals("uncapitalize(empty-string) failed", "", StringUtil.uncapitalize(""));
    assertEquals("uncapitalize(single-char-string) failed", "x", StringUtil.uncapitalize("X"));

    // Examples from uncapitalize Javadoc
    assertEquals("cat", StringUtil.uncapitalize("cat"));
    assertEquals("cat", StringUtil.uncapitalize("Cat"));
    assertEquals("cAT", StringUtil.uncapitalize("CAT"));
  }
}
