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
/* --------------------
 * ListenableGraph.java
 * --------------------
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
 * 10-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org._3pq.jgrapht;

import org._3pq.jgrapht.event.GraphListener;
import org._3pq.jgrapht.event.VertexSetListener;

/**
 * A graph that supports listeners on structural change events.
 *
 * @author Barak Naveh
 *
 * @see org._3pq.jgrapht.event.GraphListener
 * @see org._3pq.jgrapht.event.VertexSetListener
 * @since Jul 20, 2003
 */
public interface ListenableGraph extends Graph {
    /**
     * Adds the specified graph listener to this graph, if not already present.
     *
     * @param l the listener to be added.
     */
    public void addGraphListener( GraphListener l );


    /**
     * Adds the specified vertex set listener to this graph, if not already
     * present.
     *
     * @param l the listener to be added.
     */
    public void addVertexSetListener( VertexSetListener l );


    /**
     * Removes the specified graph listener from this graph, if present.
     *
     * @param l he listener to be removed.
     */
    public void removeGraphListener( GraphListener l );


    /**
     * Removes the specified vertex set listener from this graph, if present.
     *
     * @param l the listener to be removed.
     */
    public void removeVertexSetListener( VertexSetListener l );
}
