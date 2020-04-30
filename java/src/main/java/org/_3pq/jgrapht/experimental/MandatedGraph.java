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
/* ------------------
 * MandatedGraph.java
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
package org._3pq.jgrapht.experimental;

import java.util.Collection;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.graph.GraphDelegator;

/**
 * A <tt>MandatedGraph</tt> maintains some desired restriction in a graph.
 * Example restrictions are: tree structure, acyclicality, connectivity,
 * immutability, etc. An <tt>UpdateAuthority</tt> is used to fulfill such
 * desired restrictions.
 * 
 * <p>
 * All the modifying methods of the graph are redirected to an
 * <tt>UpdateAuthority</tt> which can decide what to do. Courses of action for
 * can be to allow the modifications, to ignore them, to throw exceptions,
 * etc.
 * </p>
 *
 * @author Barak Naveh
 *
 * @see UpdateAuthority
 * @since Jul 21, 2003
 * @deprecated Thought it will become useful but don't have any use for now.
 *             Will be un-deprecated if a decent use is suggested, or will be
 *             deleted if keeps being non-useful.
 */
public class MandatedGraph extends GraphDelegator {
    private UpdateAuthority m_updateAuthority;

    /**
     * Constructor for MandatedGraph.
     *
     * @param g the backing graph.
     * @param ua update authority for this graph.
     *
     * @throws IllegalArgumentException
     */
    public MandatedGraph( Graph g, UpdateAuthority ua ) {
        super( g );

        if( !ua.isUpdateAuthorityOf( g ) ) {
            throw new IllegalArgumentException( 
                "UpdateAuthority graph mismatch" );
        }

        m_updateAuthority = ua;
    }

    /**
     * Returns the update authority of this graph.
     *
     * @return the update authority of this graph.
     */
    public UpdateAuthority getUpdateAuthority(  ) {
        return m_updateAuthority;
    }


    /**
     * @see Graph#addAllEdges(Collection)
     */
    public boolean addAllEdges( Collection edges ) {
        return m_updateAuthority.addAllEdges( edges );
    }


    /**
     * @see Graph#addAllVertices(Collection)
     */
    public boolean addAllVertices( Collection vertices ) {
        return m_updateAuthority.addAllVertices( vertices );
    }


    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge( Edge e ) {
        return m_updateAuthority.addEdge( e );
    }


    /**
     * @see Graph#addEdge(Object, Object)
     */
    public Edge addEdge( Object sourceVertex, Object targetVertex ) {
        return m_updateAuthority.addEdge( sourceVertex, targetVertex );
    }


    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex( Object v ) {
        return m_updateAuthority.addVertex( v );
    }


    /**
     * @see Graph#removeAllEdges(Collection)
     */
    public boolean removeAllEdges( Collection edges ) {
        return m_updateAuthority.removeAllEdges( edges );
    }


    /**
     * @see Graph#removeAllEdges(Object, Object)
     */
    public List removeAllEdges( Object sourceVertex, Object targetVertex ) {
        return m_updateAuthority.removeAllEdges( sourceVertex, targetVertex );
    }


    /**
     * @see Graph#removeAllVertices(Collection)
     */
    public boolean removeAllVertices( Collection vertices ) {
        return m_updateAuthority.removeAllVertices( vertices );
    }


    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge( Edge e ) {
        return m_updateAuthority.removeEdge( e );
    }


    /**
     * @see Graph#removeEdge(Object, Object)
     */
    public Edge removeEdge( Object sourceVertex, Object targetVertex ) {
        return m_updateAuthority.removeEdge( sourceVertex, targetVertex );
    }


    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex( Object v ) {
        return m_updateAuthority.removeVertex( v );
    }
}
