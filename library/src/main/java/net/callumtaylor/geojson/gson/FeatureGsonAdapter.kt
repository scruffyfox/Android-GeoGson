package net.callumtaylor.geojson.gson

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import net.callumtaylor.geojson.*
import java.lang.reflect.Type

open class FeatureGsonAdapter : JsonSerializer<GeoJsonObject>, JsonDeserializer<GeoJsonObject>
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
		val builder = GsonBuilder()
		GeoGson.registerAdapters(builder)

		if (src is Feature)
		{
			val gson = builder.create()
			val geometry = gson.toJsonTree(src.geometry, types[src.geometry.type])
			val jsonTree = JsonObject()

			jsonTree.addProperty("type", "Feature")
			jsonTree.add("geometry", geometry)

			src.properties?.let {
				jsonTree.add("properties", gson.toJsonTree(it))
			}

			src.foreign?.let {
				it.forEach { k, v ->
					jsonTree.add(k, gson.toJsonTree(v))
				}
			}

			src.id?.let {
				jsonTree.addProperty("id", it)
			}

			return jsonTree
		}
		else
		{
			return builder.create().toJsonTree(src, typeOfSrc)
		}
	}

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): GeoJsonObject
	{
		val jsonObject = json?.asJsonObject
		var type = jsonObject?.get("type")?.asString

		val builder = GsonBuilder()
		GeoGson.registerAdapters(builder)

		val feature = Feature()
		val keys = arrayOf("coordinates", "type", "geometry", "bbox", "properties", "radius", "features", "geometries", "id")
		val geometry = builder.create().fromJson<GeoJsonObject>(jsonObject!!.get("geometry"), GeoJsonObject::class.java)
		feature.geometry = geometry

		if (jsonObject.has("properties"))
		{
			val properties = builder.create().fromJson<HashMap<String, Any?>>(jsonObject.get("properties"), object : TypeToken<HashMap<String, Any?>>(){}.type)
			feature.properties = properties as java.util.HashMap<String, Any?>?
		}

		if (jsonObject.has("id"))
		{
			feature.id = jsonObject.get("id").asString
		}

		if (jsonObject.has("bbox"))
		{
			feature.bbox = builder.create().fromJson<ArrayList<Double>>(jsonObject.get("bbox"), object : TypeToken<ArrayList<Double>>(){}.type)
		}

		val iterator = jsonObject.keySet()?.iterator()!!
		while (iterator.hasNext())
		{
			val key = iterator.next()
			if (!keys.contains(key))
			{
				feature.foreign = feature.foreign ?: hashMapOf()
				feature.foreign!![key] = when (jsonObject.get(key)) {
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

		return feature
	}
}
