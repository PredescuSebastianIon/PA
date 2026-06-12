# Dijkstra

Rezolva problema `cel mai scurt drum` de la un nod src la orice alt nod din 
graf. **Acest algoritm nu merge pentru muchii negative**.

Acesta este relativ simplu: mentin continuu o coada de prioritati (pentru insert 
si pop rapid) cu distanta de la nodul src la celelalte noduri (dam push la 
$\{dist[x], x\}$), sortam dupa distanta si dupa vizitam vecinii nodului cu 
distanta minima. Daca putem ajunge la un vecin de a lui $x$ mai rapid, atunci 
modificam distanta si adaugam in coada.

## Implementare

```java
record Elem(int dist, int node) {}

record Muchie(int to, int cost) {}

static int[] dijkstra(int n, int src, List<List<Muchie>> graph) {
    int[] dist = new int[n];

    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;

    PriorityQueue<Elem> queue = new PriorityQueue<>((elem1, elem2) -> elem1.dist() - elem2.dist());
    queue.add(new Elem(0, src));

    while (!queue.isEmpty()) {
        Elem curr = queue.poll();

        // optimizare
        // daca deja avem o distanta mai buna decat cea din coada, sarim peste
        if (curr.dist() > dist[curr.node()])
            continue;

        for (Muchie neigh : graph.get(curr.node())) {
            if (dist[neigh.to()] > curr.dist() + neigh.cost()) {
                dist[neigh.to()] = curr.dist() + neigh.cost();
                queue.add(new Elem(dist[neigh.to()], neigh.to()));
            }
        }
    }

    return dist;
}
```

Complexitatea temporala depinde de cum este implementat algoritmul:
- In versiunea de mai sus (cu PriorityQueue), avem $O(M \times log N)$
- In versiunea *neoptimizata*, in care am folosi un vector normal, in loc de 
coada de prioritati, la fiecare iteratie ar trebui sa parcurgem tot vectorul 
pentru a gasi nodul cu distanta minima (cautare liniara). Deci, complexitatea 
temporala va fi $O(N ^ 2)$


> [!NOTE]
> TODO: 
> implementari cu vector, heap binar, heap fibonnacci
> Demonstrare corectitudine algoritm
> De facut o paralela cand e mai bine fiecare implementare de folosit
> De scris detalii despre heap ul fibonacci
> Caz special Dijstra -- graf cu costuri mici -- Algoritmul lui Dial
> Aplicatie:
> Problema: găsește ciclul de cost minim care trece prin nodul v
> Un **ciclu** care trece prin v înseamnă: pleci din v, mergi prin niște noduri, 
> și te întorci înapoi la v.
> v → ... → u → v
> Costul ciclului = d[u] + w(u, v), adică distanța de la v până la u, plus arcul final înapoi la v.

