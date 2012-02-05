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
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.ArrayConstructorNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.StringConstantNode;
import com.cowlark.cowbel.backend.ImperativeBackend;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.instructions.ArrayConstructorInstruction;
import com.cowlark.cowbel.instructions.BooleanConstantInstruction;
import com.cowlark.cowbel.instructions.ConstructInstruction;
import com.cowlark.cowbel.instructions.CreateObjectReferenceInstruction;
import com.cowlark.cowbel.instructions.DirectFunctionCallInstruction;
import com.cowlark.cowbel.instructions.FunctionExitInstruction;
import com.cowlark.cowbel.instructions.GotoInstruction;
import com.cowlark.cowbel.instructions.HasInputVariables;
import com.cowlark.cowbel.instructions.HasOutputVariables;
import com.cowlark.cowbel.instructions.IfInstruction;
import com.cowlark.cowbel.instructions.Instruction;
import com.cowlark.cowbel.instructions.IntegerConstantInstruction;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.instructions.StringConstantInstruction;
import com.cowlark.cowbel.instructions.VarCopyInstruction;
import com.cowlark.cowbel.methods.FunctionMethod;
import com.cowlark.cowbel.methods.PrimitiveMethod;
import com.cowlark.cowbel.methods.VirtualMethod;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.ArrayType;
import com.cowlark.cowbel.types.Type;

public class CBackend extends ImperativeBackend
{
	private static Charset UTF8 = Charset.forName("utf-8");
	
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
	
	private int _funcid;
	private CTypeNameBuilder _typeNameBuilder = new CTypeNameBuilder(this);
	private String _returnvalue;
	
	public CBackend(Compiler compiler, OutputStream os)
    {
	    super(compiler, os);
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
	    		new RecursiveVisitor()
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
		s = "struct C" + _constructorTypes.size() + "_" +
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
		s = "c" + _constructorLabels.size() + "_" +
			escape(f.getName().getText());
		
		_constructorLabels.put(constructor, s);
		return s;
	}
	
	private String clabel(Symbol symbol)
	{
		String s = _symbolLabels.get(symbol);
		if (s != null)
			return s;
		
		s = "s" + _symbolLabels.size() + "_" + escape(symbol.getName());
		
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
		return ctype(symbol.getSymbolType());
	}
	
	private String ctype(Type type)
	{
		String s = _typeLabels.get(type);
		if (s != null)
			return s;
		
		s = _typeNameBuilder.buildName(type);
		_typeLabels.put(type, s);
		return s;
	}
	
	private String cid(String name)
	{
		return "f" + _funcid++ + "_" + name;
	}

	/* Produces an lvalue to the variable's storage. */
	
	private void printvar(Node node, Variable var)
	{
		Constructor varcon = var.getConstructor();
		
		if (varcon.isStackVariable(var))
		{
			Constructor current = node.getScope().getConstructor();
			
			print(clabel(current));
			if (current != varcon)
			{
				print("->");
				print(clabel(varcon));
			}
			print("->");
			print(clabel(var));
		}
		else
		{
			if (var.isOutputParameter())
				print("*");
			print(clabel(var));
		}
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
		
		print("};\n\n");
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
			print(ctype(parent));
			print("* ");
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
	}
	
	private <T extends Instruction & HasInputVariables & HasOutputVariables>
		void function_call(T insn, Function function)
	{
		Node node = insn.getNode();
		
		print("\t");
		print(clabel(function));
		print("(");
		print(clabel(function.getConstructor().getParentConstructor()));
		
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
		Function function = insn.getFunction();
		function_call(insn, function);
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
		function_call(insn, function);
	}
	
	@Override
	public void visit(MethodCallInstruction insn, VirtualMethod method)
	{
		assert(false);
		throw null;
	}
	
	@Override
	public void visit(ArrayConstructorInstruction insn)
	{
		ArrayConstructorNode node = (ArrayConstructorNode) insn.getNode();
		ArrayType type = (ArrayType) node.getType();
		List<Variable> values = insn.getValues();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = S_CONSTRUCT_ARRAY(");
		print(ctype(type.getChildType()));
		print(", ");
		print(values.size());
		print(");\n");

		if (values.size() > 0)
		{
			print("\tS_INIT_ARRAY(");
			printvar(node, insn.getOutputVariable());
			
			for (Variable v : values)
			{
				print(", ");
				printvar(node, v);
			}
			print(");\n");
		}
	}
	
	@Override
	public void visit(VarCopyInstruction insn)
	{
		Node node = insn.getNode();
		
		print("\t");
		printvar(node, insn.getOutputVariable());
		print(" = ");
		printvar(node, insn.getInputVariable());
		print(";\n");
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
