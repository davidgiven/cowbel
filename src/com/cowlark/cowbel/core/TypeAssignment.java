/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.utils.ById;
import com.cowlark.cowbel.utils.ComparableTreeMap;

public class TypeAssignment extends ComparableTreeMap<ById<IdentifierNode>, Interface>
{
    private static final long serialVersionUID = -2907880329764750153L;
}
