# Prim

Algoritmul lui Prim rezolva problema AMA in urmatorul mod:
1. porneste dintr-un nod sursa S
2. La fiecare pas, se adauga muchia de cost minim care are exact un capat in nodurile ce face parte din AMA-ul actual
3. Se continua pana cand toate nodurile sunt in AMA

```java
record Edge(int to, int cost) {}

record CompleteEdge(int from, int to, int cost) {}

static List<CompleteEdge> prim(int n, List<List<Edge>> graph) {
	PriorityQueue<CompleteEdge> queue = new PriorityQueue<>(
		(e1, e2) -> Integer.compare(e1.cost(), e2.cost())
	);
	
	boolean[] inAMA = new boolean[n];
	
	inAMA[0] = true;
	queue.add(new CompleteEdge(0, 0, 0));
	
	List<CompleteEdge> AMA = new ArrayList<>();
	
	while (AMA.size() < n - 1) {
		int curr = -1;
		
		while (!queue.isEmpty()) {
			CompleteEdge min = queue.poll();
			
			if (!inAMA[min.to()]) {
				curr = min.to();
				if (min.to() != 0)
					AMA.add(min);

				break;
			}
		}
		
		if (curr == -1)
			return null;
			
		inAMA[curr] = true;
		
		for (Edge e : graph.get(curr)) {
			if (inAMA[e.to()])
				continue;
			
			queue.add(new CompleteEdge(curr, e.to(), e.cost()));
		}
	}
	
	return AMA;
}
```

Complexitate temporala: 
- Coada de prioritati: $O(M \times log N)$
- fara heap $O(N ^ 2)$

Fara heap, retinem un vector $d[i] =$ costul minim al muchiei ce aduce nodul $i$ in AMA, iar la fiecare iteratie parcurgem vectorul pentru a gasi costul minim 
