package com.cowlark.sake.backend.c;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import com.cowlark.sake.BasicBlock;
import com.cowlark.sake.Compiler;
import com.cowlark.sake.Constructor;
import com.cowlark.sake.ast.RecursiveVisitor;
import com.cowlark.sake.ast.nodes.ArrayConstructorNode;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;
import com.cowlark.sake.ast.nodes.ReturnStatementNode;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.ast.nodes.StringConstantNode;
import com.cowlark.sake.backend.ImperativeBackend;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.instructions.ArrayConstructorInstruction;
import com.cowlark.sake.instructions.BooleanConstantInstruction;
import com.cowlark.sake.instructions.ConstructInstruction;
import com.cowlark.sake.instructions.DirectFunctionCallInstruction;
import com.cowlark.sake.instructions.DiscardInstruction;
import com.cowlark.sake.instructions.FunctionExitInstruction;
import com.cowlark.sake.instructions.GetLocalInstruction;
import com.cowlark.sake.instructions.GetUpvalueInstruction;
import com.cowlark.sake.instructions.GotoInstruction;
import com.cowlark.sake.instructions.IfInstruction;
import com.cowlark.sake.instructions.IntegerConstantInstruction;
import com.cowlark.sake.instructions.MethodCallInstruction;
import com.cowlark.sake.instructions.SetLocalInstruction;
import com.cowlark.sake.instructions.SetReturnValueInstruction;
import com.cowlark.sake.instructions.SetUpvalueInstruction;
import com.cowlark.sake.instructions.StringConstantInstruction;
import com.cowlark.sake.symbols.Function;
import com.cowlark.sake.symbols.Symbol;
import com.cowlark.sake.symbols.Variable;
import com.cowlark.sake.types.ArrayType;
import com.cowlark.sake.types.FunctionType;
import com.cowlark.sake.types.Type;

public class CBackend extends ImperativeBackend
{
	private static Charset UTF8 = Charset.forName("utf-8");
	
	private HashMap<Constructor, String> _constructorTypes =
		new HashMap<Constructor, String>();
	private HashMap<Constructor, String> _constructorLabels =
		new HashMap<Constructor, String>();
	private HashMap<Symbol, String> _symbolLabels =
		new HashMap<Symbol, String>();
	private HashMap<Type, String> _typeLabels =
		new HashMap<Type, String>();
	private HashMap<BasicBlock, String> _bbLabels =
		new HashMap<BasicBlock, String>();
	private HashMap<StringConstantNode, String> _stringLabels =
		new HashMap<StringConstantNode, String>();
	
	private int _funcid;
	private CTypeNameBuilder _typeNameBuilder = new CTypeNameBuilder();
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
	    
