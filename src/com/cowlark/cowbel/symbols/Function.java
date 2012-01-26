package com.cowlark.cowbel.symbols;

import com.cowlark.cowbel.BasicBlock;
import com.cowlark.cowbel.BasicBlockBuilderVisitor;
import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.types.FunctionType;

public class Function extends Symbol
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
		_entryBB = new BasicBlock(this);
		_exitBB = new BasicBlock(this);
		
		BasicBlockBuilderVisitor visitor = new BasicBlockBuilderVisitor(this);
		FunctionDefinitionNode node = (FunctionDefinitionNode) getNode();
		node.getFunctionBody().visit(visitor);
		
		visitor.getCurrentBasicBlock().insnFunctionExit(node);
	}
	
	public static String calculateMangledName(IdentifierNode name, int arguments)
	{
		return name.getText() + "." + arguments;
	}
	
	private String _mangled_name;
	@Override
	public String getMangledName()
	{
		if (_mangled_name == null)
		{
			FunctionType type = (FunctionType) getSymbolType();
			
			_mangled_name = calculateMangledName(getSymbolName(),
					type.getInputArgumentTypes().size());
		}
		
		return _mangled_name;
	}
	
	@Override
	public boolean collidesWith(Symbol other)
	{
		if (other instanceof Variable)
			return getName().equals(other.getName());
		else
			return getMangledName().equals(other.getMangledName());
	}
	
	@Override
	public void addToConstructor(Constructor constructor)
	{
		constructor.addFunction(this);
	}
}
