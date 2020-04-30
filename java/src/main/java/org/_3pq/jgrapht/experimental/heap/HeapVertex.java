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

/**
 * .
 *
 * @author Michael Behrisch
 * @version 1.0
 */
public class HeapVertex implements HeapElement, Comparable {
    private ElementPeer  _p;
    private Object       _additional;
    private final Object _vertex;
    private double       _priority;

    /**
     * Creates a new HeapVertex object.
     */
    public HeapVertex(  ) {
        _vertex = this;
    }


    /**
     * Creates a new HeapVertex object.
     *
     * @param vertex
     */
    public HeapVertex( Object vertex ) {
        _vertex = vertex;
    }

    /**
     * .
     *
     * @param add
     */
    public void setAdditional( Object add ) {
        _additional = add;
    }


    /**
     * .
     *
     * @return
     */
    public Object getAdditional(  ) {
        return _additional;
    }


    /**
     * .
     *
     * @param peer
     */
    public void setPeer( ElementPeer peer ) {
        _p = peer;
    }


    /**
     * .
     *
     * @return
     */
    public ElementPeer getPeer(  ) {
        return _p;
    }


    /**
     * .
     *
     * @param prio
     */
    public void setPriority( double prio ) {
        _priority = prio;
    }


    /**
     * .
     *
     * @return
     */
    public double getPriority(  ) {
        return _priority;
    }


    /**
     * .
     *
     * @return
     */
    public Object getVertex(  ) {
        return _vertex;
    }


    /**
     * .
     *
     * @param o
     *
     * @return
     */
    public int compareTo( Object o ) {
        HeapVertex other = (HeapVertex) o;

        if( other._priority < _priority ) {
            return 1;
        }

        if( other._priority > _priority ) {
            return -1;
        }

        return 0;
    }


    /**
     * .
     *
     * @return
     */
    public String toString(  ) {
        if( _vertex != this ) {
            return _vertex.toString(  ) + " (" + _priority + ")";
        }

        return super.toString(  ) + " (" + _priority + ")";
    }
}
