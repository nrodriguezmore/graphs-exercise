package com.exercises;

import com.exercises.algorithms.Executor;
import com.exercises.mappers.Queries;
import com.exercises.model.Graph;
import com.exercises.services.GraphService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.Scanner;

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


        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println( "Hello, please enter the graph you want to execute the queries, " +
                "press enter and then enter the serialized queries to solve\n" );

        String graph_id = scanner.nextLine();
        String jsonData = scanner.nextLine();
            //convert json string to object

        Queries queries = new Gson().fromJson(jsonData, Queries.class);
        Graph graph = graphService.load(graph_id);

        Executor executor = new Executor();
        JsonArray results = executor.execute(graph, queries);
        System.out.println(results.toString());
    }
}
