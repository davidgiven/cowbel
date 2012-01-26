/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

public class StringType extends PrimitiveType
{
	private static StringType _instance =
		Type.canonicalise(new StringType());
	
	public static StringType create()
	{
		return _instance;
	}
	
	private StringType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "string";
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
