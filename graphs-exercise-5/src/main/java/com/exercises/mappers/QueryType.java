package com.exercises.mappers;

import com.google.gson.annotations.SerializedName;

public enum QueryType {

    @SerializedName("cheapest")
    CHEAPEST,
    @SerializedName("paths")
    PATHS
}
