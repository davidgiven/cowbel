/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasConcreteType;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.types.AbstractConcreteType;
import com.cowlark.cowbel.types.InferenceFailedConcreteType;
import com.cowlark.cowbel.types.InterfaceConcreteType;

/** Placeholder class containing useful methods used by the type inference
 * stuff.
 */

public abstract class TypeInferenceEngine
{
	/** Detect any cycles in the TypeRef graph using Tarjan's Strongly
	 * Connected Components algorithm. */
	
	private static class Tarjan
	{
		@SuppressWarnings("serial")
        private static class SCC extends TreeSet<TypeRef> {}
		
		private ArrayList<SCC> _sccs = new ArrayList<SCC>();
		private TreeMap<TypeRef, Integer> _indices = new TreeMap<TypeRef, Integer>();
		private Stack<TypeRef> _stack = new Stack<TypeRef>();
		private int index = 0;
		
		public Tarjan()
        {
			for (TypeRef tr : TypeRef.getAllTypeRefs())
			{
				if (tr.index == -1)
					strongconnect(tr);
			}
        }
		
		public Collection<SCC> getStronglyConnectedComponents()
        {
	        return _sccs;
        }
		
		private void strongconnect(TypeRef v)
		{
			v.index = v.lowlink = index++;
			_stack.push(v);
			
			for (TypeRef w : v.getChildren())
			{
				if (w.index == -1)
				{
					/* w not yet visited; recurse on it. */
					strongconnect(w);
					v.lowlink = Math.min(v.lowlink, w.lowlink);
				}
				else if (_stack.contains(w))
				{
					/* w is in the stack, therefore in the current SCC */
					v.lowlink = Math.min(v.lowlink, w.index);
				}
			}
			
			/* If v is a root node, pop the stack and generate an SCC. */
			
			if (v.lowlink == v.index)
			{
				SCC scc = new SCC();
				
				TypeRef w;
				do
				{
					w = _stack.pop();
					scc.add(w);
				}
				while (w != v);
				
				_sccs.add(scc);
			}
		}
	}
	
	public static void undoCycles()
	{
		Tarjan tarjan = new Tarjan();
		for (Tarjan.SCC scc : tarjan.getStronglyConnectedComponents())
		{
			if (scc.size() > 1)
			{
				/* Collect all parents and children of the cycle. */
				
				TreeSet<TypeRef> parents = new TreeSet<TypeRef>();
				TreeSet<TypeRef> children = new TreeSet<TypeRef>();
				
				for (TypeRef tr : scc)
				{
					for (TypeRef parent : tr.getParents())
					{
						if (!scc.contains(parent))
							parents.add(parent);
					}
					
					for (TypeRef child : tr.getChildren())
					{
						if (!scc.contains(child))
							children.add(child);
					}
				}
				
				/* Create a new node for the parent and child. */
				
				TypeRef newparent = new TypeRef(scc.first().getNode());
				TypeRef newchild = new TypeRef(scc.last().getNode());
				
				/* Disconnect the nodes from the graph, and then reconnect
				 * them to the new parent and child. */
				
				for (TypeRef tr : scc)
					tr.disconnect();
				
				for (TypeRef tr : scc)
				{
					tr.addParent(newparent);
					tr.addChild(newchild);
				}

				/* Now wire up the new parent and child to the collected
				 * parents and children we recorded earlier. */
				
				for (TypeRef parent : parents)
					newparent.addParent(parent);
				for (TypeRef child : children)
					newchild.addChild(child);
			}
		}
	}
	
	private static void recurseThroughTypes(TypeRef tr)
			throws CompilationException
	{
		/* Ignore nodes which already have a type calculated for them. */
		
		if (tr.getConcreteType() != null)
			return;
		
		AbstractConcreteType ctype = null;	
		switch (tr.getParents().size())
		{
			case 0:
			{
				/* No parents. This TypeRef's node must be able to provide
				 * a concrete type, otherwise we have a type inference error. */
				
				IsNode node = tr.getNode();
				if (!(node instanceof HasConcreteType))
				{
//					throw new FailedToInferTypeException(node, null);
					ctype = new InferenceFailedConcreteType(node);
				}
				else
				{
					HasConcreteType hct = (HasConcreteType) tr.getNode();
					ctype = hct.createConcreteType();
				}
				
				break;
			}
			
			case 1:
			{
				/* One parent. This TypeRef's ctype is its parent's. */
				
				TypeRef parent = tr.getParents().first();
				recurseThroughTypes(parent);
				ctype = parent.getConcreteType();
				break;
			}
			
			default:
			{
				/* Many parents. In this situation, then *either* all the
				 * parents must have the same concrete type; *or* this node
				 * has type constraints that all the parents must satisfy,
				 * and then this node's concrete type becomes an interface. */
				
				TreeSet<AbstractConcreteType> parenttypes = new TreeSet<AbstractConcreteType>();
				for (TypeRef parent : tr.getParents())
				{
					recurseThroughTypes(parent);
					AbstractConcreteType ct = parent.getConcreteType();
					parenttypes.add(ct);
				}
				
				/* If parenttypes now contains exactly one item, then they
				 * all have the same concrete type. */
				
				if (parenttypes.size() == 1)
				{
					ctype = parenttypes.first();
				}
				else
				{
					/* This node *must* have constraints. */
					
					assert(!tr.getConstraints().isEmpty());
					
					for (AbstractConcreteType ct : parenttypes)
						ct.checkTypeConstraints(tr);
					
					ctype = new InterfaceConcreteType(tr.getNode(),
							tr.getConstraints(), parenttypes);
				}
			}
		}

		assert(ctype != null);
		ctype.checkTypeConstraints(tr);
		tr.setConcreteType(ctype);
	}
	
	public static void assignConcreteTypes()
			throws CompilationException
	{
		/* Find all leaf nodes: those with no children. */
		
		TreeSet<TypeRef> leaves = new TreeSet<TypeRef>();
		for (TypeRef tr : TypeRef.getAllTypeRefs())
			if (tr.getChildren().isEmpty())
				leaves.add(tr);
		
		/* Now recursively determine the concrete type of each leaf. */
		
		for (TypeRef tr : leaves)
			recurseThroughTypes(tr);
	}	
}