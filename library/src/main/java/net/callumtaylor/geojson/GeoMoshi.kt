package net.callumtaylor.geojson

import com.squareup.moshi.Moshi
import net.callumtaylor.geojson.moshi.CircleJsonAdapter
import net.callumtaylor.geojson.moshi.GeoJsonObjectMoshiAdapter
import net.callumtaylor.geojson.moshi.LngLatAltMoshiAdapter
import net.callumtaylor.geojson.moshi.PointJsonAdapter

/**
 * Entrypoint for generating Moshi parser with required overrides
 */
object GeoMoshi
{
	/**
	 * Add the required serialization adapters to the Moshi builder
	 */
	@JvmStatic
	fun registerAdapters(builder: Moshi.Builder): Moshi.Builder
	{
		builder.add(Point::class.java, PointJsonAdapter())
		builder.add(Circle::class.java, CircleJsonAdapter())
		builder.add(GeoJsonObject::class.java, GeoJsonObjectMoshiAdapter())
		builder.add(LngLatAlt::class.java, LngLatAltMoshiAdapter())
		return builder
	}
}
