package net.callumtaylor.geojson

import java.util.*

open class Geometry<T> : GeoJsonObject
{
	protected val coordinates: ArrayList<T> = arrayListOf<T>()

	init { type = "Geometry" }

	constructor()
	constructor(vararg elements: T)
	{
		elements.forEach { coordinates.add(it) }
	}
}
