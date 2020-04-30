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
 * GraphSquare.java
 * ----------------------
 * (C) Copyright 2004, by Michael Behrisch and Contributors.
 *
 * Original Author:  Michael Behrisch
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 14-Sep-2004 : Initial revision (MB);
 *
 */
package org._3pq.jgrapht.experimental;

import java.util.*;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.GraphHelper;
import org._3pq.jgrapht.ListenableGraph;
import org._3pq.jgrapht.event.GraphEdgeChangeEvent;
import org._3pq.jgrapht.event.GraphListener;
import org._3pq.jgrapht.event.GraphVertexChangeEvent;
import org._3pq.jgrapht.graph.AbstractBaseGraph;

/**
 * DOCUMENT ME!
 *
 * @author Michael Behrisch
 *
 * @since Sep 14, 2004
 */
public class GraphSquare extends AbstractBaseGraph {
    private static final String UNMODIFIABLE = "this graph is unmodifiable";

    /**
     * Constructor for GraphSquare.
     *
     * @param g the graph of which a square is to be created.
     * @param createLoops
     */
    public GraphSquare( final Graph g, final boolean createLoops ) {
        super( g.getEdgeFactory(  ), false, createLoops );
        super.addAllVertices( g.vertexSet(  ) );
        addSquareEdges( g, createLoops );

        if( g instanceof ListenableGraph ) {
            ( (ListenableGraph) g ).addGraphListener( new GraphListener(  ) {
                    public void edgeAdded( GraphEdgeChangeEvent e ) {
                        Edge edge = e.getEdge(  );
                        addEdgesStartingAt( g, edge.getSource(  ),
                            edge.getTarget(  ), createLoops );
                        addEdgesStartingAt( g, edge.getTarget(  ),
                            edge.getSource(  ), createLoops );
                    }


                    public void edgeRemoved( GraphEdgeChangeEvent e ) { // this is not a very performant implementation
                        GraphSquare.super.removeAllEdges( edgeSet(  ) );
                        addSquareEdges( g, createLoops );
                    }


                    public void vertexAdded( GraphVertexChangeEvent e ) {}


                    public void vertexRemoved( GraphVertexChangeEvent e ) {}
                } );
        }
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


    private void addEdgesStartingAt( final Graph g, final Object v,
        final Object u, boolean createLoops ) {
        if( !g.containsEdge( v, u ) ) {
            return;
        }

        final List adjVertices = GraphHelper.neighborListOf( g, u );

        for( int i = 0; i < adjVertices.size(  ); i++ ) {
            final Object w = adjVertices.get( i );

            if( g.containsEdge( u, w ) && ( v != w || createLoops ) ) {
                super.addEdge( v, w );
            }
        }
    }


    private void addSquareEdges( Graph g, boolean createLoops ) {
        for( Iterator it = g.vertexSet(  ).iterator(  ); it.hasNext(  ); ) {
            Object v           = it.next(  );
            List   adjVertices = GraphHelper.neighborListOf( g, v );

            for( int i = 0; i < adjVertices.size(  ); i++ ) {
                addEdgesStartingAt( g, v, adjVertices.get( i ), createLoops );
            }
        }
    }
}
