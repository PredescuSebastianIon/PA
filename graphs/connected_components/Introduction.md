# Componente tare conexe

Terminologie:
- CTC = componenta tare conexa
- SCC = Strongly connected component = CTC

Definitie: Fie G = (V, E) - un graf orientat. G este tare conex $\equiv$ 
oricare ar fi u si v - 2 noduri din V, exista un drum de la u la v, dar si unul 
de la v la u.

Definitie: Fie G = (V, E) - graf orientat. G' = (V', E'), o multime de noduri 
si muchii inclusa in G. G' este SCC a lui G daca, G' este tare conex si G' 
are numarul maxim de noduri (orice nod nou adaugat din G rezulta intr-o 
componenta ce nu va mai fi tare conexa).

Lema: Lema spune: dacă G' e o CTC, atunci orice nod de pe orice drum între 
două noduri din G' se află tot în G'.

Teorema: când rulezi DFS pe G, toate nodurile dintr-o CTC ajung în același 
arbore DFS, sub rădăcina u = primul nod descoperit din acea componentă.

