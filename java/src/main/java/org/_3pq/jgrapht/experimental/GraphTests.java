package org._3pq.jgrapht.experimental;

import java.util.*;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.GraphHelper;

public final class GraphTests {

    private GraphTests() {}

    public static boolean isEmpty(Graph g) {
        return g.edgeSet().isEmpty();
    }

    public static boolean isComplete(Graph g) {
        int n = g.vertexSet().size();
        return g.edgeSet().size() == n * (n-1) / 2;
    }

    public static boolean isConnected(Graph g) {

        int numVertices = g.vertexSet().size();
        int numEdges = g.edgeSet().size();
        
        if (numEdges < numVertices - 1) return false;
        if (numVertices < 2 ||
            numEdges > (numVertices-1)*(numVertices-2)/2 ) return true;
        
        Set known = new HashSet();
        LinkedList queue = new LinkedList();
        Object v = g.vertexSet().iterator().next();
        
        queue.add(v);    // start with node 1
        known.add(v);
        
        while (!queue.isEmpty()) {
            v = queue.removeFirst();
            for (Iterator it = GraphHelper.neighborListOf(g, v).iterator(); it.hasNext();) {
                v = it.next();
                if (!known.contains(v)) {
                    known.add(v);
                    queue.add(v);
                }
            }
        }
        return known.size() == numVertices;
    }  

    public static boolean isTree(Graph g) {
        return isConnected(g) && g.edgeSet().size() == g.vertexSet().size() - 1;
    }

    public static boolean isBipartite(Graph g) {
        if (4 * g.edgeSet().size() > g.vertexSet().size() * g.vertexSet().size()) return false;
        if (isEmpty(g)) return true;

        Set unknown = new HashSet(g.vertexSet());
        LinkedList queue = new LinkedList();
        Object v = unknown.iterator().next();
        Set odd = new HashSet();
        
        queue.add(v);
        
        while (!unknown.isEmpty()) {
            if (queue.isEmpty()) {
                queue.add(unknown.iterator().next());
            }

            v = queue.removeFirst();
            unknown.remove(v);

            for (Iterator it = GraphHelper.neighborListOf(g, v).iterator(); it.hasNext();) {
                Object n = it.next();
                if (unknown.contains(n)) {
                    queue.add(n);
                    if (!odd.contains(v)) {
                        odd.add(n);
                    }
                }
                else if (!(odd.contains(v) ^ odd.contains(n))) {
                    return false;
                }
            }
        }
        return true;
    }

}
