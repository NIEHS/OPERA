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
package org._3pq.jgrapht.experimental;

import java.io.*;

import java.util.*;

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.graph.*;
import org._3pq.jgrapht.traverse.*;

/**
 * .
 * @author  Michael Behrisch
 * @version 1.0
 */
public class GraphReader {
    private final BufferedReader _in;

    /**
     * Creates a new GraphReader object.
     *
     * @param file  
     *
     * @throws IOException  
     */
    public GraphReader( String file ) throws IOException {
        _in = new BufferedReader( new FileReader( file ) );
    }

    /**
     * .
     *
     * @param args  
     *
     * @throws Exception  
     */
    public static void main( String[] args ) throws Exception {
        Graph g = new SimpleWeightedGraph(  );
        new GraphReader( args[ 0 ] ).readGraph( g );
        System.out.println( "graph read" );
        
        {long time = System.currentTimeMillis();
        ShortestPathIterator spi = new ShortestPathIterator(g, g.vertexSet(  ).iterator(  ).next(), org._3pq.jgrapht.experimental.heap.FibonacciHeap.getFactory());
        while (spi.hasNext(  )) {
            Object v = spi.next(  );
//             System.out.println( v + " " + spi.getSpanningTreeEdge(v) + " " + spi.getPrio(v));
        }
        System.out.println( System.currentTimeMillis() - time );
        time = System.currentTimeMillis();
        ClosestFirstIterator cfi = new ClosestFirstIterator(g, g.vertexSet(  ).iterator(  ).next());
        while (cfi.hasNext(  )) {
            Object v = cfi.next(  );
//             System.out.println( v + " " + cfi.getSpanningTreeEdge(v));
        }
        System.out.println( System.currentTimeMillis() - time );}
        {long time = System.currentTimeMillis();
        ShortestPathIterator spi = new ShortestPathIterator(g, g.vertexSet(  ).iterator(  ).next(), org._3pq.jgrapht.experimental.heap.FibonacciHeap.getFactory());
        while (spi.hasNext(  )) {
            Object v = spi.next(  );
//             System.out.println( v + " " + spi.getSpanningTreeEdge(v) + " " + spi.getPrio(v));
        }
        System.out.println( System.currentTimeMillis() - time );
        time = System.currentTimeMillis();
        ClosestFirstIterator cfi = new ClosestFirstIterator(g, g.vertexSet(  ).iterator(  ).next());
        while (cfi.hasNext(  )) {
            Object v = cfi.next(  );
//             System.out.println( v + " " + cfi.getSpanningTreeEdge(v));
        }
        System.out.println( System.currentTimeMillis() - time );}
        long time = System.currentTimeMillis();
        ShortestPathIterator spi = new ShortestPathIterator(g, g.vertexSet(  ).iterator(  ).next(), org._3pq.jgrapht.experimental.heap.FibonacciHeap.getFactory());
        while (spi.hasNext(  )) {
            Object v = spi.next(  );
//             System.out.println( v + " " + spi.getSpanningTreeEdge(v) + " " + spi.getPrio(v));
        }
        System.out.println( System.currentTimeMillis() - time );
        time = System.currentTimeMillis();
        ClosestFirstIterator cfi = new ClosestFirstIterator(g, g.vertexSet(  ).iterator(  ).next());
        while (cfi.hasNext(  )) {
            Object v = cfi.next(  );
//             System.out.println( v + " " + cfi.getSpanningTreeEdge(v));
        }
        System.out.println( System.currentTimeMillis() - time );
        time = System.currentTimeMillis();

    }


    /**
     * .
     *
     * @param g  
     *
     * @throws IOException  
     */
    public void readGraph( Graph g ) throws IOException {
        while( ready(  ) ) {
            String[] cols = skipComments(  );

            if( cols.length > 0 ) {
                if( cols[ 0 ].equals( "e" ) ) {
                    int x = Integer.parseInt( cols[ 1 ] ) - 1;
                    int y = Integer.parseInt( cols[ 2 ] ) - 1;

                    if( x != y ) {
                        if (cols.length > 3) {
                            GraphHelper.addEdgeWithVertices( g, new Integer( x ),
                                new Integer( y ), Double.parseDouble(cols[ 3 ]) );
                        } else {
                            GraphHelper.addEdgeWithVertices( g, new Integer( x ),
                                new Integer( y ) );
                        }
                    }
                }
            }
        }
    }


    /**
     * .
     *
     * @return  
     *
     * @throws IOException  
     */
    public boolean ready(  ) throws IOException {
        return _in.ready(  );
    }


    private String[] skipComments(  ) throws IOException {
        String[] cols = split( _in.readLine(  ) );

        while( cols.length == 0 || cols[ 0 ].equals( "c" ) ) {
            cols = split( _in.readLine(  ) );
        }

        return cols;
    }


    private String[] split( String src ) {
        final List            l   = new ArrayList(  );
        final StringTokenizer tok = new StringTokenizer( src );

        while( tok.hasMoreTokens(  ) ) {
            l.add( tok.nextToken(  ) );
        }

        return (String[]) l.toArray( new String[ l.size(  ) ] );
    }
}
