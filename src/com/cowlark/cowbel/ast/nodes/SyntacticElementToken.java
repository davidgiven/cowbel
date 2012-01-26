/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.Token;

public class SyntacticElementToken extends Token
{
	public SyntacticElementToken(Location start, Location end)
    {
        super(start, end);
    }
}
