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
/* ----------------
 * EdgeListFactory.java
 * ----------------
 * (C) Copyright 2005, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 01-Jun-2005 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.graph;

import java.util.List;

/**
 * A factory for edge lists.  This interface allows the creator of a graph to
 * choose the {@link java.util.List} implementation used internally by the
 * graph to maintain lists of edges.  This provides control over performance
 * tradeoffs between memory and CPU usage.
 *
 * @author John V. Sichi
 * @version $Id$
 */
public interface EdgeListFactory {
    /**
     * Create a new edge list for a particular vertex.
     *
     * @param vertex the vertex for which the edge list is being created;
     *        sophisticated factories may be able to use this information to
     *        choose an optimal list representation (e.g. ArrayList for a
     *        vertex expected to have low degree, and TreeList for a vertex
     *        expected to have high degree)
     *
     * @return new list
     */
    public List createEdgeList( Object vertex );
}


// End EdgeListFactory.java
