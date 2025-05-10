package util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphTest {

    @Test
    public void testAddVertex_NewVertex_ReturnsTrueAndVertexContained() {
        Graph<String> graph = new Graph<>();
        assertTrue("Adding a new vertex should return true", graph.addVertex("A"));
        assertTrue("Graph should contain the added vertex", graph.containsVertex("A"));
    }

    @Test
    public void testAddVertex_ExistingVertex_ReturnsFalse() {
        Graph<String> graph = new Graph<>();
        graph.addVertex("A"); // Add once
        assertFalse("Adding an existing vertex should return false", graph.addVertex("A"));
        // Para acceder a adjacencyList.size(), necesitaría ser protected o tener un getter
        // o simplemente confiar en que containsVertex y addVertex funcionan correctamente.
        // assertTrue(graph.containsVertex("A")); // Aún debería estar allí
        // assertEquals(1, /* graph.getVertexCount() */); // Si tuviéramos tal método
    }

    @Test
    public void testContainsVertex_ExistingAndNonExisting() {
        Graph<String> graph = new Graph<>();
        graph.addVertex("A");
        assertTrue(graph.containsVertex("A"));
        assertFalse(graph.containsVertex("B"));
    }


    @Test
    public void testAddEdge_NewEdgeNewVertices_ReturnsTrueAndEdgeExists() throws Exception {
        Graph<String> graph = new Graph<>();
        assertTrue("Adding a new edge should return true", graph.addEdge("A", "B"));
        assertTrue("Graph should contain vertex v1", graph.containsVertex("A"));
        assertTrue("Graph should contain vertex v2", graph.containsVertex("B"));
        Set<String> adjA = graph.obtainAdjacents("A");
        assertTrue("Adjacents of v1 should contain v2", adjA.contains("B"));
        // Para un grafo dirigido:
        Set<String> adjB = graph.obtainAdjacents("B");
        assertFalse("Adjacents of v2 should NOT contain v1 (directed graph)", adjB.contains("A"));
        assertTrue("Adjacents of v2 should be empty if no outgoing edges from B", adjB.isEmpty());
    }

    @Test
    public void testAddEdge_ExistingEdge_ReturnsFalse() throws Exception {
        Graph<String> graph = new Graph<>();
        graph.addEdge("A", "B"); // Add once
        assertFalse("Adding an existing edge should return false", graph.addEdge("A", "B"));
        assertEquals("A should still have 1 adjacent", 1, graph.obtainAdjacents("A").size());
    }

    @Test(expected = Exception.class)
    public void testObtainAdjacents_NonExistingVertex_ThrowsException() throws Exception {
        Graph<String> graph = new Graph<>();
        graph.obtainAdjacents("Z");
    }

    @Test
    public void testObtainAdjacents_VertexWithNoOutgoingEdges_ReturnsEmptySet() throws Exception {
        Graph<String> graph = new Graph<>();
        graph.addVertex("A");
        assertTrue("Adjacents of an isolated vertex should be empty", graph.obtainAdjacents("A").isEmpty());
    }

    @Test
    public void testToString_SimpleGraph_OrderedOutput() {
        Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addVertex(4);
        graph.addVertex(0); // Para probar el orden de los vértices también

        String expected = "0=[]\n" +
                "1=[2, 3]\n" + // Asumiendo que los adyacentes también se ordenan si son Comparable
                "2=[]\n" +
                "3=[]\n" +
                "4=[]\n";
        assertEquals(expected, graph.toString());
    }

    @Test
    public void testToString_EmptyGraph() {
        Graph<String> graph = new Graph<>();
        assertEquals("toString of empty graph should be empty string", "", graph.toString());
    }


    // --- Pruebas para shortestPath ---

    @Test
    public void shortestPathFindsAPath() {
        System.out.println("\nTest shortestPathFindsAPath");
        System.out.println("----------------------------");
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 2);
        g.addEdge(1, 5);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(5, 4);
        List<Integer> expectedPath = new ArrayList<>();
        expectedPath.add(1);
        expectedPath.add(5);
        expectedPath.add(4);
        assertEquals(expectedPath, g.shortestPath(1, 4));
    }

    @Test
    public void shortestPath_NoPathExists_ReturnsNull() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 2);
        g.addEdge(3, 4); // Grafo desconectado
        assertNull("Should return null if no path exists", g.shortestPath(1, 4));
        assertNull("Should return null if no path exists (reverse)", g.shortestPath(4,1));
        g.addVertex(5); // Isolated vertex
        assertNull(g.shortestPath(1,5));

    }

    @Test
    public void shortestPath_PathToSelf_ReturnsSingleNodeList() {
        Graph<Integer> g = new Graph<>();
        g.addVertex(1);
        List<Integer> expectedPath = new ArrayList<>();
        expectedPath.add(1);
        assertEquals(expectedPath, g.shortestPath(1, 1));
    }

    @Test
    public void shortestPath_StartVertexDoesNotExist_ReturnsNull() {
        Graph<Integer> g = new Graph<>();
        g.addVertex(2);
        assertNull(g.shortestPath(1, 2)); // 1 no existe
    }

    @Test
    public void shortestPath_EndVertexDoesNotExist_ReturnsNull() {
        Graph<Integer> g = new Graph<>();
        g.addVertex(1);
        assertNull(g.shortestPath(1, 2)); // 2 no existe
    }

    @Test
    public void shortestPath_BothVerticesDoNotExist_ReturnsNull() {
        Graph<Integer> g = new Graph<>();
        assertNull(g.shortestPath(1, 2)); // ninguno existe
    }


    @Test
    public void shortestPath_EmptyGraph_ReturnsNull() {
        Graph<String> g = new Graph<>();
        assertNull(g.shortestPath("A", "B"));
    }

    @Test
    public void shortestPath_PrefersShorterPath() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B"); // A-B-D (longitud 2)
        g.addEdge("B", "D");

        g.addEdge("A", "E"); // A-E-F-D (longitud 3)
        g.addEdge("E", "F");
        g.addEdge("F", "D");

        g.addEdge("A", "C"); // A-C-G-H-D (longitud 4)
        g.addEdge("C", "G");
        g.addEdge("G", "H");
        g.addEdge("H", "D");


        List<String> expectedPath = Arrays.asList("A", "B", "D");
        List<String> actualPath = g.shortestPath("A", "D");

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void shortestPath_CycleDoesNotConfuse() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 1); // Ciclo 1-2-3-1
        g.addEdge(3, 4); // Salida del ciclo a 4

        List<Integer> expectedPath = Arrays.asList(1, 2, 3, 4);
        assertEquals(expectedPath, g.shortestPath(1, 4));
    }

    @Test
    public void shortestPath_DirectPathVsIndirectPath() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 4);       // Camino directo (longitud 1)
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);       // Camino indirecto (longitud 3)

        List<Integer> expectedPath = Arrays.asList(1, 4);
        assertEquals(expectedPath, g.shortestPath(1, 4));
    }
}