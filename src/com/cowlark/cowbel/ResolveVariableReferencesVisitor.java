package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.ast.nodes.VarReferenceNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.IdentifierNotFound;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Symbol;

public class ResolveVariableReferencesVisitor extends RecursiveVisitor
{
	@Override
	public void visit(GotoStatementNode node) throws CompilationException
	{
		ScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getLabelName();
		Label label = scope.lookupLabel(in);
		
		node.setLabel(label);
	    super.visit(node);
	}
	
	@Override
	public void visit(DirectFunctionCallNode node) throws CompilationException
	{
		ScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getIdentifier();
		
		/* Try to look this up as a function first. */
		
		Symbol sym = scope.lookupFunction(in, node.getArguments().size());
		if (sym == null)
		{
			/* If that failed, treat it as a variable reference (doing an
			 * indirect function call via a delegate). */
			
			sym = scope.lookupVariable(in);
		}
		
		node.setSymbol(sym);
	    super.visit(node);
	}
	@Override
	public void visit(VarReferenceNode node)
	        throws CompilationException
	{
		ScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getVariableName();
		Symbol symbol = scope.lookupVariable(in);
		
		if (scope != symbol.getScope())
			scope.importSymbol(symbol);
		
		node.setSymbol(symbol);
	    super.visit(node);
	}
	
	@Override
	public void visit(VarAssignmentNode node)
	        throws CompilationException
	{
		ScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getVariableName();
		Symbol symbol = scope.lookupVariable(in);
		
		if (scope != symbol.getScope())
			scope.importSymbol(symbol);
		
		node.setSymbol(symbol);
	    super.visit(node);
	}
}
