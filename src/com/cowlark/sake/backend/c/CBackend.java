package com.cowlark.sake.backend.c;

import java.io.OutputStream;
import com.cowlark.sake.Constructor;
import com.cowlark.sake.backend.ImperativeBackend;
import com.cowlark.sake.symbols.Function;

public class CBackend extends ImperativeBackend
{
	public CBackend(OutputStream os)
    {
	    super(os);
    }
	
	@Override
	public void visit(Constructor constructor)
	{
		print("struct ");
		print(constructor.toString());
		print(" {\n");
		print("};\n");
	}
	
	@Override
	public void compileFunction(Function f)
	{
	    //super.compileFunction(f);
	}
}
