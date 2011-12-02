package com.cowlark.sake;

import com.cowlark.sake.ast.RecursiveVisitor;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
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

		ScopeNode ast = getAst();
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
				
				/* Set up the function definition's scope and storage. */
				
				ScopeNode body = node.getFunctionBody();
				LocalSymbolStorage storage = new LocalSymbolStorage();
				body.setSymbolStorage(storage);
				
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
