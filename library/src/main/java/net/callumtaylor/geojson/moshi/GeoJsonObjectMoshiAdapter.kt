package net.callumtaylor.geojson.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import net.callumtaylor.geojson.Circle
import net.callumtaylor.geojson.Feature
import net.callumtaylor.geojson.FeatureCollection
import net.callumtaylor.geojson.GeoJsonObject
import net.callumtaylor.geojson.GeometryCollection
import net.callumtaylor.geojson.LineString
import net.callumtaylor.geojson.MultiLineString
import net.callumtaylor.geojson.MultiPoint
import net.callumtaylor.geojson.MultiPolygon
import net.callumtaylor.geojson.Point
import net.callumtaylor.geojson.Polygon
import java.util.HashMap

open class GeoJsonObjectMoshiAdapter() : JsonAdapter<GeoJsonObject>()
{
	companion object
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

		public val OPTIONS: JsonReader.Options = JsonReader.Options.of("type", "coordinates", "bbox", "properties", "foreign")
	}

	override fun fromJson(reader: JsonReader): GeoJsonObject?
	{
//		var properties: Map<*, *>? = null
//		var foreign: HashMap<String, Any?> = hashMapOf<String, Any?>()
//		var bbox: Array<Double>? = null
//
//		reader.beginObject()
//		while (reader.hasNext())
//		{
//			when (reader.selectName(OPTIONS))
//			{
//				0, 1 -> {
//					reader.skipName()
//					reader.skipValue()
//				}
//				2 -> bbox = reader.readJsonValue() as Array<Double>?
//				3 -> properties = reader.readJsonValue() as Map<*, *>?
//				-1 -> {
//					val name = reader.nextName()
//					val value = reader.readJsonValue()
//					foreign[name] = value
//				}
//			}
//		}
//
//		reader.endObject()

		val geoObj: GeoJsonObject = GeoJsonObject()
//		geoObj.properties = properties as HashMap<*, *>?
//		geoObj.foreign = if (foreign.isEmpty()) null else foreign
//		geoObj.bbox = bbox

		return geoObj
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
				val name = reader.nextName()
				val value = reader.readJsonValue()
				outObj.foreign!![name] = value
			}
		}
	}
}
