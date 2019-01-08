package net.callumtaylor.geojson.moshi

import com.squareup.moshi.*
import net.callumtaylor.geojson.Feature
import net.callumtaylor.geojson.FeatureCollection
import net.callumtaylor.geojson.moshi.GeoJsonObjectMoshiAdapter.Companion.OPTIONS

class FeatureCollectionJsonAdapter : JsonAdapter<FeatureCollection>()
{
	private val defaultAdapter = GeoJsonObjectMoshiAdapter()
	private val feaureAdapter = FeatureJsonAdapter()

	@FromJson
	override fun fromJson(reader: JsonReader): FeatureCollection
	{
		val feature = FeatureCollection()
		var type = ""

		reader.beginObject()
		while (reader.hasNext())
		{
			when (val index = reader.selectName(JsonReader.Options.of(*OPTIONS, "features")))
			{
				0 -> type = reader.nextString()
				1 -> {
					reader.skipName()
					reader.skipValue()
				}
				OPTIONS.size -> {
					val geometryJson = reader.peekJson()
					geometryJson.beginArray()
					val features = arrayListOf<Feature>()
					while (geometryJson.hasNext())
					{
						features.add(feaureAdapter.fromJson(geometryJson))
					}
					geometryJson.endArray()
					reader.skipValue()

					feature.features = features
				}
				else -> {
					defaultAdapter.readDefault(feature, index, reader)
				}
			}
		}
		reader.endObject()

		if (type != "FeatureCollection")
		{
			throw JsonDataException("Required type is not a FeatureCollection at ${reader.path}")
		}

		return feature
	}

	@ToJson
	override fun toJson(writer: JsonWriter, value: FeatureCollection?)
	{
		if (value == null)
		{
			throw NullPointerException("FeatureCollection was null! Wrap in .nullSafe() to write nullable values.")
		}

		writer.beginObject()
		writer.name("type")
		writer.value(value.type)
		writer.endObject()
	}
}
