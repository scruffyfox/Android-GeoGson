package net.callumtaylor.geojson.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import net.callumtaylor.geojson.GeoJsonObject
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
		var defaults = GeoJsonObject()

		reader.beginObject()
		while (reader.hasNext())
		{
			when (val index = reader.selectName(OPTIONS))
			{
				0 -> type = reader.nextString()
				1 -> position = positionJsonAdapter.fromJson(reader)
				-1, 2, 3, 4, 5 -> {
					defaultAdapter.readDefault(point, index, reader)
				}
				else -> {
					reader.skipName()
					reader.skipValue()
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
