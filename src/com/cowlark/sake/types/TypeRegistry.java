package com.cowlark.sake.types;

import java.util.HashMap;
import java.util.Map;

class TypeRegistry
{
	private static Map<String, Type> _typeMap =
		new HashMap<String, Type>();
	
	static <T extends Type> T canonicalise(T candidate)
	{
		String canonicalName = candidate.getCanonicalTypeName();
		Type type = _typeMap.get(canonicalName);
		if (type != null)
			return (T) type;
		
		_typeMap.put(canonicalName, candidate);
		return candidate;
	}
}
