package com.comcast.animalsapp.model

data class Animal(
    val name: String,
    val taxonomy: Taxonomy,
    val characteristics: Characteristics,

    // App-specific field used to tag the animal category ("dog", "bird", "bug").
    // Not part of the API response; manually injected for filtering/grouping in UI.

    val type: String = ""
)

data class Taxonomy(
    val phylum: String,
    val scientific_name: String
)

data class Characteristics(
    val common_name: String = "",
    val slogan: String? = null,
    val lifespan: String? = null,
    val wingspan: String? = null,
    val habitat: String? = null,
    val prey: String? = null,
    val predators: String? = null
)