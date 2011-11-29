package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.nodes.Parser;

public class Compiler
{
	private Location _input;
	private ParseResult _ast;
	
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
	}

	public Node getAst()
	{
		return (Node) _ast;
	}
}
