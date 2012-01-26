/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.types.Type;

public class Utils
{
	static boolean unifyTypeLists(Node node,
			List<Type> funclist, List<Type> calllist) throws CompilationException
	{
		if (funclist.size() != calllist.size())
			return false;
		
		try
		{
			for (int i = 0; i < funclist.size(); i++)
			{
				Type t1 = funclist.get(i);
				Type t2 = calllist.get(i);
				t1.unifyWith(node, t2);
				t2.ensureConcrete(node);
			}
		}
		catch (TypesNotCompatibleException e)
		{
			return false;
		}
		
		return true;	
	}
}
