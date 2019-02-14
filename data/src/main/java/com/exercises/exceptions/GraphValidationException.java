package com.exercises.exceptions;

public class GraphValidationException extends Exception {

    public static String NODE_ALREADY_EXISTS = "Node with this id already exists";
    public static String GRAPH_NOT_ENOUGH_NODES = "Graph needs at least one node";
    public static String INVALID_EDGE_FROM_NODE_DOES_NOT_EXIST = "Invalid edge, from node does not exist";
    public static String INVALID_EDGE_TO_NODE_DOES_NOT_EXIST = "Invalid edge, to node does not exist";
    public static String INVALID_NODE_FORMAT = "Invalid node format";
    public static String INVALID_EDGE_FORMAT = "Invalid edge format";
    public static String INVALID_EDGE = "Invalid edge";
    public static String INVALID_EDGE_COST_NAN = "Invalid edge cost, not a number";
    public static String INVALID_EDGE_COST_NEGATIVE = "Invalid edge cost, negative";


    public GraphValidationException(String errorMessage) {
        super(errorMessage);
    }

}
