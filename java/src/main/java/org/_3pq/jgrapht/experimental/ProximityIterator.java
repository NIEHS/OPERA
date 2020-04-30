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
/* -------------------------
 * ClosestFirstIterator.java
 * -------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   Barak Naveh
 *
 * $Id$
 *
 * Changes
 * -------
 * 02-Sep-2003 : Initial revision (JVS);
 * 31-Jan-2004 : Reparented and changed interface to parent class (BN);
 *
 */
package org._3pq.jgrapht.experimental;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.traverse.*;
import org._3pq.jgrapht.experimental.heap.*;

/**
 * A closest-first iterator for a directed or undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The results
 * of such modifications are undefined.
 * 
 * <p>
 * The metric for <i>closest</i> here is the path length from a start vertex.
 * Edge.getWeight() is summed to calculate path length. Negative edge weights
 * will result in an IllegalArgumentException.
 * </p>
 *
 * @author John V. Sichi
 *
 * @since Sep 2, 2003
 */
public abstract class ProximityIterator extends CrossComponentIterator {
    /** Priority queue of fringe vertices. */
    private final Heap m_heap;

    /**
     * in which direction to compare. 1 (or any other positive value) means the
     * smaller the better, -1 (or other negative value) the opposite. This is
     * useful only for subclasses.
     */
    private final int _compare;

    /**
     * Creates a new closest-first iterator for the specified graph. Iteration
     * will start at the specified start vertex and will be limited to the
     * connected component that includes that vertex. If the specified start
     * vertex is <code>null</code>, iteration will start at an arbitrary
     * vertex and will not be limited, that is, will be able to traverse all
     * the graph.
     *
     * @param g the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    protected ProximityIterator( Graph g, Object startVertex, HeapFactory factory,
        boolean maximum ) {
        super( g, startVertex );
        m_heap = factory.createHeap(maximum);
        _compare = maximum ? -1 : 1;
    }

    /**
     * Get the spanning tree edge reaching a vertex which has been seen already
     * in this traversal.  This edge is the last link in the shortest known
     * path between the start vertex and the requested vertex.  If the vertex
     * has already been visited, then it is truly the minimum spanning tree
     * edge; otherwise, it is the best candidate seen so far.
     *
     * @param vertex the spanned vertex.
     *
     * @return the spanning tree edge, or null if the vertex either has not
     *         been seen yet or is the start vertex.
     */
    public final Edge getSpanningTreeEdge( Object vertex ) {
        return (Edge) getHeapVertex( vertex ).getAdditional();
    }


    /**
     * Get the spanning tree edge reaching a vertex which has been seen already
     * in this traversal.  This edge is the last link in the shortest known
     * path between the start vertex and the requested vertex.  If the vertex
     * has already been visited, then it is truly the minimum spanning tree
     * edge; otherwise, it is the best candidate seen so far.
     *
     * @param vertex the spanned vertex.
     *
     * @return the spanning tree edge, or null if the vertex either has not
     *         been seen yet or is the start vertex.
     */
    public final double getPrio( Object vertex ) {
        return getHeapVertex( vertex ).getPriority();
    }


    /**
     * @see org._3pq.jgrapht.traverse.CrossComponentIterator#isConnectedComponentExhausted()
     */
    protected final boolean isConnectedComponentExhausted(  ) {
        return m_heap.isEmpty();
    }


    /**
     * @see org._3pq.jgrapht.traverse.CrossComponentIterator#encounterVertex(java.lang.Object,
     *      org._3pq.jgrapht.Edge)
     */
    protected final void encounterVertex( Object vertex, Edge edge ) {
        HeapVertex heapV;

        if( vertex instanceof HeapVertex ) {
            heapV = (HeapVertex) vertex;
        }
        else {
            heapV = new HeapVertex( vertex );
            putSeenData( vertex, heapV );
        }
        heapV.setPriority( _compare > 0 ? 0 : Double.POSITIVE_INFINITY );
        if (edge != null) {
            heapV.setPriority( priorityFunction(heapV.getPriority(), edge.getWeight()) );
        }
        heapV.setAdditional( edge );
        m_heap.add( heapV );
    }


    /**
     * Override superclass.  When we see a vertex again, we need to see if the
     * new edge provides a shorter path than the old edge.
     *
     * @param vertex the vertex re-encountered
     * @param edge the edge via which the vertex was re-encountered
     */
    protected final void encounterVertexAgain( Object vertex, Edge edge ) {
        HeapVertex heapV = getHeapVertex( vertex );
        if (heapV.getPeer() == null) return; 
        HeapVertex heapPre = getHeapVertex( edge.oppositeVertex( vertex ) );

        double newPrio = priorityFunction( heapPre.getPriority(  ), edge.getWeight(  ) );

        if( _compare * ( heapV.getPriority(  ) - newPrio ) > 0 ) {
            heapV.setPriority( newPrio );
            heapV.setAdditional( edge );
            m_heap.update( heapV );
        }
    }

    private final HeapVertex getHeapVertex( Object v ) {
        if( v instanceof HeapVertex ) {
            return (HeapVertex) v;
        }

        return (HeapVertex) getSeenData( v );
    }


    protected abstract double priorityFunction( double vertexPrio, double edgeWeight );
    
    /**
     * @see org._3pq.jgrapht.traverse.CrossComponentIterator#provideNextVertex()
     */
    protected final Object provideNextVertex(  ) {
        return ((HeapVertex)m_heap.extractTop(  )).getVertex();
    }
}
