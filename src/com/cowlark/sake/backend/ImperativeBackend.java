package com.cowlark.sake.backend;

import java.io.OutputStream;

public abstract class ImperativeBackend extends Backend
{
	public ImperativeBackend(OutputStream os)
    {
	    super(os);
    }
}
