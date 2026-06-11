# Kosaraju

> [!NOTE]
> Definim un arbore de afundare (sink tree) al nodului T:
> Arbore in care exista un drum din orice nod in T, dar T nu duce intr-un niciun nod

Kosaraju este un algoritm care gaseste toate componenetele tare-conexe intr-un 
graf orientat.

O varianta directa si neoptima a rezolvarii acestei probleme, este 
brute-force-ul, prin care daca isPath(x, y) && isPath(y, x) atunci x si y se 
afla in aceeasi componenta tare conexa.

Varianta imbunatatita este algoritmul `Kosaraju`: definim timpul de terminare a 
unui nod intr-o parcurgere DFS in momentul in care am terminat de vizitat toti 
vecinii sai (cand ne intoarcem de pe recursivitate). Putem mentine intr-o stiva 
ordinea.

De exemplu, daca avem graful $1$ &rarr; $2$ &rarr; $3$ &rarr; 4 atunci ordinea 
de pe stiva va fi ${4, 3, 2, 1}$.

La ce ne ajuta aceasta *stiva*? Daca am porni de la nodul $4$ catre nodul $1$, 
am avea automat componentele tare-conexe, doar ca in practica este imposibil 
sa gasim nodurile optime din care sa pornim pentru a gasi rezultatul. Asadar, 
pentru a compensa, creem graful transpus (intoarcem muchiile) si parcurgem in 
ordinea inversa de pe stiva.

**De ce aceasta parcurgere ne da componentele?** Daca un nod X face parte din 
SCC1 si un nod Y face parte din SCC2 si exista muchie de la X la Y, atunci toate 
nodurile din componenta SCC1 isi vor termina parcurgerea dupa Y (vor aparea in 
stiva dupa Y). Nodurile cu finish time mare in graful original devin puncte de 
start in graful transpus. In graful transpus, muchiile intre SCC-uri sunt 
inversate, deci un DFS pornit dintr-un nod cu finish time mare nu poate 'scapa' 
in alt SCC — ramane captiv in propriul SCC. Deci, la fiecare DFS complet pe 
graful transpus (pornind din varful stivei), obtinem exact un SCC.

## Implementare

```java
static void dfs_stack(int curr, List<List<Integer>> graph, Deque<Integer> stack, boolean[] marked) {
    marked[curr] = true;

    for (Integer neigh : graph.get(curr)) {
        if (marked[neigh])
            continue;

        dfs_stack(neigh, graph, stack, marked);
    }

    stack.addLast(curr);
}

static void dfs_transposed(int curr, List<List<Integer>> graph, List<Integer> SCC, boolean[] marked) {
    marked[curr] = true;
    SCC.add(curr);

    for (Integer neigh : graph.get(curr)) {
        if (marked[neigh])
            continue;

        dfs_transposed(neigh, graph, SCC, marked);
    }
}

static List<List<Integer>> kosaraju(int n, List<List<Integer>> graph) {
    // step 1 -- Create that stack using a classic DFS
    boolean[] marked = new boolean[n];
    Deque<Integer> stack = new ArrayDeque<>(n);
    
    for (int i = 0; i < n; i++) {
        if (marked[i] == true)
            continue;

        dfs_stack(i, graph, stack, marked);
    }

    // step 2 -- create the transpose graph
    List<List<Integer>> transposedGraph = new ArrayList<>(n);
    
    for (int i = 0; i < n; i++)
        transposedGraph.add(new ArrayList<>());

    for (int i = 0; i < n; i++) {
        for (Integer neigh : graph.get(i))
            transposedGraph.get(neigh).add(i);
    }

    // step 3 -- now dfs over the transposed graph in the stack order
    Arrays.fill(marked, false);

    List<List<Integer>> SCC = new ArrayList<>();

    while (!stack.isEmpty()) {
        int currNode = stack.pollLast();

        if (marked[currNode])
            continue;

        SCC.add(new ArrayList<>());
        dfs_transposed(currNode, transposedGraph, SCC.getLast(), marked);
    }

    return SCC;
}
```
