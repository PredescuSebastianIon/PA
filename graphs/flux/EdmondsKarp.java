import java.util.*;

// Main class
public class EdmondsKarp {

    record TraceBack(int from, Edge originalEdge) {}

    static boolean bfs(
	    int src,
	    int sink,
        List<List<Edge>> graph, 
        int n,
        TraceBack[] path) 
    {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(src);

        Arrays.fill(path, null);
        path[src] = new TraceBack(src, null);

        while (!queue.isEmpty()) {
            int curr = queue.poll();

            for (Edge e : graph.get(curr)) {
                if (e.cost > 0 && path[e.to] == null) {
                    path[e.to] = new TraceBack(curr, e);
                    queue.add(e.to);

                    if (e.to == sink)
                        return true;
                }
            }
        }

        return false;
    }

    static int edmondsKarp(
	    int source, 
	    int sink,
	    List<List<Edge>> graph, 
	    int n) 
	{
        int totalFlow = 0;
        TraceBack[] path = new TraceBack[n];

        while (bfs(source, sink, graph, n, path)) {

            // 1. Găsim bottleneck-ul
            int pathFlow = Integer.MAX_VALUE;
            for (int curr = sink; curr != source; curr = path[curr].from()) {
                pathFlow = Math.min(pathFlow, path[curr].originalEdge().cost);
            }

            // 2. Actualizăm graful rezidual
            for (int curr = sink; curr != source; curr = path[curr].from()) {
                path[curr].originalEdge().cost -= pathFlow;
                path[curr].originalEdge().revEdge.cost += pathFlow;
            }

            totalFlow += pathFlow;
        }

        return totalFlow;
    }

    public static void main(String[] args) {
        List<List<Edge>> graph = CreateGraph.getGraph();
        int n = graph.size();
        int source = 0, sink = n - 1;

        int totalFlow = edmondsKarp(source, sink, graph, n);
        System.out.printf("Max flow de la %d la %d este %d\n", source, sink, totalFlow);
        // Output: Max flow de la 0 la 5 este 23
    }
}

// Helpers
class Edge {
    int to;
    int cost;
    Edge revEdge;

    public Edge(int to, int cost) {
        this.to = to;
        this.cost = cost;
    }
}

class CreateGraph {
    private static void addEdge(List<List<Edge>> graph, int u, int v, int cap) {
        Edge forward  = new Edge(v, cap);
        Edge backward = new Edge(u, 0);

        forward.revEdge = backward;
        backward.revEdge = forward;

        graph.get(u).add(forward);
        graph.get(v).add(backward);
    }

    public static List<List<Edge>> getGraph() {
        int n = 6;
        List<List<Edge>> graph = new ArrayList<>();

        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        addEdge(graph, 0, 1, 16);
        addEdge(graph, 0, 2, 13);
        addEdge(graph, 1, 2, 4);
        addEdge(graph, 2, 1, 10);
        addEdge(graph, 1, 3, 12);
        addEdge(graph, 3, 2, 2);
        addEdge(graph, 2, 4, 14);
        addEdge(graph, 4, 3, 7);
        addEdge(graph, 3, 5, 20);
        addEdge(graph, 4, 5, 4);

        return graph;
    }
}
