# Tarjan BCC (Biconnected Components)

O componenta biconexa este o componenta care:
* este conexa
* Nu are niciun cut vertex: oricum elimini un nod, componenta ramane conexa

## Implementare

```java
static void tarjanBCC(
    int curr, 
    List<List<Integer>> graph, 
    Deque<Integer> stack,
    int parent,
    int[] time, 
    int[] low, 
    int[] currTime, 
    List<List<Integer>> BCC
) {
    time[curr] = low[curr] = currTime[0]++;
    stack.addLast(curr);

    for (Integer neigh : graph.get(curr)) {
        if (time[neigh] == -1) {
            // Case 1 - we did not visited this, so it's a node in curr subtree
            tarjanBCC(neigh, graph, stack, curr, time, low, currTime, BCC);
            low[curr] = Integer.min(low[curr], low[neigh]);

            if (low[neigh] >= time[curr]) {
                // found a BCC
                List<Integer> newBCC = new ArrayList<>();
                newBCC.add(curr);
                
                while (!stack.isEmpty()) {
                    int node = stack.pollLast();

                    newBCC.add(node);
                    if (node == neigh)
                        break;
                }

                BCC.add(newBCC);
            }
        } else if (neigh != parent) {
            // Case 2 - we found an ancestor (back edge)
            low[curr] = Integer.min(low[curr], time[neigh]);
        }
    }
}
```
