/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.types.Type;

public class FailedToInferTypeException extends CompilationException
{
    private static final long serialVersionUID = 1703271215661976233L;
    
    private Node _node;
	private Type _type;
    
	public FailedToInferTypeException(Node node, Type type)
    {
		_node = node;
		_type = type;
    }
}
