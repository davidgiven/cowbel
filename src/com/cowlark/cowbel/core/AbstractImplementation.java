
package com.cowlark.cowbel.core;

import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsCallNode;
import com.cowlark.cowbel.utils.DeterministicObject;

public abstract class AbstractImplementation
		extends DeterministicObject<Implementation>
{
	protected TreeMap<FunctionTemplateSignature, FunctionTemplate> _templates = new TreeMap<FunctionTemplateSignature, FunctionTemplate>();
	private TreeMap<FunctionSignature, Function> _functions = new TreeMap<FunctionSignature, Function>();
	private TreeSet<Interface> _interfaces = new TreeSet<Interface>();

	public AbstractImplementation()
	{
	}

	public void addInterface(Interface i)
    {
    	_interfaces.add(i);
    }

	public TreeSet<Interface> getInterfaces()
    {
        return _interfaces;
    }

	/** Attempts to find an already instantiated function on this implementation.
	 * Returns null if one does not exist. */
	
    public Callable getMethod(FunctionSignature signature)
    {
    	return _functions.get(signature);
    }
    
	/** Attempts to find an already instantiated function on this implementation.
	 * Returns null if one does not exist. */
	
    public Callable getMethod(FunctionTemplateSignature signature, IsCallNode node)
    		throws CompilationException
    {
    	FunctionTemplate template = _templates.get(signature);
    	if (template == null)
    		return null;
    
    	FunctionSignature functionSignature = new FunctionSignature(signature,
    			template, node);
    	Function function = _functions.get(functionSignature);
    	if (function == null)
    		return null;
    	
    	return function;
    }
    
    /** Attempts to find an already instantiated function on this implementation.
     * Instantiates it if it does not exist. */
    
    public Callable lookupMethod(FunctionTemplateSignature signature, IsCallNode node)
            throws CompilationException
    {
    	FunctionTemplate template = _templates.get(signature);
    	if (template == null)
    		return null;
    
    	FunctionSignature functionSignature = new FunctionSignature(signature,
    			template, node);
    	Function function = _functions.get(functionSignature);
    	if (function == null)
    	{
    		function = template.instantiate(node, functionSignature,
    				functionSignature.getTypeAssignments());
    		_functions.put(functionSignature, function);
    	}
    	
    	return function;
    }
	
}
