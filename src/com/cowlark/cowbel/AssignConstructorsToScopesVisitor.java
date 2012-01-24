package com.cowlark.cowbel;

import java.util.Set;
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.ForStatementNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Function;

public class AssignConstructorsToScopesVisitor extends RecursiveVisitor
{
	private Set<Constructor> _constructors;
	
	public AssignConstructorsToScopesVisitor(Set<Constructor> constructors)
    {
		_constructors = constructors;
    }
	
	private void assign_stackframe(ScopeConstructorNode node,
			boolean persistent)
	{
		if (node.getConstructor() != null)
			return;
		
		Constructor sf = new Constructor(node);
		_constructors.add(sf);
		node.setConstructor(sf);
		
		if (persistent)
			sf.setPersistent(true);
	}
	
	private void inherit_stackframe(ScopeConstructorNode node)
	{
		if (node.getConstructor() != null)
			return;
		
		Constructor sf = node.getScope().getConstructor();
		node.setConstructor(sf);
	}
	
	private boolean is_complex_scope(ScopeConstructorNode node)
	{
		/* If this scope is exporting to a scope that's part of a different
		 * function, it's complex. */
		
		Function thisfunction = node.getFunction();
		for (ScopeConstructorNode s : node.getExportedScopes())
		{
			Function f = s.getFunction();
			if (f != thisfunction)
				return true;
		}
		
		return false;
	}

	@Override
	public void visit(ForStatementNode node) throws CompilationException
	{
		ScopeConstructorNode body = node.getBodyStatement();
		if (is_complex_scope(body))
			assign_stackframe(body, true);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(WhileStatementNode node) throws CompilationException
	{
		ScopeConstructorNode body = node.getBodyStatement();
		if (is_complex_scope(body))
			assign_stackframe(body, true);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(DoWhileStatementNode node) throws CompilationException
	{
		ScopeConstructorNode body = node.getBodyStatement();
		if (is_complex_scope(body))
			assign_stackframe(body, true);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(ScopeConstructorNode node) throws CompilationException
	{
		if (node.isFunctionScope())
		{
			assign_stackframe(node, is_complex_scope(node));
		}
		else if (!node.getLabels().isEmpty() && is_complex_scope(node))
			assign_stackframe(node, true);
		else
			inherit_stackframe(node);
		
		super.visit(node);
	}
}
