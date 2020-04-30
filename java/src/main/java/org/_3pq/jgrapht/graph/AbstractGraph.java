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
/* ------------------
 * AbstractGraph.java
 * ------------------
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;

/**
 * A skeletal implementation of the <tt>Graph</tt> interface, to minimize the
 * effort required to implement graph interfaces. This implementation is
 * applicable to both: directed graphs and undirected graphs.
 *
 * @author Barak Naveh
 *
 * @see org._3pq.jgrapht.Graph
 * @see org._3pq.jgrapht.DirectedGraph
 * @see org._3pq.jgrapht.UndirectedGraph
 */
public abstract class AbstractGraph implements Graph {
    /**
     * Construct a new empty graph object.
     */
    public AbstractGraph(  ) {}

    /**
     * @see Graph#addAllEdges(Collection)
     */
    public boolean addAllEdges( Collection edges ) {
        boolean modified = false;

        for( Iterator iter = edges.iterator(  ); iter.hasNext(  ); ) {
            modified |= addEdge( (Edge) iter.next(  ) );
        }

        return modified;
    }


    /**
     * @see Graph#addAllVertices(Collection)
     */
    public boolean addAllVertices( Collection vertices ) {
        boolean modified = false;

        for( Iterator iter = vertices.iterator(  ); iter.hasNext(  ); ) {
            modified |= addVertex( iter.next(  ) );
        }

        return modified;
    }


    /**
     * @see Graph#containsEdge(Object, Object)
     */
    public boolean containsEdge( Object sourceVertex, Object targetVertex ) {
        return getEdge( sourceVertex, targetVertex ) != null;
    }


    /**
     * @see Graph#removeAllEdges(Collection)
     */
    public boolean removeAllEdges( Collection edges ) {
        boolean modified = false;

        for( Iterator iter = edges.iterator(  ); iter.hasNext(  ); ) {
            modified |= removeEdge( (Edge) iter.next(  ) );
        }

        return modified;
    }


    /**
     * @see Graph#removeAllEdges(Object, Object)
     */
    public List removeAllEdges( Object sourceVertex, Object targetVertex ) {
        List removed = getAllEdges( sourceVertex, targetVertex );
        removeAllEdges( removed );

        return removed;
    }


    /**
     * @see Graph#removeAllVertices(Collection)
     */
    public boolean removeAllVertices( Collection vertices ) {
        boolean modified = false;

        for( Iterator iter = vertices.iterator(  ); iter.hasNext(  ); ) {
            modified |= removeVertex( iter.next(  ) );
        }

        return modified;
    }


    /**
     * Returns a string of the parenthesized pair (V, E) representing this
     * G=(V,E) graph. 'V' is the string representation of the vertex set, and
     * 'E' is the string representation of the edge set.
     *
     * @return a string representation of this graph.
     */
    public String toString(  ) {
        return toStringFromSets( vertexSet(  ), edgeSet(  ) );
    }


    /**
     * Ensures that the specified vertex exists in this graph, or else throws
     * exception.
     *
     * @param v vertex
     *
     * @return <code>true</code> if this assertion holds.
     *
     * @throws NullPointerException if specified vertex is <code>null</code>.
     * @throws IllegalArgumentException if specified vertex does not exist in
     *         this graph.
     */
    protected boolean assertVertexExist( Object v ) {
        if( containsVertex( v ) ) {
            return true;
        }
        else if( v == null ) {
            throw new NullPointerException(  );
        }
        else {
            throw new IllegalArgumentException( "no such vertex in graph" );
        }
    }


    /**
     * Removes all the edges in this graph that are also contained in the
     * specified edge array.  After this call returns, this graph will contain
     * no edges in common with the specified edges. This method will invoke
     * the {@link Graph#removeEdge(Edge)} method.
     *
     * @param edges edges to be removed from this graph.
     *
     * @return <tt>true</tt> if this graph changed as a result of the call.
     *
     * @see Graph#removeEdge(Edge)
     * @see Graph#containsEdge(Edge)
     */
    protected boolean removeAllEdges( Edge[] edges ) {
        boolean modified = false;

        for( int i = 0; i < edges.length; i++ ) {
            modified |= removeEdge( edges[ i ] );
        }

        return modified;
    }


    /**
     * Helper for subclass implementations of toString(  ).
     *
     * @param vertexSet the vertex set V to be printed
     * @param edgeSet the edge set E to be printed
     *
     * @return a string representation of (V,E)
     */
    protected String toStringFromSets( Collection vertexSet, Collection edgeSet ) {
        return "(" + vertexSet.toString(  ) + ", " + edgeSet.toString(  ) + ")";
    }
}
