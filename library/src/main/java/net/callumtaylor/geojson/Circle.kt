package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

open class Circle() : Point()
{
	@field:Json(name = "radius")
	@SerializedName("radius")
	var radius: Double = 0.0

	init { type = "Circle" }

	constructor(lng: Double, lat: Double, alt: Double? = null, radius: Double = 0.0) : this()
	{
		this.coordinates = LngLatAlt(lng, lat, alt)
		this.radius = radius
	}
}
