/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.CheckAndInferStatementTypesVisitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public abstract class AbstractStatementNode extends Node
{
	public AbstractStatementNode(Location start, Location end)
    {
        super(start, end);
    }
	
	private static ASTVisitor _check_and_infer_statement_types_visitor =
		new CheckAndInferStatementTypesVisitor();
	public void checkTypes() throws CompilationException
	{
		visit(_check_and_infer_statement_types_visitor);
	}
	
}