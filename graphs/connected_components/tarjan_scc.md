# Tarjan SCC (Strongly Connected Components)

> [!NOTE]
> Algoritmul lui Tarjan este foarte versatil si poate fi adaptat
> Acesta poate gasi: componentele tare conexe, nodurile critice, muchiile critice etc.

Ideea de baza a algoritmului:
- Un DFS produce un arbore din graful initial
- SCC-urile sunt subarbori ai arborelui produs de DFS
- Mai avem doar de gasit **radacina** din fiecare SCC

Pentru a gasi radacina, trebuie sa definim:
* `time[node]` = timpul la care am ajuns la nodul *node* prin dfs 
(al catelea nod in parcurgere)
* `low[node]` = cel mai mic `time` accesibil din subarborele lui *node* in 
arborele DFS, folosind oricate muchii din arbore si cel mult o muchie de intoarcere

Initial, `low[node] = time[node]`, dar, dupa se actualizeaza in timp ce parcurgem:
* low[node] = min(low[node], low[fiu])
* low[node] = min(low[node], time[stramos])

Totusi, pentru a functiona algoritmul, acesta necesita si o stiva, care reprezinta 
toate nodurile care sunt in curs de vizitare si nu apartin unui SCC inca. Asadar, 
in momentul in care gasim un SCC, scoatem toate nodurile de pe stiva. Acest lucru 
ne ajuta sa limitam putin low-ul, comparandu-l cu timpul unui stramos **NUMAI** 
daca stramosul se afla pe stiva. Daca nu se afla, atunci apartine deja unui SCC 
si nu mai prezinta interes.

Nodul *node* este radacina daca `low[node] = time[node]` dupa ce s-a terminat 
parcurgerea. Aceasta inseamna ca nu exista niciun nod care sa poata *urca* mai 
sus in arbore, sunt toate captive in SCC-ul curent.

## Implementare

```java
static void tarjanSCC(
    int curr, 
    List<List<Integer>> graph, 
    Deque<Integer> stack,  
    boolean[] onStack, 
    int[] time, 
    int[] low, 
    int[] currTime, 
    List<List<Integer>> SCC
) {
    time[curr] = currTime[0]++;
    low[curr] = time[curr];
    stack.addLast(curr);
    onStack[curr] = true;

    for (Integer neigh : graph.get(curr)) {
        if (time[neigh] == -1) {
            // Case 1 - we did not visited this, so it's a node in curr subtree
            tarjanSCC(neigh, graph, stack, onStack, time, low, currTime, SCC);
            low[curr] = Integer.min(low[curr], low[neigh]);
        } else if (onStack[neigh]) {
            // Case 2 - we found an ancestor (back edge)
            low[curr] = Integer.min(low[curr], time[neigh]);
        }
    }


    // time to check if this is the head of a SCC
    if (low[curr] != time[curr])
        return;

    // found the SCC
    SCC.add(new ArrayList<>());

    while (!stack.isEmpty()) {
        int node = stack.pollLast();

        onStack[node] = false;
        SCC.getLast().add(node);
        if (node == curr) break;
    }
}

public static void main(String[] args) {
    // Read the graph or hard code it

    // Initialise the SCC, time (with -1), low, onStack, stack

    // start the tarjan SCC alg just like a normal DFS

    // Congrats! You now have all strongly connected components

    // Java don't support sending primitives as reference
    // Also Integer type is immutable
    // So a great trick for getting around this limitation is 
    // either to have a static field in the class
    // or to make an array int[] currTime = {0} with a size of 1
    // and use it as a single int
}
```
