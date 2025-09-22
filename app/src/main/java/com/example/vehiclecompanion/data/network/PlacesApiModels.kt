package com.example.vehiclecompanion.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverResponse(
    @SerialName("pois")
    val places: List<DiscoverPlace>
)

@Serializable
data class DiscoverPlace(
    val id: Int,
    val name: String?,
    val url: String?,
    val rating: Double?,
    @SerialName("primary_category_display_name")
    val categoryDisplayName: String?,
    @SerialName("v_320x320_url")
    val imageUrl: String?,
    val loc: DoubleArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiscoverPlace

        if (id != other.id) return false
        if (rating != other.rating) return false
        if (name != other.name) return false
        if (url != other.url) return false
        if (categoryDisplayName != other.categoryDisplayName) return false
        if (imageUrl != other.imageUrl) return false
        if (!loc.contentEquals(other.loc)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (categoryDisplayName?.hashCode() ?: 0)
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (loc?.contentHashCode() ?: 0)
        return result
    }
}

@Serializable
data class Place(
    val id: Int,
    val name: String?,
    val url: String?,
    @SerialName("average_rating")
    val rating: Double?,
    @SerialName("primary_category")
    val category: Category?,
    val image: Image?,
)

@Serializable
data class Category(
    @SerialName("display_name")
    val displayName: String
)

@Serializable
data class Image(
    @SerialName("v_320x320_url")
    val url: String?
)
