# Ford-Fulkerson

> [!NOTE]
> Ford-Fulkerson nu este un algoritm, ci mai degraba o metoda
> Acest lucru se datoreaza pentru ca nu specifica cum gasesti drumul de augumentare

Ideea de baza
- Cat timp exista un drum de la S -> D cu capacitate reziduala > 0
- Gaseste-l (DFS / BFS / etc)
- Fluxul pe care-l putem trimite pe el e costul minim al muchiilor
- Actualizam path (scadem din fiecare muchie fluxul pe acest path)
Complexitate temporala: $O(m \times f_{max})$

```java
import java.util.*;

public class EdmondsKarp {

    static class Edge {
        int to;
        int cost;
        Edge revEdge;

        public Edge(int to, int cost) {
            this.to   = to;
            this.cost = cost;
        }
    }

    record TraceBack(int from, Edge originalEdge) {}

    static boolean dfs(
	    int curr,
	    int sink,
        List<List<Edge>> graph, 
        int n,
        TraceBack[] path) 
    {
        if (curr == sink)
	        return true;
	    
	    for (Edge e : graph.get(curr)) {
		    if (path[e.to] != null || e.cost == 0)
			    continue;
			
			path[e.to] = new TraceBack(curr, e);
			if (dfs(e.to, sink, graph, path))
				return true;
	    }

        return false;
    }

    static int fordFulkerson(
	    int source, 
	    int sink,
	    List<List<Edge>> graph, 
	    int n) 
	{
        int totalFlow = 0;
        TraceBack[] path = new TraceBack[n];

        while (true) {
	        // 1. initializam vectorul de traceback si cautam un drum cu dfs
	        Arrays.fill(path, null);
	        path[source] = new TraceBack(source, null);
	        
	        boolean foundPath = dfs(source, sink, graph, n, path);
	        
	        if (!foundPath)
		        break;

            // 2. Găsim bottleneck-ul
            int pathFlow = Integer.MAX_VALUE;
            for (int curr = sink; curr != source; curr = path[curr].from()) {
                pathFlow = Math.min(pathFlow, path[curr].originalEdge().cost);
            }

            // 3. Actualizăm graful rezidual
            for (int curr = sink; curr != source; curr = path[curr].from()) {
                path[curr].originalEdge().cost -= pathFlow;
                path[curr].originalEdge().revEdge.cost += pathFlow;
            }

            totalFlow += pathFlow;
        }

        return totalFlow;
    }

    static void addEdge(List<List<Edge>> graph, int u, int v, int cap) {
        Edge forward  = new Edge(v, cap);
        Edge backward = new Edge(u, 0);

        forward.revEdge = backward;
        backward.revEdge = forward;

        graph.get(u).add(forward);
        graph.get(v).add(backward);
    }

    public static void main(String[] args) {
        int n = 6;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        addEdge(graph, 0, 1, 16);
        addEdge(graph, 0, 2, 13);
        addEdge(graph, 1, 2, 4);
        addEdge(graph, 2, 1, 10);
        addEdge(graph, 1, 3, 12);
        addEdge(graph, 3, 2, 2);
        addEdge(graph, 2, 4, 14);
        addEdge(graph, 4, 3, 7);
        addEdge(graph, 3, 5, 20);
        addEdge(graph, 4, 5, 4);

        int totalFlow = fordFulkerson(0, n - 1, graph, n);
        System.out.printf("Max flow de la 0 la %d este %d%n", n - 1, totalFlow);
        // Output: Max flow de la 0 la 5 este 23
    }
}
```

# Edmonds-Karp

Edmons-Karp este o implementare concreta a lui Ford-Fulkerson. Diferenta de baza 
este ca acesta inlocuieste gasirea drumului: foloseste BFS. Matematic, acest lucru 
garanteaza alegerea celui mai scurt drum (ca numar de muchii), limitand numarul de augmentari.