	    compiler.getAst().visit(
	    		new RecursiveVisitor()
	    		{
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

	private String ctype(Constructor constructor)
	{
		String s = _constructorTypes.get(constructor);
		if (s != null)
			return s;
		
		ScopeConstructorNode node = constructor.getNode();
		Function f = node.getFunctionScope().getFunction();
		s = "struct C" + _constructorTypes.size() + "_" + escape(f.getMangledName()) +
			"_" + escape(node.locationAsString());
		
		_constructorTypes.put(constructor, s);
		return s;
	}
	
	private String clabel(Constructor constructor)
	{
		String s = _constructorLabels.get(constructor);
		if (s != null)
			return s;
		
		ScopeConstructorNode node = constructor.getNode();
		Function f = node.getFunctionScope().getFunction();
		s = "c" + _constructorLabels.size() + "_" + escape(f.getMangledName()) +
			"_" + escape(node.locationAsString());
		
		_constructorLabels.put(constructor, s);
		return s;
	}
	
	private String clabel(Symbol symbol)
	{
		String s = _symbolLabels.get(symbol);
		if (s != null)
			return s;
		
		Node node = symbol.getNode();
		s = "s" + _symbolLabels.size() + "_" + escape(symbol.getName()) +
			"_" + escape(node.locationAsString());
		
		_symbolLabels.put(symbol, s);
		return s;
	}
	
	private String clabel(StringConstantNode node)
	{
		String s = _stringLabels.get(node);
		if (s != null)
			return s;
		
		s = "sc" + _stringLabels.size() +
			"_" + escape(node.locationAsString());
		
		_stringLabels.put(node, s);
		return s;
	}
	
	private String clabel(BasicBlock bb)
	{
		String s = _bbLabels.get(bb);
		if (s != null)
			return s;
		
		s = "B" + _bbLabels.size() + "_" + escape(bb.getFunction().getName());
		
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
		FunctionType type = (FunctionType) f.getSymbolType();
		FunctionDefinitionNode node = (FunctionDefinitionNode) f.getNode();
		ParameterDeclarationListNode parameters = node.getFunctionHeader().getParametersNode();
		
		print("static ");
		print(ctype(type.getReturnType()));
		print(" ");
		print(clabel(f));
		print("(");

		boolean first = true;
		Constructor constructor = f.getConstructor();
		if (constructor != null)
		{
			Constructor parent = constructor.getParentConstructor();
			print(ctype(parent));
			print("* ");
			print(clabel(parent));
			first = false;
		}
		
		for (Node n : parameters.getChildren())
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
		
		print(")\n");
	}
	
	@Override
	public void compileFunction(Function f)
	{
		function_header(f);
		print("{\n");
		
		_funcid = 0;
		
		FunctionType type = (FunctionType) f.getSymbolType();
		Type returntype = type.getReturnType();
		if (!returntype.isVoidType())
		{
			_returnvalue = cid("return");
			
			print("\t");
			print(ctype(returntype));
			print(" ");
			print(_returnvalue);
			print(";\n");
		}
		else
			_returnvalue = null;
		
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
		FunctionDefinitionNode node = (FunctionDefinitionNode) insn.getNode();
		Function function = node.getFunctionBody().getFunction();
		FunctionType type = (FunctionType) function.getSymbolType();
		Type returntype = type.getReturnType();
		
		if (!returntype.isVoidType())
		{
			print("\treturn ");
			print(_returnvalue);
			print(";\n");
		}
		else
			print("\treturn;\n");
	}
	
	@Override
	public void visit(SetReturnValueInstruction insn)
	{
		ReturnStatementNode node = (ReturnStatementNode) insn.getNode();
		Function function = node.getScope().getFunction();
		FunctionType type = (FunctionType) function.getSymbolType();
		Type returntype = type.getReturnType();
		
		assert(!returntype.isVoidType());
		print("\t");
		print(_returnvalue);
		print(" = ");
		compileFromIterator();
		print(";\n");
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
		print("\tif (");
		compileFromIterator();
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
	
	@Override
	public void visit(DiscardInstruction insn)
	{
		print("\t(void) ");
		compileFromIterator();
		print(";\n");
	}
	
	@Override
	public void visit(DirectFunctionCallInstruction insn)
	{
		Function function = insn.getFunction();
		
		print(clabel(function));
		print("(");
		print(clabel(function.getConstructor().getParentConstructor()));
		
		for (int i = 0; i < insn.getNumberOfOperands(); i++)
		{
			print(", ");
			compileFromIterator();
		}
		
		print(")");
	}
	
	@Override
	public void visit(MethodCallInstruction insn)
	{
        MethodCallNode node = (MethodCallNode) insn.getNode();
        
        print("S_METHOD_");
        String methodsig = node.getMethod().getIdentifier();
        print(methodsig.replace('.', '_').toUpperCase());

        print("(");
        compileFromIterator();
        
        for (int i = 0; i < insn.getNumberOfArguments(); i++)
        {
                print(", ");
                compileFromIterator();
        }
        
        print(")");

	}
	
	private void upvalue_lvalue(Node node, Variable var)
	{
		Constructor current = node.getScope().getConstructor();
		Constructor varcon = var.getConstructor();
		
		print(clabel(current));
		if (current != varcon)
		{
			print("->");
			print(clabel(varcon));
		}
		print("->");
		print(clabel(var));
	}
	
	@Override
	public void visit(SetUpvalueInstruction insn)
	{
		print("\t");
		upvalue_lvalue(insn.getNode(), insn.getVariable());
		print(" = ");
		compileFromIterator();
		print(";\n");
	}
	
	@Override
	public void visit(GetUpvalueInstruction insn)
	{
		upvalue_lvalue(insn.getNode(), insn.getVariable());
	}
	
	@Override
	public void visit(SetLocalInstruction insn)
	{
		Variable var = insn.getVariable();
		
		print("\t");
		print(clabel(var));
		print(" = ");
		compileFromIterator();
		print(";\n");
	}

	@Override
	public void visit(GetLocalInstruction insn)
	{
		Variable var = insn.getVariable();
		
		print(clabel(var));
	}
	
	@Override
	public void visit(ArrayConstructorInstruction insn)
	{
		ArrayConstructorNode node = (ArrayConstructorNode) insn.getNode();
		ArrayType type = (ArrayType) node.getType();
		int length = insn.getNumberOfOperands();
		
		if (length > 0)
		{
			print("S_INIT_ARRAY(");
		}
		
		print("S_CONSTRUCT_ARRAY(");
		print(ctype(type.getChildType()));
		print(", ");
		print(length);
		print(")");

		if (length > 0)
		{
			for (int i = 0; i < length; i++)
			{
				print(", ");
				compileFromIterator();
			}
			print(")");
		}
	}
	
	@Override
	public void visit(IntegerConstantInstruction insn)
	{
		print(insn.getValue());
	}
	
	@Override
	public void visit(StringConstantInstruction insn)
	{
		print("&");
		print(clabel((StringConstantNode) insn.getNode()));
	}
	
	@Override
	public void visit(BooleanConstantInstruction insn)
	{
		if (insn.getValue())
			print("1");
		else
			print("0");
	}
}
