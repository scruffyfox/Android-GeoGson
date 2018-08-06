package net.callumtaylor.geojson

open class MultiPolygon : Geometry<List<List<LngLatAlt>>>()
{
	init { type = "MultiPolygon" }

	operator fun contains(point: Point): Boolean = coordinates.any { GeoGson.pointInPolygon(it, point) }
}
