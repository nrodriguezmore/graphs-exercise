package com.exercises.algorithms;

import com.exercises.model.Adjacent;
import com.exercises.model.Graph;
import com.exercises.model.Node;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.*;

public class CheapestPath implements Algorithm {

    @Override
    public JsonObject apply(Graph graph, String from, String to) {

        HashMap<String, String> paths = dikjstra(graph, from);

        ArrayList<String> path = new ArrayList<>();
        path.add(to);
        String current = paths.get(to);
        while(current != null && !current.equals(from)) {
            path.add(0, current);
            current = paths.get(current);
        }
        path.add(0, from);

        Gson gsonBuilder = new GsonBuilder().create();

        Map<String, Object> cheapest = new HashMap<>();
        cheapest.put("from", from);
        cheapest.put("to", to);

        if (from.equals(current)) {
            cheapest.put("path", path);
        } else {
            cheapest.put("path", false);
        }

        Map<String, Map> result = new HashMap<>();
        result.put("cheapest", cheapest);

        JsonObject jsonFromJavaMap = gsonBuilder.toJsonTree(result).getAsJsonObject();
        return jsonFromJavaMap;
    }

    private Adjacent cheapestAdjacent(Graph graph, Node node, HashMap<String, Boolean> visited) {

        Double min = Double.MAX_VALUE;
        Adjacent result = null;
        for (Adjacent adjacent : graph.getAdjacents(node)) {
            if (!visited.get(adjacent.node.getId()) && adjacent.cost < min) {
                min = adjacent.cost;
                result = adjacent;
            }
        }
        return result;
    }


    private Node cheapestDistance(Graph graph, HashMap<String, Double> distances, HashMap<String, Boolean> visited) {

        Double min = Double.MAX_VALUE;
        Node result = null;
        for (String node_id : distances.keySet()) {
            if (!visited.get(node_id) && distances.get(node_id) < min) {
                min = distances.get(node_id);
                result = graph.getNode(node_id);
            }
        }
        return result;
    }


    private HashMap<String, String> dikjstra(Graph graph, String from) {
        int size = graph.getNodes().size();
        HashMap<String, Double> distances = new HashMap<String, Double>();
        HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
        HashMap<String, String> paths = new HashMap<String, String>();

        for (Node node : graph.getNodes()) {
            distances.put(node.getId(), Double.MAX_VALUE);
            visited.put(node.getId(), false);
        }

        distances.put(from, 0.0);
        paths.put(from, from);

        for (Node node : graph.getNodes()) {

            Node cheapest = cheapestDistance(graph, distances, visited);

            visited.put(cheapest.getId(), true);

            for (Node to : graph.getNodes()) {

                //we get the edge from cheapestAdjacent to to
                Adjacent adjacent = graph.getAdjacent(cheapest, to);
                if (!visited.get(to.getId()) &&
                        adjacent != null &&
                        distances.get(cheapest.getId()) + adjacent.cost < distances.get(to.getId())) {
                    paths.put(to.getId(), cheapest.getId());
                    distances.put(to.getId(), distances.get(cheapest.getId()) + adjacent.cost);
                }
            }
        }



        return paths;
    }

    private HashMap<String, String> dikjstra2(Graph graph, String from) {
        int size = graph.getNodes().size();
        HashMap<String, Double> distances = new HashMap<String, Double>();
        HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
        HashMap<String, String> paths = new HashMap<String, String>();

        for (Node node : graph.getNodes()) {
            distances.put(node.getId(), Double.MAX_VALUE);
            visited.put(node.getId(), false);
        }

        distances.put(from, 0.0);
        paths.put(from, from);

        for (Node node : graph.getNodes()) {

            Adjacent cheapest = cheapestAdjacent(graph, node, visited);

            if (cheapest!=null) {
                visited.put(cheapest.node.getId(), true);

                for (Node to : graph.getNodes()) {

                    //we get the edge from cheapestAdjacent to to
                    Adjacent adjacent = graph.getAdjacent(cheapest.node, to);
                    if (!visited.get(to.getId()) &&
                            adjacent != null &&
                            cheapest.cost + adjacent.cost < distances.get(to.getId())) {
                        paths.put(to.getId(), cheapest.node.getId());
                        distances.put(to.getId(), cheapest.cost + adjacent.cost);
                    }
                }
            }

        }

        return paths;
    }
}