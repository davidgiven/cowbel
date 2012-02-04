/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.core;

import java.util.HashSet;

public class Cache
{
	private static class CacheKey
	{
		Location location;
		int node;
	}
	
	private HashSet<CacheKey> _cache = new HashSet<CacheKey>();
	
}
