package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.types.FunctionType;

public class Function extends GlobalVariable
{
	private BasicBlock _entryBB;
	private BasicBlock _exitBB;
	
	public Function(FunctionDefinitionNode node)
    {
		super(node, node.getFunctionHeader().getFunctionName(),
				node.getFunctionHeader().getFunctionType());
    }
	
	public Function()
    {
		super(null, null, FunctionType.createVoidVoid());
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
		_entryBB = new BasicBlock();
		_exitBB = new BasicBlock();
		
		BasicBlockBuilderVisitor visitor = new BasicBlockBuilderVisitor(this);
		FunctionDefinitionNode node = (FunctionDefinitionNode) getNode();
		node.getFunctionBody().visit(visitor);
	}
}
