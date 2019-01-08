package net.callumtaylor.geojson.moshi

import com.squareup.moshi.*
import net.callumtaylor.geojson.LngLatAlt
import net.callumtaylor.geojson.Polygon
import net.callumtaylor.geojson.moshi.GeoJsonObjectMoshiAdapter.Companion.OPTIONS

class PolygonJsonAdapter : JsonAdapter<Polygon>()
{
	private val defaultAdapter = GeoJsonObjectMoshiAdapter()
	private val positionJsonAdapter = LngLatAltMoshiAdapter()

	@FromJson
	override fun fromJson(reader: JsonReader): Polygon
	{
		val point = Polygon()
		var type = ""
		var position: ArrayList<ArrayList<LngLatAlt>>? = arrayListOf()

		reader.beginObject()
		while (reader.hasNext())
		{
			when (val index = reader.selectName(JsonReader.Options.of(*OPTIONS)))
			{
				0 -> type = reader.nextString()
				1 -> {
					position = arrayListOf()
					reader.beginArray()
					while (reader.hasNext())
					{
						reader.beginArray()
						val polyLine = arrayListOf<LngLatAlt>()
						while (reader.hasNext())
						{
							polyLine.add(positionJsonAdapter.fromJson(reader)!!)
						}
						position.add(polyLine)
						reader.endArray()
					}
					reader.endArray()
				}
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

		if (type != "Polygon")
		{
			throw JsonDataException("Required type is not a Polygon at ${reader.path}")
		}

		point.coordinates = position
		return point
	}

	@ToJson
	override fun toJson(writer: JsonWriter, value: Polygon?)
	{
		if (value == null)
		{
			throw NullPointerException("Polygon was null! Wrap in .nullSafe() to write nullable values.")
		}

		writer.beginObject()
		writer.name("coordinates")
		writer.beginArray()
		value.coordinates.forEach { polyRing ->
			writer.beginArray()
			polyRing.forEach { coordinate ->
				positionJsonAdapter.toJson(writer, coordinate)
			}
			writer.endArray()
		}
		writer.endArray()
		defaultAdapter.writeDefault(value, writer)
		writer.endObject()
	}
}
