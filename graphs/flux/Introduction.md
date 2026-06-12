# Flux maxim

Problema fluxului maxim spune ca intr-un graf orientat ponderat, avand un nod 
de plecare S (source node) si un nod destinatie D (sink node), vrem sa aflam 
care este cantitatea maxima (sa zicem de apa) pe care o putem trimite de la S la D.

Hai sa luam urmatorul exemplu:

```mermaid
graph LR

A(0) --> |16| B(1)
A --> |13| C(2)
B --> |4| C
C --> |10| B
B --> |12| D(3)
D --> |2| C
C --> |14| E(4)
E --> |7| D
D --> |20| F(5)
E --> |4| F
```

Algoritmi care rezolva aceasta problema:
* Ford-Fulkerson
* Edmonds-Karp
* Dinic
* Preflow-push / push-relabel

https://cp-algorithms.com/graph/edmonds_karp.html

