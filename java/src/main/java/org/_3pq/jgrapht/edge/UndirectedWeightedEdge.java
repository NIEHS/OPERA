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
 * UndirectedWeightedEdge.java
 * ---------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 10-Aug-2003 : General edge refactoring (BN);
 *
 */
package org._3pq.jgrapht.edge;

/**
 * An implementation of undirected weighted edge.
 *
 * @author Barak Naveh
 *
 * @since Jul 16, 2003
 */
public class UndirectedWeightedEdge extends UndirectedEdge {
    private static final long serialVersionUID = 4120853256903012915L;
    private double            m_weight = DEFAULT_EDGE_WEIGHT;

    /**
     * @see UndirectedEdge#UndirectedEdge(Object, Object)
     */
    public UndirectedWeightedEdge( Object sourceVertex, Object targetVertex ) {
        super( sourceVertex, targetVertex );
    }


    /**
     * Constructor for UndirectedWeightedEdge.
     *
     * @param sourceVertex source vertex of the new edge.
     * @param targetVertex target vertex of the new edge.
     * @param weight the weight of the new edge.
     */
    public UndirectedWeightedEdge( Object sourceVertex, Object targetVertex,
        double weight ) {
        super( sourceVertex, targetVertex );
        m_weight = weight;
    }

    /**
     * @see org._3pq.jgrapht.Edge#setWeight(double)
     */
    public void setWeight( double weight ) {
        m_weight = weight;
    }


    /**
     * @see org._3pq.jgrapht.Edge#getWeight()
     */
    public double getWeight(  ) {
        return m_weight;
    }
}
