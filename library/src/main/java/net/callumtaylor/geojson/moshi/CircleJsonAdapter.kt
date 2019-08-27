package net.callumtaylor.geojson.moshi

import com.squareup.moshi.*
import net.callumtaylor.geojson.Circle
import net.callumtaylor.geojson.LngLatAlt
import net.callumtaylor.geojson.moshi.GeoJsonObjectMoshiAdapter.Companion.OPTIONS

class CircleJsonAdapter : JsonAdapter<Circle>()
{
	private val defaultAdapter = GeoJsonObjectMoshiAdapter()
	private val positionJsonAdapter = LngLatAltMoshiAdapter()

	@FromJson
	override fun fromJson(reader: JsonReader): Circle
	{
		val circle = Circle()
		var type = ""
		var position: LngLatAlt? = null
		var radius: Double? = null

		reader.beginObject()
		while (reader.hasNext())
		{
			when (val index = reader.selectName(JsonReader.Options.of(*OPTIONS, "radius")))
			{
				0 -> type = reader.nextString()
				1 -> position = positionJsonAdapter.fromJson(reader)
				OPTIONS.size -> radius = reader.nextDouble()
				else -> {
					defaultAdapter.readDefault(circle, index, reader)
				}
			}
		}
		reader.endObject()

		if (position == null)
		{
			throw JsonDataException("Required positions are missing at ${reader.path}")
		}

		if (radius == null)
		{
			throw JsonDataException("Required radius is missing at ${reader.path}")
		}

		if (type != "Circle")
		{
			throw JsonDataException("Required type is not a Circle at ${reader.path}")
		}

		circle.coordinates = position
		circle.radius = radius
		return circle
	}

	@ToJson
	override fun toJson(writer: JsonWriter, value: Circle?)
	{
		if (value == null)
		{
			throw NullPointerException("Circle was null! Wrap in .nullSafe() to write nullable values.")
		}

		writer.beginObject()
		writer.name("coordinates")
		positionJsonAdapter.toJson(writer, value.coordinates)
		writer.name("radius")
		writer.value(value.radius)
		defaultAdapter.writeDefault(value, writer)
		writer.endObject()
	}
}
