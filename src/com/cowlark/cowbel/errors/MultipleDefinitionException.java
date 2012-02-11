/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.interfaces.HasNode;

public class MultipleDefinitionException extends CompilationException
{
    private static final long serialVersionUID = 5324462605948584281L;

    private Node _old;
    private Node _current;
    
	public MultipleDefinitionException(HasNode old, HasNode current)
    {
		_old = old.getNode();
		_current = current.getNode();
    }
	
	@Override
	public String getMessage()
	{
		HasIdentifier oldidnode = (HasIdentifier) _old;
		IdentifierNode oldid = oldidnode.getIdentifier();
		HasIdentifier currentidnode = (HasIdentifier) _current;
		IdentifierNode currentid = currentidnode.getIdentifier();
		
		return "The definition of the symbol '"+currentid.getText()+"' at " +
			currentid.locationAsString() + " was previously defined at " +
			oldid.locationAsString() + ".";
	}
}
