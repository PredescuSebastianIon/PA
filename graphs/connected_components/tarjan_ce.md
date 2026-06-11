# Tarjan CE (Critical Edges / Bridges)

O **muchie critica** (bridge) este o muchie a carei eliminare deconecteaza graful.

Conditia: `low[neigh] > time[curr]` (strict mai mare, spre deosebire de cut vertex).

## Implementare

```java
record Muchie(int from, int to) {}

static void tarjanCE(
    int curr,
    List<List<Integer>> graph,
    int parent,
    int[] time, int[] low, int[] currTime, 
    List<Muchie> bridges
) {
    time[curr] = low[curr] = currTime[0]++;

    for (int neigh : graph.get(curr)) {
        if (time[neigh] == -1) {
            tarjanCE(neigh, graph, curr, time, low, currTime, bridges);

            low[curr] = Integer.min(low[curr], low[neigh]);

            // Conditie pentru bridge - strict mai mare
            if (low[neigh] > time[curr])
                bridges.add(new Muchie(curr, neigh));
        } else if (neigh != parent) {
            low[curr] = Integer.min(low[curr], time[neigh]);
        }
    }
}
```
