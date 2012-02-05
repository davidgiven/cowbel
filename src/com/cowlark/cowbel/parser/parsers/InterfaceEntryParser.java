/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class InterfaceEntryParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult entrypr = FunctionHeaderParser.parse(location);
		if (entrypr.failed())
			return entrypr;
		
		ParseResult pr = SemicolonParser.parse(entrypr.end());
		if (pr.failed())
			return pr;
		
		/* Hack --- adjust end position of the function header node. */
		entrypr.setEnd(pr.end());
		
		return entrypr; 
	}
}
