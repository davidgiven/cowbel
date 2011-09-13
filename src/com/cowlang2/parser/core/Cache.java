package com.cowlang2.parser.core;

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
