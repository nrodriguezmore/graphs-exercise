package com.exercises.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Graph {

    private String id;
    private String name;

    private HashMap<String, Node> nodesMap;
    private HashMap<String, ArrayList<Adjacent> > adjacentsMap;


    public Graph(){

        nodesMap = new HashMap<>();
        adjacentsMap = new HashMap<>();

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

    public void addNode(Node node) {
        if (node == null) {
            return;
        }
        if (!existsNode(node)) {
            nodesMap.put(node.getId(), node);
            ArrayList<Adjacent> adjacents = new ArrayList<Adjacent>();
            adjacentsMap.put(node.getId(), adjacents);
        }
    }

    public Node getNode(String id) {
        return nodesMap.get(id);
    }

    public Collection<Node> getNodes() {
        return nodesMap.values();
    }

    public ArrayList<Adjacent> getAdjacents(Node node) {
        return adjacentsMap.get(node.getId());
    }

    public Adjacent getAdjacent(Node from, Node to) {
        Adjacent result = null;
        Iterator<Adjacent> iterator = getAdjacents(from).iterator();
        while (iterator.hasNext() && result == null) {
            Adjacent current = iterator.next();
            if (current.node.getId().equals(to.getId())) {
                result = current;
            }
        }
        return result;
    }

    public Boolean existsNode(Node node) {
        return (nodesMap.get(node.getId()) != null);
    }
    public Boolean existsNode(String nodeId) {
        return (nodesMap.get(nodeId) != null);
    }

    public void addEdge(String from, String to, Double cost) {
        Node nfrom = getNode(from);
        Node nto = getNode(to);
        Adjacent adjacent = new Adjacent();
        adjacent.node = nto;
        adjacent.cost = cost;
        adjacentsMap.get(nfrom.getId()).add(adjacent);
    }

    public void addEdge(Node from, Node to, Double cost) {
        Adjacent adjacent = new Adjacent();
        adjacent.node = to;
        adjacent.cost = cost;
        adjacentsMap.get(from.getId()).add(adjacent);
    }

}
