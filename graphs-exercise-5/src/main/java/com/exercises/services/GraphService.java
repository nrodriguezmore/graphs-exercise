package com.exercises.services;

import com.exercises.model.Adjacent;
import com.exercises.model.Graph;
import com.exercises.model.Node;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class GraphService {

    private String dbUser;
    private String dbPassword;
    private String dbName;
    private String dbUrl;

    private static String get_graph = "SELECT * from graphs where id = ?";
    private static String get_nodes = "SELECT * from nodes where graph_id = ?";
    private static String get_edges = "SELECT * from edges where graph_id = ?";

    public GraphService() throws IOException {

        Properties prop = new Properties();
        InputStream input = null;

        String filename = "dbconfig.properties";
        input = this.getClass().getClassLoader().getResourceAsStream(filename);

        //load a properties file from class path, inside static method
        prop.load(input);

        //get the property value and print it out
        this.dbName = prop.getProperty("dbname");
        this.dbPassword = prop.getProperty("dbpassword");
        this.dbUser = prop.getProperty("dbuser");
        this.dbUrl = prop.getProperty("dburl");

    }

    public Graph load(String graph_id) {
        Connection connection = null;
        Graph result = null;
        try {
            connection = DriverManager.getConnection(this.dbUrl + this.dbName, this.dbUser, this.dbPassword);

            PreparedStatement pst_get_graph = connection.prepareStatement(GraphService.get_graph);
            PreparedStatement pst_get_nodes = connection.prepareStatement(GraphService.get_nodes);
            PreparedStatement pst_get_edges = connection.prepareStatement(GraphService.get_edges);

            pst_get_graph.setString(1, graph_id);
            pst_get_nodes.setString(1, graph_id);
            pst_get_edges.setString(1, graph_id);

            ResultSet rs_graph = pst_get_graph.executeQuery();
            ResultSet rs_nodes = pst_get_nodes.executeQuery();
            ResultSet rs_edges = pst_get_edges.executeQuery();

            result = new Graph();
            if (rs_graph.next()) {
                String graph_name = rs_graph.getString("name");
                result.setName(graph_name);
            }
            result.setId(graph_id);

            while (rs_nodes.next()) {
                String node_id = rs_nodes.getString("id");
                String node_name = rs_nodes.getString("name");

                Node node = new Node();
                node.setName(node_name);
                node.setId(node_id);

                result.addNode(node);
            }

            while (rs_edges.next()) {
                String from = rs_edges.getString("nfrom");
                String to = rs_edges.getString("nto");
                Double cost = rs_edges.getDouble("cost");
                result.addEdge(from, to, cost);
            }
            connection.close();

        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    return null;
                }
            }
        }
        return result;
    }



}