Complexitate temporala: $O(n \times m^2)$ <br>
Se observa ca obtinem o complexitate independenta de valoarea fluxului, fiind astfel mult mai
sigura.

```java
import java.util.*;

public class EdmondsKarp {

    static class Edge {
        int to;
        int cost;
        Edge revEdge;

        public Edge(int to, int cost) {
            this.to   = to;
            this.cost = cost;
        }
    }

    record TraceBack(int from, Edge originalEdge) {}

    static boolean bfs(
	    int src,
	    int sink,
        List<List<Edge>> graph, 
        int n,
        TraceBack[] path) 
    {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(src);

        Arrays.fill(path, null);
        path[src] = new TraceBack(src, null);

        while (!queue.isEmpty()) {
            int curr = queue.poll();

            for (Edge e : graph.get(curr)) {
                if (e.cost > 0 && path[e.to] == null) {
                    path[e.to] = new TraceBack(curr, e);
                    queue.add(e.to);

                    if (e.to == sink)
                        return true;
                }
            }
        }

        return false;
    }

    static int edmondsKarp(
	    int source, 
	    int sink,
	    List<List<Edge>> graph, 
	    int n) 
	{
        int totalFlow = 0;
        TraceBack[] path = new TraceBack[n];

        while (bfs(source, sink, graph, n, path)) {

            // 1. Găsim bottleneck-ul
            int pathFlow = Integer.MAX_VALUE;
            for (int curr = sink; curr != source; curr = path[curr].from()) {
                pathFlow = Math.min(pathFlow, path[curr].originalEdge().cost);
            }

            // 2. Actualizăm graful rezidual
            for (int curr = sink; curr != source; curr = path[curr].from()) {
                path[curr].originalEdge().cost -= pathFlow;
                path[curr].originalEdge().revEdge.cost += pathFlow;
            }

            totalFlow += pathFlow;
        }

        return totalFlow;
    }

    static void addEdge(List<List<Edge>> graph, int u, int v, int cap) {
        Edge forward  = new Edge(v, cap);
        Edge backward = new Edge(u, 0);

        forward.revEdge = backward;
        backward.revEdge = forward;

        graph.get(u).add(forward);
        graph.get(v).add(backward);
    }

    public static void main(String[] args) {
        int n = 6;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        addEdge(graph, 0, 1, 16);
        addEdge(graph, 0, 2, 13);
        addEdge(graph, 1, 2, 4);
        addEdge(graph, 2, 1, 10);
        addEdge(graph, 1, 3, 12);
        addEdge(graph, 3, 2, 2);
        addEdge(graph, 2, 4, 14);
        addEdge(graph, 4, 3, 7);
        addEdge(graph, 3, 5, 20);
        addEdge(graph, 4, 5, 4);

        int totalFlow = edmondsKarp(0, n - 1, graph, n);
        System.out.printf("Max flow de la 0 la %d este %d%n", n - 1, totalFlow);
        // Output: Max flow de la 0 la 5 este 23
    }
}
```

## Dinic

Algoritmul lui Dinic rezolva aceasta problema tot prin bfs ca si EdmonsKarp, dar complexitatea temporala va fi $O(N ^ 2 \times M)$

Observatie cheie la Dinic fata de EdmonsKarp:
> De ce sa facem cate un BFS pentru fiecare cale, cand putem folosi acelasi BFS pentru a trimite flux simultan pe toate caile de aceeasi lungime?

Astfel, mai intai construim BFS in care calculam pe ce nivel se afla fiecare nod. Apoi trebuie sa gasim toate caile care duc catre nivelul $level[sink]$. Cum facem asta? Printr-un DFS

