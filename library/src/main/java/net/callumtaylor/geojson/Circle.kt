package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName

open class Circle : Point
{
	@SerializedName("radius") var radius: Double = 0.0

	init { type = "Circle" }

	constructor()
	constructor(lng: Double, lat: Double, alt: Double = Double.NaN, radius: Double = 0.0) : super(lng, lat, alt)
	{
		this.radius = radius
	}
}
