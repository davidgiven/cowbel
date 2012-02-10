/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.cowlark.cowbel.ast.nodes.IntegerConstantNode;
import com.cowlark.cowbel.ast.nodes.RealConstantNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.MalformedIntegerConstant;

public class NumericConstantParser extends Parser
{
	private static Pattern _number_pattern =
			Pattern.compile("([-+]?[0-9]*)(\\.[0-9]+([eE][-+]?[0-9]+)?)?");
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		Matcher matcher = _number_pattern.matcher(location);
		if (matcher.find())
		{
			String integerpart = matcher.group(1);
			String realpart = matcher.group(2);
			
			try
			{
				MutableLocation ml = new MutableLocation(location);
				if (realpart == null)
				{
					/* Integer constant */
					
					ml.advance(integerpart.length());
					
					long value = Long.valueOf(integerpart);
					return new IntegerConstantNode(location, ml, value);
				}
				else
				{
					String s = integerpart + realpart;
					ml.advance(s.length());
					
					double value = Double.valueOf(s);
					return new RealConstantNode(location, ml, value);
				}
			}
			catch (NumberFormatException e)
			{
				return new MalformedIntegerConstant(location);
			}			
		}
		else
			return new MalformedIntegerConstant(location);
	}
}
