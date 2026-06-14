> [!WARNING]
> This section is still in maintenance


## 1. Task Description

Problema fluxului maxim spune ca intr-un graf orientat ponderat, avand un nod 
de plecare S (source node) si un nod destinatie D (sink node), vrem sa aflam 
care este cantitatea maxima (sa zicem de apa) pe care o putem trimite de la S la D.

## 2. Introduction

Definitie formala: O retea de flux este un fraf orientat G(V, E), avand numai muchii cu capacitati pozitive, daca o muchie nu exista spunem ca are capacitate 0, un nod sursa si unul destinatie.
**Conditie importanta:** Orice nod care nu e sursa sau destinatie trebuie sa se afle pe minim un drum de la s la t. Altfel, avem noduri izolate care nu au sens sa se afle in retea.

Fie urmatoarele functii, pe care le vom utiliza in urmatoarele sectiuni:
- $c: V \times V \to \mathbb{R} \text{ , capacitatea retelei}$
- $f: V \times V \to \mathbb{R} \text{ , fluxul prin reteaua G}$
## 3. Theory & Methods

Proprietati:
- $\forall u, v \text{ - noduri din } V, f(u, v) \leq c(u, v)$ $\Longleftrightarrow$ fluxul printr-un arc este mai mic sau egal cu capacitatea arcului
- $\forall u, v \text{ - noduri din } V, f(u, v) = -f(v, u)$ $\Longleftrightarrow$ simetria fluxului
- $\sum f(u, v) = 0, \quad \forall u \in V \setminus \{s, t\}$ - conservarea fluxului
### Surse multiple si destinatii multiple

In cazul in care avem mai multe surse $\{s_1, s_2, \dots, s_n\}$ si mai multe destinatii $\{t_1, t_2, \dots, t_m\}$, putem reduce la o singura sursa si destinatie astfel:
- introducem o super-sursa S cu arce de capacitate infinita spre fiecare sursa reala
- introducem o super-destinatie D, fiecare destinatie avand cate un arc de capacitate infinita spre T

**Arc rezidual:** Un arc (u, v) se numeste rezidual daca fluxul pe el nu a atins capacitatea maxima (adica prin el se mai poate trimite un flux suplimentar).

**Capacitate reziduala:** Capacitatea reziduala este egala cu capacitatea reala a unui arc minus fluxul trimis pe acest arc. Aceasta este egala cu $c(u, v) - f(u, v)$

Un drum de ameliorare (augumenting path sau cale reziduala) este un drum de la sursa $s$ la destinatia $t$ in graful rezidual, pe care muchiile au capacitate reziduala strict pozitiva.

**Retea reziduala:** $G_f = (V, E_f) \text{ unde } E_f = \{(u,v) \in V \times V \; | \; c_f(u,v) > 0 \}$

Este reteaua formata din toate arcele pe care mai poti mari fluxul.

Observatie critica: $E_f \not\subset E$ - Reteaua reziduala poate contine arce care nu exista initial in graful original. Aceste arce sunt cele cu minus practic: drum (u, v) cu $f(u, v) > 0$ atunci exista un arc rezidual cu $c_f(v, u) = f(u, v) > 0$, fiind practic o implicatie directa a faptului ca poti anula fluxul trimis anterior inversand directia. 

Lema: Daca aveam o retea de flux $G = (V, E)$, un flux $f$ in retea si $G_f$ - reteaua reziduala a lui G. Daca gasim un flux $f'$ in reteaua $G_f$, atunci $f + f'$ reprezinta un flux valid in $G$. Practic, aceasta lema ne spune cum ne putem mari fluxul intr-o retea.

#### Taieturi in retele de fluxuri

Definim o taietura $(S, T)$ a unei retele de flux ca fiind o partitionare a nodurilor in 2 multimi disjuncte $S \text{ si } T = V \setminus S$, astfel incat $s \in S \text{ si } t \in T$.
- $f(S, T) = \sum_{x \in S} \sum_{y \in T} f(x, y)$ - fluxul prin taietura
- $c(S, T) = \sum_{x \in S} \sum_{y \in T} c(x, y)$ - capacitatea taieturii

Lema: fluxul prin taietura este egal cu fluxul prin intreaga retea, oricare ar fi taietura valida $(S, T)$.

