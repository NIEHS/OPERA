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
public class BinaryHeap extends AbstractHeap {
    private static final HeapFactory FACTORY =
        new AbstractHeapFactory(  ) {
            public Heap createHeap( Collection c, Comparator comp,
                boolean maximum ) {
                return new BinaryHeap( c, comp, maximum );
            }
        };

    private final List _elems;

    /**
     * Creates a new BinaryHeap object.
     *
     * @param c  
     * @param comp  
     * @param maximum  
     */
    public BinaryHeap( Collection c, Comparator comp, boolean maximum ) {
        super( comp, maximum );
        _elems = new ArrayList( Math.max( c.size(  ), 10 ) );
        addAll( c );
    }

    /**
     * .
     *
     * @return  
     */
    public static final HeapFactory getFactory(  ) {
        return FACTORY;
    }


    /**
     * .
     */
    public void clear(  ) {
        _elems.clear(  );
    }


    /**
     * .
     *
     * @return  
     */
    public Object extractTop(  ) {
        Elem z = (Elem) _elems.get( 0 );
        Elem e = (Elem) _elems.remove( _elems.size(  ) - 1 );

        if( !isEmpty(  ) ) {
            _elems.set( 0, e );
            e.pos = 0;
            percolateDown( 0 );
        }
        if( z.x instanceof HeapElement ) {
            ( (HeapElement) z.x ).setPeer( null );
        }
        return z.x;
    }


    /**
     * .
     *
     * @return  
     */
    public int size(  ) {
        return _elems.size(  );
    }


    /**
     * .
     *
     * @return  
     */
    public String toString(  ) {
        return _elems.toString(  );
    }


    /**
     * .
     *
     * @param x  
     *
     * @return  
     */
    protected ElementPeer createPeer( Object x ) {
        Elem e = new Elem( x );
        _elems.add( e );
        e.pos = _elems.size(  ) - 1;
        percolateUp( e.pos );

        return e;
    }


    private void percolateDown( int pos ) {
        Elem h = (Elem) _elems.get( pos );

        while( 2 * pos + 1 < size(  ) ) {
            int  i = 1;
            Elem c = (Elem) _elems.get( 2 * pos + i );

            if( 2 * pos + 2 < size(  )
                    && isSmaller( ( (Elem) _elems.get( 2 * pos + 2 ) ).x, c.x ) ) {
                i++;
                c = (Elem) _elems.get( 2 * pos + i );
            }

            if( isSmaller( h.x, c.x ) ) {
                break;
            }

            _elems.set( pos, c );
            c.pos     = pos;
            pos       = 2 * pos + i;
        }

        _elems.set( pos, h );
        h.pos = pos;
    }


    private void percolateUp( int pos ) {
        Elem h = (Elem) _elems.get( pos );

        while( pos != 0
                && isSmaller( h.x,
                    ( (Elem) _elems.get( ( pos + 1 ) / 2 - 1 ) ).x ) ) {
            Elem p = (Elem) _elems.get( ( pos + 1 ) / 2 - 1 );
            _elems.set( pos, p );
            p.pos     = pos;
            pos       = ( pos + 1 ) / 2 - 1;
        }

        _elems.set( pos, h );
        h.pos = pos;
    }

    private final class Elem implements ElementPeer {
        Object x;
        int    pos;

        Elem( Object e ) {
            x = e;
        }

        /**
         * .
         *
         * @return  
         */
        public Object getObject(  ) {
            return x;
        }


        /**
         * .
         *
         * @return  
         */
        public String toString(  ) {
            return x.toString(  );
        }


        /**
         * .
         */
        public void update(  ) {
            percolateUp( pos );
        }
    }
}
