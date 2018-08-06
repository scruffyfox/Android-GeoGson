package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName

open class Point : GeoJsonObject
{
	@SerializedName("coordinates") var coordinates: LngLatAlt = LngLatAlt()

	init { type = "Point" }

	constructor()
	constructor(lng: Double, lat: Double, alt: Double = Double.NaN)
	{
		coordinates = LngLatAlt(lng, lat, alt)
	}
}
