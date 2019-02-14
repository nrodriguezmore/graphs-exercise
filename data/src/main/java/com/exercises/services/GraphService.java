package com.exercises.services;


import com.exercises.model.Edge;
import com.exercises.model.Graph;
import com.exercises.model.Node;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class GraphService {

    private String dbUser;
    private String dbPassword;
    private String dbName;
    private String dbUrl;

    private static String create_graph_query = "INSERT INTO graphs(id, name) VALUES(?, ?)";
    private static String create_node_query = "INSERT INTO nodes(id, name, graph_id) VALUES(?, ?, ?)";
    private static String create_edge_query = "INSERT INTO edges(id, nfrom, nto, cost, graph_id) VALUES(?, ?, ?, ?, ?)";

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

    public Boolean store(Graph graph) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.dbUrl + this.dbName, this.dbUser, this.dbPassword);
            connection.setAutoCommit(false);

            PreparedStatement pst_create_graph = connection.prepareStatement(GraphService.create_graph_query);
            PreparedStatement pst_create_node = connection.prepareStatement(GraphService.create_node_query);
            PreparedStatement pst_create_edge = connection.prepareStatement(GraphService.create_edge_query);

            pst_create_graph.setString(1, graph.getId());
            pst_create_graph.setString(2, graph.getName());
            pst_create_graph.executeUpdate();

            for (Node node: graph.getNodes()) {
                pst_create_node.setString(1, node.getId());
                pst_create_node.setString(2, node.getName());
                pst_create_node.setString(3, graph.getId());
                pst_create_node.executeUpdate();
            }

            for (Edge edge: graph.getEdges()) {
                pst_create_edge.setString(1, edge.getId());
                pst_create_edge.setString(2, edge.getFrom());
                pst_create_edge.setString(3, edge.getTo());
                pst_create_edge.setDouble(4, edge.getCost());
                pst_create_edge.setString(5, graph.getId());
                pst_create_edge.executeUpdate();
            }

            connection.commit();
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    return false;
                }
            }
        }

        return true;
    }



}
