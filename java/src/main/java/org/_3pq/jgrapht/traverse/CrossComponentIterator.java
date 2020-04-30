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
/* ---------------------------
 * CrossComponentIterator.java
 * ---------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 31-Jul-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 * 31-Jan-2004 : Extracted cross-component traversal functionality (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.event.ConnectedComponentTraversalEvent;
import org._3pq.jgrapht.event.EdgeTraversalEvent;
import org._3pq.jgrapht.event.VertexTraversalEvent;

/**
 * Provides a cross-connected-component traversal functionality for iterator
 * subclasses.
 *
 * @author Barak Naveh
 *
 * @since Jan 31, 2004
 */
public abstract class CrossComponentIterator extends AbstractGraphIterator {
    private static final int CCS_BEFORE_COMPONENT = 1;
    private static final int CCS_WITHIN_COMPONENT = 2;
    private static final int CCS_AFTER_COMPONENT  = 3;

    //
    private final ConnectedComponentTraversalEvent m_ccFinishedEvent =
        new ConnectedComponentTraversalEvent( this,
            ConnectedComponentTraversalEvent.CONNECTED_COMPONENT_FINISHED );
    private final ConnectedComponentTraversalEvent m_ccStartedEvent =
        new ConnectedComponentTraversalEvent( this,
            ConnectedComponentTraversalEvent.CONNECTED_COMPONENT_STARTED );

    // TODO: support ConcurrentModificationException if graph modified 
    // during iteration. 
    private FlyweightEdgeEvent   m_reusableEdgeEvent;
    private FlyweightVertexEvent m_reusableVertexEvent;
    private Iterator             m_vertexIterator = null;

    /**
     * Stores the vertices that have been seen during iteration and
     * (optionally) some additional traversal info regarding each vertex.
     */
    private Map       m_seen        = new HashMap(  );
    private Object    m_startVertex;
    private Specifics m_specifics;

    /** The connected component state */
    private int m_state = CCS_BEFORE_COMPONENT;

    /**
     * Creates a new iterator for the specified graph. Iteration will start at
     * the specified start vertex. If the specified start vertex is
     * <code>null</code>, Iteration will start at an arbitrary graph vertex.
     *
     * @param g the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     *
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public CrossComponentIterator( Graph g, Object startVertex ) {
        super(  );

        if( g == null ) {
            throw new NullPointerException( "graph must not be null" );
        }

        m_specifics          = createGraphSpecifics( g );
        m_vertexIterator     = g.vertexSet(  ).iterator(  );
        setCrossComponentTraversal( startVertex == null );

        m_reusableEdgeEvent       = new FlyweightEdgeEvent( this, null );
        m_reusableVertexEvent     = new FlyweightVertexEvent( this, null );

        if( startVertex == null ) {
            // pick a start vertex if graph not empty 
            if( m_vertexIterator.hasNext(  ) ) {
                m_startVertex = m_vertexIterator.next(  );
            }
            else {
                m_startVertex = null;
            }
        }
        else if( g.containsVertex( startVertex ) ) {
            m_startVertex = startVertex;
        }
        else {
            throw new IllegalArgumentException( 
                "graph must contain the start vertex" );
        }
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext(  ) {
        if( m_startVertex != null ) {
            encounterStartVertex(  );
        }

        if( isConnectedComponentExhausted(  ) ) {
            if( m_state == CCS_WITHIN_COMPONENT ) {
                m_state = CCS_AFTER_COMPONENT;
                fireConnectedComponentFinished( m_ccFinishedEvent );
            }

            if( isCrossComponentTraversal(  ) ) {
                while( m_vertexIterator.hasNext(  ) ) {
                    Object v = m_vertexIterator.next(  );

                    if( !isSeenVertex( v ) ) {
                        encounterVertex( v, null );
                        m_state = CCS_BEFORE_COMPONENT;

                        return true;
                    }
                }

                return false;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }


    /**
     * @see java.util.Iterator#next()
     */
    public Object next(  ) {
        if( m_startVertex != null ) {
            encounterStartVertex(  );
        }

        if( hasNext(  ) ) {
            if( m_state == CCS_BEFORE_COMPONENT ) {
                m_state = CCS_WITHIN_COMPONENT;
                fireConnectedComponentStarted( m_ccStartedEvent );
            }

            Object nextVertex = provideNextVertex(  );
            fireVertexTraversed( createVertexTraversalEvent( nextVertex ) );

            addUnseenChildrenOf( nextVertex );

            return nextVertex;
        }
        else {
            throw new NoSuchElementException(  );
        }
    }


