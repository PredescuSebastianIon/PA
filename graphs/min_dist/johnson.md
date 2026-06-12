# Johnson

Algoritmul lui Johson este tot un algoritm care calculeaza distanta intre toate nodurile 
dintr-un graf, exact ca Floyd-Warshall. Diferenta este ca Johnson este mult mai eficient 
pentru grafurile rare, avand complexitatea temporala $O(N^2 \times log N + N \times M)$

Idee: Daca toate costurile sunt pozitive, rulam Dijkstra din fiecare nod si obtine: $N \times O(M \times log N) = O(M \times N \times log N) < O(N ^ 3)$

In cazul in care avem si costuri negative, Johnson a venit cu urmatoare solutie de a retransforma 
costurile astfel incat sa devina toata pozitive, pastrand ordinea drumurilor minime.
1. Adaugam un nod auxiliar S. Il legam de toate nodurile, folosind arce de cost 0
2. Rulam Bellman-Ford din S, obtinand h(v) pentru fiecare nod din graf. Atentie - daca detectam ciclu negativ, ne oprim si semnalam ca nu e valid graful
3. Recalculam costurile astfel: daca inainte aveam w(u, v) -- costul de la u la v, acum vom avea w'(u, v) = w(u, v) + h(u) - h(v). Stim ca este pozitiv, pentru ca inegalitatea triunghiului ne garanteaza ca h(v) < h(u) + w(u, v)
4. Rulam Dijkstra din fiecare nod pe noul graf cu w' ca si costuri. Vom obtine o matrice, pentru care delta (u, v) reprezinta distanta minima de la u la v folosind w'
5. Refacem distantele originale: d(u, v) = delta(u, v) + h(v) - h(u)
   
> [!NOTE]
> TODO: De implementat acest algoritm

