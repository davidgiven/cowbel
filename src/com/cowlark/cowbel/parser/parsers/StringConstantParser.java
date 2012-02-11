/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.ExpectedStringConstant;
import com.cowlark.cowbel.parser.errors.InvalidCharacterInStringConstant;

public class StringConstantParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		int delimiter = location.codepointAtOffset(0);
		if ((delimiter != '"') && (delimiter != '\''))
			return new ExpectedStringConstant(location);
		
		StringBuilder sb = new StringBuilder();
		MutableLocation end = new MutableLocation(location);
		end.advance();
		
		for (;;)
		{
			int c = end.codepointAtOffset(0);
			end.advance();
			
			if (c == delimiter)
				break;
			
			switch (c)
			{
				case '\\':
				{
					c = end.codepointAtOffset(0);
					end.advance();

					switch (c)
					{
						case 'n':
							sb.appendCodePoint('\n');
							break;
							
						case 'r':
							sb.appendCodePoint('\r');
							break;
					
						case '\\':
						case '"':
						case '\'':
							sb.appendCodePoint(c);
							break;
							
						default:
							return new InvalidCharacterInStringConstant(end);
					}
					break;
				}

				case -1:
				case '\n':
				case '\r':
					return new InvalidCharacterInStringConstant(end);
					
				default:
					sb.appendCodePoint(c);
			}
		}
		
		return new StringConstantNode(location, end, sb.toString());
	}
}
