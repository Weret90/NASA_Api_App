package com.umbrella.nasaapiapp.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MeteorsDTO(
    @SerializedName("near_earth_objects")
    val meteors: List<Meteor>
)

data class Meteor(
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproachData>,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: EstimatedDiameter,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_limited")
    val nameLimited: String
) : Serializable

data class CloseApproachData(
    @SerializedName("close_approach_date")
    val closeApproachDate: String,
    @SerializedName("close_approach_date_full")
    val closeApproachDateFull: String,
    @SerializedName("miss_distance")
    val missDistance: MissDistance
)

data class MissDistance(
    @SerializedName("kilometers")
    val kilometers: String
)

data class EstimatedDiameter(
    @SerializedName("kilometers")
    val kilometers: Kilometers
)

data class Kilometers(
    @SerializedName("estimated_diameter_max")
    val estimatedDiameterMax: Double,
    @SerializedName("estimated_diameter_min")
    val estimatedDiameterMin: Double
)

