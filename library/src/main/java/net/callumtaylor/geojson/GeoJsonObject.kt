package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

open class GeoJsonObject : Serializable
{
	@SerializedName("type") var type: String = "GeoJson"
	@SerializedName("crs") var crs: Crs? = null
	@SerializedName("bbox") var bbox: Array<Double>? = null
	@SerializedName("properties") var properties: HashMap<String, Any>? = null

	/**
	 * See @Link [6.1 - Foreign members](https://tools.ietf.org/html/rfc7946#section-6.1)
	 */
	var foreign: HashMap<String, Any>? = null

	fun getProperty(key: String) = properties?.get(key)
	fun setProperty(key: String, value: Any)
	{
		properties = properties ?: HashMap()
		properties!!.put(key, value)
	}

	open fun finishPopulate() {}
}
