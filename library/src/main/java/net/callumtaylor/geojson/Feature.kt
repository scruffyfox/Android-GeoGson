package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName

open class Feature : GeoJsonObject()
{
	@SerializedName("geometry") var geometry: GeoJsonObject = GeoJsonObject()
	@SerializedName("id") var id: String = ""

	init { type = "Feature" }
}
