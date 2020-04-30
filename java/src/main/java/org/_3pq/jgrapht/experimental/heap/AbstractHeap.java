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
public abstract class AbstractHeap implements Heap {
    private final Comparator _comp;
    private final int        _compareFactor;
    private Map              _peerMap = null;

    /**
     * Creates a new AbstractHeap object.
     *
     * @param comp  
     * @param maximum  
     */
    public AbstractHeap( Comparator comp, boolean maximum ) {
        _comp              = comp;
        _compareFactor     = maximum ? -1 : 1;
    }

    /**
     * .
     *
     * @return  
     */
    public boolean isEmpty(  ) {
        return size(  ) == 0;
    }


    /**
     * .
     *
     * @param x  
     */
    public final void add( Object x ) {
        ElementPeer peer = createPeer( x );

        if( x instanceof HeapElement ) {
            ( (HeapElement) x ).setPeer( peer );
        }
        else {
            if( _peerMap == null ) {
                _peerMap = new HashMap(  );
            }

            _peerMap.put( x, peer );
        }
    }


    /**
     * .
     *
     * @param c  
     */
    public void addAll( Collection c ) {
        Iterator it = c.iterator(  );

        while( it.hasNext(  ) ) {
            add( it.next(  ) );
        }
    }


    /**
     * .
     *
     * @param x  
     */
    public void update( Object x ) {
        if( x instanceof HeapElement ) {
            ( (HeapElement) x ).getPeer(  ).update(  );
        }
        else {
            ( (ElementPeer) _peerMap.get( x ) ).update(  );
        }
    }


    /**
     * .
     *
     * @param x  
     *
     * @return  
     */
    protected abstract ElementPeer createPeer( Object x );


    /**
     * .
     *
     * @param x  
     * @param y  
     *
     * @return  
     */
    protected final boolean isSmaller( Object x, Object y ) {
        if( _comp != null ) {
            return _comp.compare( x, y ) * _compareFactor < 0;
        }

        return ( (Comparable) x ).compareTo( y ) * _compareFactor < 0;
    }
}
