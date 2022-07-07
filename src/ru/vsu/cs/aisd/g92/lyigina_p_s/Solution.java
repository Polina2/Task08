package ru.vsu.cs.aisd.g92.lyigina_p_s;

import java.util.*;

public class Solution {
    public static int solution(WeightedGraph graph) {
        boolean[] visited = new boolean[graph.vertexCount()];
        GraphAlgorithms.dfsWeighted(graph, 0, (v) -> {
            visited[v] = true;
        });
        for (boolean b : visited){
            if (!b)
                return -1;
        }

        List<Integer> odd = new ArrayList<>();
        for (int i = 0; i < graph.vertexCount(); i++) {
            if (graph.getAdjCount(i) % 2 != 0)
                odd.add(i);
        }
        if (odd.isEmpty())
            return euler1(graph, 0, 0, new int[graph.vertexCount()]);
        else if (odd.size() == 2)
            return euler1(graph, odd.get(0), odd.get(1), new int[graph.vertexCount()]);
        else {
            int[] newEdges = new int[graph.vertexCount()];
            doubleEdges(graph, odd, newEdges);
            return euler1(graph, 0, 0, newEdges);
        }
    }

    private static void doubleEdges(WeightedGraph graph, List<Integer> odd, int[] newEdges) {
        int[][] dist = new int[odd.size()][odd.size()];
        GraphAlgorithms.MinDistanceSearchResult[] results = new GraphAlgorithms.MinDistanceSearchResult[odd.size()];
        for (int i = 0; i < odd.size(); i++) {
            results[i] = GraphAlgorithms.dijkstra(graph, odd.get(i));
            for (int j = 0; j < odd.size(); j++) {
                if (i == j)
                    dist[i][j] = Integer.MAX_VALUE;
                else
                    dist[i][j] = (int)results[i].d[odd.get(j)];
            }
        }

        HungarianAlgorithm ha = new HungarianAlgorithm(dist);
        int[][] assignment = ha.findOptimalAssignment();

        for (int i = 0; i < assignment.length; i++) {
            int[] pair = assignment[i];
            int[] from = results[pair[0]].from;
            int v = odd.get(pair[1]);
            while (from[v] >= 0){
                if (graph.countEdges(v, from[v]) < 2) {
                    graph.addEdge(v, from[v], graph.getWeight(v, from[v]));
                    newEdges[v]++;
                    newEdges[from[v]]++;
                }
                v = from[v];
            }
        }
    }

    private static int euler1(WeightedGraph graph, int begin, int end, int[] newEdges) {
        int time = graph.getAdjCount(begin) - newEdges[begin];
        int count = 1;
        int currV = begin;
        WeightedGraph.WeightedEdgeTo currE = null;
        WeightedGraph.WeightedEdgeTo tempE = null;
        int[][] edgesCount = new int[graph.vertexCount()][graph.vertexCount()];
        for (int v = 0; v < graph.vertexCount(); v++) {
            for (WeightedGraph.WeightedEdgeTo e : graph.adjacenciesWithWeights(v)){
                edgesCount[v][e.to()]++;
            }
        }
        for (int v = 0; v < graph.vertexCount(); v++) {
            for (WeightedGraph.WeightedEdgeTo e : graph.adjacenciesWithWeights(v)){
                if (edgesCount[v][e.to()]==1)
                    edgesCount[v][e.to()] = 0;
            }
        }
        boolean visited = false;
        w:
        while (currV != end || !visited){
            for (WeightedGraph.WeightedEdgeTo e : graph.adjacenciesWithWeights(currV)){
                if (e != null && e.getNumber()==0) {
                    if (edgesCount[currV][e.to()] == 2) {
                        currE = e;
                        edgesCount[currV][e.to()]--;
                        edgesCount[e.to()][currV]--;
                        break;
                    }
                    if (edgesCount[currV][e.to()] == 1 || e.to() == end)
                        tempE = e;
                    else {
                        currE = e;
                    }
                }
            }
            if (currE == null){
                currE = tempE;
            }
            time += currE.weight() + graph.getAdjCount(currE.to()) - newEdges[currE.to()];
            currE.setNumber(count);
            for (WeightedGraph.WeightedEdgeTo edge : graph.adjacenciesWithWeights(currE.to())) {
                if (edge.to() == currV && edge.getNumber()==0){
                    edge.setNumber(count);
                    break;
                }
            }
            count++;
            currV = currE.to();
            currE = null; tempE = null;
            if (currV == end){
                for (WeightedGraph.WeightedEdgeTo edge : graph.adjacenciesWithWeights(currV)) {
                    if (edge.getNumber()==0)
                        continue w;
                }
                visited = true;
            }
        }
        return time;
    }

    private static int euler(WeightedGraph graph, int begin, int end, int[] newEdges) {
        int time = graph.getAdjCount(begin) - newEdges[begin];
        int count = 1;
        int currV = begin;
        boolean visited = false;
        w:
        while (currV != end || !visited) {
            boolean isEnd = false, changed = false;
            int newV = currV;
            WeightedGraph.WeightedEdgeTo endEdge = null;
            for (WeightedGraph.WeightedEdgeTo edge : graph.adjacenciesWithWeights(currV)) {
                if (edge != null && edge.getNumber() == 0) {
                    if (edge.to() != end){
                        time += edge.weight() + graph.getAdjCount(edge.to()) - newEdges[edge.to()];
                        edge.setNumber(count);
                        for (WeightedGraph.WeightedEdgeTo edge1 : graph.adjacenciesWithWeights(edge.to())) {
                            if (edge1.to() == currV && edge1.getNumber()==0){
                                edge1.setNumber(count);
                                break;
                            }
                        }
                        newV = edge.to();
                        changed = true;
                        count++;
                        break;
                    } else {
                        isEnd = true;
                        endEdge = edge;
                    }
                }
            }
            if (!changed && isEnd) {
                endEdge.setNumber(count);
                time += endEdge.weight() + graph.getAdjCount(end) - newEdges[end];
                for (WeightedGraph.WeightedEdgeTo edge : graph.adjacenciesWithWeights(end)){
                    if (edge.to() == currV && edge.getNumber()==0){
                        edge.setNumber(count);
                        break;
                    }
                }
                count++;
                currV = end;
                for (WeightedGraph.WeightedEdgeTo edge : graph.adjacenciesWithWeights(currV)) {
                    if (edge.getNumber()==0)
                        continue w;
                }
                visited = true;
            } else
                currV = newV;
        }
        return time;
    }
}
