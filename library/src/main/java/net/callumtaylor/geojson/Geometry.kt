package net.callumtaylor.geojson

import java.util.ArrayList

open class Geometry<T> : GeoJsonObject
{
	public val coordinates: ArrayList<T> = arrayListOf<T>()

	init { type = "Geometry" }

	constructor()
	constructor(vararg elements: T)
	{
		elements.forEach { coordinates.add(it) }
	}
}
