package com.cowlark.sake.backend;

import java.io.OutputStream;

public abstract class FunctionalBackend extends Backend
{
	public FunctionalBackend(OutputStream os)
    {
	    super(os);
    }
}
