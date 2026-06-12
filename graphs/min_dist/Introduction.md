# Algoritmi de distanta minima

Notatii:
* N - numarul de noduri
* M - numarul de muchii

Leme si teoreme importante:

**Lema**: Subdrumurile unui drum minim sunt si ele minime! Deci, daca avem un drum $p = v_1 v_2 v_3 \dots v_k$ , atunci orice subdrum $v_i \dots v_j$ va fi si el minim.

Corolar: $p = s \dots u \; \text{-->} \; v$ e drum optim, atunci distanta de la s la v = distanta de la s la u plus arcul de la u la v. Practic, distanta optima se "descompune" pe ultimul arc.

Dijkstra si Bellmanford se bazeaza pe relaxarea muchiilor: daca descoperim ca putem ajunge 
mai rapid la nodul u prin intermediul muchiei (v, u), atunci actualizam distanta si spune ca 
am relaxat nodul u prin v.