package net.callumtaylor.geojson.moshi

import com.squareup.moshi.*
import net.callumtaylor.geojson.LngLatAlt
import net.callumtaylor.geojson.Point
import net.callumtaylor.geojson.moshi.GeoJsonObjectMoshiAdapter.Companion.OPTIONS

class PointJsonAdapter : JsonAdapter<Point>()
{
	private val defaultAdapter = GeoJsonObjectMoshiAdapter()
	private val positionJsonAdapter = LngLatAltMoshiAdapter()

	@FromJson
	override fun fromJson(reader: JsonReader): Point
	{
		val point = Point()
		var type = ""
		var position: LngLatAlt? = null

		reader.beginObject()
		while (reader.hasNext())
		{
			when (val index = reader.selectName(JsonReader.Options.of(*OPTIONS)))
			{
				0 -> type = reader.nextString()
				1 -> position = positionJsonAdapter.fromJson(reader)
				else -> {
					defaultAdapter.readDefault(point, index, reader)
				}
			}
		}
		reader.endObject()

		if (position == null)
		{
			throw JsonDataException("Required positions are missing at ${reader.path}")
		}

		if (type != "Point")
		{
			throw JsonDataException("Required type is not a Point at ${reader.path}")
		}

		point.coordinates = position
		return point
	}

	@ToJson
	override fun toJson(writer: JsonWriter, value: Point?)
	{
		if (value == null)
		{
			throw NullPointerException("Point was null! Wrap in .nullSafe() to write nullable values.")
		}

		writer.beginObject()
		writer.name("coordinates")
		positionJsonAdapter.toJson(writer, value.coordinates)
		writer.name("type")
		writer.value(value.type)
		writer.endObject()
	}
}
