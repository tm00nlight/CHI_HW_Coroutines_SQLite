package com.tm00nlight.chi_hw_7_network.data

data class Animal(
    val name: String,
    val latin_name: String,
    val animal_type: String,
    val image_link: String,
) {
    companion object {
        const val TABLE = "Animals"
        const val ID = "id"
        const val NAME = "name"
        const val LATIN_NAME = "latin_name"
        const val ANIMAL_TYPE = "animal_type"
        const val IMAGE_LINK = "image_link"
    }
}