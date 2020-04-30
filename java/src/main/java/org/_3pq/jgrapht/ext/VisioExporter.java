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
/* ------------------
 * VisioExporter.java
 * ------------------
 * (C) Copyright 2003, by Avner Linder and Contributors.
 *
 * Original Author:  Avner Linder
 * Contributor(s):   Barak Naveh
 *
 * $Id$
 *
 * Changes
 * -------
 * 27-May-2004 : Initial Version (AL);
 *
 */
package org._3pq.jgrapht.ext;

import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Iterator;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;

/**
 * Exports a graph to a csv format that can be imported into MS Visio.
 * 
 * <p>
 * <b>Tip:</b> By default, the exported graph doesn't show link directions. To
 * show link directions:<br>
 * 
 * <ol>
 * <li>
 * Select All (Ctrl-A)
 * </li>
 * <li>
 * Right Click the selected items
 * </li>
 * <li>
 * Format/Line...
 * </li>
 * <li>
 * Line ends: End: (choose an arrow)
 * </li>
 * </ol>
 * </p>
 *
 * @author Avner Linder
 */
public class VisioExporter {
    private static final VertexNameProvider DEFAULT_VERTEX_NAME_PROVIDER =
        new VertexNameProvider(  ) {
            public String getVertexName( Object vertex ) {
                return vertex.toString(  );
            }
        };

    private VertexNameProvider m_vertexNameProvider;

    /**
     * Creates a new VisioExporter object with the specified naming policy.
     *
     * @param vertexNameProvider the vertex name provider to be used for naming
     *        the Visio shapes.
     */
    public VisioExporter( VertexNameProvider vertexNameProvider ) {
        m_vertexNameProvider = vertexNameProvider;
    }


    /**
     * Creates a new VisioExporter object.
     */
    public VisioExporter(  ) {
        this( DEFAULT_VERTEX_NAME_PROVIDER );
    }

    /**
     * Exports the specified graph into a Visio csv file format.
     *
     * @param output the print stream to which the graph to be exported.
     * @param g the graph to be exported.
     */
    public void export( OutputStream output, Graph g ) {
        PrintStream out = new PrintStream( output );

        for( Iterator i = g.vertexSet(  ).iterator(  ); i.hasNext(  ); ) {
            exportVertex( out, i.next(  ) );
        }

        for( Iterator i = g.edgeSet(  ).iterator(  ); i.hasNext(  ); ) {
            exportEdge( out, (Edge) i.next(  ) );
        }

        out.flush(  );
    }


    private void exportEdge( PrintStream out, Edge edge ) {
        String sourceName =
            m_vertexNameProvider.getVertexName( edge.getSource(  ) );
        String targetName =
            m_vertexNameProvider.getVertexName( edge.getTarget(  ) );

        out.print( "Link," );

        // create unique ShapeId for link
        out.print( sourceName );
        out.print( "-->" );
        out.print( targetName );

        // MasterName and Text fields left blank
        out.print( ",,," );
        out.print( sourceName );
        out.print( "," );
        out.print( targetName );
        out.print( "\n" );
    }


    private void exportVertex( PrintStream out, Object vertex ) {
        String name = m_vertexNameProvider.getVertexName( vertex );

        out.print( "Shape," );
        out.print( name );
        out.print( ",," ); // MasterName field left empty
        out.print( name );
        out.print( "\n" );
    }

    /**
     * Assigns a display name for each of the graph vertices.
     */
    public interface VertexNameProvider {
        /**
         * Returns the shape name for the vertex as to be appeared in the Visio
         * diagram.
         *
         * @param vertex the vertex
         *
         * @return vertex display name for Visio shape.
         */
        public String getVertexName( Object vertex );
    }
}
