package net.callumtaylor.geojson.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import net.callumtaylor.geojson.Circle
import net.callumtaylor.geojson.Feature
import net.callumtaylor.geojson.FeatureCollection
import net.callumtaylor.geojson.GeoGson
import net.callumtaylor.geojson.GeoJsonObject
import net.callumtaylor.geojson.GeometryCollection
import net.callumtaylor.geojson.LineString
import net.callumtaylor.geojson.MultiLineString
import net.callumtaylor.geojson.MultiPoint
import net.callumtaylor.geojson.MultiPolygon
import net.callumtaylor.geojson.Point
import net.callumtaylor.geojson.Polygon
import java.lang.reflect.Type

open class GeoJsonGsonObjectAdapter : JsonSerializer<GeoJsonObject>, JsonDeserializer<GeoJsonObject>
{
	private val types = mapOf(
		"Circle" to Circle::class.java,
		"Feature" to Feature::class.java,
		"FeatureCollection" to FeatureCollection::class.java,
		"GeometryCollection" to GeometryCollection::class.java,
		"LineString" to LineString::class.java,
		"MultiLineString" to MultiLineString::class.java,
		"Point" to Point::class.java,
		"MultiPoint" to MultiPoint::class.java,
		"Polygon" to Polygon::class.java,
		"MultiPolygon" to MultiPolygon::class.java
	)

	override fun serialize(src: GeoJsonObject?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement
	{
		var cls: Class<out GeoJsonObject>? = types[src?.type ?: ""]

		if (cls == null)
		{
			for (it in types)
			{
				if (it.key == src?.type)
				{
					cls = it.value
					break
				}
			}
		}

		val builder = GsonBuilder()
		GeoGson.registerAdapters(builder)

		return builder.create().toJsonTree(src, cls)
	}

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): GeoJsonObject
	{
		val jsonObject = json?.asJsonObject
		var type = jsonObject?.get("type")?.asString
		var cls: Class<out GeoJsonObject>? = types[type ?: ""]

		if (cls == null)
		{
			for (it in types)
			{
				if (it.key == type)
				{
					cls = it.value
					break
				}
			}
		}

		val builder = GsonBuilder()
		GeoGson.registerAdapters(builder)

		val keys = arrayOf("coordinates", "type", "geometry", "bbox", "properties", "radius", "features", "geometries", "id")
		val geoJsonObject = builder.create().fromJson<GeoJsonObject>(json, cls)
		val iterator = jsonObject?.keySet()?.iterator()!!
		while (iterator.hasNext())
		{
			val key = iterator.next()
			if (!keys.contains(key))
			{
				geoJsonObject.foreign = geoJsonObject.foreign ?: hashMapOf()
				geoJsonObject.foreign!![key] = when (jsonObject.get(key)) {
					is JsonPrimitive -> {
						with (jsonObject.get(key).asJsonPrimitive) {
							if (isBoolean) asBoolean
							if (isNumber) asDouble
							if (isString) asString
							else null
						}
					}
					is JsonObject -> {
						builder.create().fromJson(jsonObject.get(key), HashMap::class.java)
					}
					is JsonArray -> {
						builder.create().fromJson(jsonObject.get(key), ArrayList::class.java)
					}
					else -> null
				}
			}
		}

		return geoJsonObject
	}
}
