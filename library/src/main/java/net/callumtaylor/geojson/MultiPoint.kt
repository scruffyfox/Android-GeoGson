package net.callumtaylor.geojson

open class MultiPoint : Geometry<LngLatAlt>
{
	init { type = "MultiPoint" }

	constructor()
	constructor(vararg points: LngLatAlt): super(*points)
}
