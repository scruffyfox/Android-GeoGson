package net.callumtaylor.geojson.gson

import com.google.gson.*
import net.callumtaylor.geojson.*
import java.lang.reflect.Type

open class GeoJsonObjectAdapter : JsonSerializer<GeoJsonObject>, JsonDeserializer<GeoJsonObject>
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

		if (GeoGson.ignoreTypeCase && cls == null)
		{
			for (it in types)
			{
				if (it.key.equals(src?.type, true))
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

		if (GeoGson.ignoreTypeCase && cls == null)
		{
			for (it in types)
			{
				if (it.key.equals(type, true))
				{
					cls = it.value
					break
				}
			}
		}

		val builder = GsonBuilder()
		GeoGson.registerAdapters(builder)

		val geoObject = builder.create().fromJson(json, cls)
		geoObject.finishPopulate()

		return geoObject
	}
}
