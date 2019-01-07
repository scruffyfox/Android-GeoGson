package net.callumtaylor.geojson

open class MultiPolygon() : Geometry<ArrayList<ArrayList<LngLatAlt>>>()
{
	init { type = "MultiPolygon" }

	operator fun contains(point: Point): Boolean = coordinates.any { GeoGson.pointInPolygon(it, point) }
}
