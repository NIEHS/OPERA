/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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
/* ------------------
 * GraphIterator.java
 * ------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 31-Jul-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import java.util.Iterator;

import org._3pq.jgrapht.event.TraversalListener;

/**
 * A graph iterator.
 *
 * @author Barak Naveh
 *
 * @since Jul 31, 2003
 */
public interface GraphIterator extends Iterator {
    /**
     * Test whether this iterator is set to traverse the grpah across connected
     * components.
     *
     * @return <code>true</code> if traverses across connected components,
     *         otherwise <code>false</code>.
     */
    public boolean isCrossComponentTraversal(  );


    /**
     * Sets a value the <code>reuseEvents</code> flag. If the
     * <code>reuseEvents</code> flag is set to <code>true</code> this class
     * will reuse previously fired events and will not create a new object for
     * each event. This option increases performance but should be used with
     * care, especially in multithreaded environment.
     *
     * @param reuseEvents whether to reuse previously fired event objects
     *        instead of creating a new event object for each event.
     */
    public void setReuseEvents( boolean reuseEvents );


    /**
     * Tests whether the <code>reuseEvents</code> flag is set. If the flag is
     * set to <code>true</code> this class will reuse previously fired events
     * and will not create a new object for each event. This option increases
     * performance but should be used with care, especially in multithreaded
     * environment.
     *
     * @return the value of the <code>reuseEvents</code> flag.
     */
    public boolean isReuseEvents(  );


    /**
     * Adds the specified traversal listener to this iterator.
     *
     * @param l the traversal listener to be added.
     */
    public void addTraversalListener( TraversalListener l );


    /**
     * Unsupported.
     *
     * @throws UnsupportedOperationException
     */
    public void remove(  );


    /**
     * Removes the specified traversal listener from this iterator.
     *
     * @param l the traversal listener to be removed.
     */
    public void removeTraversalListener( TraversalListener l );
}
