package com.cowlark.sake;

import java.util.Set;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;

public class DataflowAnalyser extends BasicBlockVisitor
{
	private boolean _finished;
	
	public static void initialiseFunction(Function f)
	{
		BasicBlock bb = f.getEntryBB();
		FunctionDefinitionNode node = (FunctionDefinitionNode) f.getNode();
		ParameterDeclarationListNode pdln = node.getFunctionHeader().getParametersNode();
		
		for (Node n : pdln.getChildren())
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			LocalVariable v = (LocalVariable) pdn.getSymbol();
			bb.getInputVariables().add(v);
		}
	}
	
	public boolean isFinished()
	{
		return _finished;
	}
	
	public void reset()
	{
		_finished = true;
	}
	
	public void visit(BasicBlock bb)
	{
		Set<LocalVariable> inputs = bb.getInputVariables();
		Set<LocalVariable> outputs = bb.getOutputVariables();
		Set<LocalVariable> defines = bb.getDefinedVariables();
		
		/* All variables this bb exports, it must import or define. */
		
		for (LocalVariable var : outputs)
		{
			if (!defines.contains(var))
				add_to_set(inputs, var);
		}
		
		/* All variables this bb imports, its predecessors must export. */
		
		for (LocalVariable var : inputs)
		{
			for (BasicBlock b : bb.getSourceBlocks())
				add_to_set(b.getOutputVariables(), var);
		}
		
		/* All variables this bb exports, its successors must import. */
		
		for (LocalVariable var : outputs)
		{
			for (BasicBlock b : bb.getDestinationBlocks())
				add_to_set(b.getInputVariables(), var);
		}
	}
	
	private boolean add_to_set(Set<LocalVariable> set, LocalVariable var)
	{
		if (!set.contains(var))
		{
			set.add(var);
			_finished = false;
			return true;
		}
		
		return false;
	}
}
