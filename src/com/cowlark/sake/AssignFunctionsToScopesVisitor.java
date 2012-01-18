package com.cowlark.sake;

import java.util.Set;
import com.cowlark.sake.ast.RecursiveVisitor;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.symbols.Function;

public class AssignFunctionsToScopesVisitor extends RecursiveVisitor
{
	private Set<Function> _functions;
	
	public AssignFunctionsToScopesVisitor(Set<Function> functions)
	{
		_functions = functions;
	}
	
	@Override
	public void visit(ScopeConstructorNode node) throws CompilationException
	{
		if (node.isFunctionScope())
			_functions.add(node.getFunction());
		else
		{
			Function f = node.getScope().getFunction();
			node.setFunction(f);
		}
		
		super.visit(node);
	}
}
