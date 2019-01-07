package net.callumtaylor.geojson

open class LineString() : MultiPoint()
{
	init { type = "LineString" }

	constructor(vararg points: LngLatAlt): this()
	{
		this.coordinates = arrayListOf(*points)
	}
}
