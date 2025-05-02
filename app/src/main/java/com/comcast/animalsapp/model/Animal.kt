package com.comcast.animalsapp.model

data class Animal(
    val name: String,
    val taxonomy: Taxonomy,
    val characteristics: Characteristics,
    val type: String = "" // manually added: "dog", "bird", or "bug"
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