/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.backend.c;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.ExternStatementNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.backend.Backend;
import com.cowlark.cowbel.core.BasicBlock;
import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.core.Compiler;
import com.cowlark.cowbel.core.Constructor;
import com.cowlark.cowbel.core.Function;
import com.cowlark.cowbel.core.Method;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.InvalidExternTemplate;
import com.cowlark.cowbel.instructions.BooleanConstantInstruction;
import com.cowlark.cowbel.instructions.ConstructInstruction;
import com.cowlark.cowbel.instructions.CreateObjectReferenceInstruction;
import com.cowlark.cowbel.instructions.DirectFunctionCallInstruction;
import com.cowlark.cowbel.instructions.ExternFunctionCallInstruction;
import com.cowlark.cowbel.instructions.ExternInstruction;
import com.cowlark.cowbel.instructions.FunctionExitInstruction;
import com.cowlark.cowbel.instructions.GotoInstruction;
import com.cowlark.cowbel.instructions.HasInputVariables;
import com.cowlark.cowbel.instructions.HasOutputVariables;
import com.cowlark.cowbel.instructions.IfInstruction;
import com.cowlark.cowbel.instructions.Instruction;
import com.cowlark.cowbel.instructions.IntegerConstantInstruction;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.instructions.RealConstantInstruction;
import com.cowlark.cowbel.instructions.StringConstantInstruction;
import com.cowlark.cowbel.instructions.VarCopyInstruction;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.AbstractConcreteType;
import com.cowlark.cowbel.types.ExternObjectConcreteType;
import com.cowlark.cowbel.types.InterfaceConcreteType;
import com.cowlark.cowbel.types.ObjectConcreteType;

public class CBackend extends Backend
{
	private static Charset UTF8 = Charset.forName("utf-8");
	
	private HashMap<AbstractConcreteType, String> _typeTypes =
		new HashMap<AbstractConcreteType, String>();
	private HashMap<AbstractConcreteType, String> _typeLabels =
		new HashMap<AbstractConcreteType, String>();
	private HashMap<Method, String> _methodLabels =
		new HashMap<Method, String>();
	private HashMap<Constructor, String> _constructorTypes =
		new HashMap<Constructor, String>();
	private HashMap<Constructor, String> _constructorLabels =
		new HashMap<Constructor, String>();
	private HashMap<Function, String> _functionLabels =
		new HashMap<Function, String>();
	private HashMap<Symbol, String> _symbolLabels =
		new HashMap<Symbol, String>();
	private HashMap<BasicBlock, String> _bbLabels =
		new HashMap<BasicBlock, String>();
	private HashMap<StringConstantNode, String> _stringLabels =
		new HashMap<StringConstantNode, String>();
	
	private boolean _inputparamsinvalid;
	private int _funcid;
	private String _returnvalue;
	
	public CBackend(Compiler compiler, OutputStream os)
    {
	    super(compiler, os);
    }
	
	@Override
	public void setMainFunction(Function mainFunction)
	{
		_functionLabels.put(mainFunction, "cowbel_main");
	}
	
