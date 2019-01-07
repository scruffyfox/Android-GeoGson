package net.callumtaylor.geojson.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import net.callumtaylor.geojson.GeoJsonObject
import java.util.HashMap

open class GeoJsonObjectMoshiAdapter() : JsonAdapter<GeoJsonObject>()
{
	companion object
	{
		private val types = mapOf<String, JsonAdapter<*>>(
			"Circle" to CircleJsonAdapter(),
//			"Feature" to Feature::class.java,
//			"FeatureCollection" to FeatureCollection::class.java,
//			"GeometryCollection" to GeometryCollection::class.java,
			"LineString" to LineStringJsonAdapter(),
			"MultiLineString" to MultiLineStringJsonAdapter(),
			"Point" to PointJsonAdapter(),
			"MultiPoint" to MultiPointJsonAdapter()
//			"Polygon" to Polygon::class.java,
//			"MultiPolygon" to MultiPolygon::class.java
		)

		public val OPTIONS = arrayOf("type", "coordinates", "bbox", "properties")
	}

	override fun fromJson(reader: JsonReader): GeoJsonObject?
	{
		var type = ""
		val dataReader = reader.peekJson()
		reader.beginObject()
		read@ while (reader.hasNext())
		{
			when (reader.selectName(JsonReader.Options.of(*OPTIONS)))
			{
				0 -> {
					type = reader.nextString()
				}
				else -> {
					if (reader.peek() == JsonReader.Token.NAME)
					{
						reader.skipName()
					}

					reader.skipValue()
				}
			}
		}

		reader.endObject()
		return types[type]?.fromJson(dataReader) as GeoJsonObject?
	}

	override fun toJson(writer: JsonWriter, value: GeoJsonObject?)
	{
	}

	public fun readDefault(outObj: GeoJsonObject, paramIndex: Int, reader: JsonReader)
	{
		when (paramIndex)
		{
			2 -> outObj.bbox = reader.readJsonValue() as List<Double>?
			3 -> {
				val map = reader.readJsonValue() as Map<String, Any?>?
				outObj.properties = HashMap(map)
			}
			-1 -> {
				outObj.foreign = outObj.foreign ?: HashMap<String, Any?>()

				if (reader.peek() == JsonReader.Token.NAME)
				{
					val name = reader.nextName()
					val value = reader.readJsonValue()
					outObj.foreign!![name] = value
				}
				else
				{
					reader.skipValue()
				}
			}
		}
	}
}
