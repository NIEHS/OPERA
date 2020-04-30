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
package org._3pq.jgrapht.experimental.alg;

import org._3pq.jgrapht.WeightedGraph;
import org._3pq.jgrapht.experimental.heap.HeapFactory;

/**
 * A concrete implementation of ShortestPathAlgorithm using Dijkstra's method
 * which works for directed and undirected graphs. If the vertices in the
 * graph are HeapVertices this implementation meets the runtime conditions on
 * Dijkstra (O(m log(n))) provided the graph can access adjacent edges in O(1)
 * and the heap used is a FibonacciHeap (which it is not by default, since
 * FibonacciHeaps have a large (constant factor) overhead over BinaryHeaps).
 *
 * @author Michael Behrisch
 */
public final class DijkstraShortestPathAlgorithm extends DijkstraAlgorithm
    implements ShortestPathAlgorithm {
    /**
     * Creates an instance of DijkstraShortestPathAlgorithm for the graph given
     * using the default Heap.
     *
     * @param wgraph The WeightedGraph where a shortest path spanning tree will
     *        be determined.
     */
    public DijkstraShortestPathAlgorithm( WeightedGraph wgraph ) {
        super( wgraph, false );
    }


    /**
     * Creates an instance of DijkstraShortestPathAlgorithm for the graph given
     * using the given Heap.
     *
     * @param wgraph The WeightedGraph where a shortest path spanning tree will
     *        be determined.
     * @param factory The factory to be used for creating heaps.
     */
    public DijkstraShortestPathAlgorithm( WeightedGraph wgraph,
        HeapFactory factory ) {
        super( wgraph, factory, false );
    }

    /**
     * Determines the shortest path from a given vertex to all other vertices
     * that are in the same connected set as the given vertex in the weighted
     * graph using Dijkstra's algorithm.
     *
     * @param from The Vertex from where we want to obtain the shortest path to
     *        all other vertices.
     *
     * @return A WeightedGraph comprising of the shortest path spanning tree.
     */
    public final WeightedGraph shortestPathTree( Object from ) {
        return optimumPathTree( from );
    }


    /**
     * .
     *
     * @param vertexPrio  
     * @param edgeWeight  
     *
     * @return  
     */
    protected double priorityFunction( double vertexPrio, double edgeWeight ) {
        return vertexPrio + edgeWeight;
    }
}
