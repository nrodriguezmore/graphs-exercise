package com.exercises;

import com.exercises.algorithms.Executor;
import com.exercises.mappers.Queries;
import com.exercises.model.Graph;
import com.exercises.services.GraphService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) {

        GraphService graphService;
        try {
            graphService = new GraphService();
        } catch (IOException ex) {
            System.out.println("There is a problem loading the data to access the database\n");
            return;
        }

        System.out.println( "Hello, please insert the serialized queries to solve\n" );

        //convert json string to object
        String jsonData = "{\"queries\":[{\"type\":\"paths\",\"start\":\"n1\",\"end\":\"n4\"},{\"type\":\"cheapest\",\"start\":\"n1\",\"end\":\"n4\"},{\"type\":\"paths\",\"start\":\"n2\",\"end\":\"n3\"},{\"type\":\"cheapest\",\"start\":\"n2\",\"end\":\"n3\"},{\"type\":\"paths\",\"start\":\"n4\",\"end\":\"n1\"}]}";


        Queries queries = new Gson().fromJson(jsonData, Queries.class);
        Graph graph = graphService.load("g1");

        Executor executor = new Executor();
        JsonArray results = executor.execute(graph, queries);
        System.out.println(results.toString());
    }
}
