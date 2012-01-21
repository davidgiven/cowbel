package com.cowlark.sake.backend;

import java.io.OutputStream;
import com.cowlark.sake.Compiler;

public abstract class ImperativeBackend extends Backend
{
	public ImperativeBackend(Compiler compiler, OutputStream os)
    {
	    super(compiler, os);
    }
}
