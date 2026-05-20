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

## Sortare Topologica (TopoSort)

## Tarjan SCC (strongly connected componenets)

## Tarjan CV (cut vertex)

## Tarjan CE (critical edges)

## Tarjan BCC (biconex...)

## Kosaraju

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

