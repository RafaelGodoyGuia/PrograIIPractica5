package pr2;

import util.Graph; // Importa la clase Graph del paquete util
import java.util.List;
import java.util.Arrays; // Útil para crear listas rápidamente para comparación o impresión

public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Demostración de la clase Grafo:");
        System.out.println("==============================");

        // Crear un grafo de enteros
        Graph<Integer> g = new Graph<>();

        // 1. Añadir vértices (aunque addEdge también los añade si no existen)
        System.out.println("\nAñadiendo vértices:");
        System.out.println("Añadir vértice 10: " + g.addVertex(10)); // true
        System.out.println("Añadir vértice 10 de nuevo: " + g.addVertex(10)); // false
        System.out.println("Contiene vértice 10: " + g.containsVertex(10)); // true
        System.out.println("Contiene vértice 20: " + g.containsVertex(20)); // false

        // 2. Añadir arcos (como en la prueba)
        System.out.println("\nAñadiendo arcos (creando el grafo de la prueba shortestPathFindsAPath):");
        // Grafo:
        // 1 -> 2 -> 3
        // |         |
        // v         v
        // 5 ------> 4
        g.addEdge(1, 2);
        g.addEdge(1, 5);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(5, 4);
        // Añadir un arco extra para probar la no existencia de un arco previo
        System.out.println("Añadir arco 1 -> 2 (ya existe implícitamente por los anteriores, pero podría ser un nuevo vértice): " + g.addEdge(1,2)); //false
        System.out.println("Añadir arco 1 -> 6 (nuevo): " + g.addEdge(1,6)); // true

        // 3. Mostrar el grafo
        System.out.println("\nGrafo actual (lista de adyacencia):");
        System.out.println(g.toString());

        // 4. Obtener adyacentes
        System.out.println("\nObteniendo adyacentes:");
        try {
            System.out.println("Adyacentes de 1: " + g.obtainAdjacents(1));
            System.out.println("Adyacentes de 4 (no tiene arcos salientes): " + g.obtainAdjacents(4));
            System.out.println("Adyacentes de 10 (vértice aislado): " + g.obtainAdjacents(10));
        } catch (Exception e) {
            System.err.println("Error obteniendo adyacentes: " + e.getMessage());
        }

        System.out.println("\nIntentando obtener adyacentes de un vértice no existente (99):");
        try {
            g.obtainAdjacents(99);
        } catch (Exception e) {
            System.out.println("Excepción capturada como se esperaba: " + e.getMessage());
        }


        // 5. Calcular caminos más cortos
        System.out.println("\nCalculando caminos más cortos:");

        // Caso de prueba: 1 -> 4 (esperado: [1, 5, 4])
        List<Integer> path1_4 = g.shortestPath(1, 4);
        System.out.println("Camino más corto de 1 a 4: " + (path1_4 != null ? path1_4.toString() : "No encontrado"));

        // Caso: camino a sí mismo 3 -> 3 (esperado: [3])
        List<Integer> path3_3 = g.shortestPath(3, 3);
        System.out.println("Camino más corto de 3 a 3: " + (path3_3 != null ? path3_3.toString() : "No encontrado"));

        // Caso: no hay camino 1 -> 10 (10 es un vértice aislado)
        List<Integer> path1_10 = g.shortestPath(1, 10);
        System.out.println("Camino más corto de 1 a 10: " + (path1_10 != null ? path1_10.toString() : "No encontrado"));

        // Caso: vértice de origen no existe 99 -> 1
        List<Integer> path99_1 = g.shortestPath(99, 1);
        System.out.println("Camino más corto de 99 a 1 (99 no existe): " + (path99_1 != null ? path99_1.toString() : "No encontrado"));

        // Caso: vértice de destino no existe 1 -> 88
        List<Integer> path1_88 = g.shortestPath(1, 88);
        System.out.println("Camino más corto de 1 a 88 (88 no existe): " + (path1_88 != null ? path1_88.toString() : "No encontrado"));

        // Caso: grafo con strings
        System.out.println("\nProbando con un grafo de Strings:");
        Graph<String> stringGraph = new Graph<>();
        stringGraph.addEdge("Madrid", "Paris");
        stringGraph.addEdge("Paris", "Berlin");
        stringGraph.addEdge("Madrid", "Lisboa");
        stringGraph.addEdge("Lisboa", "Paris"); // Hace un ciclo más corto Madrid-Lisboa-Paris

        System.out.println(stringGraph.toString());
        List<String> pathMadBer = stringGraph.shortestPath("Madrid", "Berlin");
        System.out.println("Camino más corto de Madrid a Berlin: " + (pathMadBer != null ? pathMadBer.toString() : "No encontrado"));
        // Esperado: [Madrid, Lisboa, Paris, Berlin] o [Madrid, Paris, Berlin] si el orden de adición de arcos hace que BFS explore "Paris" desde "Madrid" primero.
        // Dado que "Paris" es añadido como adyacente de "Madrid" primero, y luego "Lisboa", BFS probablemente tomará Madrid->Paris->Berlin.
        // Si añadimos Madrid-Lisboa primero, y Lisboa-Paris, y Madrid-Paris después, el camino podría ser Madrid-Lisboa-Paris-Berlin
        // El BFS garantiza el camino más corto en número de arcos. Si hay varios de la misma longitud, el resultado puede depender del orden de exploración.

        // Reconstruyamos para forzar el camino más corto obvio
        Graph<String> stringGraph2 = new Graph<>();
        stringGraph2.addEdge("A", "B");
        stringGraph2.addEdge("B", "C");
        stringGraph2.addEdge("A", "D"); // Camino más largo A-D-C
        stringGraph2.addEdge("D", "C");

        System.out.println("\nGrafo String 2:");
        System.out.println(stringGraph2.toString());
        List<String> pathA_C_2 = stringGraph2.shortestPath("A", "C");
        System.out.println("Camino más corto de A a C: " + (pathA_C_2 != null ? pathA_C_2.toString() : "No encontrado"));
        // Debería ser [A, B, C] si el BFS explora B antes que D, o si A-B-C es el único camino de longitud 2.
        // Si A-D-C también es de longitud 2, el resultado podría ser [A, D, C].
        // En nuestro grafo, A->B y A->D. Si B se procesa antes que D, se encontrará A-B-C.

        System.out.println("\nFin de la demostración.");
    }
}
