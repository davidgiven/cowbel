/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import java.util.List;
import com.cowlark.cowbel.symbols.Variable;

public interface HasInputVariables
{
	public List<Variable> getInputVariables();
}
