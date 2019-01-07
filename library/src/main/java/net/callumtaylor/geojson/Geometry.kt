package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import java.util.ArrayList

open class Geometry<T>() : GeoJsonObject()
{
	@field:Json(name = "coordinates")
	@SerializedName("coordinates")
	public var coordinates: ArrayList<T> = arrayListOf<T>()

	init { type = "Geometry" }

	constructor(vararg elements: T) : this()
	{
		elements.forEach { coordinates.add(it) }
	}
}
