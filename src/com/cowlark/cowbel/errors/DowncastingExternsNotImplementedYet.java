/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.types.AbstractConcreteType;

public class DowncastingExternsNotImplementedYet extends CompilationException
{
    private static final long serialVersionUID = 188517477180634642L;
    
	private IsNode _node;
	private AbstractConcreteType _ctype;
    
	public DowncastingExternsNotImplementedYet(IsNode node,
			AbstractConcreteType ctype)
    {
		_node = node;
		_ctype = ctype;
    }
	
	@Override
	public String getMessage()
	{
		return "Sorry, downcasting extern objects is not supported yet, at " +
			_node.locationAsString();
	}
}
