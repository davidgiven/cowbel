/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.CheckAndInferExpressionTypesVisitor;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.Type;

public abstract class AbstractExpressionNode extends Node
{
	private static Visitor _check_and_infer_expression_types_visitor =
		new CheckAndInferExpressionTypesVisitor();

	private Type _type = null;
	
	public AbstractExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public final Type calculateType() throws CompilationException
	{
		if (_type == null)
			visit(_check_and_infer_expression_types_visitor);
		
		return _type;
	}
	
	public final Type getType()
	{
		assert(_type != null);
		return _type;
	}
	
	public void setType(Type type)
	{
		assert(_type == null);
		_type = type;
	}
}