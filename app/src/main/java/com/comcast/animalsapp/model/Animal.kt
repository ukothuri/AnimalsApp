package com.comcast.animalsapp.model

data class Animal(
    val name: String,
    val taxonomy: Taxonomy,
    val characteristics: Characteristics
)

data class Taxonomy(
    val phylum: String,
    val scientific_name: String
)

data class Characteristics(
    val common_name: String,
    val slogan: String?,
    val lifespan: String?,
    val wingspan: String?,
    val habitat: String?,
    val prey: String?,
    val predators: String?
)