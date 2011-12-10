package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FailedParseException;
import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.parsers.Parser;

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
		
		ast.visit(new RecordVariableDeclarationsVisitor(ast));
		ast.visit(new ResolveVariableReferencesVisitor());

		ast.checkTypes();
		for (Function f : _globals.getFunctions())
		{
			FunctionDefinitionNode node = (FunctionDefinitionNode) f.getNode();
			node.getFunctionBody().checkTypes();
		}
	}

	public ScopeNode getAst()
	{
		return (ScopeNode) _ast;
	}
}
