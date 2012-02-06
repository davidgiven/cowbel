/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.BasicBlock;

public class InstructionVisitor
{
	public void visit(BasicBlock bb)
	{
	}
	
	public void visit(Instruction insn)
	{
	}
	
	public void visit(ConstructInstruction insn)
	{
		visit((Instruction) insn);
	}
		
	public void visit(FunctionExitInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(GotoInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(IfInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(MethodCallInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(DirectFunctionCallInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(BooleanConstantInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(StringConstantInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(IntegerConstantInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(VarCopyInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(CreateObjectReferenceInstruction insn)
	{
		visit((Instruction) insn);
	}
}
