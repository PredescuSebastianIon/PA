# Bellman-Ford

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

## Implementare

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

Complexitate temporala: $O(M \times N)$
