# Arbori minimi de acoperire

Acestia se mai numesc si AMA / Arbore partial minim (APM) / Minimum Spanning Tree (MST)

In contextul acestei probleme, vom lucra cu un graf neorientat si conex G = (V, E). Graful este unul ponderat, definind w(u, v) ca fiind weight ul de la nodul u la nodul v.

Definitie: Arborele liber a lui G este un subgraf Arb = (V', E'), inclus in G, astfel incat oricare 2 noduri din V' sunt conectate printr-o cale unica (ca intr-un arbore).

Costul unui arbore liber este supa tuturor muchiilor sale.

Un arbore liber devine un **arbore de acoperire** daca V' = V (include toate nodurile grafului). <br>
Notam cu ARB(G) padurea tuturor arborilor de acoperire posibili pe graful G.

Arborele minim de acoperire este acela cu costul minim din ARB(G).

O partitionare (S, V-S) a lui V imparte multimea de noduri in 2 submultimi disjuncte si complementare. Numin o muchie (u, v) ca taie frontiera daca un capat rste in S si unul in V-S.

O partitionare respecta multimea A (A inclus in E) daca nu exista muchii ce sa taie.

Numim o muchie sigura daca muchia e sigura in raport cu multimea muchiilor curente (adica nu stricam optimalitatea algoritmului).

### Algoritm AMA

1. Pornim de la noduri fara nicio muchie
2. La fiecare pas adaugam o muchie sigura
3. Continuam pana toate nodurile sunt conectate
4. Criteriu de optim: suma minica a costurilor muchiilor

**Teorema Muchiei Sigure**: 
Fie A o mulțime de muchii ale unui AMA parțial al grafului G = (V, E). Fie (S, V-S) o partiționare care respectă A, iar (u, v) ∈ E o muchie care taie frontiera dintre S și V-S cu proprietatea că are costul minim dintre toate muchiile care taie această frontieră:

w(u,v) = min { w(x,y) | (x,y) ∈ E și (x ∈ S, y ∈ V-S) sau (x ∈ V-S, y ∈ S) }

Atunci muchia (u, v) este **sigură în raport cu A**.

**Demonstrație (prin reducere la absurd):**

Presupunem că (u, v) **nu** este muchie sigură.

**Pasul 1:** Deoarece A ⊆ E' este parte dintr-un AMA parțial, există un AMA complet Arb' = (V, E') cu A ⊆ E'. Presupunem că (u, v) ∉ Arb' (altfel ar fi sigură automat).

**Pasul 2:** Deoarece Arb' este arbore de acoperire, există o cale de la u la v în Arb'. Deoarece (S, V-S) este o partiționare iar u ∈ S, v ∈ V-S, calea u..v trebuie să traverseze frontiera cel puțin o dată. Deci există o muchie (x, y) pe această cale care taie frontiera, cu (x, y) ∈ Arb'.

**Pasul 3:** (x, y) ∉ A deoarece partiționarea respectă A (A nu conține muchii care taie frontiera). De asemenea, (u, v) ∉ A din același motiv. Și w(u, v) ≤ w(x, y) deoarece (u, v) are costul minim dintre muchiile care taie frontiera.

**Pasul 4:** Construim Arb'' = (V, E'') prin E'' = E' – {(x, y)} + {(u, v)}, adică eliminăm (x, y) din Arb' și adăugăm (u, v). Arb'' rămâne arbore de acoperire (eliminând o muchie din ciclu și adăugând alta care restabilește conexitatea).

**Pasul 5:** C(Arb'') = C(Arb') - w(x,y) + w(u,v) ≤ C(Arb'). Dar Arb' este AMA, deci C(Arb') ≤ C(Arb''). Rezultă C(Arb'') = C(Arb'), deci Arb'' este tot un AMA, și (u, v) ∈ Arb'' → (u, v) este muchie sigură. **Contradicție!**

---

#### Proprietatea 1 (Muchia de cost maxim dintr-un ciclu nu e în AMA)

**Enunț:**

Fie G = (V, E) și C = (V', E') un ciclu în G. Fie e ∈ E' cu proprietatea că w(e) = max { w(e') | e' ∈ E' }, adică e este muchia cu costul maxim din ciclu. Atunci e ∉ Arb(G), unde Arb(G) este AMA al lui G.

**Demonstrație (prin reducere la absurd):**

Presupunem că e ∈ Arb(G).

**Pasul 1:** Eliminând e din Arb(G) se obțin două mulțimi de noduri: S₁ și S₂ (arborele devine deconectat în exact două componente, deoarece era arbore — aciclic și conex).

**Pasul 2:** e ∈ E' (face parte din ciclu), deci există o altă muchie e' ∈ E' din ciclu cu proprietatea că un capăt din e' este în S₁ și celălalt în S₂ (ciclul trebuie să traverseze și el „tăietura" creată). Mai mult, w(e) > w(e') deoarece e are costul maxim din ciclu (și e ≠ e').

**Pasul 3:** Arb(G) – e + e' este tot arbore de acoperire (reconectăm cele două componente cu e').

**Pasul 4:** Cost(Arb(G) - w(e) + w(e')) < Cost(Arb(G)) deoarece w(e') < w(e). Deci am găsit un arbore de acoperire cu cost mai mic, ceea ce contrazice faptul că Arb(G) este AMA. **Contradicție!** ∎

**Interpretare practică:** Niciodată nu vom include în AMA muchia cea mai scumpă dintr-un ciclu. Kruskal va exploata exact această proprietate.

---

#### Proprietatea 2 (Muchia de cost minim care „iese" dintr-un AMA parțial aparține AMA)

**Enunț:**

Fie G = (V, E) și S = (V', E') un AMA parțial al lui G cu V' ⊂ V. Fie e = (u, v) o muchie cu proprietatea că e ∉ E' și exact unul dintre capete este în V' (u ∈ V' și v ∉ V', sau invers), cu proprietatea că:

w(u, v) = min { w(u', v') | (u' ∈ V' și v' ∉ V') sau (u' ∉ V' și v' ∈ V') }

adică (u, v) este muchia de cost minim care conectează AMA-ul parțial la un nod din afara lui.

Atunci (u, v) ∈ AMA.

**Demonstrație (prin reducere la absurd):**

Presupunem că e ∉ AMA.

**Pasul 1:** Fie AMA Arb(G) și Arb' = Arb(G) – e' + e, unde e' este o muchie similară cu e (care conectează V' de V\V').

**Pasul 2:** Arb' este tot arbore de acoperire (înlocuim o muchie de traversare cu alta).

**Pasul 3:** Cost(Arb') ≤ Cost(Arb) deoarece w(u,v) ≤ w(e') (e = (u,v) are costul minim). Arb(G) este AMA, deci Cost(Arb) ≤ Cost(Arb'). Rezultă Cost(Arb') = Cost(Arb), deci Arb' este și el AMA, și e ∈ Arb' → e ∈ AMA. **Contradicție!** ∎

**Interpretare practică:** Prim va exploata exact această proprietate — la fiecare pas alegem muchia de cost minim cu un capăt în AMA-ul construit și un capăt afară.