package net.callumtaylor.geojson.test

import com.google.gson.GsonBuilder
import net.callumtaylor.geojson.*
import org.junit.Assert
import org.junit.Test

class GeometryCollectionTest
{
	private val mapper = GeoGson.registerAdapters(GsonBuilder()).create()

	@Test
	@Throws(Exception::class)
	fun itShouldSerialize()
	{
		var gc = GeometryCollection()
		gc.geometries.add(Point(100.0, 0.0, Double.NaN))
		gc.geometries.add(LineString(LngLatAlt(101.0, 0.0, Double.NaN), LngLatAlt(102.0, 1.0, Double.NaN)))

		val expected = ("{\"geometries\":[{\"coordinates\":[100.0,0.0],\"type\":\"Point\"},"
			+ "{\"coordinates\":[[101.0,0.0],[102.0,1.0]],\"type\":\"LineString\"}],"
			+ "\"type\":\"GeometryCollection\"}")

		val generated = mapper.toJson(gc)

		Assert.assertEquals(expected, generated)
	}
}
