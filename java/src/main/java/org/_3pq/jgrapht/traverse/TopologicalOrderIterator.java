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
/* -----------------------------
 * TopologicalOrderIterator.java
 * -----------------------------
 * (C) Copyright 2004, by Marden Neubert and Contributors.
 *
 * Original Author:  Marden Neubert
 * Contributor(s):   Barak Naveh, John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 17-Dec-2004 : Initial revision (MN);
 * 25-Apr-2005 : Fixes for start vertex order (JVS);
 *
 */
package org._3pq.jgrapht.traverse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.util.ModifiableInteger;

/**
 * Implements topological order traversal for a directed graph. A topological
 * sort is a permutation <tt>p</tt> of the vertices of a graph such that an
 * edge <tt>(i,j)</tt> implies that <tt>i</tt> appears before <tt>j</tt> in
 * <tt>p</tt> (Skiena 1990, p. 208). See also <a
 * href="http://mathworld.wolfram.com/TopologicalSort.html">
 * http://mathworld.wolfram.com/TopologicalSort.html</a>.
 * 
 * <p>
 * See "Algorithms in Java, Third Edition, Part 5: Graph Algorithms" by Robert
 * Sedgewick and "Data Structures and Algorithms with Object-Oriented Design
 * Patterns in Java" by Bruno R. Preiss for implementation alternatives. The
 * latter can be found online at <a
 * href="http://www.brpreiss.com/books/opus5/">http://www.brpreiss.com/books/opus5/</a>
 * </p>
 * 
 * <p>
 * For this iterator to work correctly the graph must not be modified during
 * iteration. Currently there are no means to ensure that, nor to fail-fast.
 * The results of such modifications are undefined.
 * </p>
 *
 * @author Marden Neubert
 *
 * @since Dec 18, 2004
 */
public class TopologicalOrderIterator extends CrossComponentIterator {
    private LinkedList m_queue;
    private Map        m_inDegreeMap;

    /**
     * Creates a new topological order iterator over the directed graph
     * specified. Traversal will start at one of the graphs <i>sources</i>.
     * See the definition of source at <a
     * href="http://mathworld.wolfram.com/Source.html">
     * http://mathworld.wolfram.com/Source.html</a>.
     *
     * @param dg the directed graph to be iterated.
     */
    public TopologicalOrderIterator( DirectedGraph dg ) {
        this( dg, new LinkedList(  ), new HashMap(  ) );
    }


    // NOTE: This is a hack to deal with the fact that CrossComponentIterator
    // needs to know the start vertex in its constructor
    private TopologicalOrderIterator( DirectedGraph dg, LinkedList queue,
        Map inDegreeMap ) {
        this( dg, initialize( dg, queue, inDegreeMap ) );
        m_queue           = queue;
        m_inDegreeMap     = inDegreeMap;
    }


    // NOTE: This is intentionally private, because starting the sort "in the
    // middle" doesn't make sense.
    private TopologicalOrderIterator( DirectedGraph dg, Object start ) {
        super( dg, start );
    }

    /**
     * @see CrossComponentIterator#isConnectedComponentExhausted()
     */
    protected boolean isConnectedComponentExhausted(  ) {
        // FIXME jvs 25-Apr-2005: This isn't correct for a graph with more than
        // one component.  We will actually exhaust a connected component
        // before the queue is empty, because initialize adds roots from all
        // components to the queue.
        return m_queue.isEmpty(  );
    }


    /**
     * @see CrossComponentIterator#encounterVertex(Object, Edge)
     */
    protected void encounterVertex( Object vertex, Edge edge ) {
        putSeenData( vertex, null );
        decrementInDegree( vertex );
    }


    /**
     * @see CrossComponentIterator#encounterVertexAgain(Object, Edge)
     */
    protected void encounterVertexAgain( Object vertex, Edge edge ) {
        decrementInDegree( vertex );
    }


    /**
     * @see CrossComponentIterator#provideNextVertex()
     */
    protected Object provideNextVertex(  ) {
        return m_queue.removeFirst(  );
    }


    /**
     * Decrements the in-degree of a vertex.
     *
     * @param vertex the vertex whose in-degree will be decremented.
     */
    private void decrementInDegree( Object vertex ) {
        ModifiableInteger inDegree =
            (ModifiableInteger) m_inDegreeMap.get( vertex );

        if( inDegree.value > 0 ) {
            inDegree.value--;

            if( inDegree.value == 0 ) {
                m_queue.addLast( vertex );
            }
        }
    }


    /**
     * Initializes the internal traversal object structure. Sets up the
     * internal queue with the directed graph vertices and creates the control
     * structure for the in-degrees.
     *
     * @param dg the directed graph to be iterated.
     * @param queue initializer for m_queue
     * @param inDegreeMap initializer for m_inDegreeMap
     *
     * @return start vertex
     */
    private static Object initialize( DirectedGraph dg, LinkedList queue,
        Map inDegreeMap ) {
        for( Iterator i = dg.vertexSet(  ).iterator(  ); i.hasNext(  ); ) {
            Object vertex = i.next(  );

            int    inDegree = dg.inDegreeOf( vertex );
            inDegreeMap.put( vertex, new ModifiableInteger( inDegree ) );

            if( inDegree == 0 ) {
                queue.add( vertex );
            }
        }

        if( queue.isEmpty(  ) ) {
            return null;
        }
        else {
            return queue.getFirst(  );
        }
    }
}
