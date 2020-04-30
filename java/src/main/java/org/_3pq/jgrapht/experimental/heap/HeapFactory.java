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
package org._3pq.jgrapht.experimental.heap;

import java.util.*;

/**
 * .
 * @author  Michael Behrisch
 * @version 1.0
 */
public interface HeapFactory {
    /**
     * .
     *
     * @return  
     */
    public Heap createHeap(  );


    /**
     * .
     *
     * @param comp  
     *
     * @return  
     */
    public Heap createHeap( Comparator comp );


    /**
     * .
     *
     * @param c  
     *
     * @return  
     */
    public Heap createHeap( Collection c );


    /**
     * .
     *
     * @param c  
     * @param comp  
     *
     * @return  
     */
    public Heap createHeap( Collection c, Comparator comp );


    /**
     * .
     *
     * @param maximum  
     *
     * @return  
     */
    public Heap createHeap( boolean maximum );


    /**
     * .
     *
     * @param comp  
     * @param maximum  
     *
     * @return  
     */
    public Heap createHeap( Comparator comp, boolean maximum );


    /**
     * .
     *
     * @param c  
     * @param maximum  
     *
     * @return  
     */
    public Heap createHeap( Collection c, boolean maximum );


    /**
     * .
     *
     * @param c  
     * @param comp  
     * @param maximum  
     *
     * @return  
     */
    public Heap createHeap( Collection c, Comparator comp, boolean maximum );
}
