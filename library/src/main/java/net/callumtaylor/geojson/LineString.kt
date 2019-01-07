package net.callumtaylor.geojson

import com.squareup.moshi.JsonClass

open class LineString() : MultiPoint()
{
	init { type = "LineString" }

	constructor(vararg points: LngLatAlt): this()
	{
		this.coordinates = arrayListOf(*points)
	}
}
