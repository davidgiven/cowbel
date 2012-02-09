/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;

public class IntegerType extends PrimitiveType
{
	private static IntegerType _instance = new IntegerType();
	
	public static IntegerType create()
	{
		return _instance;
	}
	
	private IntegerType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "integer";
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
