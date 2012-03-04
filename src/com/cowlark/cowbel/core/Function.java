/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Collection;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasScope;

public class Function extends Callable
		implements HasNode, HasScope
{
	private static TreeSet<Function> _allFunctions =
		new TreeSet<Function>();
	
	public static Collection<Function> getAllFunctions()
	{
		return _allFunctions;
	}
	
	private FunctionDefinitionNode _definition;
	private BasicBlock _entryBB;
	private BasicBlock _exitBB;
	
	public Function(FunctionSignature signature, FunctionDefinitionNode node)
		throws CompilationException
    {
		super(signature, node.getFunctionHeader());
		_definition = node;
		
		_allFunctions.add(this);
    }
	
	public FunctionDefinitionNode getDefinition()
	{
		return _definition;
	}
	
	public FunctionScopeConstructorNode getBody()
	{
		return _definition.getFunctionBody();
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
		_definition.getFunctionBody().visit(visitor);
		
		visitor.getCurrentBasicBlock().insnGoto(_definition, _exitBB);
		_exitBB.insnFunctionExit(_definition);
	}
	
}
