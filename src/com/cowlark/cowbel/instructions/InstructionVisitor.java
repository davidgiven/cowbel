/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.core.BasicBlock;
import com.cowlark.cowbel.errors.CompilationException;

public class InstructionVisitor
{
	public void visit(BasicBlock bb)
			throws CompilationException
	{
	}
	
	public void visit(Instruction insn)
			throws CompilationException
	{
	}
	
	public void visit(ConstructInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
		
	public void visit(FunctionExitInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(GotoInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(IfInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(MethodCallInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(DirectFunctionCallInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(ExternFunctionCallInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
		
	public void visit(BooleanConstantInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(StringConstantInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(IntegerConstantInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(RealConstantInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(VarCopyInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(CreateObjectReferenceInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}
	
	public void visit(ExternInstruction insn)
			throws CompilationException
	{
		visit((Instruction) insn);
	}	
}
