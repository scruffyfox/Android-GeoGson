package net.callumtaylor.geojson

import java.util.*

open class Polygon : Geometry<List<LngLatAlt>>
{
	init { type = "Polygon" }

	constructor()
	constructor(vararg polygon: List<LngLatAlt>): super(*polygon)

	fun getExteriorRing(): List<LngLatAlt>
	{
		assertExteriorRing()
		return coordinates[0]
	}

	fun getInteriorRings(): List<List<LngLatAlt>>
	{
		assertExteriorRing()
		return coordinates.subList(1, coordinates.size)
	}

	fun getInteriorRing(index: Int): List<LngLatAlt>
	{
		assertExteriorRing()
		return coordinates[1 + index]
	}

	fun addInteriorRing(points: List<LngLatAlt>)
	{
		assertExteriorRing()
		coordinates.add(points)
	}

	fun addInteriorRing(vararg points: LngLatAlt)
	{
		assertExteriorRing()
		coordinates.add(Arrays.asList(*points))
	}

	private fun assertExteriorRing()
	{
		if (coordinates.isEmpty())
		{
			throw RuntimeException("No exterior ring defined")
		}
	}

	operator fun contains(point: Point): Boolean = GeoGson.pointInPolygon(coordinates, point)
}
