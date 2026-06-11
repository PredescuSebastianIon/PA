# Tarjan CV (Cut Vertex)

Un **cut vertex** (nod critic) este un nod a carui eliminare deconecteaza graful.

## Implementare

```java
static void tarjanCV(
    int curr, 
    List<List<Integer>> graph, 
    int parent,
    int[] time, 
    int[] low, 
    int[] currTime, 
    boolean[] isCutVertex
) {
    time[curr] = low[curr] = currTime[0]++;
    int children = 0;

    for (int neigh : graph.get(curr)) {
        if (time[neigh] == -1) {
            children++;
            tarjanCV(neigh, graph, curr, time, low, currTime, isCutVertex);
            low[curr] = Integer.min(low[curr], low[neigh]);

            // Conditia pentru cut vertex
            if (parent != -1 && low[neigh] >= time[curr])
                isCutVertex[curr] = true;
        } else if (neigh != parent) {
            // Back edge
            low[curr] = Integer.min(low[curr], time[neigh]);
        }
    }

    // conditia pentru root
    if (parent == -1 && children > 1) {
        isCutVertex[curr] = true;
    }
}
```
