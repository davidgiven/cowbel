/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasScope;
import com.cowlark.cowbel.types.FunctionType;

public class Function implements Comparable<Function>, HasNode, HasScope
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	private String _signature;
	private FunctionDefinitionNode _node;
	private AbstractScopeConstructorNode _scope;
	private FunctionType _type;
	private BasicBlock _entryBB;
	private BasicBlock _exitBB;
	
	public Function(String signature, FunctionDefinitionNode node)
		throws CompilationException
    {
		_signature = signature;
		_node = node;
    }
	
	@Override
	public int compareTo(Function o)
	{
	    if (_id < o._id)
	    	return -1;
	    if (_id > o._id)
	    	return 1;
	    return 0;
	}
	
	@Override
	public String toString()
	{
	    return getSignature() + " " + getName().getText();
	}
	
	@Override
	public FunctionDefinitionNode getNode()
    {
	    return _node;
    }
	
	public String getSignature()
    {
	    return _signature;
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
	
	public FunctionType getType()
    {
	    return _type;
    }

	public void setType(FunctionType type)
    {
	    _type = type;
    }
	
	public TypeContext getTypeContext()
    {
	    return _node.getTypeContext();
    }
	
	public IdentifierNode getName()
	{
		return _node.getFunctionHeader().getFunctionName();
	}
	
	public FunctionScopeConstructorNode getBody()
	{
		return _node.getFunctionBody();
	}
	
	public Constructor getConstructor()
	{
		return getBody().getConstructor();
	}
	
	public BasicBlock getEntryBB()
	{
		return _entryBB;
	}
	
	public BasicBlock getExitBB()
	{
		return _exitBB;
	}
	
	public void buildBasicBlocks() throws CompilationException
	{
		_entryBB = new BasicBlock(this);
		_exitBB = new BasicBlock(this);
		
		BasicBlockBuilderVisitor visitor = new BasicBlockBuilderVisitor(this);
		_node.getFunctionBody().visit(visitor);
		
		visitor.getCurrentBasicBlock().insnGoto(_node, _exitBB);
		_exitBB.insnFunctionExit(_node);
	}
	
}