	@Override
	public void prologue() throws CompilationException
	{
		Compiler compiler = getCompiler();
        InputStream is = getClass().getResourceAsStream("prologue.h");
        print(is);
	    
        /* Find any #include externs. */
        
	    compiler.visit(
	    		new RecursiveASTVisitor()
	    		{
	    			@Override
                    public void visit(FunctionDefinitionNode node) throws CompilationException
	    			{
	    			};
	    			
	    			@Override
                    public void visit(ExternStatementNode node) throws CompilationException
	    			{
	    				String template = node.getTemplate();
	    				
	    				if (template.startsWith("#include"))
	    				{
	    					if (template.contains("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u0008\u0009"))
	    						throw new InvalidExternTemplate(node);
	    					
	    					print(template);
	    					print('\n');
	    				}
	    			};
	    		}
	    	);
	    print("\n");

	    /* Emit prototypes for interfaces, objects and constructors. */
	    
		for (InterfaceConcreteType t : InterfaceConcreteType.getAllInterfaceTypes())
		{
			print(ctypes(t));
			print(";\n");
		}
		for (ObjectConcreteType t : ObjectConcreteType.getAllObjectTypes())
		{
			print(ctypes(t));
			print(";\n");
		}
	    for (Constructor c : Constructor.getAllConstructors())
	    {
	    	print(ctype(c));
	    	print(";\n");
	    }
	    
	    /* Emit prototypes for functions. */
	    
	    for (Function f : Function.getAllFunctions())
	    {
	    	function_header(f);
	    	print(";\n");
	    }
	    print("\n");
	    
	    /* Emit string constants. */
	    
	    compiler.visit(
	    		new RecursiveASTVisitor()
	    		{
	    			@Override
                    public void visit(FunctionDefinitionNode node) throws CompilationException
	    			{
	    			};
	    			
	    			@Override
                    public void visit(StringConstantNode node)
	    				throws CompilationException
	    			{
	    				byte[] bytes = node.getValue().getBytes(UTF8);
	    				
	    				String id = "S" + clabel(node);
	    				print("static const char ");
	    				print(id);
	    				print("[] = {");
	    				boolean first = true;
	    				for (byte b : bytes)
	    				{
	    					if (!first)
	    						print(", ");
	    					first = false;

	    					print((int) b);
	    				}
	    				print("};\n");
	    				
	    				print("static s_string_t ");
	    				print(clabel(node));
	    				print(" = { NULL, NULL, ");
	    				print(id);
	    				print(", ");
	    				print(bytes.length);
	    				print(", ");
	    				print(bytes.length);
	    				print(", NULL};\n");
	    			}
	    		}
	    	);
	    print("\n");
	}
	
	@Override
	public void epilogue() throws CompilationException
	{
        InputStream is = getClass().getResourceAsStream("epilogue.h");
        print(is);
	}

	private String escape(String s)
	{
		StringBuilder sb = new StringBuilder();
		
		int length = s.length();
		int offset = 0;
		while (offset < length)
		{
			int cp = s.codePointAt(offset);
			if (Character.isJavaIdentifierPart(cp))
				sb.appendCodePoint(cp);
			else
				sb.append('_');
			
		   offset += Character.charCount(cp);
		}
		
		return sb.toString();
	}

	String ctype(Constructor constructor)
	{
		String s = _constructorTypes.get(constructor);
		if (s != null)
			return s;
		
		AbstractScopeConstructorNode node = constructor.getNode();
		Function f = node.getFunctionScope().getFunction();
		s = "struct C" + constructor.getId() + "_" +
			escape(f.getName().getText());
		
		_constructorTypes.put(constructor, s);
		return s;
	}
	
	private String clabel(Constructor constructor)
	{
		String s = _constructorLabels.get(constructor);
		if (s != null)
			return s;
		
		AbstractScopeConstructorNode node = constructor.getNode();
		Function f = node.getFunctionScope().getFunction();
		s = "c" + constructor.getId() + "_" +
			escape(f.getName().getText());
		
		_constructorLabels.put(constructor, s);
		return s;
	}
	
	private String clabel(Symbol symbol)
	{
		String s = _symbolLabels.get(symbol);
		if (s != null)
			return s;
		
		s = "s" + _symbolLabels.size() + "_" +
			escape(symbol.getIdentifier().getText());
		
		_symbolLabels.put(symbol, s);
		return s;
	}
	
	private String clabel(Function function)
	{
		String s = _functionLabels.get(function);
		if (s != null)
			return s;
		
		s = "f" + _symbolLabels.size() + "_" +
			escape(function.getName().getText());
		
		_functionLabels.put(function, s);
		return s;
	}
	
