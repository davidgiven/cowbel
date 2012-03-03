/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.interfaces;

import com.cowlark.cowbel.ast.IdentifierListNode;

public interface HasTypeVariables
{
	public IdentifierListNode getTypeVariables();
}
