# Kruskal

Kruskal ofera o rezolvare diferita pentru aceasta problema problema: in loc sa plecam cu nodurile ca la Prim si sa ne uitam de fiecare data care este cea mai mica muchie de taiere, putem considera fiecare nod ca fiind un arbore liber independent, iar apoi ne uitam peste muchii, alegand mereu muchia cu cost minim care este o muchie de taiere intre 2 arbori liberi.

Mai precis:
1. Avem N arbori liberi
2. Sortam muchiile, crescator dupa cost
3. La fiecare pas (timp de N - 1) alegem cea mai mica muchie care uneste 2 noduri ce se afla i arbori diferiti

Aceasta tehnica utilizeaza paduri de multimi disjuncte.

```java
record Edge(int from, int to, int cost) {}

static int arb(int curr, int[] parent) {
	if (curr == parent[curr])
		return curr;
	
	// for future faster retrival we update the parent[curr]
	// in this way we can reach faster the root node
	int root = arb(parent[curr], parent);
	parent[curr] = root;
	
	return root;
}

static void union(int x, int y, int[] parent) {
	int rootX = arb(x, parent);
	int rootY = arb(y, parent);
	
	parent[rootX] = rootY;
}

static List<Edge> kruskal(int n, List<Edge> edges) {
	List<Edge> AMA = new ArrayList<>();
	int[] parent = new int[n];
	
	for (int i = 0; i < n; i++)
		parent[i] = i;
	
	// sort all the edges in increasing order
	Collections.sort(edges, (e1, e2) -> Integer.compare(e1.cost(), e2.cost()));
	
	// for n - 1 steps
	for (int step = 1; step <= n - 1; step++) {
		Edge curr = null;
		while (!edges.isEmpty()) {
			Edge actual = edges.removeFirst();
			
			if (arb(actual.from(), parent) == arb(actual.to(), parent))
				continue;
			
			curr = actual;
			break;
		}
		
		// Graph is not conex
		if (curr == null)
			return null;
		
		AMA.add(curr);
		union(curr.from(), curr.to(), parent);
	}
	
	return AMA;
}
```

Complexitate temporala: $O(M \times log M) \leq O(M \times log N^2) = O(M \times 2 \times log N) \approx O(M \times log N)$
