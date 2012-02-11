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
import com.cowlark.cowbel.BasicBlock;
import com.cowlark.cowbel.Compiler;
import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.Function;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.backend.Backend;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.instructions.BooleanConstantInstruction;
import com.cowlark.cowbel.instructions.ConstructInstruction;
import com.cowlark.cowbel.instructions.CreateObjectReferenceInstruction;
import com.cowlark.cowbel.instructions.DirectFunctionCallInstruction;
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
import com.cowlark.cowbel.methods.FunctionMethod;
import com.cowlark.cowbel.methods.PrimitiveMethod;
import com.cowlark.cowbel.methods.VirtualMethod;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.HasInterfaces;
import com.cowlark.cowbel.types.InterfaceType;
import com.cowlark.cowbel.types.Type;

public class CBackend extends Backend
{
	private static Charset UTF8 = Charset.forName("utf-8");
	
	private HashMap<InterfaceType, String> _interfaceTypes =
		new HashMap<InterfaceType, String>();
	private HashMap<InterfaceType, String> _interfaceLabels =
		new HashMap<InterfaceType, String>();
	private HashMap<VirtualMethod, String> _methodLabels =
		new HashMap<VirtualMethod, String>();
	private HashMap<Constructor, String> _constructorTypes =
		new HashMap<Constructor, String>();
	private HashMap<Constructor, String> _constructorLabels =
		new HashMap<Constructor, String>();
	private HashMap<Function, String> _functionLabels =
		new HashMap<Function, String>();
	private HashMap<Symbol, String> _symbolLabels =
		new HashMap<Symbol, String>();
	private HashMap<Type, String> _typeLabels =
		new HashMap<Type, String>();
	private HashMap<BasicBlock, String> _bbLabels =
		new HashMap<BasicBlock, String>();
	private HashMap<StringConstantNode, String> _stringLabels =
		new HashMap<StringConstantNode, String>();
	
	private boolean _inputparamsinvalid;
	private int _funcid;
	private CTypeNameBuilder _typeNameBuilder = new CTypeNameBuilder(this);
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
	    
        /* Emit prototypes for constructors. */
        
	    for (Constructor c : compiler.getConstructors())
	    {
	    	print(ctype(c));
	    	print(";\n");
	    }
	    
        /* Emit prototypes for interfaces. */
        
	    for (InterfaceType i : compiler.getInterfaces())
	    {
	    	print(ctype(i));
	    	print(";\n");
	    }
	    print("\n");
	    
	    /* Emit prototypes for functions. */
	    
