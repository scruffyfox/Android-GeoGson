package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

open class Feature() : GeoJsonObject()
{
	@field:Json(name = "geometry")
	@SerializedName("geometry")
	var geometry: GeoJsonObject = GeoJsonObject()

	@field:Json(name = "id")
	@SerializedName("id")
	var id: String? = null

	init { type = "Feature" }
}