```java
import java.util.*;

public class Dinic {

    static class Edge {
        int to;
        int cap;
        Edge revEdge;

        public Edge(int to, int cap) {
            this.to  = to;
            this.cap = cap;
        }
    }

    // -------------------------------------------------------------------------
    // BFS — construieste graful pe niveluri (level graph)
    // Returneaza true daca t e accesibil din s
    // -------------------------------------------------------------------------
    static boolean bfs(
            int src,
            int sink,
            List<List<Edge>> graph,
            int n,
            int[] level)
    {
        Arrays.fill(level, -1);
        level[src] = 0;

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(src);

        while (!queue.isEmpty()) {
            int curr = queue.poll();

            for (Edge e : graph.get(curr)) {
                // Viziteaza doar arce reziduale nevizitate
                if (e.cap > 0 && level[e.to] == -1) {
                    level[e.to] = level[curr] + 1;
                    queue.add(e.to);
                }
            }
        }

        // Daca sink a primit un nivel => e accesibil => mai exista cale
        return level[sink] != -1;
    }

    // -------------------------------------------------------------------------
    // DFS — trimite flux de-a lungul unei cai in level graph
    // ptr[] = pointer per nod: indicele primului arc nevizitat din lista sa
    //         (optimizarea Dead-end / advance pointer)
    // Returneaza cat flux a putut trimite (0 = nod mort)
    // -------------------------------------------------------------------------
    static int dfs(
            int curr,
            int sink,
            int pushed,                 // cat flux "transportam" pana aici
            List<List<Edge>> graph,
            int[] level,
            int[] ptr)
    {
        if (curr == sink)
            return pushed;             // am ajuns la destinatie, intoarce fluxul

        // Iteram de la ptr[curr] in continuare (nu de la 0!)
        for (; ptr[curr] < graph.get(curr).size(); ptr[curr]++) {
            Edge e = graph.get(curr).get(ptr[curr]);

            // Arcul e valid in level graph daca:
            //   1) are capacitate reziduala
            //   2) duce la nivelul urmator
            if (e.cap <= 0 || level[e.to] != level[curr] + 1)
                continue;

            int d = dfs(
                    e.to,
                    sink,
                    Math.min(pushed, e.cap),
                    graph, level, ptr);

            if (d > 0) {
                // Am gasit cale — actualizam graful rezidual
                e.cap          -= d;
                e.revEdge.cap  += d;
                return d;
            }

            // d == 0 => e.to e nod mort => ptr[curr] avanseaza automat (for-loop)
        }

        return 0; // nod mort — nu exista cale din curr spre sink
    }

    // -------------------------------------------------------------------------
    // Dinic principal
    // -------------------------------------------------------------------------
    static int dinic(
            int source,
            int sink,
            List<List<Edge>> graph,
            int n)
    {
        int totalFlow = 0;
        int[] level   = new int[n];  // nivelul fiecarui nod (BFS)
        int[] ptr     = new int[n];  // pointer per nod (DFS)

        // Fiecare iteratie a while = o FAZA
        // O faza = un BFS + toate DFS-urile posibile pe level graph-ul curent
        while (bfs(source, sink, graph, n, level)) {

            // Resetam pointerii pentru aceasta faza
            Arrays.fill(ptr, 0);

            // Trimitem flux cat timp DFS gaseste cai in level graph-ul curent
            int pushed;
            while ((pushed = dfs(source, sink, Integer.MAX_VALUE, graph, level, ptr)) > 0) {
                totalFlow += pushed;
            }

            // Level graph-ul curent e complet blocat.
            // BFS-ul urmator va construi un level graph nou, mai adanc.
        }

        return totalFlow;
    }

    // -------------------------------------------------------------------------
    // Helper — adauga arc orientat cu arc invers (backward) de capacitate 0
    // -------------------------------------------------------------------------
    static void addEdge(List<List<Edge>> graph, int u, int v, int cap) {
        Edge forward  = new Edge(v, cap);
        Edge backward = new Edge(u, 0);

        forward.revEdge  = backward;
        backward.revEdge = forward;

        graph.get(u).add(forward);
        graph.get(v).add(backward);
    }

    // -------------------------------------------------------------------------
    // Main — acelasi graf ca in EdmondsKarp pentru comparatie
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        int n = 6;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        addEdge(graph, 0, 1, 16);
        addEdge(graph, 0, 2, 13);
        addEdge(graph, 1, 2, 4);
        addEdge(graph, 2, 1, 10);
        addEdge(graph, 1, 3, 12);
        addEdge(graph, 3, 2, 2);
        addEdge(graph, 2, 4, 14);
        addEdge(graph, 4, 3, 7);
        addEdge(graph, 3, 5, 20);
        addEdge(graph, 4, 5, 4);

        int totalFlow = dinic(0, n - 1, graph, n);
        System.out.printf("Max flow de la 0 la %d este %d%n", n - 1, totalFlow);
        // Output: Max flow de la 0 la 5 este 23
    }
}
```


