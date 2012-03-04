/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.io.PrintStream;
import java.util.Collection;
import java.util.TreeSet;
import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.core.FunctionTemplateSignature;
import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsCallNode;
import com.cowlark.cowbel.utils.DeterministicObject;

public abstract class AbstractConcreteType
		extends DeterministicObject<AbstractConcreteType>
{
	public static TreeSet<AbstractConcreteType> _allConcreteTypes =
		new TreeSet<AbstractConcreteType>();
	
	public static Collection<AbstractConcreteType> getAllConcreteTypes()
	{
		return _allConcreteTypes;
	}

	public AbstractConcreteType()
    {	
		_allConcreteTypes.add(this);
    }
	
	protected abstract Collection<Interface> getSupportedInterfaces();
	
	public void checkTypeConstraints(TypeRef tr)
			throws CompilationException
	{
		Collection<Interface> supportedinterfaces = getSupportedInterfaces();
		
		for (Interface interf : tr.getConstraints())
		{
//			if (!supportedinterfaces.contains(interf))
//				throw new TypesNotCompatibleException(tr.getNode(), tr, interf);
		}
	}
	
	public abstract Callable getCallable(FunctionTemplateSignature fts, IsCallNode node)
			throws CompilationException;
	
	/** Informs this ACT that it must be downcastable to this interface. */
	
	public abstract void supportsInterface(InterfaceConcreteType ctype);
	
	/** Dumps this type to the specified stream. */
	
	public void dump(PrintStream ps)
	{
		ps.println(toString());
	}
}
