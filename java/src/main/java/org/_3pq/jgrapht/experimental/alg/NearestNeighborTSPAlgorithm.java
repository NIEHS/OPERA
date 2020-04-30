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

import java.util.*;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.WeightedGraph;
import org._3pq.jgrapht.graph.SimpleWeightedGraph;

/**
 * A trivial heuristic for the Travelling Salesperson problem which simply
 * usest the closest vertex available. Fast, but graphs can be generated,
 * where it performs arbitrarily bad.
 *
 * @author Michael Behrisch
 */
public class NearestNeighborTSPAlgorithm extends WeightedGraphAlgorithm
    implements TravellingSalespersonAlgorithm {
    /**
     * Creates a new NearestNeighborTSPAlgorithm object.
     *
     * @param wgraph  
     */
    public NearestNeighborTSPAlgorithm( WeightedGraph wgraph ) {
        super( wgraph );
    }

    /**
     * .
     *
     * @return  
     *
     * @throws Exception  
     */
    public WeightedGraph tspTour(  ) throws Exception {
        WeightedGraph tour      = new SimpleWeightedGraph(  );
        Set           used      = new HashSet(  );
        Object        posVertex = _wgraph.vertexSet(  ).iterator(  ).next(  );
        used.add( posVertex );

        while( used.size(  ) < _wgraph.vertexSet(  ).size(  ) ) {
            double min  = Double.POSITIVE_INFINITY;
            Edge   next = null;

            for( Iterator it = _wgraph.edgesOf( posVertex ).iterator(  );
                    it.hasNext(  ); ) {
                Edge edge = (Edge) it.next(  );

                if( !used.contains( edge.oppositeVertex( posVertex ) )
                        && edge.getWeight(  ) < min ) {
                    min      = edge.getWeight(  );
                    next     = edge;
                }
            }

            if( next == null ) {
                throw new Exception( "NoTSPTour" );
            }

            posVertex = next.oppositeVertex( posVertex );
            used.add( posVertex );
            tour.addEdge( next );
        }

        return tour;
    }
}