Corolar: $S, T$ - taietura oarecare, fluxul maxim prin aceasta este limitat superior de capacitatea taieturii.

> [!NOTE]
> To add example for cut

Teorema taieturii minime: Fie $G = (V, E)$ o retea de flux. Urmatoarele afirmatii sunt echivalente:
- $f$ este o functie de flux in G astfel incat $|f|$ este flux maxim total in $G$
- reteaua reziduala $G_f$ nu are cai reziduale
- exista o taietura $(S, T)$ astfel incat $|f| = c(S, T)$

Explicatie:
- $f$ este un flux maxim $\Longleftrightarrow$ Am gasit cea mai mare cantitate de flux posibila de la $s$ la $t$
- reteaua reziduala nu mai are cai $\Longleftrightarrow$ In graful rezidual nu mai exista niciun drum de la $s$ la $t$, ceea ce inseamna ca nu mai putem mari fluxul, adica avem cantitatea maxima de flux $\Longleftrightarrow$ flux maxim
- exista o taietura $(S, T)$ pentru care $|f| = c(S, T)$ $\Longleftrightarrow$ Stim ca pentru orice taietura este adevarat ca $|f| \leq c(S, T)$, adica valoarea oricarui flux este limitata superior de capacitatea oricarei taieturi. Daca gasim o taietura pentru care $|f| = c(S, T)$, atunci fluxul nostru atinge deja aceasta limita superioara. Cum niciun flux nu poate depasi capacitatea taieturii, rezulta ca nu poate exista un flux mai mare. Prin urmare, f este flux maxim.

## 4. Algorithms

Algoritmi care rezolva aceasta problema:
* Ford-Fulkerson
* Edmonds-Karp
* Dinic
* Preflow-push / push-relabel

### Ford-Fulkerson

### Edmonds-Karp

### Dinic

### Push-Relabel

Idee: in loc sa cautam cate un drum / mai multe drumuri odata si sa planificam tot, ne imaginam ca $S$ este sus, $T$ este jos, iar apa curge asa la vale. 

Asadar, apa curge in jos, fiecare nod putand sa acumuleze un exces: de exemplu daca avem $1 \rightarrow 2$ cu capacitate de $10$ si $2 \rightarrow 3$ cu capacitate $5$, spunem ca in nodul $2$ avem exces de $5$.

Definim urmatoarele date pe care le retinem in fiecare nod:
- $h[u] = \text{inaltimea nodului } u$
- $e[u] = \text{excesul nodului } u$

Acum, regula este ca poti impinge flux doar dintr-un nod mai inalt catre un nod mai jos (chiar doar la nodurile de pe nivelul urmator). Dar ce facem daca suntem blocati? Daca nu mai avem niciun nod la urmatorul nivel care sa permita primirea de apa? Atunci, aplicam operatia de *relabel*, prin care *ridicam inaltimea* nodului curent.

Conditii:
- $h[s] = n$ - sursa e MEREU la inaltimea $n$
- $h[t] = 0$ - destinatia e mereu la inaltimea $0$
- $h[*] = 0$ - restul nodurilor incep la inaltimea $0$
- Putem pompa ala de la $u$ la $v$ numai daca $h[u] = h[v] + 1$

Operatii admise:
1. PUSH
	- **Cand?** Nodul $u$ are exces ($e[u] > 0$) si exista vecin $v$ cu $h[u] = h[v] + 1$ si arc rezidual.
	- **Ce face?** Trimite flux de la $u$ la $v$.
	- **Cat trimite?** $min(e[u], c_f(u,v))$
		- nu poti trimite mai mult decat ai in rezervor
		- nu poti trimite mai mult decat permite teava
2. RELABEL
	- **Cand?** Nodul u are exces ($e[u] > 0$) dar **nu poate pompa nicaieri** — toti vecinii reziduali sunt la aceeași înălțime sau mai sus.
	- **Ce face?** Ridică înălțimea lui u astfel încât să poată pompa spre cel mai jos vecin.
	- $h[u] = 1 + min\{ h[v] \; | \; (u,v) \text{ arc rezidual} \}$

Implementare: Vezi [aici](./PushRelabel.java).
## 10. References

1. https://cp-algorithms.com/graph/edmonds_karp.html