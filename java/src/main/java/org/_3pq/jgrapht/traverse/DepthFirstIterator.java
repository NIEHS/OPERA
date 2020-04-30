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
/* -----------------------
 * DepthFirstIterator.java
 * -----------------------
 * (C) Copyright 2003, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   Barak Naveh
 *
 * $Id$
 *
 * Changes
 * -------
 * 29-Jul-2003 : Initial revision (LR);
 * 31-Jul-2003 : Fixed traversal across connected components (BN);
 * 06-Aug-2003 : Extracted common logic to TraverseUtils.XXFirstIterator (BN);
 * 31-Jan-2004 : Reparented and changed interface to parent class (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import java.util.ArrayList;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;

/**
 * A depth-first iterator for a directed and an undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The results
 * of such modifications are undefined.
 *
 * @author Liviu Rau
 * @author Barak Naveh
 *
 * @since Jul 29, 2003
 */
public class DepthFirstIterator extends CrossComponentIterator {
    private List m_stack = new ArrayList(  );

    /**
     * Creates a new depth-first iterator for the specified graph.
     *
     * @param g the graph to be iterated.
     */
    public DepthFirstIterator( Graph g ) {
        this( g, null );
    }


    /**
     * Creates a new depth-first iterator for the specified graph. Iteration
     * will start at the specified start vertex and will be limited to the
     * connected component that includes that vertex. If the specified start
     * vertex is <code>null</code>, iteration will start at an arbitrary
     * vertex and will not be limited, that is, will be able to traverse all
     * the graph.
     *
     * @param g the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    public DepthFirstIterator( Graph g, Object startVertex ) {
        super( g, startVertex );
    }

    /**
     * @see org._3pq.jgrapht.traverse.CrossComponentIterator#isConnectedComponentExhausted()
     */
    protected boolean isConnectedComponentExhausted(  ) {
        return m_stack.isEmpty(  );
    }


    /**
     * @see org._3pq.jgrapht.traverse.CrossComponentIterator#encounterVertex(java.lang.Object,
     *      org._3pq.jgrapht.Edge)
     */
    protected void encounterVertex( Object vertex, Edge edge ) {
        putSeenData( vertex, null );
        m_stack.add( vertex );
    }


    /**
     * @see org._3pq.jgrapht.traverse.CrossComponentIterator#encounterVertexAgain(java.lang.Object,
     *      org._3pq.jgrapht.Edge)
     */
    protected void encounterVertexAgain( Object vertex, Edge edge ) {}


    /**
     * @see org._3pq.jgrapht.traverse.CrossComponentIterator#provideNextVertex()
     */
    protected Object provideNextVertex(  ) {
        return m_stack.remove( m_stack.size(  ) - 1 );
    }
}
