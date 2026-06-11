# Roy-Floyd (Floyd-Warshall)

Rezolva problema `cel mai scurt drum` intr-un graf dens. Acesta gaseste toate 
perechile de drumuri intre oricare 2 noduri din graf.

Ideea algoritmului: <br>
$dist[x][y]$ = drumul minim de la $x$ la $y$ <br>
$dist[x][y] = min(dist[x][y], dist[x][k] + dist[k][y])$<br>
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

## Implementare

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
intermediar din multimea $\{0, 1, \dots, k\}$
3. Stiu ca drumul va fi minim pentru ca de asemenea $dist[x][k]$ si $dist[k][y]$ 
sunt drumurile minime ce au un nod intemediar in multimea ${0, 1, \dots, k - 1}$
