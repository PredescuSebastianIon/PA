/*
 * Author: Predescu Sebastian-Ion
 * Link to problem: https://leetcode.com/problems/is-graph-bipartite/
*/

public class Bipartite {
    enum label {
        UNVISITED, A, B
    }

    private boolean bfs(int node, int[][] graph, label[] status) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            int curr = queue.poll();

            for (int neigh : graph[curr]) {
                if (status[neigh] == status[curr])
                    return false;
                
                if (status[neigh] == label.UNVISITED) {
                    status[neigh] = (status[curr] == label.A ? label.B : label.A);
                    queue.add(neigh);
                }
            }
        }

        return true;
    }

    public boolean isBipartite(int[][] graph) {
        // number of nodes from graph
        int n = graph.length;

        label[] status = new label[n];
        Arrays.fill(status, label.UNVISITED);

        for (int i = 0; i < n; i++) {
            if (status[i] == label.UNVISITED) {
                status[i] = label.A;
                if (bfs(i, graph, status) == false)
                    return false;
            }
        }

        return true;
    }
}
