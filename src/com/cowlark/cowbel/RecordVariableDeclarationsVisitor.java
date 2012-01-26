package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Variable;

public class RecordVariableDeclarationsVisitor extends RecursiveVisitor
{
	private Node _rootNode;
	
	public RecordVariableDeclarationsVisitor(Node rootNode)
    {
		_rootNode = rootNode;
    }
	
	@Override
	public void visit(FunctionDefinitionNode node)
	        throws CompilationException
	{
		/* Add this function to the current scope. */
		
		Function f = new Function(node);
		node.getScope().addSymbol(f);
		node.setSymbol(f);
		
		/* Add function parameters to its scope. */
		
		ParameterDeclarationListNode pdln = node.getFunctionHeader().getParametersNode();
		FunctionScopeConstructorNode body = node.getFunctionBody();
		body.setFunction(f);
		f.setScope(body);
		
		for (Node n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;

			Variable v = new Variable(pdn);
			v.setParameter(true);
			v.setScope(body);
			body.addSymbol(v);
			pdn.setSymbol(v);
		}

		super.visit(node);
	}
	
	@Override
	public void visit(VarDeclarationNode node)
	        throws CompilationException
	{
		/* Add this symbol to the current scope. */
		
		ScopeConstructorNode scope = node.getScope();
		Variable v = new Variable(node);
		v.setScope(scope);
		scope.addSymbol(v);
		node.setSymbol(v);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(LabelStatementNode node) throws CompilationException
	{
		/* Add this label to the current scope. */
		
		ScopeConstructorNode scope = node.getScope();
		Label label = new Label(node);
		
		scope.addLabel(label);
		node.setLabel(label);
		
	    super.visit(node);
	}
}
