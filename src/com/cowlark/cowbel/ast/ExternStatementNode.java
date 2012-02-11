/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.List;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ExternStatementNode extends AbstractStatementNode
{
	private String _template;
	
	public ExternStatementNode(Location start, Location end,
			String template)
	{
		super(start, end);
		_template = template;
	}
	
	public ExternStatementNode(Location start, Location end,
			String template,
			List<VarReferenceNode> vars)
    {
        super(start, end);
        addChildren(vars);
        _template = template;
    }
	
	public String getTemplate()
	{
		return _template;
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
