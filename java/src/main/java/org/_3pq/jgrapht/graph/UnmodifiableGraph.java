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
/* ----------------------
 * UnmodifiableGraph.java
 * ----------------------
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

import java.util.Collection;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;

/**
 * An unmodifiable view of the backing graph specified in the constructor. This
 * graph allows modules to provide users with "read-only" access to internal
 * graphs. Query operations on this graph "read through" to the backing graph,
 * and attempts to modify this graph result in an
 * <code>UnsupportedOperationException</code>.
 * 
 * <p>
 * This graph does <i>not</i> pass the hashCode and equals operations through
 * to the backing graph, but relies on <tt>Object</tt>'s <tt>equals</tt> and
 * <tt>hashCode</tt> methods.  This graph will be serializable if the backing
 * graph is serializable.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Jul 24, 2003
 */
public class UnmodifiableGraph extends GraphDelegator implements Serializable {
    private static final long   serialVersionUID = 3544957670722713913L;
    private static final String UNMODIFIABLE = "this graph is unmodifiable";

    /**
     * Creates a new unmodifiable graph based on the specified backing graph.
     *
     * @param g the backing graph on which an unmodifiable graph is to be
     *        created.
     */
    public UnmodifiableGraph( Graph g ) {
        super( g );
    }

    /**
     * @see Graph#addAllEdges(Collection)
     */
    public boolean addAllEdges( Collection edges ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#addAllVertices(Collection)
     */
    public boolean addAllVertices( Collection vertices ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge( Edge e ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#addEdge(Object, Object)
     */
    public Edge addEdge( Object sourceVertex, Object targetVertex ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex( Object v ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#removeAllEdges(Collection)
     */
    public boolean removeAllEdges( Collection edges ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#removeAllEdges(Object, Object)
     */
    public List removeAllEdges( Object sourceVertex, Object targetVertex ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#removeAllVertices(Collection)
     */
    public boolean removeAllVertices( Collection vertices ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge( Edge e ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#removeEdge(Object, Object)
     */
    public Edge removeEdge( Object sourceVertex, Object targetVertex ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }


    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex( Object v ) {
        throw new UnsupportedOperationException( UNMODIFIABLE );
    }
}
