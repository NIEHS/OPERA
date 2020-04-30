/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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
/* --------------------
 * UpdateAuthority.java
 * --------------------
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
package org._3pq.jgrapht.experimental;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;

/**
 * An authority that manages modifications made to a <tt>MandatedGraph</tt> and
 * makes sure some desired restriction is maintained in the graph. There can
 * be various restrictions on <tt>MandatedGraph</tt> such as: tree structure,
 * acyclicality, connectivity, immutability, etc.
 * 
 * <p>
 * All the modifying methods of a <tt>MandatedGraph</tt> are redirected to an
 * <tt>UpdateAuthority</tt> which can decide what to do with them. Courses of
 * action for can be to allow the modifications, to ignore them, to throw
 * exceptions, etc.
 * </p>
 * 
 * <p>
 * The <tt>UpdateAuthority</tt> may offer additional methods to update the
 * graph in a "safe" way, that is, in a way that does not violate the specific
 * restrictions it tries to maintain.
 * </p>
 *
 * @author Barak Naveh
 *
 * @see org._3pq.jgrapht.graph.MandatedGraph
 * @since Jul 21, 2003
 * @deprecated Thought it will become useful but don't have any use for now.
 *             Will be un-deprecated if a decent use is suggested, or will be
 *             deleted if keeps being non-useful.
 */
public interface UpdateAuthority extends Serializable {
    /**
     * Tests if this is the update authority of the specified graph.
     *
     * @param g a graph to test if this is an update authority of.
     *
     * @return <code>true</code> if this is the update authority of the
     *         specified graph; otherwise <code>false</code>.
     */
    public boolean isUpdateAuthorityOf( Graph g );


    /**
     * @see Graph#addAllEdges(Collection)
     */
    public boolean addAllEdges( Collection edges );


    /**
     * @see Graph#addAllVertices(Collection)
     */
    public boolean addAllVertices( Collection vertices );


    /**
     * @see Graph#addEdge(Object, Object)
     */
    public Edge addEdge( Object sourceVertex, Object targetVertex );


    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge( Edge e );


    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex( Object v );


    /**
     * @see Graph#removeAllEdges(Collection)
     */
    public boolean removeAllEdges( Collection edges );


    /**
     * @see Graph#removeAllEdges(Object, Object)
     */
    public List removeAllEdges( Object sourceVertex, Object targetVertex );


    /**
     * @see Graph#removeAllVertices(Collection)
     */
    public boolean removeAllVertices( Collection vertices );


    /**
     * @see Graph#removeEdge(Object, Object)
     */
    public Edge removeEdge( Object sourceVertex, Object targetVertex );


    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge( Edge e );


    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex( Object v );
}