    /**
     * Returns <tt>true</tt> if there are no more uniterated vertices in the
     * currently iterated connected component; <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if there are no more uniterated vertices in the
     *         currently iterated connected component; <tt>false</tt>
     *         otherwise.
     */
    protected abstract boolean isConnectedComponentExhausted(  );


    /**
     * Update data structures the first time we see a vertex.
     *
     * @param vertex the vertex encountered
     * @param edge the edge via which the vertex was encountered, or null if
     *        the vertex is a starting point
     */
    protected abstract void encounterVertex( Object vertex, Edge edge );


    /**
     * Returns the vertex to be returned in the following call to the iterator
     * <code>next</code> method.
     *
     * @return the next vertex to be returned by this iterator.
     */
    protected abstract Object provideNextVertex(  );


    /**
     * Access the data stored for a seen vertex.
     *
     * @param vertex a vertex which has already been seen.
     *
     * @return data associated with the seen vertex or <code>null</code> if no
     *         data was associated with the vertex. A <code>null</code> return
     *         can also indicate that the vertex was explicitly associated
     *         with <code>null</code>.
     */
    protected Object getSeenData( Object vertex ) {
        return m_seen.get( vertex );
    }


    /**
     * Determines whether a vertex has been seen yet by this traversal.
     *
     * @param vertex vertex in question
     *
     * @return <tt>true</tt> if vertex has already been seen
     */
    protected boolean isSeenVertex( Object vertex ) {
        return m_seen.containsKey( vertex );
    }


    /**
     * Called whenever we re-encounter a vertex.  The default implementation
     * does nothing.
     *
     * @param vertex the vertex re-encountered
     * @param edge the edge via which the vertex was re-encountered
     */
    protected abstract void encounterVertexAgain( Object vertex, Edge edge );


    /**
     * Stores iterator-dependent data for a vertex that has been seen.
     *
     * @param vertex a vertex which has been seen.
     * @param data data to be associated with the seen vertex.
     *
     * @return previous value associated with specified vertex or
     *         <code>null</code> if no data was associated with the vertex. A
     *         <code>null</code> return can also indicate that the vertex was
     *         explicitly associated with <code>null</code>.
     */
    protected Object putSeenData( Object vertex, Object data ) {
        return m_seen.put( vertex, data );
    }


    static Specifics createGraphSpecifics( Graph g ) {
        if( g instanceof DirectedGraph ) {
            return new DirectedSpecifics( (DirectedGraph) g );
        }
        else {
            return new UndirectedSpecifics( g );
        }
    }


    private void addUnseenChildrenOf( Object vertex ) {
        List edges = m_specifics.edgesOf( vertex );

        for( Iterator i = edges.iterator(  ); i.hasNext(  ); ) {
            Edge e = (Edge) i.next(  );
            fireEdgeTraversed( createEdgeTraversalEvent( e ) );

            Object v = e.oppositeVertex( vertex );

            if( isSeenVertex( v ) ) {
                encounterVertexAgain( v, e );
            }
            else {
                encounterVertex( v, e );
            }
        }
    }


    private EdgeTraversalEvent createEdgeTraversalEvent( Edge edge ) {
        if( isReuseEvents(  ) ) {
            m_reusableEdgeEvent.setEdge( edge );

            return m_reusableEdgeEvent;
        }
        else {
            return new EdgeTraversalEvent( this, edge );
        }
    }