	private String clabel(StringConstantNode node)
	{
		String s = _stringLabels.get(node);
		if (s != null)
			return s;
		
		Function f = node.getScope().getFunction();
		s = "sc" + _stringLabels.size() +
			"_" + escape(f.getName().getText());
		
		_stringLabels.put(node, s);
		return s;
	}
	
	private String clabel(BasicBlock bb)
	{
		String s = _bbLabels.get(bb);
		if (s != null)
			return s;
		
		s = "B" + _bbLabels.size() + "_" + escape(bb.getFunction().getName().getText());
		
		_bbLabels.put(bb, s);
		return s;
	}
	
	private String ctype(Symbol symbol)
	{
		return ctype(symbol.getTypeRef().getConcreteType());
	}
	
	private String ctype(AbstractConcreteType t)
	{
		/* Order matters here */
		if (t instanceof InterfaceConcreteType)
			return ctype((InterfaceConcreteType) t);
		else if (t instanceof ExternObjectConcreteType)
			return ctype((ExternObjectConcreteType) t);
		else if (t instanceof ObjectConcreteType)
			return ctype((ObjectConcreteType) t);
		else
		{
			assert(false);
			throw null;
		}
	}
	
	String ctypes(InterfaceConcreteType type)
	{
		String s = _typeTypes.get(type);
		if (s != null)
			return s;
		
		String namehint = type.getSupportedInterfaces().iterator().next().getNameHint();
		if (namehint == null)
			namehint = "";
		else
			namehint = "_" + namehint;
		
	    s = "struct I" + type.getId() + namehint;
		_typeTypes.put(type, s);
		return s;
	}
	
	String ctype(InterfaceConcreteType type)
	{
		return ctypes(type) + "*";
	}
	
	String ctypes(ObjectConcreteType type)
	{
		return ctype(type.getImplementation().getNode().getConstructor());
	}
	
	String ctype(ObjectConcreteType type)
	{
		return ctypes(type) + "*";
	}
	
	String ctypes(ExternObjectConcreteType type)
	{
		return type.getExternName();
	}
	
	String ctype(ExternObjectConcreteType type)
	{
		return type.getExternName();
	}
	
	String clabel(AbstractConcreteType type)
	{
		String s = _typeLabels.get(type);
		if (s != null)
			return s;
		
		s = "t" + _typeLabels.size() + "_" + type.toString();
		_typeLabels.put(type, s);
		return s;
	}
	
	String clabel(Method method)
	{
		String s = _methodLabels.get(method);
		if (s != null)
			return s;

		s = "m" + method.getId() + "_" + method.getName().getText();
		
		_methodLabels.put(method, s);
		return s;
	}
	
	private String cid(String name)
	{
		return "f" + _funcid++ + "_" + name;
	}

	/* Produces an lvalue to a constructor. */
	
	private String clvalue(Node node, Constructor c)
	{
		StringBuilder sb = new StringBuilder();
		Constructor current = node.getScope().getConstructor();
		
		sb.append(clabel(current));
		if (current != c)
		{
			sb.append("->");
			sb.append(clabel(c));
		}
		
		return sb.toString();
	}
	
	/* Produces an lvalue to a variable's storage. */
	
	private String clvalue(Node node, Variable var)
	{
		StringBuilder sb = new StringBuilder();
		Constructor varcon = var.getConstructor();
		
		if (varcon.isStackVariable(var))
		{
			sb.append(clvalue(node, varcon));
			sb.append("->");
			sb.append(clabel(var));
		}
		else
		{
			if (var.isOutputParameter())
				sb.append("*");
			sb.append(clabel(var));
		}
		
		return sb.toString();
	}
	
	private void printvar(Node node, Variable var)
	{
		print(clvalue(node, var));
	}
	
	@Override
	public void visit(InterfaceConcreteType ct)
	{
		print(ctypes(ct));
		print("\n{\n");
		print("\tvoid* o;\n");
		
		for (Method m : ct.getMethods())
		{
			print("\t");
			method_header(m);
			print(";\n");
		}
		
		print("};\n\n");
	}
	
