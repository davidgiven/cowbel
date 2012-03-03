/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.Collection;
import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.core.InterfaceContext;
import com.cowlark.cowbel.core.MethodTemplate;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.parser.core.Location;

public class InterfaceTypeNode extends AbstractTypeExpressionNode
{
	public InterfaceTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public InterfaceTypeNode(Location start, Location end,
			Collection<FunctionHeaderNode> entries)
    {
        super(start, end);
        addChildren(entries);
    }
		
	@Override
	public Interface createInterface() throws CompilationException
	{
		InterfaceContext tc = getTypeContext();
		Interface type = new Interface(tc, this);
		
		for (IsNode n : this)
		{
			FunctionHeaderNode fhn = (FunctionHeaderNode) n;
			MethodTemplate template = new MethodTemplate(tc, fhn, type);
			type.addMethodTemplate(template);
		}
		
		return type;
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}