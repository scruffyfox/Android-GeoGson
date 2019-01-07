package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

open class Point() : GeoJsonObject()
{
	@field:Json(name = "coordinates")
	@SerializedName("coordinates")
	var coordinates: LngLatAlt = LngLatAlt()

	init { type = "Point" }

	constructor(lng: Double, lat: Double, alt: Double? = null) : this()
	{
		coordinates = LngLatAlt(lng, lat, alt)
	}
}
