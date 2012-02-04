/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

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
