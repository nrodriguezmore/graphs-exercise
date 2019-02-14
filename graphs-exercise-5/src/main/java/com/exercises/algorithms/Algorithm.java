package com.exercises.algorithms;

import com.exercises.model.Graph;
import com.google.gson.JsonObject;

public interface Algorithm {

    JsonObject apply(Graph graph, String from, String to);
}
