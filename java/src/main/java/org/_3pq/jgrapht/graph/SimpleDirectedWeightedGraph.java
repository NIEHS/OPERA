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
/* --------------------------------
 * SimpleDirectedWeightedGraph.java
 * --------------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.WeightedGraph;
import org._3pq.jgrapht.edge.EdgeFactories;

/**
 * A simple directed weighted graph. A simple directed weighted graph is a
 * simple directed graph for which edges are assigned weights.
 */
public class SimpleDirectedWeightedGraph extends SimpleDirectedGraph
    implements WeightedGraph {
    private static final long serialVersionUID = 3904960841681220919L;

    /**
     * Creates a new simple directed weighted graph with the specified edge
     * factory.
     *
     * @param ef the edge factory of the new graph.
     */
    public SimpleDirectedWeightedGraph( EdgeFactory ef ) {
        super( ef );
    }


    /**
     * Creates a new simple directed weighted graph.
     */
    public SimpleDirectedWeightedGraph(  ) {
        this( new EdgeFactories.DirectedWeightedEdgeFactory(  ) );
    }
}
