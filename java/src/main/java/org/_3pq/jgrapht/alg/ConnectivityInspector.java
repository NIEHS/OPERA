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
/* --------------------------
 * ConnectivityInspector.java
 * --------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Aug-2003 : Initial revision (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.UndirectedGraph;
import org._3pq.jgrapht.event.ConnectedComponentTraversalEvent;
import org._3pq.jgrapht.event.GraphEdgeChangeEvent;
import org._3pq.jgrapht.event.GraphListener;
import org._3pq.jgrapht.event.GraphVertexChangeEvent;
import org._3pq.jgrapht.event.TraversalListenerAdapter;
import org._3pq.jgrapht.event.VertexTraversalEvent;
import org._3pq.jgrapht.graph.AsUndirectedGraph;
import org._3pq.jgrapht.traverse.BreadthFirstIterator;

/**
 * Allows obtaining various connectivity aspects of a graph. The <i>inspected
 * graph</i> is specified at construction time and cannot be modified.
 * Currently, the inspector supports connected components for an undirected
 * graph and weakly connected components for a directed graph.  To find
 * strongly connected components, use {@link StrongConnectivityInspector}
 * instead.
 * 
 * <p>
 * The inspector methods work in a lazy fashion: no computation is performed
 * unless immediately necessary. Computation are done once and results and
 * cached within this class for future need.
 * </p>
 * 
 * <p>
 * The inspector is also a {@link org._3pq.jgrapht.event.GraphListener}. If
 * added as a listener to the inspected graph, the inspector will amend
 * internal cached results instead of recomputing them. It is efficient when a
 * few modifications are applied to a large graph. If many modifications are
 * expected it will not be efficient due to added overhead on graph update
 * operations. If inspector is added as listener to a graph other than the one
 * it inspects, results are undefined.
 * </p>
 *
 * @author Barak Naveh
 * @author John V. Sichi
 *
 * @since Aug 6, 2003
 */
public class ConnectivityInspector implements GraphListener {
    List          m_connectedSets;
    Map           m_vertexToConnectedSet;
    private Graph m_graph;

    /**
     * Creates a connectivity inspector for the specified undirected graph.
     *
     * @param g the graph for which a connectivity inspector to be created.
     */
    public ConnectivityInspector( UndirectedGraph g ) {
        init(  );
        m_graph = g;
    }


    /**
     * Creates a connectivity inspector for the specified directed graph.
     *
     * @param g the graph for which a connectivity inspector to be created.
     */
    public ConnectivityInspector( DirectedGraph g ) {
        init(  );
        m_graph = new AsUndirectedGraph( g );
    }

    /**
     * Test if the inspected graph is connected. An empty graph is <i>not</i>
     * considered connected.
     *
     * @return <code>true</code> if and only if inspected graph is connected.
     */
    public boolean isGraphConnected(  ) {
        return lazyFindConnectedSets(  ).size(  ) == 1;
    }


    /**
     * Returns a set of all vertices that are in the maximally connected
     * component together with the specified vertex. For more on maximally
     * connected component, see <a
     * href="http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html">
     * http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html</a>.
     *
     * @param vertex the vertex for which the connected set to be returned.
     *
     * @return a set of all vertices that are in the maximally connected
     *         component together with the specified vertex.
     */
    public Set connectedSetOf( Object vertex ) {
        Set connectedSet = (Set) m_vertexToConnectedSet.get( vertex );

        if( connectedSet == null ) {
            connectedSet = new HashSet(  );

            BreadthFirstIterator i =
                new BreadthFirstIterator( m_graph, vertex );

            while( i.hasNext(  ) ) {
                connectedSet.add( i.next(  ) );
            }

            m_vertexToConnectedSet.put( vertex, connectedSet );
        }

        return connectedSet;
    }


    /**
     * Returns a list of <code>Set</code>s, where each set contains all
     * vertices that are in the same maximally connected component. All graph
     * vertices occur in exactly one set.  For more on maximally connected
     * component, see <a
     * href="http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html">
     * http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html</a>.
     *
     * @return Returns a list of <code>Set</code>s, where each set contains all
     *         vertices that are in the same maximally connected component.
     */
    public List connectedSets(  ) {
        return lazyFindConnectedSets(  );
    }


    /**
     * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
     */
    public void edgeAdded( GraphEdgeChangeEvent e ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    /**
     * @see GraphListener#edgeRemoved(GraphEdgeChangeEvent)
     */
    public void edgeRemoved( GraphEdgeChangeEvent e ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    /**
     * Tests if there is a path from the specified source vertex to the
     * specified target vertices. For a directed graph, direction is ignored
     * for this interpretation of path.
     * 
     * <p>
     * Note: Future versions of this method might not ignore edge directions
     * for directed graphs.
     * </p>
     *
     * @param sourceVertex one end of the path.
     * @param targetVertex another end of the path.
     *
     * @return <code>true</code> if and only if there is a path from the source
     *         vertex to the target vertex.
     */
    public boolean pathExists( Object sourceVertex, Object targetVertex ) {
        /*
         * TODO: Ignoring edge direction for directed graph may be
         * confusing. For directed graphs, consider Dijkstra's algorithm.
         */
        Set sourceSet = connectedSetOf( sourceVertex );

        return sourceSet.contains( targetVertex );
    }


    /**
     * @see org._3pq.jgrapht.event.VertexSetListener#vertexAdded(GraphVertexChangeEvent)
     */
    public void vertexAdded( GraphVertexChangeEvent e ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    /**
     * @see org._3pq.jgrapht.event.VertexSetListener#vertexRemoved(GraphVertexChangeEvent)
     */
    public void vertexRemoved( GraphVertexChangeEvent e ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    private void init(  ) {
        m_connectedSets            = null;
        m_vertexToConnectedSet     = new HashMap(  );
    }


    private List lazyFindConnectedSets(  ) {
        if( m_connectedSets == null ) {
            m_connectedSets = new ArrayList(  );

            Set vertexSet = m_graph.vertexSet(  );

            if( vertexSet.size(  ) > 0 ) {
                BreadthFirstIterator i =
                    new BreadthFirstIterator( m_graph, null );
                i.addTraversalListener( new MyTraversalListener(  ) );

                while( i.hasNext(  ) ) {
                    i.next(  );
                }
            }
        }

        return m_connectedSets;
    }

    /**
     * A traversal listener that groups all vertices according to to their
     * containing connected set.
     *
     * @author Barak Naveh
     *
     * @since Aug 6, 2003
     */
    private class MyTraversalListener extends TraversalListenerAdapter {
        private Set m_currentConnectedSet;

        /**
         * @see TraversalListenerAdapter#connectedComponentFinished(ConnectedComponentTraversalEvent)
         */
        public void connectedComponentFinished( 
            ConnectedComponentTraversalEvent e ) {
            m_connectedSets.add( m_currentConnectedSet );
        }


        /**
         * @see TraversalListenerAdapter#connectedComponentStarted(ConnectedComponentTraversalEvent)
         */
        public void connectedComponentStarted( 
            ConnectedComponentTraversalEvent e ) {
            m_currentConnectedSet = new HashSet(  );
        }


        /**
         * @see TraversalListenerAdapter#vertexTraversed(Object)
         */
        public void vertexTraversed( VertexTraversalEvent e ) {
            Object v = e.getVertex(  );
            m_currentConnectedSet.add( v );
            m_vertexToConnectedSet.put( v, m_currentConnectedSet );
        }
    }
}
