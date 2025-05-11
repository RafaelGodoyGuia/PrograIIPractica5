Este proyecto proporciona una clase Java `Graph<V>` que implementa una estructura de datos de grafo **dirigido** genérico. La genericidad (`<V>`) permite que los vértices del grafo puedan ser de cualquier tipo de objeto (por ejemplo, `String`, `Integer`, objetos personalizados, etc.), siempre que dicho tipo implemente adecuadamente los métodos `equals()` y `hashCode()` para su correcto funcionamiento en colecciones como `HashMap` y `HashSet`.

La implementación se basa en una **lista de adyacencia** para almacenar las conexiones (arcos) entre los vértices.

## Motivación

El objetivo es ofrecer una clase de grafo reutilizable y fácil de entender para aplicaciones que requieran modelar relaciones entre entidades, como redes sociales, mapas de carreteras (simplificados), dependencias de tareas, etc.

## Características Principales

*   **Genérico (`<V>`)**: Permite trabajar con vértices de cualquier tipo.
*   **Dirigido**: Los arcos tienen una dirección específica (de un vértice origen a un vértice destino). Para simular un grafo no dirigido, se deben añadir arcos en ambas direcciones explícitamente (ej., `addEdge(A, B)` y `addEdge(B, A)`).
*   **Lista de Adyacencia**: Utiliza `Map<V, Set<V>>` para una representación eficiente y para evitar arcos duplicados.
*   **Operaciones Fundamentales**:
    *   Añadir vértices (`addVertex`).
    *   Añadir arcos (`addEdge`).
    *   Obtener vértices adyacentes (`obtainAdjacents`).
    *   Comprobar la existencia de un vértice (`containsVertex`).
*   **Algoritmo de Camino Más Corto**:
    *   Implementa `shortestPath` para encontrar el camino con el menor número de arcos entre dos vértices.
    *   Utiliza el algoritmo de **Búsqueda en Anchura (BFS)**, adecuado para grafos no ponderados.
*   **Representación en Cadena (`toString`)**:
    *   Proporciona una visualización legible de la lista de adyacencia.
    *   Intenta ordenar los vértices y sus adyacentes (si el tipo `V` es `Comparable`) para una salida determinista.
    * 