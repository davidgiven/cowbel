/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.interfaces.HasMethods;
import com.cowlark.cowbel.interfaces.HasNode;

public class Implementation extends AbstractImplementation 
		implements HasNode, HasMethods
{
	protected AbstractScopeConstructorNode _node;
	private String _externType;

	public Implementation(AbstractScopeConstructorNode node)
	{
		_node = node;
	}
	
	@Override
    public AbstractScopeConstructorNode getNode()
    {
    	return _node;
    }

	public void addMethodTemplate(FunctionTemplate template)
			throws CompilationException
	{
		FunctionTemplateSignature signature = new FunctionTemplateSignature(
				template.getNode());
		if (_templates.containsKey(signature))
			throw new MultipleDefinitionException(
					_templates.get(signature), template);
		
		_templates.put(signature, template);
	}
	
	public void addExternType(String externType)
			throws CompilationException
	{
		assert(_externType == null);
		_externType = externType;
	}
	
	public String getExternType()
    {
	    return _externType;
    }
}
