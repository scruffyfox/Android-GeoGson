package net.callumtaylor.geojson.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import net.callumtaylor.geojson.GeoJsonObject
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
		var defaults = GeoJsonObject()

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
		value.coordinates.forEach { coordinateList ->
			writer.beginArray()
			coordinateList.forEach { coordinate ->
				positionJsonAdapter.toJson(writer, coordinate)
			}
			writer.endArray()
		}
		writer.name("type")
		writer.value(value.type)
		writer.endObject()
	}
}
