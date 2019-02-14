package com.exercises;



import com.exercises.exceptions.GraphValidationException;
import com.exercises.model.Graph;
import com.exercises.parser.StaxParser;
import com.exercises.services.GraphService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
        } catch (IOException e) {
            System.out.println("There is a problem loading the data to access the database\n");
            return;
        }


        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        while (!exit) {

            System.out.println("Please, select option:\n" +
                    "load-xml-url: for loading an xml file from a remote url\n" +
                    "load-xml-resource: for loading an xml file from the local resources folder\n" +
                    "exit: finish the program");
            String command = scanner.nextLine();

            if ("load-xml-url".equals(command)) {
                System.out.println("Enter url:");

                try {
                    URL url = new URL(scanner.nextLine());
                    InputStream inputStream = url.openConnection().getInputStream();
                    Graph graph = StaxParser.parse(inputStream);
                    Boolean stored = graphService.store(graph);
                    if (stored) {
                        System.out.println(graph.getId() + " was parsed and loaded to the db correctly");
                    } else {
                        System.out.println(graph.getId() + " was parsed but could not be loaded to the db correctly");
                    }
                } catch (GraphValidationException ex) {
                    System.out.println(ex.getMessage());
                } catch (MalformedURLException mue) {
                    return;
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                    return;
                }

            } else if ("load-xml-resource".equals(command)) {
                System.out.println("Enter fileName:");
                String fileName = scanner.nextLine();

                ClassLoader classLoader = App.class.getClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream(fileName);
                try {
                    Graph graph = StaxParser.parse(inputStream);
                    Boolean stored = graphService.store(graph);
                    if (stored) {
                        System.out.println(graph.getId() + " was parsed and loaded to the db correctly");
                    } else {
                        System.out.println(graph.getId() + " was parsed but could not be loaded to the db correctly");
                    }
                } catch (GraphValidationException ex) {
                    System.out.println(ex.getMessage());
                }

            } else if ("exit".equals(command)) {
                exit = true;
            }
        }
    }
}
