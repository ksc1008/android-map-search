package ksc.campus.tech.kakao.map.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeywordSearchResponse(
    val meta: Meta,
    val documents: List<Document>
)

@Serializable
data class Meta(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("pageable_count")
    val pageableCount: Int,
    @SerialName("is_end")
    val isEnd: Boolean,
    @SerialName("same_name")
    val sameName: SameName
)

@Serializable
data class SameName(
    @SerialName("region")
    val region: List<String>,
    @SerialName("keyword")
    val keyword: String,
    @SerialName("selected_region")
    val selectedRegion: String
)

@Serializable
data class Document(
    @SerialName("id")
    val id: String,
    @SerialName("place_name")
    val placeName: String,
    @SerialName("category_name")
    val categoryName: String,
    @SerialName("category_group_code")
    val categoryGroupCode: String,
    @SerialName("category_group_name")
    val categoryGroupName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("address_name")
    val addressName: String,
    @SerialName("road_address_name")
    val roadAddressName: String,
    @SerialName("x")
    val x: String,
    @SerialName("y")
    val y: String,
    @SerialName("place_url")
    val placeUrl: String,
    @SerialName("distance")
    val distance: String
)