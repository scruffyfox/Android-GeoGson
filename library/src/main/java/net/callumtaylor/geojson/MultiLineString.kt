package net.callumtaylor.geojson

open class MultiLineString() : Geometry<ArrayList<LngLatAlt>>()
{
	init { type = "MultiLineString" }

	constructor(vararg line: ArrayList<LngLatAlt>): this()
	{
		this.coordinates = arrayListOf(*line)
	}
}
