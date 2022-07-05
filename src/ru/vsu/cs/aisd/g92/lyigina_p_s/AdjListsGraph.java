package ru.vsu.cs.aisd.g92.lyigina_p_s;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Реализация графа на основе списков смежности
 */
public class AdjListsGraph implements WeightedGraph {
    private List<List<WeightedEdgeTo>> vAdjLists = new ArrayList<>();
    private int vCount = 0;
    private int eCount = 0;

    private static Iterable<Integer> nullIterable = new Iterable<Integer>() {
        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Integer next() {
                    return null;
                }
            };
        }
    };

    private static Iterable<WeightedEdgeTo> nullIterableWeighted = new Iterable<WeightedEdgeTo>() {
        @Override
        public Iterator<WeightedEdgeTo> iterator() {
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public WeightedEdgeTo next() {
                    return null;
                }
            };
        }
    };

    @Override
    public int vertexCount() {
        return vCount;
    }

    @Override
    public int edgeCount() {
        return eCount;
    }

    @Override
    public void addEdge(int v1, int v2) {

    }

    private int countingRemove(List<WeightedEdgeTo> list, int v) {
        int count = 0;
        if (list != null) {
            for (Iterator<WeightedEdgeTo> it = list.iterator(); it.hasNext(); ) {
                if (it.next().to() == v) {
                    it.remove();
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void removeEdge(int v1, int v2) {
        eCount -= countingRemove(vAdjLists.get(v1), v2);
        countingRemove(vAdjLists.get(v2), v1);
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        return nullIterable;
        //return vAdjLists.get(v) == null ? nullIterable : vAdjLists.get(v);
    }

    @Override
    public int getAdjCount(int v){
        if (vAdjLists.get(v) == null)
            return 0;
        return vAdjLists.get(v).size();
    }
    //возможно добавить кратность
    @Override
    public void addEdge(int v1, int v2, double weight) {
        int maxV = Math.max(v1, v2);
        // добавляем вершин в список списков связности
        for (; vCount <= maxV; vCount++) {
            vAdjLists.add(null);
        }

        if (vAdjLists.get(v1) == null) {
            vAdjLists.set(v1, new LinkedList<>());
        }
        vAdjLists.get(v1).add(new WeightedEdgeTo() {
            private int number = 0;
            private boolean visited = false;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            @Override
            public void setVisited(boolean v) {
                this.visited = v;
            }

            @Override
            public boolean getVisited() {
                return visited;
            }

            @Override
            public int to() {
                return v2;
            }

            @Override
            public double weight() {
                return weight;
            }
        });
        eCount++;
        if (vAdjLists.get(v2) == null) {
            vAdjLists.set(v2, new LinkedList<>());
        }
        vAdjLists.get(v2).add(new WeightedEdgeTo() {
            private int number = 0;
            private boolean visited= false;

            @Override
            public void setVisited(boolean v) {
                this.visited = v;
            }

            @Override
            public boolean getVisited() {
                return visited;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            @Override
            public int to() {
                return v1;
            }

            @Override
            public double weight() {
                return weight;
            }
        });
    }

    @Override
    public void addVertex() {
        vAdjLists.add(new LinkedList<>());
        vCount++;
    }

    @Override
    public void removeVertex(int v) {
        vCount--;
        eCount -= vAdjLists.get(v).size();
        //
        for (WeightedEdgeTo edge : adjacenciesWithWeights(v)) {

            countingRemove(vAdjLists.get(edge.to()), v);
        }
        vAdjLists.set(v, null);
    }

    @Override
    public Iterable<WeightedEdgeTo> adjacenciesWithWeights(int v) {
        return vAdjLists.get(v) == null ? nullIterableWeighted : vAdjLists.get(v);
    }
}
