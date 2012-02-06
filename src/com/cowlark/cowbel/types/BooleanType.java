/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

public class BooleanType extends PrimitiveType
{
	private static BooleanType _instance = new BooleanType();
	
	public static BooleanType create()
	{
		return _instance;
	}
	
	private BooleanType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "boolean";
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
