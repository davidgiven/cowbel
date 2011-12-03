package com.cowlark.sake.types;

import java.util.HashMap;
import java.util.Map;

public class TypeRegistry
{
	private static Map<String, Type> _typeMap =
		new HashMap<String, Type>();
	
	public static Type canonicalise(Type candidate)
	{
		String canonicalName = candidate.getCanonicalTypeName();
		Type type = _typeMap.get(canonicalName);
		if (type != null)
			return type;
		
		_typeMap.put(canonicalName, candidate);
		return candidate;
	}
}
