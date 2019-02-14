package com.exercises.algorithms;

import com.exercises.model.Adjacent;
import com.exercises.model.Graph;
import com.exercises.model.Node;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllPaths implements Algorithm {

    @Override
    public JsonObject apply(Graph graph, String from, String to) {

        ArrayList<ArrayList<String>> results = new ArrayList<>();
        ArrayList<String> current = new ArrayList<>();
        current.add(from);
        HashMap<String, Boolean> visited = new HashMap<>();
        for (Node node : graph.getNodes()) {
            visited.put(node.getId(), false);
        }
        getAllPaths(graph, from, to, visited, current, results);


        Gson gsonBuilder = new GsonBuilder().create();

        Map<String, Object> paths = new HashMap<>();
        paths.put("from", from);
        paths.put("to", to);
        paths.put("paths", results);

        Map<String, Map> result = new HashMap<>();
        result.put("paths", paths);

        JsonObject jsonFromJavaMap = gsonBuilder.toJsonTree(result).getAsJsonObject();
        return jsonFromJavaMap;
    }

    /**
     *
     * @param graph the graph on where we execute the DFS
     * @param currentNode current working node on the search
     * @param to node we want to reach
     * @param visited hashmap we use for marking visited nodes in the backtrack
     * @param currentPath current path under review
     * @param results here we store all the valid paths
     */
    private void getAllPaths(Graph graph, String currentNode, String to, HashMap<String, Boolean> visited, ArrayList<String> currentPath, ArrayList<ArrayList<String>> results) {

        visited.put(currentNode, true);
        if (currentNode.equals(to)) {
            //we found a result
            visited.put(currentNode, false);
            results.add((ArrayList<String>) currentPath.clone());
            return;
        }

        //get all adjacents and recursion
        for (Adjacent adjacent : graph.getAdjacents(graph.getNode(currentNode))) {
            String adjId = adjacent.node.getId();
            if (!visited.get(adjId)) {
                currentPath.add(adjId);
                getAllPaths(graph, adjId, to, visited, currentPath, results);
                currentPath.remove(currentPath.size() - 1);
            }
        }
        visited.put(currentNode, false);
    }
}
