package com.cowlark.sake;

import com.cowlark.sake.ast.RecursiveVisitor;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FailedParseException;
import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.nodes.Parser;

public class Compiler
{
	private Location _input;
	private ParseResult _ast;
	private GlobalSymbolStorage _globals = new GlobalSymbolStorage();
	
	public Compiler()
    {
    }
	
	public void setInput(Location input)
	{
		_input = input;
	}
	
	public void compile() throws CompilationException
	{
		_ast = Parser.ProgramParser.parse(_input);
		
		if (_ast.failed())
		{
			FailedParse fp = (FailedParse) _ast;
			throw new FailedParseException(fp);
		}

		final ScopeNode ast = getAst();
		ast.setSymbolStorage(_globals);
		
		RecursiveVisitor scopeVisitor = new RecursiveVisitor()
		{
			@Override
			public void visit(FunctionDefinitionNode node)
			        throws CompilationException
			{
				/* Add this function to the current scope. */
				
				Function f = new Function(node);
				node.getScope().addSymbol(f);
				node.setSymbol(f);
				
				/* Set up the function definition's scope and storage. */
				
				ScopeNode body = node.getFunctionBody();
				LocalSymbolStorage storage = new LocalSymbolStorage();
				body.setSymbolStorage(storage);
				
				/* Add function parameters to its scope. */
				
				ParameterDeclarationListNode pdln = node.getFunctionHeader().getParametersNode();
				for (Node n : pdln.getChildren())
				{
					ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;

					Variable v = new LocalVariable(pdn);
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
				
				ScopeNode scope = node.getScope();
				Variable v;
				if (scope == ast)
					v = new GlobalVariable(node);
				else
					v = new LocalVariable(node);
				
				scope.addSymbol(v);
				node.setSymbol(v);
				
			    super.visit(node);
			}
		};
		
		ast.visit(scopeVisitor);
	}

	public ScopeNode getAst()
	{
		return (ScopeNode) _ast;
	}
}
