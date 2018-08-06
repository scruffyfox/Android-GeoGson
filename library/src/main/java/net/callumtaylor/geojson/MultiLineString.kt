package net.callumtaylor.geojson

open class MultiLineString : Geometry<List<LngLatAlt>>
{
	init { type = "MultiLineString" }

	constructor()
	constructor(vararg line: List<LngLatAlt>): super(*line)
}
