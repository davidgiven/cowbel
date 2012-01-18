package com.cowlark.sake.ast.nodes;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.sake.Constructor;
import com.cowlark.sake.Label;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.AmbiguousVariableReference;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.IdentifierNotFound;
import com.cowlark.sake.errors.MultipleDefinitionException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.symbols.Function;
import com.cowlark.sake.symbols.Symbol;

public class ScopeConstructorNode extends StatementNode
{
	private TreeSet<Symbol> _importedSymbols = new TreeSet<Symbol>();
	private TreeMap<Symbol, ScopeConstructorNode> _exportedSymbols = new TreeMap<Symbol, ScopeConstructorNode>();
	private TreeSet<ScopeConstructorNode> _importedScopes = new TreeSet<ScopeConstructorNode>();
	private TreeSet<ScopeConstructorNode> _exportedScopes = new TreeSet<ScopeConstructorNode>();
	private TreeSet<Symbol> _symbols = new TreeSet<Symbol>();
	private TreeMap<String, Label> _labels = new TreeMap<String, Label>();
	private Function _function;
	private Constructor _constructor;
	
	public ScopeConstructorNode(Location start, Location end, StatementNode child)
    {
        super(start, end);
        addChild(child);
    }

	public StatementNode getChild()
	{
		return (StatementNode) getChild(0);
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
	
	public Set<Label> getLabels()
	{
		return new TreeSet<Label>(_labels.values());
	}
	
	public Set<ScopeConstructorNode> getImportedScopes()
	{
		return _importedScopes;
	}
	
	public Set<ScopeConstructorNode> getExportedScopes()
	{
		return _exportedScopes;
	}
	
	@Override
	public String getShortDescription()
	{
		return "";
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
		
		if (!_exportedScopes.isEmpty())
		{
			spaces(indent);
			System.out.println("exported scopes:");
			for (ScopeConstructorNode e : _exportedScopes)
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
		
		if (!_importedScopes.isEmpty())
		{
			spaces(indent);
			System.out.println("imported scopes:");
			for (ScopeConstructorNode e: _importedScopes)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
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
		for (Symbol s : _symbols)
		{
			if (s.collidesWith(symbol))
				throw new MultipleDefinitionException(s, symbol);
		}
		
		_symbols.add(symbol);
	}
	
	/* Recurse all the way to the top. */
	public Symbol lookupVariable(IdentifierNode name) throws CompilationException
	{
		String s = name.getText();
		TreeSet<Symbol> results = new TreeSet<Symbol>();
		
		ScopeConstructorNode scope = this;
		while (scope != null)
		{
			results.clear();
			
			for (Symbol symbol : scope.getSymbols())
			{
				if (symbol.getName().equals(s))
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

	/* Recurse all the way to the top. */
	public Function lookupFunction(IdentifierNode name, int arguments)
			throws CompilationException
	{
		String s = Function.calculateMangledName(name, arguments);
		
		ScopeConstructorNode scope = this;
		while (scope != null)
		{
			for (Symbol symbol : scope.getSymbols())
			{
				if (symbol.getMangledName().equals(s))
				{
					assert(symbol instanceof Function);
					return (Function) symbol;
				}
			}
				
			scope = scope.getScope();
		}
				
		throw null;
	}

	private void recursive_import(ScopeConstructorNode root, ScopeConstructorNode leaf)
	{
		if (root == leaf)
			return;
		
		ScopeConstructorNode n = leaf;
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
		ScopeConstructorNode symscope = symbol.getScope();
		ScopeConstructorNode thisscope = this;
		
		_importedSymbols.add(symbol);
		symscope._exportedSymbols.put(symbol, this);
		
		recursive_import(symscope, thisscope);
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
		
		ScopeConstructorNode scope = this;
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
		ScopeConstructorNode s = _exportedSymbols.get(sym);
		if (s == null)
			return false;
		
		Function f = s.getFunction();
		return (f != _function);
	}
}