/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.symbols.Symbol;

public interface HasSymbol
{
	public void setSymbol(Symbol symbol);
	public Symbol getSymbol();
}
