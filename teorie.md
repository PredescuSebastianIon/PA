# Teorie PA

**Autor**: Predescu Sebastian-Ion
**Grupa**: 322CC

\tableofcontents

# Programare dinamica

# Backtracking

# Grafuri

## BFS - Breadth-First Search

## DFS - Depth-First Search

## Componente Conexe, Tare-Conexe, Biconexe

Componenetele tare-conexe sunt sunt componenete intr-un graf orientat in care 
din orice nod se poate ajunge in orice alt nod si vice-versa.

## Sortare Topologica (TopoSort)

## Tarjan SCC (strongly connected components)



## Tarjan CV (cut vertex)

## Tarjan CE (critical edges)

## Tarjan BCC (biconex...)

## Kosaraju

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

Implementare
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

## Dijkstra

## Bellman-Ford

Rezolva problema `cel mai scurt drum` de la un nod src la orice alt nod din 
graf, chiar si cu muchii de cost negativ. Intoarce $-1$ in cazul ciclurilor 
negative.

Ne putem da seama ca avem un ciclu negativ, daca suma muchiilor unui ciclu este 
negativa. In acel moment, cu fiecare parcurgere facuta, costul va deveni din ce 
in ce mai mic.

Ceea ce face diferit de Dijkstra este sa reviziteze nodurile care au fost deja 
marcate ca fiind vizitate, deoarece poate exista o ruta mai lunga ca numar de 
noduri, dar mai scurt dat fiind existenta muchiilor negative.

**Principiul relaxarii muchiilor**: Daca gasim o ruta mai scurta catre un nod 
$x$ prin intermediul altui $(u, w)$, unde $w$ este costul muchiei, atunci 
relaxez distanta pana la $x$ cu $dist[x] = dist[u] + w$.

In urma a $N - 1$ relaxari, vom afla distanta minima de la src la toate nodurile 
pentru ca orice distanta poate avea maxim poate trece prin maxim toate nodurile, 
insemnand $N - 1$ muchii. Altfel, deja avem cicluri.

Implementare:
```java
class Muchie {
    int src, dst, cost;
}

static int[] bellmanFord(Muchie[] edges, int n, int src) {
    int[] dist = new int[n];
    
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;

    // In mod normal ar trebui sa face n - 1 relaxari
    // Putem, totusi, sa adaugam inca o relaxare pentru a prinde cazul in care 
    // avem un ciclu negativ
    // Practic, ce face o relaxarea numarul T, este sa ne asigure ca dist[node] 
    // este distanta minima de la src la node ce trece prin maxim T muchii
    for (int relaxation = 1; relaxation <= n; relaxation++) {
        for (Muchie edge : edges) {
            int x = edge.src, y = edge.dst, c = edge.cost;
            
            // Verificam daca putem actualiza dist[y] prin (x, c)
            if (dist[x] != Integer.MAX_VALUE && dist[x] + c < dist[y]) {
                dist[y] = dist[x] + c;

                // Verificam daca este un ciclu negativ
                // Daca nu avem niciun astfel de ciclu, atunci la relaxarea n
                // nu ar trebui sa mai avem nimic de actualizat
                if (relaxation == n)
                    return new int[]{-1};
            }
        }
    }

    return dist;
}
```

## Roy-Floyd (Floyd-Warshall)

Rezolva problema `cel mai scurt drum` intr-un graf dens. Acesta gaseste toate 
perechile de drumuri intre oricare 2 noduri din graf.

Ideea algoritmului: <br>
dist[x][y] = drumul minim de la x la y <br>
dist[x][y] = min(dist[x][y], dist[x][k] + dist[k][y]) <br>
Practic, folosim nodul k ca intermediar

Initializam 
$$
dist[x][y] = 
\begin{cases}
cost & \text{cand exista o muchie intre x si y} \\
INF & \text{daca nu exista muchie} \\
0 & x = y
\end{cases}
$$

```java
static void floydWarshall(int[][] dist) {
    int n = dist.length;
    int INF = Integer.MAX_VALUE;

    // alegem nodul K ca fiind intermediar
    for (int k = 0; k < n; k++)
        // alegem sursa
        for (int x = 0; x < n; x++)
            // alegem destinatia
            for (int y = 0; y < n; y++) {
                // distanta minima de la x la y
                if (dist[x][k] == INF || dist[k][y] == INF)
                    continue;

                dist[x][y] = Math.min(dist[x][y], dist[x][k] + dist[k][y]);
            }
}
```

Explicatia algoritmului:
1. Aleg nodul k ca fiind intermediar
2. Calculez drumul minim de la orice x la orice y, care trece printr-un nod 
intermediar din multimea ${0, 1, \dots, k}$
3. Stiu ca drumul va fi minim pentru ca de asemenea dist[x][k] si dist[k][y] 
sunt drumurile minime ce au un nod intemediar in multimea ${0, 1, \dots, k - 1}$


## Johnson

## Kruskal

## Prim

## Karger, Klein & Tarjan -- Algoritmi randomizati pentru APM

## Bernard Chazelle -- Algoritm determinist cu o complexitate liniara

