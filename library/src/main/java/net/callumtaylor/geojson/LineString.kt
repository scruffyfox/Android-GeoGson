package net.callumtaylor.geojson

open class LineString : MultiPoint
{
	init { type = "LineString" }

	constructor()
	constructor(vararg points: LngLatAlt): super(*points)
}
