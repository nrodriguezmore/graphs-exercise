package com.exercises.algorithms;

import com.exercises.mappers.Queries;
import com.exercises.mappers.Query;
import com.exercises.mappers.QueryType;
import com.exercises.model.Graph;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Executor {

    /**
     *
     * @param graph the graph on where to execute the queries
     * @param queries queries to execute
     * @return executes the queries in parallel, aggregates the results and returns them as a single result
     */
    public JsonArray execute(Graph graph, Queries queries) {
        JsonArray result = new JsonArray();

        List<JsonObject> processed = queries.queries.parallelStream().map(query ->  executeSingle(query, graph)).collect(Collectors.toList());

        for (JsonObject partialResult : processed) {
            result.add(partialResult);
        }
        return result;
    }

    private JsonObject executeSingle(Query query, Graph graph) {
        if (query.type == QueryType.CHEAPEST) {
            CheapestPath cheapestPath = new CheapestPath();
            return cheapestPath.apply(graph, query.start, query.end);
        } else if (query.type == QueryType.PATHS) {
            AllPaths allPaths = new AllPaths();
            return allPaths.apply(graph, query.start, query.end);
        } else {
            return null;
        }
    }
}
