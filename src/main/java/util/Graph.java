package util;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;

public class Graph<V>{

    //Lista de adyacencia.
    private Map<V, Set<V>> adjacencyList = new HashMap<>();

    /**
     * Añade el vértice `v` al grafo.
     *
     * @param v vértice a añadir.
     * @return `true` si no estaba anteriormente y `false` en caso contrario.
     */
    public boolean addVertex(V v){
        if (adjacencyList.containsKey(v)) {
            return false;
        }
        adjacencyList.put(v, new HashSet<>());
        return true;
    }

    /**
     * Añade un arco entre los vértices `v1` y `v2` al grafo. En caso de
     * que no exista alguno de los vértices, lo añade también.
     *
     * @param v1 el origen del arco.
     * @param v2 el destino del arco.
     * @return `true` si no existía el arco y `false` en caso contrario.
     */
    public boolean addEdge(V v1, V v2){
        // Asegurar que los vértices existan, añadiéndolos si no
        addVertex(v1); // El valor de retorno no es crítico aquí, solo nos aseguramos de que existan.
        addVertex(v2);

        // Añadir el arco v1 -> v2
        Set<V> adjacentsOfV1 = adjacencyList.get(v1);
        if (adjacentsOfV1.contains(v2)) {
            return false; // El arco ya existía
        }
        adjacentsOfV1.add(v2);
        return true;
    }

    /**
     * Obtiene el conjunto de vértices adyacentes a `v`.
     *
     * @param v vértice del que se obtienen los adyacentes.
     * @return conjunto de vértices adyacentes.
     * @throws Exception si el vértice no existe en el grafo.
     */
    public Set<V> obtainAdjacents(V v) throws Exception{
        if (!adjacencyList.containsKey(v)) {
            throw new Exception("Vertex " + v + " not found in the graph.");
        }
        // Devolver una copia para evitar la modificación externa de la lista de adyacencia interna
        return new HashSet<>(adjacencyList.get(v));
    }

    /**
     * Comprueba si el grafo contiene el vértice dado.
     *
     * @param v vértice para el que se realiza la comprobación.
     * @return `true` si `v` es un vértice del grafo.
     */
    public boolean containsVertex(V v){
        return adjacencyList.containsKey(v);
    }

    /**
     * Método `toString()` reescrito para la clase `Grafo.java`.
     * @return una cadena de caracteres con la lista de adyacencia.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        // Para una salida más consistente, podemos ordenar las claves si son comparables
        List<V> vertices = new ArrayList<>(adjacencyList.keySet());
        try {
            // Intenta ordenar si V es Comparable
            if (!vertices.isEmpty() && vertices.get(0) instanceof Comparable) {
                vertices.sort((v1, v2) -> ((Comparable<V>) v1).compareTo(v2));
            }
        } catch (ClassCastException e) {
            // No se puede ordenar, continuar con el orden del HashMap
        }


        for (V vertex : vertices) {
            sb.append(vertex.toString()).append("=");
            Set<V> adjacents = adjacencyList.get(vertex);
            // Ordenar también los adyacentes para una salida consistente
            List<V> sortedAdjacents = new ArrayList<>(adjacents);
            try {
                if (!sortedAdjacents.isEmpty() && sortedAdjacents.get(0) instanceof Comparable) {
                    sortedAdjacents.sort((v1, v2) -> ((Comparable<V>) v1).compareTo(v2));
                }
            } catch (ClassCastException e) {
                // No se puede ordenar, continuar con el orden del Set
            }
            sb.append(sortedAdjacents.toString()); // toString de List es [elem1, elem2]
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Obtiene, en caso de que exista, el camino más corto entre
     * `v1` y `v2`. En caso contrario, devuelve `null`.
     *
     * @param v1 el vértice origen.
     * @param v2 el vértice destino.
     * @return lista con la secuencia de vértices del camino más corto
     * entre `v1` y `v2`, o `null` si no hay camino o los vértices no existen.
     **/
    public List<V> shortestPath(V v1, V v2){
        if (!containsVertex(v1) || !containsVertex(v2)) {
            return null; // Uno o ambos vértices no están en el grafo
        }

        if (v1.equals(v2)) {
            List<V> path = new ArrayList<>();
            path.add(v1);
            return path; // Camino de un vértice a sí mismo
        }

        ArrayDeque<V> queue = new ArrayDeque<>();
        Set<V> visited = new HashSet<>();
        Map<V, V> predecessorMap = new HashMap<>(); // Clave = vértice, Valor = predecesor en el camino

        queue.offer(v1);
        visited.add(v1);

        boolean pathFound = false;
        while (!queue.isEmpty()) {
            V current = queue.poll();

            if (current.equals(v2)) {
                pathFound = true;
                break; // Camino encontrado, salir del bucle BFS
            }

            try {
                // obtainAdjacents puede lanzar Exception, pero ya hemos verificado que 'current' está en el grafo
                // porque solo se añaden a la cola vértices visitados/existentes.
                for (V neighbor : adjacencyList.get(current)) { // Acceso directo más eficiente que obtainAdjacents() que crea copia
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        predecessorMap.put(neighbor, current);
                        queue.offer(neighbor);
                    }
                }
            } catch (Exception e) {
                // Teóricamente, no debería llegar aquí si la lógica es correcta y 'current' siempre existe.
                // Esta captura sería para un caso muy inesperado.
                System.err.println("Error inesperado durante BFS: " + e.getMessage());
                return null; // Indica un error en la lógica interna.
            }
        }

        if (!pathFound) {
            return null; // No se encontró camino
        }

        // Reconstruir el camino
        List<V> path = new ArrayList<>();
        V step = v2;
        while (step != null) {
            path.add(0, step); // Añadir al principio de la lista
            if (step.equals(v1)) break; // Llegamos al inicio
            step = predecessorMap.get(step);
            // Si step se vuelve null antes de llegar a v1 (y v1 != v2), indica que no hay camino (ya manejado por pathFound)
            // o un error en predecessorMap.
            if (step == null && !v1.equals(v2)) {
                // Esto no debería suceder si pathFound es true. Podría ser un error lógico.
                System.err.println("Error en la reconstrucción del camino: predecesor nulo inesperado.");
                return null;
            }
        }
        return path;
    }
}