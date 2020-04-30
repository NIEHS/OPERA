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
 * GraphGenerator.java
 * -------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.generate;

import java.util.Map;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.VertexFactory;

/**
 * Generates a linear graph of any size.  For a directed graph, the edges are
 * oriented from START_VERTEX to END_VERTEX.
 *
 * @author John V. Sichi
 *
 * @since Sep 16, 2003
 */
public class LinearGraphGenerator implements GraphGenerator {
    /** Role for the first vertex generated. */
    public static final String START_VERTEX = "Start Vertex";

    /** Role for the last vertex generated. */
    public static final String END_VERTEX = "End Vertex";
    private int                m_size;

    /**
     * Construct a new LinearGraphGenerator.
     *
     * @param size number of vertices to be generated
     *
     * @throws IllegalArgumentException if the specified size is negative.
     */
    public LinearGraphGenerator( int size ) {
        if( size < 0 ) {
            throw new IllegalArgumentException( "must be non-negative" );
        }

        m_size = size;
    }

    /**
     * {@inheritDoc}
     */
    public void generateGraph( Graph target, VertexFactory vertexFactory,
        Map resultMap ) {
        Object lastVertex = null;

        for( int i = 0; i < m_size; ++i ) {
            Object newVertex = vertexFactory.createVertex(  );
            target.addVertex( newVertex );

            if( lastVertex == null ) {
                if( resultMap != null ) {
                    resultMap.put( START_VERTEX, newVertex );
                }
            }
            else {
                target.addEdge( lastVertex, newVertex );
            }

            lastVertex = newVertex;
        }

        if( ( resultMap != null ) && ( lastVertex != null ) ) {
            resultMap.put( END_VERTEX, lastVertex );
        }
    }
}
