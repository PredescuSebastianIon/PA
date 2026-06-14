/*
 * Author: Predescu Sebastian-Ion
 * Link to problem: https://infoarena.ro/problema/cuplaj
*/

#include <bits/stdc++.h>
using namespace std;

const int NMAX = 10005;

int n, m, e;

vector<int> g[NMAX];

int pairU[NMAX];
int pairV[NMAX];

int dist[NMAX];

bool bfs() {
    queue<int> q;

    for (int u = 1; u <= n; u++) {

        if (pairU[u] == 0) {
            dist[u] = 0;
            q.push(u);
        }
        else {
            dist[u] = -1;
        }
    }

    bool found = false;

    while (!q.empty()) {
        int u = q.front();
        q.pop();

        for (int v : g[u]) {

            if (pairV[v] == 0) {
                found = true;
            }
            else if (dist[pairV[v]] == -1) {

                dist[pairV[v]] = dist[u] + 1;

                q.push(pairV[v]);
            }
        }
    }

    return found;
}

bool dfs(int u) {

    for (int v : g[u]) {

        if (pairV[v] == 0 ||
            (dist[pairV[v]] == dist[u] + 1 &&
             dfs(pairV[v])))
        {

            pairU[u] = v;
            pairV[v] = u;

            return true;
        }
    }

    dist[u] = -1;

    return false;
}

int hopcroftKarp() {

    int matching = 0;

    while (bfs()) {

        for (int u = 1; u <= n; u++) {

            if (pairU[u] == 0) {

                if (dfs(u))
                    matching++;
            }
        }
    }

    return matching;
}

int main() {

    cin >> n >> m >> e;

    for (int i = 0; i < e; i++) {

        int u, v;
        cin >> u >> v;

        g[u].push_back(v);
    }

    int ans = hopcroftKarp();

    cout << ans << '\n';

    for (int u = 1; u <= n; u++) {

        if (pairU[u] != 0) {
            cout << u << ' ' << pairU[u] << '\n';
        }
    }
}
