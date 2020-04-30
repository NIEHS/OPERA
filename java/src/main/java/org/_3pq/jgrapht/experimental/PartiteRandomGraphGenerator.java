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
/* -------------------
 * UniformRandomGraphGenerator.java
 * -------------------
 * (C) Copyright 2003, by Michael Behrisch and Contributors.
 *
 * Original Author:  Michael Behrisch
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 13-Sep-2004 : Initial revision (MB);
 *
 */
// package org._3pq.jgrapht.generate;
package org._3pq.jgrapht.experimental;

import java.util.Arrays;
import java.util.Map;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.VertexFactory;
import org._3pq.jgrapht.generate.GraphGenerator;

/**
 * PartiteRandomGraphGenerator generates a <a
 * href="http://mathworld.wolfram.com/RandomGraph.html">uniform random
 * graph</a> of any size. A uniform random graph contains edges chosen
 * independently uniformly at random from the set of possible edges between
 * partition classes.
 *
 * @author Michael Behrisch
 *
 * @since Sep 13, 2004
 */
public class PartiteRandomGraphGenerator implements GraphGenerator {
    private final int[] m_numVertices;
    private final int   m_numEdges;

    /**
     * Construct a new PartiteRandomGraphGenerator for a bipartite graph.
     *
     * @param numVertices1 number of vertices in the first partition
     * @param numVertices2 number of vertices in the second partition
     * @param numEdges number of edges to be generated
     *
     * @throws IllegalArgumentException
     */
    public PartiteRandomGraphGenerator( int numVertices1, int numVertices2,
        int numEdges ) {
        if( numVertices1 < 0 || numVertices2 < 0 ) {
            throw new IllegalArgumentException( "must be non-negative" );
        }

        if( numEdges < 0 || numEdges > numVertices1 * numVertices2 ) {
            throw new IllegalArgumentException( "illegal number of edges" );
        }

        final int[] numVertices = { numVertices1, numVertices2 };
        m_numVertices     = numVertices;
        m_numEdges        = numEdges;
    }


    /**
     * Construct a new PartiteRandomGraphGenerator for a k-partite graph.
     *
     * @param numVertices number of vertices in the k partitions
     * @param numEdges number of edges to be generated between any two
     *        partitions
     *
     * @throws IllegalArgumentException
     */
    public PartiteRandomGraphGenerator( int[] numVertices, int numEdges ) {
        if( numEdges < 0 ) {
            throw new IllegalArgumentException( "illegal number of edges" );
        }

        for( int i = 0; i < numVertices.length; i++ ) {
            if( numVertices[ i ] < 0 ) {
                throw new IllegalArgumentException( "must be non-negative" );
            }

            for( int j = 0; j < i; j++ ) {
                if( numEdges > numVertices[ i ] * numVertices[ j ] ) {
                    throw new IllegalArgumentException( 
                        "illegal number of edges" );
                }
            }
        }

        m_numVertices     = numVertices;
        m_numEdges        = numEdges;
    }

    /**
     * @see GraphGenerator#generateGraph
     */
    public void generateGraph( Graph target, VertexFactory vertexFactory,
        Map resultMap ) {
        Object[][] vertices = new Object[ m_numVertices.length ][];

        for( int i = 0; i < m_numVertices.length; i++ ) {
            vertices[ i ] =
                RandomGraphHelper.addVertices( target, vertexFactory,
                    m_numVertices[ i ] );

            if( resultMap != null ) {
                resultMap.put( Integer.toString( i ), vertices[ i ] );
            }

            for( int j = 0; j < i; j++ ) {
                RandomGraphHelper.addEdges( target,
                    Arrays.asList( vertices[ i ] ),
                    Arrays.asList( vertices[ j ] ), m_numEdges );
            }
        }
    }
}
