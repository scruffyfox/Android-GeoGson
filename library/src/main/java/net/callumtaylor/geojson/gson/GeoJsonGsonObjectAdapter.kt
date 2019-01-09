package net.callumtaylor.geojson.gson

import com.google.gson.*
import net.callumtaylor.geojson.*
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

		return builder.create().fromJson<GeoJsonObject>(json, cls)
	}
}