## Push-relabel (preflux)

Idee: in loc sa cautam cate un drum / mai multe drumuri odata si sa planificam tot, ne imaginam ca S este sus, D este jos, iar apa curge asa la vale. 

Asadar, apa curge in jos, fiecare nod putand sa acumuleze un exces: de exemplu daca avem 1->2 cu capacitate de 10 si 2->3 cu capacitate 5, spunem ca in nodul 2 avem exces de 5.

Construim height:
```
h[u] = "înălțimea" nodului u — un număr întreg
```

Reguli fixe:

```
h[s] = n      (sursa e MEREU la înălțimea n)
h[t] = 0      (destinația e MEREU la înălțimea 0)
h[*] = 0      (restul încep la 0)
```

**Regula critică:** Poți pompa flux de la u la v **DOAR dacă:**

```
h[u] = h[v] + 1
```

### Operatii admise

1. PUSH
	- **Când?** Nodul u are exces (e[u] > 0) și există vecin v cu h[u] = h[v] + 1 și arc rezidual.
	- **Ce face?** Trimite flux de la u la v.
	- Cât trimite? min(e[u], cf(u,v))
		- nu poți trimite mai mult decât ai în rezervor
		- nu poți trimite mai mult decât permite țeava
2. RELABEL
	- **Când?** Nodul u are exces (e[u] > 0) dar **nu poate pompa nicăieri** — toți vecinii reziduali sunt la aceeași înălțime sau mai sus.
	- **Ce face?** Ridică înălțimea lui u astfel încât să poată pompa spre cel mai jos vecin.
	- $h[u] = 1 + min\{ h[v] \; | \; (u,v) \text{arc rezidual} \}$



