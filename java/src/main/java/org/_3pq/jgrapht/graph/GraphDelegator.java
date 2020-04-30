/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -------------------
 * GraphDelegator.java
 * -------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import java.io.Serializable;

import java.util.List;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.UndirectedGraph;

/**
 * A graph backed by the the graph specified at the constructor, which
 * delegates all its methods to the backing graph. Operations on this graph
 * "pass through" to the to the backing graph. Any modification made to this
 * graph or the backing graph is reflected by the other.
 * 
 * <p>
 * This graph does <i>not</i> pass the hashCode and equals operations through
 * to the backing graph, but relies on <tt>Object</tt>'s <tt>equals</tt> and
 * <tt>hashCode</tt> methods.
 * </p>
 * 
 * <p>
 * This class is mostly used as a base for extending subclasses.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Jul 20, 2003
 */
public class GraphDelegator extends AbstractGraph implements Graph,
    Serializable {
    private static final long serialVersionUID = 3257005445226181425L;

    /** The graph to which operations are delegated. */
    private Graph m_delegate;

    /**
     * Constructor for GraphDelegator.
     *
     * @param g the backing graph (the delegate).
     *
     * @throws NullPointerException
     */
    public GraphDelegator( Graph g ) {
        super(  );

        if( g == null ) {
            throw new NullPointerException(  );
        }

        m_delegate = g;
    }

    /**
     * @see Graph#getAllEdges(Object, Object)
     */
    public List getAllEdges( Object sourceVertex, Object targetVertex ) {
        return m_delegate.getAllEdges( sourceVertex, targetVertex );
    }


    /**
     * @see Graph#getEdge(Object, Object)
     */
    public Edge getEdge( Object sourceVertex, Object targetVertex ) {
        return m_delegate.getEdge( sourceVertex, targetVertex );
    }


    /**
     * @see Graph#getEdgeFactory()
     */
    public EdgeFactory getEdgeFactory(  ) {
        return m_delegate.getEdgeFactory(  );
    }


    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge( Edge e ) {
        return m_delegate.addEdge( e );
    }


    /**
     * @see Graph#addEdge(Object, Object)
     */
    public Edge addEdge( Object sourceVertex, Object targetVertex ) {
        return m_delegate.addEdge( sourceVertex, targetVertex );
    }


    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex( Object v ) {
        return m_delegate.addVertex( v );
    }


    /**
     * @see Graph#containsEdge(Edge)
     */
    public boolean containsEdge( Edge e ) {
        return m_delegate.containsEdge( e );
    }


    /**
     * @see Graph#containsVertex(Object)
     */
    public boolean containsVertex( Object v ) {
        return m_delegate.containsVertex( v );
    }


    /**
     * @see UndirectedGraph#degreeOf(Object)
     */
    public int degreeOf( Object vertex ) {
        return ( (UndirectedGraph) m_delegate ).degreeOf( vertex );
    }


    /**
     * @see Graph#edgeSet()
     */
    public Set edgeSet(  ) {
        return m_delegate.edgeSet(  );
    }


    /**
     * @see Graph#edgesOf(Object)
     */
    public List edgesOf( Object vertex ) {
        return m_delegate.edgesOf( vertex );
    }


    /**
     * @see DirectedGraph#inDegreeOf(Object)
     */
    public int inDegreeOf( Object vertex ) {
        return ( (DirectedGraph) m_delegate ).inDegreeOf( vertex );
    }


    /**
     * @see DirectedGraph#incomingEdgesOf(Object)
     */
    public List incomingEdgesOf( Object vertex ) {
        return ( (DirectedGraph) m_delegate ).incomingEdgesOf( vertex );
    }


    /**
     * @see DirectedGraph#outDegreeOf(Object)
     */
    public int outDegreeOf( Object vertex ) {
        return ( (DirectedGraph) m_delegate ).outDegreeOf( vertex );
    }


    /**
     * @see DirectedGraph#outgoingEdgesOf(Object)
     */
    public List outgoingEdgesOf( Object vertex ) {
        return ( (DirectedGraph) m_delegate ).outgoingEdgesOf( vertex );
    }


    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge( Edge e ) {
        return m_delegate.removeEdge( e );
    }


    /**
     * @see Graph#removeEdge(Object, Object)
     */
    public Edge removeEdge( Object sourceVertex, Object targetVertex ) {
        return m_delegate.removeEdge( sourceVertex, targetVertex );
    }


    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex( Object v ) {
        return m_delegate.removeVertex( v );
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString(  ) {
        return m_delegate.toString(  );
    }


    /**
     * @see Graph#vertexSet()
     */
    public Set vertexSet(  ) {
        return m_delegate.vertexSet(  );
    }
}
