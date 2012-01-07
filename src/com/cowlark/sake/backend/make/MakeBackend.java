package com.cowlark.sake.backend.make;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;
import com.cowlark.sake.BasicBlock;
import com.cowlark.sake.Function;
import com.cowlark.sake.LocalVariable;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.backend.Backend;
import com.cowlark.sake.instructions.DiscardInstruction;
import com.cowlark.sake.instructions.FunctionCallInstruction;
import com.cowlark.sake.instructions.FunctionExitInstruction;
import com.cowlark.sake.instructions.GetGlobalVariableInstruction;
import com.cowlark.sake.instructions.GetLocalVariableInstruction;
import com.cowlark.sake.instructions.GotoInstruction;
import com.cowlark.sake.instructions.IfInstruction;
import com.cowlark.sake.instructions.ListConstructorInstruction;
import com.cowlark.sake.instructions.MethodCallInstruction;
import com.cowlark.sake.instructions.SetGlobalVariableInstruction;
import com.cowlark.sake.instructions.SetLocalVariableInInstruction;
import com.cowlark.sake.instructions.SetReturnValueInstruction;
import com.cowlark.sake.instructions.StringConstantInstruction;

public class MakeBackend extends Backend
{
	private HashMap<LocalVariable, Integer> _variableAllocation =
		new HashMap<LocalVariable, Integer>();
	
	public MakeBackend(OutputStream stream)
    {
		super(stream);
    }
	
	@Override
	public void prologue()
	{
		InputStream is = getClass().getResourceAsStream("sakelib.mk");
		print(is);
	}
	
	@Override
	public void epilogue()
	{
		print("$(call $(sake.globals.<main>))\n");
	}
	
	@Override
	public void compileFunction(Function f)
	{
		print("sake.globals.");
		print(f.getSymbolName().getText());
		print(" := sake.bb.");
		print(f.getEntryBB().getName());
		print("\n");
		
	    super.compileFunction(f);
	}
	
	@Override
	public void compileBasicBlock(BasicBlock bb)
	{
		/* Assign numbers to each variable. */
		
		_variableAllocation.clear();
		int i = 1;
		for (LocalVariable var : bb.getInputVariables())
		{
			_variableAllocation.put(var, i);
			i++;
		}
		
		/* Actually emit the code for the bb. */
		
		print("sake.bb.");
	    print(bb.getName());
	    print(" = ");
	    super.compileBasicBlock(bb);
	    print("\n");
	}
	
	private void emit_output_map()
	{
		for (LocalVariable var : getCurrentBasicBlock().getOutputVariables())
		{
			print(",");
			emit_var(var);
		}
	}
	
	private void emit_string(String s)
	{
		int o = 0;
		while (o < s.length())
		{
			int c = s.codePointAt(o);
			switch (c)
			{
				case '~': print("~T"); break;
				case ' ': print("~S"); break;
				case ',': print("~C"); break;
				
				default:
					print(String.valueOf(Character.toChars(c)));
					break;
			}
			
			o += Character.charCount(c);
		}
	}
	
	private void emit_var(LocalVariable var)
	{
		int id = _variableAllocation.get(var);
		
		print("$(");
		print(id);
		print(")");
	}
	
	@Override
	public void visit(FunctionExitInstruction insn)
	{
	}
	
	@Override
	public void visit(GotoInstruction insn)
	{
		print("$(call sake.bb.");
		print(insn.getTarget().getName());
		emit_output_map();
		print(")");
	}
	
	@Override
	public void visit(IfInstruction insn)
	{
		print("$(call $(if ");
		compileFromIterator();
		print(",sake.bb.");
		print(insn.getPositiveTarget().getName());
		print(",sake.bb.");
		print(insn.getNegativeTarget().getName());
		print(")");
		emit_output_map();
		print(")");
	}
	
	@Override
	public void visit(MethodCallInstruction insn)
	{
		print("$(call sake.method.");
		
		MethodCallNode node = (MethodCallNode) insn.getNode();
		print(node.getMethod().getIdentifier());
		
		print(",");
		compileFromIterator();
		
		for (int i = 0; i < insn.getNumberOfArguments(); i++)
		{
			print(",");
			compileFromIterator();
		}
		
		print(")");
	}
	
	@Override
	public void visit(FunctionCallInstruction insn)
	{
		print("$(call ");
		compileFromIterator();
				
		for (int i = 0; i < insn.getNumberOfArguments(); i++)
		{
			print(",");
			compileFromIterator();
		}
		
		print(")");
	}
	
	@Override
	public void visit(GetGlobalVariableInstruction insn)
	{
		print("$(sake.globals.");
		print(insn.getVariable().getSymbolName().getText());
		print(")");
	}
	
	@Override
	public void visit(SetGlobalVariableInstruction insn)
	{
		print("$(eval sake.globals.");
		print(insn.getVariable().getSymbolName().getText());
		print(" := ");
		compileFromIterator();
		print(")");
	}
	
	@Override
	public void visit(GetLocalVariableInstruction insn)
	{
		LocalVariable var = insn.getVariable();
		emit_var(var);
	}
	
	@Override
	public void visit(SetLocalVariableInInstruction insn)
	{
		LocalVariable var = insn.getVariable();
		Set<LocalVariable> outputs = getCurrentBasicBlock().getOutputVariables();
		
		if (!outputs.contains(var))
		{
			/* The variable we're setting is never actually exported;
			 * evaluate it here, discard the result, and then jump to the
			 * destination bb. */
			
			print("$(call sake.discard,");
			compileFromIterator();
			print(")");
		}
		
		print("$(call sake.bb.");
		print(insn.getTarget().getName());
		
		for (LocalVariable v : getCurrentBasicBlock().getOutputVariables())
		{
			print(",");
			
			if (v == var)
				compileFromIterator();
			else
				emit_var(v);
		}
		
		print(")");
	}
	
	@Override
	public void visit(SetReturnValueInstruction insn)
	{
		compileFromIterator();
	}
	
	@Override
	public void visit(ListConstructorInstruction insn)
	{
		for (int i=0; i<insn.getNumberOfOperands(); i++)
		{
			if (i > 0)
				print(" ");
			print("$(sake.list.wrap ");
			compileFromIterator();
			print(")");
		}
	}
	
	@Override
	public void visit(StringConstantInstruction insn)
	{
		emit_string(insn.getValue());
	}
	
	@Override
	public void visit(DiscardInstruction insn)
	{
		print("$(call sake.discard,");
		compileFromIterator();
		print(")");
	}
}
