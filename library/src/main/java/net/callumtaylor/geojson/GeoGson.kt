package net.callumtaylor.geojson

import com.google.gson.GsonBuilder
import net.callumtaylor.geojson.gson.GeoJsonObjectAdapter
import net.callumtaylor.geojson.gson.LngLatAltAdapter

/**
 * Entrypoint for generating Gson parser with required overrides
 */
object GeoGson
{
	/**
	 * Sets to ignore the case of object types or not
	 */
	@JvmField
	var ignoreTypeCase = false

	/**
	 * Add the required serialization adapters to the Gson builder
	 */
	@JvmStatic
	fun registerAdapters(builder: GsonBuilder): GsonBuilder
	{
		builder.registerTypeAdapter(GeoJsonObject::class.java, GeoJsonObjectAdapter())
		builder.registerTypeAdapter(LngLatAlt::class.java, LngLatAltAdapter())
		return builder
	}

	/**
	 * Checks if a point exists within a polygon
	 */
	@JvmStatic
	fun pointInPolygon(polygon: List<List<LngLatAlt>>, point: Point): Boolean
	{
		var intersections = 0
//		for (coordinate in polygon)
//		{
//			for (i in 1 until coordinate.size)
//			{
//				val v1 = coordinate[i - 1]
//				val v2 = coordinate[i]
//
//				if (point.coordinates == v2)
//				{
//					return true
//				}
//
//				if (v1.latitude == v2.latitude
//					&& v1.latitude == point.coordinates.latitude
//					&& point.coordinates.longitude > (if (v1.longitude > v2.longitude) v2.longitude else v1.longitude)
//					&& point.coordinates.longitude < if (v1.longitude < v2.longitude) v2.longitude else v1.longitude)
//				{
//					// Is horizontal polygon boundary
//					return true
//				}
//
//				if (point.coordinates.latitude > (if (v1.latitude < v2.latitude) v1.latitude else v2.latitude)
//					&& point.coordinates.latitude <= (if (v1.latitude < v2.latitude) v2.latitude else v1.latitude)
//					&& point.coordinates.longitude <= (if (v1.longitude < v2.longitude) v2.longitude else v1.longitude)
//					&& v1.latitude != v2.latitude)
//				{
//					val intersection = (point.coordinates.latitude - v1.latitude) * (v2.longitude - v1.longitude) / (v2.latitude - v1.latitude) + v1.longitude
//
//					if (intersection == point.coordinates.longitude)
//					{
//						// Is other boundary
//						return true
//					}
//
//					if (v1.longitude == v2.longitude || point.coordinates.longitude <= intersection)
//					{
//						intersections++
//					}
//				}
//			}
//		}

		return intersections % 2 != 0
	}
}
