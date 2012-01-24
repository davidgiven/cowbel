package com.cowlark.cowbel.backend;

import java.io.OutputStream;
import com.cowlark.cowbel.Compiler;

public abstract class ImperativeBackend extends Backend
{
	public ImperativeBackend(Compiler compiler, OutputStream os)
    {
	    super(compiler, os);
    }
}
