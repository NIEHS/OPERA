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
 * TraversalListener.java
 * ----------------------
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
 * 11-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org._3pq.jgrapht.event;

/**
 * A listener on graph iterator or on a graph traverser.
 *
 * @author Barak Naveh
 *
 * @since Jul 19, 2003
 */
public interface TraversalListener {
    /**
     * Called to inform listeners that the traversal of the current connected
     * component has finished.
     *
     * @param e the traversal event.
     */
    public void connectedComponentFinished( ConnectedComponentTraversalEvent e );


    /**
     * Called to inform listeners that a traversal of a new connected component
     * has started.
     *
     * @param e the traversal event.
     */
    public void connectedComponentStarted( ConnectedComponentTraversalEvent e );


    /**
     * Called to inform the listener that the specified edge have been visited
     * during the graph traversal. Depending on the traversal algorithm, edge
     * might be visited more than once.
     *
     * @param e the edge traversal event.
     */
    public void edgeTraversed( EdgeTraversalEvent e );


    /**
     * Called to inform the listener that the specified vertex have been
     * visited during the graph traversal. Depending on the traversal
     * algorithm, vertex might be visited more than once.
     *
     * @param e the vertex traversal event.
     */
    public void vertexTraversed( VertexTraversalEvent e );
}