	@Override
	public void visit(ObjectConcreteType ct)
	{
	}
	
	@Override
	public void visit(ExternObjectConcreteType itype)
	{
	}
	
	@Override
	public void visit(Constructor constructor)
	{
		print(ctype(constructor));
		print("\n{\n");
		
		for (Variable v : constructor.getStackVariables())
		{
			print("\t");
			print(ctype(v));
			print(" ");
			print(clabel(v));
			print(";\n");
		}
		
		for (Constructor c : constructor.getParentConstructors())
		{
			print("\t");
			print(ctype(c));
			print("* ");
			print(clabel(c));
			print(";\n");
		}
		
		for (ObjectConcreteType t : constructor.getObjects())
		{
			if (t instanceof ExternObjectConcreteType)
			{
				ExternObjectConcreteType et = (ExternObjectConcreteType) t;
				print("\t");
				print(et.getExternName());
				print(" ");
				print(clabel(t));
				print(";\n\n");
			}
			else
			{
				print("\tstruct {\n");
				
				for (InterfaceConcreteType i : t.getDowncasts())
				{
					print("\t\t");
					print(ctypes(i));
					print(" ");
					print(clabel(i));
					print(";\n");
				}
				
				print("\t} ");
				print(clabel(t));
				print(";\n\n");
			}
		}
		
		print("};\n\n");
	}
	
	private void method_header(Method method)
	{
		FunctionHeaderNode fhn = method.getNode();
		ParameterDeclarationListNode inputs = fhn.getInputParametersNode();
		ParameterDeclarationListNode outputs = fhn.getOutputParametersNode();
		
		print("void (*");
		print(clabel(method));
		print(")(void*");

		for (int i = 0; i < inputs.getNumberOfChildren(); i++)
		{
			print(", ");
			print(ctype(inputs.getParameter(i).getTypeRef().getConcreteType()));
		}
		
		for (int i = 0; i < outputs.getNumberOfChildren(); i++)
		{
			print(", ");
			print(ctype(outputs.getParameter(i).getTypeRef().getConcreteType()));
			print("*");
		}
		
		print(")");
	}
	
	private String function_self(Function f)
	{
		AbstractScopeConstructorNode scope = f.getDefiningScope();
		if (scope == null)
			return null;
		return scope.getImplementation().getExternType();
	}
	
	private void function_header(Function f)
	{
		FunctionHeaderNode node = f.getNode();
		String externType = function_self(f);
		ParameterDeclarationListNode inparams = node.getInputParametersNode();
		ParameterDeclarationListNode outparams = node.getOutputParametersNode();
		
		print("static void ");
		print(clabel(f));
		print("(");

		boolean first = true;
		if (externType == null)
		{
			Constructor constructor = f.getConstructor();
			Constructor parent = constructor.getParentConstructor();
			if (parent != null)
			{
				print("void* v");
				print(clabel(parent));
				first = false;
			}
		}
		else
		{
			print(externType);
			print(" self");
			first = false;
		}
		
		for (IsNode n : inparams)
		{
			ParameterDeclarationNode p = (ParameterDeclarationNode) n;
			Symbol symbol = p.getSymbol();
			
			if (!first)
				print(", ");
			else
				first = false;
			
			print(ctype(symbol));
			print(" ");
			print(clabel(symbol));
		}
		
		for (IsNode n : outparams)
		{
			ParameterDeclarationNode p = (ParameterDeclarationNode) n;
			Symbol symbol = p.getSymbol();
			
			if (!first)
				print(", ");
			else
				first = false;
			
			print(ctype(symbol));
			print("* ");
			print(clabel(symbol));
		}
		
		print(")");
	}
	
