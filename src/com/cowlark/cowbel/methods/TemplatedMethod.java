package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.Type;

public class TemplatedMethod extends Method
{
	public abstract static class Factory
	{
		private String _signature;
		
		public Factory(String signature)
        {
			_signature = signature;
        }

		public String getSignature()
		{
			return _signature;
		}
		
		abstract Method create(Type type);
	}

	private Type _receiverType;
	
	public TemplatedMethod(Type type)
    {
		_receiverType = type;
    }
	
	public Type getReceiverType()
	{
		return _receiverType;
	}
}
