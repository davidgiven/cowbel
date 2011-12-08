package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.CheckAndInferStatementTypesVisitor;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public abstract class StatementNode extends Node
{
	public StatementNode(Location start, Location end)
    {
        super(start, end);
    }
	
	private static Visitor _check_and_infer_statement_types_visitor =
		new CheckAndInferStatementTypesVisitor();
	
	public void checkTypes() throws CompilationException
	{
		visit(_check_and_infer_statement_types_visitor);
	}
	
}