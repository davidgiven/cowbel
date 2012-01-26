/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionMediumParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ExpressionHighParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = PrefixOperatorParser.parse(location);
		if (pr2.success())
			return pr2;

		return combineParseErrors(pr1, pr2);
	}
}
