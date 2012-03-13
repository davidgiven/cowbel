/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FunctionParameterMismatch;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasOutputs;
import com.cowlark.cowbel.interfaces.HasScope;
import com.cowlark.cowbel.interfaces.IsExpressionNode;
import com.cowlark.cowbel.interfaces.IsMethodCallNode;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.utils.DeterministicObject;

public class Callable extends DeterministicObject<Callable>
		implements HasNode, HasScope
{
	private FunctionSignature _signature;
	private FunctionHeaderNode _node;
	private AbstractScopeConstructorNode _scope;
	
	public Callable(FunctionSignature signature, FunctionHeaderNode node)
		throws CompilationException
    {
		_signature = signature;
		_node = node;
		
		copyTypeRefs(node.getInputParametersNode());
		copyTypeRefs(node.getOutputParametersNode());
    }
	
	private void copyTypeRefs(ParameterDeclarationListNode pdln)
		throws CompilationException
	{
		for (IsNode n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			TypeRef typeref = pdn.getTypeRef();
			typeref.addCastConstraint(pdn.getVariableTypeNode().getInterface());
		}
	}
	
	@Override
	public String toString()
	{
	    return getName().getText() + "#" + getId();
	}
	
	public FunctionSignature getSignature()
    {
	    return _signature;
    }
	
	@Override
	public FunctionHeaderNode getNode()
    {
	    return _node;
    }
	
	@Override
    public AbstractScopeConstructorNode getScope()
    {
	    return _scope;
    }
	
	public void setScope(AbstractScopeConstructorNode scope)
    {
	    _scope = scope;
    }
	
	public AbstractScopeConstructorNode getDefiningScope()
	{
		return _node.getScope();
	}
	
	public InterfaceContext getTypeContext()
    {
	    return _node.getTypeContext();
    }
	
	public IdentifierNode getName()
	{
		return _node.getFunctionName();
	}
	
	public void wireTypesToCall(IsMethodCallNode methodcall)
			throws CompilationException
	{
		/* parent can be downcast to child.
		 * parent = caller (methodcall), child == callee (header). */
		FunctionHeaderNode header = getNode();
		boolean success =
			Utils.attachInputTypeConstraints(methodcall,
				header.getInputParametersNode(), methodcall);
		if (methodcall instanceof HasOutputs)
			success = success &&
				Utils.attachOutputTypeConstraints(methodcall,
					header.getOutputParametersNode(),
					(HasOutputs) methodcall);
		else
			success = success &&
				Utils.attachSingleOutputTypeConstraint(
					(IsExpressionNode) methodcall,
					header.getOutputParametersNode());

		if (!success)
			throw new FunctionParameterMismatch(methodcall, this);
	}
	
	public void wireTypesToHeader(FunctionHeaderNode header)
	{
		FunctionHeaderNode impl = getNode();
		boolean success =
			Utils.attachInputTypeConstraints(impl,
				impl.getInputParametersNode(), header.getInputParametersNode());
		
		success = success &&
			Utils.attachOutputTypeConstraints(impl,
				impl.getOutputParametersNode(), header.getOutputParametersNode());

		assert(success);
	}
}
