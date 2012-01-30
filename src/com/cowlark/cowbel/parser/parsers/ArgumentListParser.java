/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ArgumentListParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenParenthesisParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult argumentspr = ExpressionListParser.parse(pr.end());
		if (argumentspr.failed())
			return argumentspr;
		
		pr = CloseParenthesisParser.parse(argumentspr.end());
		if (pr.failed())
			return pr;
		
		/* Very nasty */
		argumentspr.setEnd(pr.end());
		return argumentspr;
	}
}
