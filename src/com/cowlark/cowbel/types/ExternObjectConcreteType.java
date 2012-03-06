/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.io.PrintStream;
import java.util.Collection;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.BlockExpressionNode;

public class ExternObjectConcreteType extends ObjectConcreteType
{
	public static TreeSet<ExternObjectConcreteType> _allExternObjectTypes =
		new TreeSet<ExternObjectConcreteType>();
	
	public static Collection<ExternObjectConcreteType> getAllExternObjectTypes()
	{
		return _allExternObjectTypes;
	}

	private String _externType;
	
	public ExternObjectConcreteType(BlockExpressionNode node, String externType)
    {
		super(node);
		_externType = externType;
    }
		
	@Override
    public void dump(PrintStream ps)
	{
		super.dump(ps);
		ps.println("  extern type:");
		ps.println("    " + _externType);
	}

	/** Get the extern name for this type. */
	
    public String getExternName()
    {
		return getImplementation().getExternType();
    }
}
