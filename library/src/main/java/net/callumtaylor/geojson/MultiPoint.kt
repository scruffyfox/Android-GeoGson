package net.callumtaylor.geojson

open class MultiPoint() : Geometry<LngLatAlt>()
{
	init { type = "MultiPoint" }

	constructor(vararg points: LngLatAlt): this()
	{
		this.coordinates = arrayListOf(*points)
	}
}
