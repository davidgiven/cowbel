/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.IntegerConstantNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.MalformedIntegerConstant;

public class IntegerConstantParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		boolean negated = false;
		
		StringBuilder sb = new StringBuilder();
		MutableLocation end = new MutableLocation(location);
		int c = end.codepointAtOffset(0);
		if (c == '-')
		{
			negated = true;
			end.advance();
		}
			
		for (;;)
		{
			c = end.codepointAtOffset(0);
			
			if (!Character.isDigit(c))
				break;
			
			sb.append((char) c);
			end.advance();
		}

		long value;
		try
		{
			value = Long.parseLong(sb.toString());
		}
		catch (NumberFormatException e)
		{
			return new MalformedIntegerConstant(location);
		}
		
		if (negated)
			value = -value;
		
		return new IntegerConstantNode(location, end, value);
	}
}
