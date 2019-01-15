package net.callumtaylor.geojson

import com.google.gson.GsonBuilder
import net.callumtaylor.geojson.gson.FeatureGsonAdapter
import net.callumtaylor.geojson.gson.GeoJsonGsonObjectAdapter
import net.callumtaylor.geojson.gson.LngLatAltGsonAdapter

/**
 * Entrypoint for generating Gson parser with required overrides
 */
object GeoGson
{
	/**
	 * Add the required serialization adapters to the Gson builder
	 */
	@JvmStatic
	fun registerAdapters(builder: GsonBuilder): GsonBuilder
	{
		builder.registerTypeAdapter(Feature::class.java, FeatureGsonAdapter())
		builder.registerTypeAdapter(GeoJsonObject::class.java, GeoJsonGsonObjectAdapter())
		builder.registerTypeAdapter(LngLatAlt::class.java, LngLatAltGsonAdapter())
		return builder
	}
}
