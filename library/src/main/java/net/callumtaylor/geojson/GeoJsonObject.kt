package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import java.io.Serializable
import java.util.HashMap

open class GeoJsonObject : Serializable
{
	@field:Json(name = "type")
	@SerializedName("type")
	var type: String = "GeoJson"

	@field:Json(name = "bbox")
	@SerializedName("bbox")
	var bbox: List<Double>? = null

	@field:Json(name = "properties")
	@SerializedName("properties")
	var properties: HashMap<String, Any?>? = null

	/**
	 * See @Link [6.1 - Foreign members](https://tools.ietf.org/html/rfc7946#section-6.1)
	 */
	var foreign: HashMap<String, Any?>? = null
}