	@Override
	public void compileFunction(Function f)
	{
		function_header(f);
		print("\n{\n");
		
		String externType = function_self(f);
		if (externType == null)
		{
			Constructor constructor = f.getConstructor();
			Constructor parent = constructor.getParentConstructor();
			if (parent != null)
			{
				print("\t");
				print(ctype(parent));
				print("* ");
				print(clabel(parent));
				print(" = v");
				print(clabel(parent));
				print(";\n");
			}
		}
		
		_inputparamsinvalid = true;
		_funcid = 0;
	    super.compileFunction(f);
		
		print("}\n\n");
	}
	
	@Override
	public void compileBasicBlock(BasicBlock bb)
	{
		print(clabel(bb));
		print(":;\n");
	    super.compileBasicBlock(bb);
	}
	
	@Override
	public void visit(FunctionExitInstruction insn)
	{
		print("\treturn;\n");
	}
	
	@Override
	public void visit(GotoInstruction insn)
	{
		print("\tgoto ");
		print(clabel(insn.getTarget()));
		print(";\n");
	}
	
	@Override
	public void visit(IfInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\tif (");
		printvar(node, insn.getCondition());
		print(") goto ");
		print(clabel(insn.getPositiveTarget()));
		print("; else goto ");
		print(clabel(insn.getNegativeTarget()));
		print(";\n");
	}
	
	@Override
	public void visit(ConstructInstruction insn)
	{
		Constructor constructor = insn.getConstructor();
		
		/* Allocate storage for this object. */
		
		if (constructor.isPersistent())
		{
			print("\t");
			print(ctype(constructor));
			print("* ");
			print(clabel(constructor));
			print(" = S_ALLOC_CONSTRUCTOR(");
			print(ctype(constructor));
			print(");\n");
		}
		else
		{
			String id = cid("storage");
			print("\t");
			print(ctype(constructor));
			print(" ");
			print(id);
			print(";\n");
			
			print("\t");
			print(ctype(constructor));
			print("* ");
			print(clabel(constructor));
			print(" = &");
			print(id);
			print(";\n");
		}
		
		/* Populate the object's parent constructors. */
		
		Constructor parent = constructor.getParentConstructor();
		if (parent != null)
		{
			for (Constructor c : constructor.getParentConstructors())
			{
				print("\t");
				print(clabel(constructor));
				print("->");
				print(clabel(c));
				print(" = ");
				if ((parent == c) ||
					(c.getNode().getFunctionScope() == constructor.getNode().getFunctionScope()))
					print(clabel(c));
				else
				{
					print(clabel(parent));
					print("->");
					print(clabel(c));
				}
				print(";\n");
			}
		}
		
		/* Declare register variables. (Unless they're function parameters,
		 * which are declared elsewhere.)
		 */
		
		for (Variable v : constructor.getRegisterVariables())
		{
			if (!v.isParameter())
			{
				print("\t");
				print(ctype(v));
				print(" ");
				print(clabel(v));
				print(";\n");
			}
		}
		
		/* Initialise any objects this constructor implements. */
		
		for (ObjectConcreteType oct : constructor.getObjects())
		{
			for (InterfaceConcreteType ict : oct.getDowncasts())
			{
				print("\t");
				print(clabel(constructor));
				print("->");
				print(clabel(oct));
				print(".");
				print(clabel(ict));
				print(".o = ");
				print(clabel(constructor));
				print(";\n");
				
				for (Method m : ict.getMethods())
				{
					Function f = oct.getFunctionForMethod(m);
					print("\t");
					print(clabel(constructor));
					print("->");
					print(clabel(oct));
					print(".");
					print(clabel(ict));
					print(".");
					print(clabel(m));
					print(" = ");
					print(clabel(f));
					print(";\n");
				}
			}
		}
		
		/* Copy any parameters into the constructor. */
		
		for (Variable v : constructor.getStackVariables())
		{
			if (v.isParameter())
			{				
				print("\t");
				print(clabel(constructor));
				print("->");
				print(clabel(v));
				print(" = ");
				print(clabel(v));
				print(";\n");
			}
		}
	}
	
	private <T extends Instruction & HasInputVariables & HasOutputVariables>
		void function_call(T insn, String callable, String constructor)
	{
		Node node = insn.getNode();
		
		print("\t");
		print(callable);
		print("(");
		print(constructor);
		
		for (Variable var : insn.getInputVariables())
		{
			print(", ");
			printvar(node, var);
		}
		
		for (Variable var : insn.getOutputVariables())
		{
			print(", &");
			printvar(node, var);
		}
		
		print(");\n");
	}
	
	@Override
	public void visit(DirectFunctionCallInstruction insn)
	{
		Node node = insn.getNode();
		Function function = insn.getFunction();
		Constructor parent = function.getConstructor().getParentConstructor();
		function_call(insn, clabel(function), clvalue(node, parent));
	}
	
	@Override
	public void visit(ExternFunctionCallInstruction insn)
	{
		Node node = insn.getNode();
		Function function = insn.getFunction();
		Variable receiver = insn.getReceiver();
		function_call(insn, clabel(function), clvalue(node, receiver));
	}
	
	@Override
	public void visit(MethodCallInstruction insn)
	{
		Callable callable = insn.getCallable();
		Variable receiver = insn.getReceiver();
		AbstractConcreteType ctype = receiver.getTypeRef().getConcreteType();
		
		String lvalue = clvalue(insn.getNode(), receiver);
		if (ctype instanceof InterfaceConcreteType)
		{
			String methodname = lvalue + "->" + clabel((Method) callable);
			String constructor = lvalue + "->o";
			function_call(insn, methodname, constructor);
		}
		else if (ctype instanceof ObjectConcreteType)
		{
			function_call(insn, clabel((Function) callable), lvalue);
		}
	}
	
	@Override
	public void visit(VarCopyInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = ");
		
		AbstractConcreteType srctype = insn.getInputVariable().getTypeRef().getConcreteType();
		AbstractConcreteType desttype = insn.getOutputVariable().getTypeRef().getConcreteType();
		if (srctype.equals(desttype))
			printvar(node, insn.getInputVariable());
		else
		{
			assert(desttype instanceof InterfaceConcreteType);
			
			InterfaceConcreteType destitype = (InterfaceConcreteType) desttype;
			
			print("&");
			printvar(node, insn.getInputVariable());
			print("->");
			print(clabel(destitype));
		}
		
		print(";\n");
	}
	
	@Override
	public void visit(ExternInstruction insn)
	{
		Node node = insn.getNode();
		String template = insn.getTemplate();
		List<Variable> vars = insn.getVariables();
		
		if (!template.startsWith("#include"))
		{
			for (int i=0; i<template.length(); i++)
			{
				char c = template.charAt(i);
				if (c < 10)
					printvar(node, vars.get((int) c));
				else
					print(c);
			}
			
			print("\n");
		}
	}
	
	@Override
	public void visit(CreateObjectReferenceInstruction insn)
	{
		Node node = insn.getNode();
		Variable var = insn.getOutputVariable();
		ObjectConcreteType ctype = (ObjectConcreteType) var.getTypeRef().getConcreteType();
		
		print("\t");
		printvar(node, var);
		print(" = ");
		if (ctype instanceof ExternObjectConcreteType)
		{
			print(clabel(insn.getConstructor()));
			print("->");
			print(clabel(ctype));
		}
		else
			print(clabel(insn.getConstructor()));
		print(";\n");
	}
	
	@Override
	public void visit(IntegerConstantInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = ");
		print(insn.getValue());
		print(";\n");
	}
	
	@Override
	public void visit(RealConstantInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = ");
		print(insn.getValue());
		print(";\n");
	}
	
	@Override
	public void visit(StringConstantInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = &");
		print(clabel((StringConstantNode) insn.getNode()));
		print(";\n");
	}
	
	@Override
	public void visit(BooleanConstantInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = ");
		if (insn.getValue())
			print("1");
		else
			print("0");
		print(";\n");
	}
}