```java
import java.util.*;

public class PushRelabel {

    static class Edge {
        int to, cap, rev;
        // rev = indicele arcului invers in lista graph.get(to)
    }

    // -----------------------------------------------------------------
    // Adauga arc orientat u->v cu capacitate cap
    // + arcul invers v->u cu capacitate 0
    // -----------------------------------------------------------------
    static void addEdge(List<List<Edge>> graph, int u, int v, int cap) {
        Edge forward  = new Edge();
        forward.to    = v;
        forward.cap   = cap;
        forward.rev   = graph.get(v).size(); // indicele unde va fi backward

        Edge backward  = new Edge();
        backward.to    = u;
        backward.cap   = 0;
        backward.rev   = graph.get(u).size(); // indicele unde e forward

        graph.get(u).add(forward);
        graph.get(v).add(backward);
    }

    // -----------------------------------------------------------------
    // Push — pompeaza flux de la u la v
    // Conditie apelant: e[u]>0, h[u]==h[v]+1, e.cap>0
    // -----------------------------------------------------------------
    static void push(
            int u,
            Edge e,
            int[] excess,
            List<List<Edge>> graph)
    {
        int d = Math.min(excess[u], e.cap); // cat putem pompa

        e.cap -= d;                          // scade capacitatea forward
        graph.get(e.to).get(e.rev).cap += d; // creste capacitatea backward
        excess[u] -= d;                      // scade excesul din u
        excess[e.to] += d;                   // creste excesul in v
    }

    // -----------------------------------------------------------------
    // Relabel — mareste inaltimea lui u
    // Conditie apelant: e[u]>0, niciun vecin v cu h[u]==h[v]+1 si cap>0
    // -----------------------------------------------------------------
    static void relabel(
            int u,
            List<List<Edge>> graph,
            int[] height)
    {
        int minHeight = Integer.MAX_VALUE;

        for (Edge e : graph.get(u)) {
            if (e.cap > 0) // doar arce reziduale
                minHeight = Math.min(minHeight, height[e.to]);
        }

        // Urcam u cu exact atat cat trebuie sa poata pompa spre cel mai jos vecin
        height[u] = minHeight + 1;
    }

    // -----------------------------------------------------------------
    // Push-Relabel principal
    // Varianta: FIFO (nodurile active sunt tinute intr-o coada)
    // -----------------------------------------------------------------
    static int pushRelabel(
            int source,
            int sink,
            List<List<Edge>> graph,
            int n)
    {
        int[] height = new int[n]; // h[u]
        int[] excess = new int[n]; // e[u]

        // --- Initializare ---
        height[source] = n; // sursa e la inaltimea n

        // Saturam toate arcele din sursa
        for (Edge e : graph.get(source)) {
            if (e.cap > 0) {
                // Pompeaza tot
                graph.get(e.to).get(e.rev).cap += e.cap; // backward creste
                excess[e.to] += e.cap; // excesul vecinului creste
                excess[source] -= e.cap; // excesul sursei scade
                e.cap = 0;     // arcul forward se satureaza
            }
        }

        // --- Coada FIFO cu noduri active (excess > 0, != source, != sink) ---
        Queue<Integer> active = new ArrayDeque<>();
        boolean[] inQueue     = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (i != source && i != sink && excess[i] > 0) {
                active.add(i);
                inQueue[i] = true;
            }
        }

        // --- Bucla principala ---
        while (!active.isEmpty()) {
            int u = active.poll();
            inQueue[u] = false;

            // Incearca sa pompeze din u cat timp are exces
            boolean pushed = false;
            for (Edge e : graph.get(u)) {
                if (excess[u] == 0) break; // nu mai are ce pompa

                // Pompeaza doar daca arc rezidual si diferenta de inaltime e 1
                if (e.cap > 0 && height[u] == height[e.to] + 1) {
                    push(u, e, excess, graph);
                    pushed = true;

                    // Daca vecinul a primit exces si nu e sursa/sink -> activ
                    int v = e.to;
                    if (v != source && v != sink && !inQueue[v] && excess[v] > 0) {
                        active.add(v);
                        inQueue[v] = true;
                    }
                }
            }

            // Daca nu am putut pompa nicaieri si tot am exces -> relabel
            if (!pushed && excess[u] > 0) {
                relabel(u, graph, height);

                // Dupa relabel, u e din nou activ
                active.add(u);
                inQueue[u] = true;
            }
        }

        return excess[sink]; // tot ce a ajuns la sink = flux maxim
    }

    // -----------------------------------------------------------------
    // Main — acelasi graf ca inainte
    // -----------------------------------------------------------------
    public static void main(String[] args) {
        int n = 6;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        addEdge(graph, 0, 1, 16);
        addEdge(graph, 0, 2, 13);
        addEdge(graph, 1, 2, 4);
        addEdge(graph, 2, 1, 10);
        addEdge(graph, 1, 3, 12);
        addEdge(graph, 3, 2, 2);
        addEdge(graph, 2, 4, 14);
        addEdge(graph, 4, 3, 7);
        addEdge(graph, 3, 5, 20);
        addEdge(graph, 4, 5, 4);

        int totalFlow = pushRelabel(0, n - 1, graph, n);
        System.out.printf("Max flow de la 0 la %d este %d%n", n - 1, totalFlow);
        // Output: Max flow de la 0 la 5 este 23
    }
}
```