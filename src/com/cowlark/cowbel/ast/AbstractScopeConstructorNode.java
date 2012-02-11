/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.Compiler;
import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.Function;
import com.cowlark.cowbel.FunctionTemplate;
import com.cowlark.cowbel.Label;
import com.cowlark.cowbel.errors.AmbiguousVariableReference;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.IdentifierNotFound;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.interfaces.HasInputs;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.interfaces.IsMethod;
import com.cowlark.cowbel.methods.FunctionMethod;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.methods.VirtualMethod;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.HasInterfaces;
import com.cowlark.cowbel.types.InterfaceType;

public abstract class AbstractScopeConstructorNode extends AbstractStatementNode
		implements HasInterfaces
{
	public enum ScopeType
	{
		TRIVIAL,
		SIGNIFICANT,
		PERSISTENT
	};
	
	private TreeSet<Symbol> _importedSymbols = new TreeSet<Symbol>();
	private TreeMap<Symbol, AbstractScopeConstructorNode> _exportedSymbols = new TreeMap<Symbol, AbstractScopeConstructorNode>();
	private TreeSet<Function> _importedFunctions = new TreeSet<Function>();
	private TreeMap<Function, AbstractScopeConstructorNode> _exportedFunctions = new TreeMap<Function, AbstractScopeConstructorNode>();
	private TreeSet<AbstractScopeConstructorNode> _importedScopes = new TreeSet<AbstractScopeConstructorNode>();
	private TreeSet<AbstractScopeConstructorNode> _exportedScopes = new TreeSet<AbstractScopeConstructorNode>();
	private TreeSet<Symbol> _symbols = new TreeSet<Symbol>();
	private TreeSet<Function> _functions = new TreeSet<Function>();
	private TreeSet<FunctionTemplate> _functionTemplates = new TreeSet<FunctionTemplate>();
	private TreeMap<VirtualMethod, Function> _virtualMethods = new TreeMap<VirtualMethod, Function>();
	private TreeMap<String, Label> _labels = new TreeMap<String, Label>();
	private TreeSet<InterfaceType> _interfaces = new TreeSet<InterfaceType>();
	private Function _function;
	private Constructor _constructor;
	private ScopeType _scopeType = ScopeType.TRIVIAL;
	
	public AbstractScopeConstructorNode(Location start, Location end)
    {
        super(start, end);
    }

	public AbstractScopeConstructorNode(Location start, Location end, AbstractStatementNode child)
    {
        super(start, end);
        addChild(child);
    }

	public AbstractStatementNode getChild()
	{
		return (AbstractStatementNode) getChild(0);
	}
	
	public ScopeType getScopeType()
    {
	    return _scopeType;
    }
	
	public void setScopeType(ScopeType scopeType)
    {
		if (scopeType.ordinal() > _scopeType.ordinal())
			_scopeType = scopeType;
    }
	
	public boolean isFunctionScope()
	{
		return false;
	}
	
	public Function getFunction()
	{
		return _function;
	}
	
	public void setFunction(Function function)
	{
		_function = function;
	}
	
	public Constructor getConstructor()
	{
		return _constructor;
	}
	
	public void setConstructor(Constructor constructor)
	{
		_constructor = constructor;
	}
	
	public Set<Symbol> getSymbols()
	{
		return _symbols;
	}
	
	public Set<FunctionTemplate> getFunctionTemplates()
	{
		return _functionTemplates;
	}
	
	public Collection<Label> getLabels()
	{
		return Collections.unmodifiableCollection(_labels.values());
	}
	
	public Set<AbstractScopeConstructorNode> getImportedScopes()
	{
		return _importedScopes;
	}
	
	public Set<AbstractScopeConstructorNode> getExportedScopes()
	{
		return _exportedScopes;
	}
	
	@Override
    public Collection<InterfaceType> getInterfaces()
    {
	    return Collections.unmodifiableCollection(_interfaces);
    }
	
	@Override
	public String getShortDescription()
	{
		return ": " + _scopeType.toString();
	}
	
	@Override
	public void dumpDetails(int indent)
	{
		if (_function != null)
		{
			spaces(indent);
			System.out.print("function: ");
			System.out.println(_function.toString());
		}
		
		if (_constructor != null)
		{
			spaces(indent);
			System.out.print("stack frame: ");
			System.out.println(_constructor.toString());
		}
		
		if (!_symbols.isEmpty())
		{
			spaces(indent);
			System.out.println("defined here:");
			for (Symbol s: _symbols)
			{
				spaces(indent+1);
				System.out.println(s.toString());
			}
		}
		
		if (!_exportedSymbols.isEmpty())
		{
			spaces(indent);
			System.out.println("exported symbols:");
			for (Symbol e : _exportedSymbols.keySet())
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_exportedFunctions.isEmpty())
		{
			spaces(indent);
			System.out.println("exported functions:");
			for (Function e : _exportedFunctions.keySet())
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_exportedScopes.isEmpty())
		{
			spaces(indent);
			System.out.println("exported scopes:");
			for (AbstractScopeConstructorNode e : _exportedScopes)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_importedSymbols.isEmpty())
		{
			spaces(indent);
			System.out.println("imported symbols:");
			for (Symbol e: _importedSymbols)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_importedFunctions.isEmpty())
		{
			spaces(indent);
			System.out.println("imported functions:");
			for (Function e: _importedFunctions)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_importedScopes.isEmpty())
		{
			spaces(indent);
			System.out.println("imported scopes:");
			for (AbstractScopeConstructorNode e: _importedScopes)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_interfaces.isEmpty())
		{
			spaces(indent);
			System.out.println("interfaces implemented here:");
			for (InterfaceType i : _interfaces)
			{
				spaces(indent+1);
				System.out.println(i.toString());
			}
		}
	}
	
	private FunctionScopeConstructorNode _functionScope;
	public FunctionScopeConstructorNode getFunctionScope()
	{
		if (_functionScope == null)
			_functionScope = getScope().getFunctionScope();
		return _functionScope;
	}
	
	public void addSymbol(Symbol symbol) throws CompilationException
	{
		String symbolname = symbol.getIdentifier().getText();
		
		for (Symbol s : _symbols)
		{
			if (s.getIdentifier().getText().equals(symbolname))
				throw new MultipleDefinitionException(s, symbol);
		}
		
		for (FunctionTemplate ft : _functionTemplates)
		{
			if (ft.getName().equals(symbolname))
				throw new MultipleDefinitionException(ft, symbol);
		}
		
		_symbols.add(symbol);
	}
	
	/* Recurse all the way to the top. */
	public Symbol lookupVariable(IdentifierNode name) throws CompilationException
	{
		String s = name.getText();
		TreeSet<Symbol> results = new TreeSet<Symbol>();
		
		AbstractScopeConstructorNode scope = this;
		while (scope != null)
		{
			results.clear();
			
			for (Symbol symbol : scope.getSymbols())
			{
				if (symbol.getIdentifier().getText().equals(s))
					results.add(symbol);
			}
			
			switch (results.size())
			{
				case 1:
					return results.first();
					
				default:
					throw new AmbiguousVariableReference(this, name, results);
					
				case 0:
					break;
			}
				
			scope = scope.getScope();
		}
				
		throw new IdentifierNotFound(this, name);
	}

	public void addFunctionTemplate(FunctionTemplate template)
			throws CompilationException
	{
		String name = template.getName();
		for (Symbol s : _symbols)
		{
			if (s.getIdentifier().getText().equals(name))
				throw new MultipleDefinitionException(s, template);
		}
		
		String signature = template.getSignature();
		for (FunctionTemplate ft : _functionTemplates)
		{
			if (ft.getSignature().equals(signature))
				throw new MultipleDefinitionException(ft, template);
		}
		
		_functionTemplates.add(template);
	}
	
	/* Recurse all the way to the top. */
	public <T extends Node & HasInputs & HasIdentifier & HasTypeArguments>
		Function lookupFunction(T node)
			throws CompilationException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(node.getIdentifier().getText());
		sb.append("<");
		sb.append(node.getTypeArguments().getNumberOfChildren());
		sb.append(">(");
		sb.append(node.getInputs().getNumberOfChildren());
		sb.append(")");
		String signature = sb.toString();

		AbstractScopeConstructorNode scope = this;
		while (scope != null)
		{
			for (FunctionTemplate ft : scope.getFunctionTemplates())
			{
				if (ft.getSignature().equals(signature))
				{
					Function function = ft.instantiate(node, node.getTypeArguments());
					scope.addFunction(function);
					return function;
				}
			}
				
			scope = scope.getScope();
		}
				
		return null;
	}

	/** Looks up a method on the specified scope and returns it. Used for
	 * direct class calls only (*not* virtual calls via interfaces).
	 * 
	 * Currently just looks at the current scope. */

	public <T extends Node & HasInputs & IsMethod & HasTypeArguments>
		Method lookupMethod(T node)
			throws CompilationException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(node.getMethodIdentifier().getText());
		sb.append("<");
		sb.append(node.getTypeArguments().getNumberOfChildren());
		sb.append(">(");
		sb.append(node.getInputs().getNumberOfChildren());
		sb.append(")");
		String signature = sb.toString();

		for (FunctionTemplate ft : getFunctionTemplates())
		{
			if (ft.getSignature().equals(signature))
			{
				Function function = ft.instantiate(node,
						node.getTypeArguments());
				addFunction(function);
				return new FunctionMethod(function);
			}
		}

		return null;
	}
	
	public Method lookupMethod(Node node, VirtualMethod method)
			throws CompilationException
	{
		String signature = method.getName();
	
		for (FunctionTemplate ft : getFunctionTemplates())
		{
			if (ft.getSignature().equals(signature))
			{
				Function function = ft.instantiate(node,
						method.getTypeAssignments());
				addVirtualMethod(method, function);
				return new FunctionMethod(function);
			}
		}
	
		return null;
	}

	private void recursive_import(AbstractScopeConstructorNode root, AbstractScopeConstructorNode leaf)
	{
		if (root == leaf)
			return;
		
		AbstractScopeConstructorNode n = leaf;
		for (;;)
		{
			n._importedScopes.add(root);
			root._exportedScopes.add(n);
			
			if (n.getScope() == root)
				break;
			n = n.getScope();
		}
		
		recursive_import(n, leaf);
	}
	
	public void importSymbol(Symbol symbol)
	{
		AbstractScopeConstructorNode symscope = symbol.getScope();
		AbstractScopeConstructorNode thisscope = this;
		
		/* No need to import symbols from the current scope. */
		
		if (symscope == thisscope)
			return;
		
		_importedSymbols.add(symbol);
		symscope._exportedSymbols.put(symbol, this);
		
		recursive_import(symscope, thisscope);
	}
	
	public void importFunction(Function function)
	{
		AbstractScopeConstructorNode funcscope = function.getDefiningScope();
		AbstractScopeConstructorNode thisscope = this;
		
		/* No need to import symbols from the current scope. */
		
		if (funcscope == thisscope)
			return;
		
		_importedFunctions.add(function);
		funcscope._exportedFunctions.put(function, this);
		
		recursive_import(funcscope, thisscope);
	}
	
	public void addLabel(Label label) throws CompilationException
	{
		IdentifierNode name = label.getLabelName();
		String s = name.getText();
		
		if (_labels.containsKey(s))
		{
			Label oldlabel = _labels.get(s);
			throw new MultipleDefinitionException(oldlabel, label);
		}
		
		_labels.put(s, label);
	}
	
	/* Recurse only up to the next function scope. */
	public Label lookupLabel(IdentifierNode name) throws CompilationException
	{
		String s = name.getText();
		
		AbstractScopeConstructorNode scope = this;
		while (scope != null)
		{
			Label label = scope._labels.get(s);
			if (label != null)
				return label;
			
			if (scope.isFunctionScope())
				break;
			
			scope = scope.getScope();
		}
		
		throw new IdentifierNotFound(this, name);
	}
	
	@Override
	public boolean isLoopingNode()
	{
		return !_labels.isEmpty();
	}
	
	public boolean isSymbolExportedToDifferentFunction(Symbol sym)
	{
		AbstractScopeConstructorNode s = _exportedSymbols.get(sym);
		if (s == null)
			return false;
		
		Function f = s.getFunction();
		return (f != _function);
	}
	
	public void addInterface(InterfaceType itype) throws CompilationException
	{
		itype.addImplementation(this);
		_interfaces.add(itype);
		Compiler.Instance.addInterface(itype);
	}
	
	public void addFunction(Function function)
	{
		_functions.add(function);
	}
	
	public Collection<Function> getFunctions()
	{
		return Collections.unmodifiableCollection(_functions);
	}
	
	public void addVirtualMethod(VirtualMethod method, Function function)
	{
		addFunction(function);
		_virtualMethods.put(method, function);
	}
	
	public Collection<Map.Entry<VirtualMethod, Function>> getVirtualMethods()
	{
		return Collections.unmodifiableCollection(_virtualMethods.entrySet());
	}
}