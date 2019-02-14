package com.exercises.model;

import com.exercises.exceptions.GraphValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Graph {

    private String id;
    private String name;

    private HashMap<String, Node> nodesMap;
    private HashMap<String, Edge> edgesMap;
    private ArrayList<Edge> edges;

    public Graph(){

        nodesMap = new HashMap<>();
        edgesMap = new HashMap<>();
        edges = new ArrayList<>();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNode(Node node) throws GraphValidationException {
        if (node == null) {
            return;
        }
        if (!existsNode(node)) {
            nodesMap.put(node.getId(), node);
        }
    }

    public Node getNode(String id) {
        return nodesMap.get(id);
    }

    public Collection<Node> getNodes() {
        return nodesMap.values();
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Boolean existsNode(Node node) {
        return (nodesMap.get(node.getId()) != null);
    }
    public Boolean existsNode(String nodeId) {
        return (nodesMap.get(nodeId) != null);
    }

    public void addEdge(Edge edge) {
        if (isValidEdge(edge)) {
            edges.add(edge);
            edgesMap.put(getEdgeKey(edge), edge);
        }
    }

    public Boolean isValidEdge(Edge edge) {
        if (edge == null) {
            return false;
        }
        if (!existsNode(edge.from) ||  !existsNode(edge.to)) {
            return false;
        }
        return !existsEdge(edge);
    }

    private Boolean existsEdge(Edge edge) {
        return (edgesMap.get(getEdgeKey(edge)) != null);
    }


    private String getEdgeKey(Edge edge) {
        return edge.getFrom() + "|" + edge.getTo();
    }
}
