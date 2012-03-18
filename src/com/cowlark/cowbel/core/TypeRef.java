/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import com.cowlark.cowbel.interfaces.HasMethods;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasTypeRef;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.types.AbstractConcreteType;
import com.cowlark.cowbel.utils.DeterministicObject;

/** A holder for a type. A TypeRef may refer to a concrete type or may
 * refer to an unresolved type specified via a set of type constraints.
 */

public class TypeRef extends DeterministicObject<TypeRef>
		implements HasNode
{
	private static TreeSet<TypeRef> _allTypeRefs =
		new TreeSet<TypeRef>();
	
	public static Collection<TypeRef> getAllTypeRefs()
	{
		return _allTypeRefs;
	}
	
	private IsNode _node;
	
	/** This typeref is used here. */
	private TreeSet<HasTypeRef> _uses = new TreeSet<HasTypeRef>();
	
	/** We are of this type. */
	private AbstractConcreteType _type;
	
	/** We know we are this implementation. */
	private Implementation _implementation;
	
	/** We depend on these types (with constraints applied). */
	private TreeSet<TypeRef> _parents = new TreeSet<TypeRef>();
	
	/** TypeRefs which depend on this one. */
	private TreeSet<TypeRef> _children = new TreeSet<TypeRef>();
	
	/** This node must be downcastable from these interfaces. */
	private TreeSet<Interface> _constraints;
	
	/** Have the constraints on this node been validated? */
	private boolean _constraintsValidated = false;
	
	/** Used by the type inference engine. */
	
	int index = -1;
	int lowlink = -1;
	
	public TypeRef(IsNode node)
    {
		_node = node;
		_allTypeRefs.add(this);
    }
	
	public void addUse(HasTypeRef use)
	{
		_uses.add(use);
	}
	
	@Override
	public IsNode getNode()
	{
	    return _node;
	}
	
	public SortedSet<TypeRef> getParents()
    {
	    return _parents;
    }
	
	public SortedSet<TypeRef> getChildren()
    {
	    return _children;
    }
	
	public Collection<Interface> getConstraints()
    {
		if (_constraints == null)
			return Collections.<Interface>emptyList();
	    return _constraints;
    }

	public boolean areConstraintsValidated()
    {
	    return _constraintsValidated;
    }
	
	public void setConstraintsValidated(boolean constraintsValidated)
    {
	    _constraintsValidated = constraintsValidated;
    }
	
    public AbstractConcreteType getConcreteType()
	{
		return _type;
	}
	
	public TypeRef setConcreteType(AbstractConcreteType type)
	{
		_type = type;
		
		return this;
	}

	public TypeRef setImplementation(Implementation implementation)
	{
		assert(_implementation == null);
		_implementation = implementation;
		
		return this;
	}

	public Implementation getImplementation()
    {
	    return _implementation;
    }
	
	public TypeRef addParent(TypeRef parent)
	{
		_parents.add(parent);
		parent._children.add(this);
		
		return this;
	}
	
	public TypeRef disconnect()
	{
		for (TypeRef parent : _parents)
			parent._children.remove(this);
		_parents.clear();
		
		for (TypeRef child : _children)
			child._parents.remove(this);
		_children.clear();
		
		return this;
	}
	
	public TypeRef addChild(TypeRef child)
	{
		_children.add(child);
		child._parents.add(this);
		
		return this;
	}
	
//	private TypeRef addConstraint(AbstractTypeConstraint constraint)
//	{
//		if (_constraints == null)
//			_constraints = new TreeSet<AbstractTypeConstraint>();
//		
//		_constraints.add(constraint);
//		
//		return this;
//	}
	
	public TypeRef addCastConstraint(Interface type)
	{
		if (type != null)
		{
			if (_constraints == null)
				_constraints = new TreeSet<Interface>();
			
			_constraints.add(type);
		}
		return this;
	}
	
	private void collect_method_providers(Set<HasMethods> accumulator,
			Set<TypeRef> processed)
	{
		processed.add(this);
		
		if (_implementation != null)
		{
			accumulator.add(_implementation);
			accumulator.addAll(_implementation.getInterfaces());
			return;
		}

		if (_constraints != null)
		{
			for (Interface i : _constraints)
				accumulator.add(i);
			
			/* No need to go further up the tree than this, as any additional
			 * interfaces will not be visible. */
		}
		else
		{		
			for (TypeRef p : _parents)
			{
				if (!processed.contains(p))
					p.collect_method_providers(accumulator, processed);
			}
		}
	}
	
	public Set<HasMethods> collectMethodProviders()
	{
		Set<HasMethods> accumulator = new TreeSet<HasMethods>();
		Set<TypeRef> processed = new TreeSet<TypeRef>();
		collect_method_providers(accumulator, processed);
		
		return accumulator;
	}
	
	/** This typeref is declared to be an alias of another. */
	
	public void alias(TypeRef other)
	{
		this.addParent(other);
		other.addParent(this);
	}
}
