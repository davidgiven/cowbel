package com.cowlark.sake;

import com.cowlark.sake.ast.RecursiveVisitor;
import com.cowlark.sake.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.symbols.Symbol;

public class AssignVariablesToConstructorsVisitor extends RecursiveVisitor
{
	@Override
	public void visit(ScopeConstructorNode node) throws CompilationException
	{
		Constructor sf = node.getConstructor();
		for (Symbol s : node.getSymbols())
			s.addToConstructor(sf);

		for (ScopeConstructorNode s : node.getImportedScopes())
			sf.addConstructor(s.getConstructor());
		
		super.visit(node);
	}
}
