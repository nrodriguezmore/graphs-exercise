package com.exercises.parser;

import com.exercises.exceptions.GraphValidationException;
import com.exercises.model.Edge;
import com.exercises.model.Graph;
import com.exercises.model.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

public class StaxParser {

    public static Graph parse(InputStream inputStream) throws GraphValidationException {

        Graph graph = new Graph();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

            while(xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {

                    StartElement startElement = xmlEvent.asStartElement();
                    if(startElement.getName().getLocalPart().equals("id")){
                        xmlEvent = xmlEventReader.nextEvent();
                        graph.setId(xmlEvent.asCharacters().getData());
                    }else if(startElement.getName().getLocalPart().equals("name")){
                        xmlEvent = xmlEventReader.nextEvent();
                        graph.setName(xmlEvent.asCharacters().getData());
                    } else if(startElement.getName().getLocalPart().equals("nodes")){
                        //we need to read all the nodes
                        StaxParser.parseNodes(graph, xmlEventReader);
                        if (graph.getNodes().size() == 0) {
                            throw new GraphValidationException(GraphValidationException.GRAPH_NOT_ENOUGH_NODES);
                        }
                    }else if(startElement.getName().getLocalPart().equals("edges")) {
                        //we need to read all the edges
                        StaxParser.parseEdges(graph, xmlEventReader);
                    }
                }
                //if Employee end element is reached, add employee object to list
                if(xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();
                    if(endElement.getName().getLocalPart().equals("graph")){
                        //we ended reading the graph
                    }
                }
            }

        } catch (XMLStreamException e) {
            e.printStackTrace();

        }
        return graph;
    }

    private static void parseNodes(Graph graph, XMLEventReader xmlEventReader) throws XMLStreamException, GraphValidationException {

        Boolean finishNodes = false;
        while(!finishNodes) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            Node node = null;

            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();

                if(startElement.getName().getLocalPart().equals("node")) {
                    node = StaxParser.parseNode(xmlEventReader);
                    if (node == null) {
                        throw new GraphValidationException(GraphValidationException.INVALID_NODE_FORMAT);
                    }
                    if (graph.existsNode(node)) {
                        throw new GraphValidationException(GraphValidationException.NODE_ALREADY_EXISTS);
                    }
                    graph.addNode(node);
                }

            }

            if (xmlEvent.isEndElement()) {
                EndElement endElement = xmlEvent.asEndElement();

                if (endElement.getName().getLocalPart().equals("nodes")) {
                    finishNodes = true;
                }
            }
        }
    }

    private static Node parseNode(XMLEventReader xmlEventReader) throws XMLStreamException {

        XMLEvent xmlEvent = xmlEventReader.nextEvent();
        String id = null;
        String name = null;
        while (!(xmlEvent.isEndElement() && xmlEvent.asEndElement().getName().getLocalPart().equals("node"))) {  //we read the node while the closing node tag has not arrived
            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("id")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    id = xmlEvent.asCharacters().getData();
                } else if (startElement.getName().getLocalPart().equals("name")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    name = xmlEvent.asCharacters().getData();
                }
            }
            xmlEvent = xmlEventReader.nextEvent();
        }
        Node result = null;
        if (id != null && name != null) {
            result = new Node();
            result.setId(id);
            result.setName(name);
        }
        return result;
    }

    private static void parseEdges(Graph graph, XMLEventReader xmlEventReader) throws XMLStreamException, GraphValidationException {

        Boolean finishEdges = false;
        while(!finishEdges) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            Edge edge = null;

            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();

                if(startElement.getName().getLocalPart().equals("edge")) {
                    edge = StaxParser.parseEdge(xmlEventReader);
                    if (edge == null) {
                        throw new GraphValidationException(GraphValidationException.INVALID_EDGE_FORMAT);
                    }
                    if (!graph.existsNode(edge.getFrom())) {
                        throw new GraphValidationException(GraphValidationException.INVALID_EDGE_FROM_NODE_DOES_NOT_EXIST);
                    }
                    if (!graph.existsNode(edge.getTo())) {
                        throw new GraphValidationException(GraphValidationException.INVALID_EDGE_TO_NODE_DOES_NOT_EXIST);
                    }
                    if (!graph.isValidEdge(edge)) {
                        throw new GraphValidationException(GraphValidationException.INVALID_EDGE);
                    }
                    graph.addEdge(edge);
                }

            }

            if (xmlEvent.isEndElement()) {
                EndElement endElement = xmlEvent.asEndElement();

                if (endElement.getName().getLocalPart().equals("edges")) {
                    finishEdges = true;
                }
            }
        }
    }

    private static Edge parseEdge(XMLEventReader xmlEventReader) throws XMLStreamException, GraphValidationException {

        XMLEvent xmlEvent = xmlEventReader.nextEvent();
        String id = null;
        String from = null;
        String to = null;
        Double cost = 0.0;

        while (!(xmlEvent.isEndElement() && xmlEvent.asEndElement().getName().getLocalPart().equals("edge"))) {  //we read the node while the closing node tag has not arrived
            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("id")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    id = xmlEvent.asCharacters().getData();
                } else if (startElement.getName().getLocalPart().equals("from")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    from = xmlEvent.asCharacters().getData();
                } else if (startElement.getName().getLocalPart().equals("to")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    to = xmlEvent.asCharacters().getData();
                } else if (startElement.getName().getLocalPart().equals("cost")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    try {
                        cost = Double.parseDouble(xmlEvent.asCharacters().getData());
                    } catch (NumberFormatException nfe) {
                        throw new GraphValidationException(GraphValidationException.INVALID_EDGE_COST_NAN);
                    }
                    if (cost < 0.0) {
                        throw new GraphValidationException(GraphValidationException.INVALID_EDGE_COST_NEGATIVE);
                    }
                }
            }
            xmlEvent = xmlEventReader.nextEvent();
        }
        Edge result = null;
        if (id != null && from != null && to != null) {
            result = new Edge();
            result.setId(id);
            result.setFrom(from);
            result.setTo(to);
            result.setCost(cost);
        }
        return result;

    }
}