    private VertexTraversalEvent createVertexTraversalEvent( Object vertex ) {
        if( isReuseEvents(  ) ) {
            m_reusableVertexEvent.setVertex( vertex );

            return m_reusableVertexEvent;
        }
        else {
            return new VertexTraversalEvent( this, vertex );
        }
    }


    private void encounterStartVertex(  ) {
        encounterVertex( m_startVertex, null );
        m_startVertex = null;
    }

    static interface SimpleContainer {
        /**
         * Tests if this container is empty.
         *
         * @return <code>true</code> if empty, otherwise <code>false</code>.
         */
        public boolean isEmpty(  );


        /**
         * Adds the specified object to this container.
         *
         * @param o the object to be added.
         */
        public void add( Object o );


        /**
         * Remove an object from this container and return it.
         *
         * @return the object removed from this container.
         */
        public Object remove(  );
    }

    /**
     * Provides unified interface for operations that are different in directed
     * graphs and in undirected graphs.
     */
    abstract static class Specifics {
        /**
         * Returns the edges outgoing from the specified vertex in case of
         * directed graph, and the edge touching the specified vertex in case
         * of undirected graph.
         *
         * @param vertex the vertex whose outgoing edges are to be returned.
         *
         * @return the edges outgoing from the specified vertex in case of
         *         directed graph, and the edge touching the specified vertex
         *         in case of undirected graph.
         */
        public abstract List edgesOf( Object vertex );
    }


    /**
     * A reusable edge event.
     *
     * @author Barak Naveh
     *
     * @since Aug 11, 2003
     */
    static class FlyweightEdgeEvent extends EdgeTraversalEvent {
        private static final long serialVersionUID = 4051327833765000755L;

        /**
         * @see EdgeTraversalEvent#EdgeTraversalEvent(Object, Edge)
         */
        public FlyweightEdgeEvent( Object eventSource, Edge edge ) {
            super( eventSource, edge );
        }

        /**
         * Sets the edge of this event.
         *
         * @param edge the edge to be set.
         */
        protected void setEdge( Edge edge ) {
            m_edge = edge;
        }
    }


    /**
     * A reusable vertex event.
     *
     * @author Barak Naveh
     *
     * @since Aug 11, 2003
     */
    static class FlyweightVertexEvent extends VertexTraversalEvent {
        private static final long serialVersionUID = 3834024753848399924L;

        /**
         * @see VertexTraversalEvent#VertexTraversalEvent(Object, Object)
         */
        public FlyweightVertexEvent( Object eventSource, Object vertex ) {
            super( eventSource, vertex );
        }

        /**
         * Sets the vertex of this event.
         *
         * @param vertex the vertex to be set.
         */
        protected void setVertex( Object vertex ) {
            m_vertex = vertex;
        }
    }


    /**
     * An implementation of {@link TraverseUtils.Specifics} for a directed
     * graph.
     */
    private static class DirectedSpecifics extends Specifics {
        private DirectedGraph m_graph;

        /**
         * Creates a new DirectedSpecifics object.
         *
         * @param g the graph for which this specifics object to be created.
         */
        public DirectedSpecifics( DirectedGraph g ) {
            m_graph = g;
        }

        /**
         * @see CrossComponentIterator.Specifics#edgesOf(Object)
         */
        public List edgesOf( Object vertex ) {
            return m_graph.outgoingEdgesOf( vertex );
        }
    }


    /**
     * An implementation of {@link TraverseUtils.Specifics} in which edge
     * direction (if any) is ignored.
     */
    private static class UndirectedSpecifics extends Specifics {
        private Graph m_graph;

        /**
         * Creates a new UndirectedSpecifics object.
         *
         * @param g the graph for which this specifics object to be created.
         */
        public UndirectedSpecifics( Graph g ) {
            m_graph = g;
        }

        /**
         * @see CrossComponentIterator.Specifics#edgesOf(Object)
         */
        public List edgesOf( Object vertex ) {
            return m_graph.edgesOf( vertex );
        }
    }
}