	    for (Function f : compiler.getFunctions())
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
		return ctype(symbol.getType());
	}
	
	String ctype(Type type)
	{
		String s = _typeLabels.get(type);
		if (s != null)
			return s;
		
		s = _typeNameBuilder.buildName(type);
		_typeLabels.put(type, s);
		return s;
	}
	
	String ctype(InterfaceType type)
	{
		String s = _interfaceTypes.get(type);
		if (s != null)
			return s;

		s = "struct I" + _interfaceTypes.size() + "_" + type.getId() +
			"_" + type.toString();
		
		_interfaceTypes.put(type, s);
		return s;
	}
	
	String clabel(InterfaceType type)
	{
		String s = _interfaceLabels.get(type);
		if (s != null)
			return s;

		s = "i" + _interfaceTypes.size() + "_" + type.getId() +
			"_" + type.toString();
		
		_interfaceLabels.put(type, s);
		return s;
	}
	
	String clabel(VirtualMethod method)
	{
		String s = _methodLabels.get(method);
		if (s != null)
			return s;

		s = "m" + _methodLabels.size() + "_" + method.getId();
		
		_methodLabels.put(method, s);
		return s;
	}
	
	private String cid(String name)
	{
		return "f" + _funcid++ + "_" + name;
	}

	/* Produces an lvalue to the constructor. */
	
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
	
	/* Produces an lvalue to the variable's storage. */
	
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
	public void visit(InterfaceType itype)
	{
		print(ctype(itype));
		print("\n{\n");
		print("\tvoid* o;\n");
		
		for (VirtualMethod m : itype.getMethods())
		{
			print("\t");
			method_header(m);
			print(";\n");
		}
		
		print("};\n\n");
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
		
		for (InterfaceType i : constructor.getInterfaces())
		{
			print("\t");
			print(ctype(i));
			print(" ");
			print(clabel(i));
			print(";\n");
		}
		
		print("};\n\n");
	}
	
	private void method_header(VirtualMethod method)
	{
		List<Type> intypes = method.getInputTypes();
		List<Type> outtypes = method.getOutputTypes();
		
		print("void (*");
		print(clabel(method));
		print(")(void*");

		for (Type t : intypes)
		{
			print(", ");
			print(ctype(t));
		}
		
		for (Type t : outtypes)
		{
			print(", ");
			print(ctype(t));
			print("*");
		}
		
		print(")");
	}
	
	private void function_header(Function f)
	{
		FunctionDefinitionNode node = (FunctionDefinitionNode) f.getNode();
		ParameterDeclarationListNode inparams = node.getFunctionHeader().getInputParametersNode();
		ParameterDeclarationListNode outparams = node.getFunctionHeader().getOutputParametersNode();
		
		print("static void ");
		print(clabel(f));
		print("(");

		boolean first = true;
		Constructor constructor = f.getConstructor();
		Constructor parent = constructor.getParentConstructor();
		if (parent != null)
		{
			print("void* v");
			print(clabel(parent));
			first = false;
		}
		
		for (Node n : inparams)
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
		
		for (Node n : outparams)
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
			print(" = ");
			print(" &");
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
		
		/* Initialise any interfaces the constructor implements. */
		
		for (InterfaceType i : constructor.getInterfaces())
		{
			print("\t");
			print(clabel(constructor));
			print("->");
			print(clabel(i));
			print(".o = ");
			print(clabel(constructor));
			print(";\n");
			
			for (VirtualMethod vm : i.getMethods())
			{
				Function f = constructor.getFunctionForVirtualMethod(vm);
				print("\t");
				print(clabel(constructor));
				print("->");
				print(clabel(i));
				print(".");
				print(clabel(vm));
				print(" = ");
				print(clabel(f));
				print(";\n");
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
		function_call(insn, clabel(function),
				clvalue(node, function.getConstructor().getParentConstructor()));
	}
	
	@Override
	public void visit(MethodCallInstruction insn, PrimitiveMethod method)
	{
		Node node = insn.getNode();
        
        print("\tS_METHOD_");
        String methodsig = method.getIdentifier();
        print(methodsig.replace('.', '_').toUpperCase());

        print("(");
        printvar(node, insn.getReceiver());
        
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
	public void visit(MethodCallInstruction insn, FunctionMethod method)
	{
		Function function = method.getFunction();
		function_call(insn, clabel(function),
				clabel(function.getConstructor().getParentConstructor()));
	}
	
	@Override
	public void visit(MethodCallInstruction insn, VirtualMethod method)
	{
		Variable var = insn.getReceiver();
		String lvalue = clvalue(insn.getNode(), var);
		String callable = lvalue + "->" + clabel(method);
		String constructor = lvalue + "->o";
		function_call(insn, callable, constructor);
	}
	
	@Override
	public void visit(VarCopyInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = ");
		
		Type srctype = insn.getInputVariable().getType().getConcreteType();
		Type desttype = insn.getOutputVariable().getType().getConcreteType();
		if (srctype.equals(desttype))
			printvar(node, insn.getInputVariable());
		else
		{
			if (desttype instanceof InterfaceType)
			{
				InterfaceType destitype = (InterfaceType) desttype;
				assert(srctype instanceof HasInterfaces);
				
				print("&");
				printvar(node, insn.getInputVariable());
				print("->");
				print(clabel(destitype));
			}
		}
		
		print(";\n");
	}
	
	@Override
	public void visit(ExternInstruction insn)
	{
		Node node = insn.getNode();
		String template = insn.getTemplate();
		List<Variable> vars = insn.getVariables();
		
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
	
	@Override
	public void visit(CreateObjectReferenceInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = ");
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
