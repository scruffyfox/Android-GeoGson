package net.callumtaylor.geojson.test

import com.google.gson.GsonBuilder
import net.callumtaylor.geojson.*
import org.junit.Assert
import org.junit.Test

public class GsonTests
{
	private val gson = GeoGson.registerAdapters(GsonBuilder()).create()

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeAPoint()
	{
		val point = Point(100.0, 0.0)
		Assert.assertEquals("{\"coordinates\":[100.0,0.0],\"type\":\"Point\"}", gson.toJson(point))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeAPoint()
	{
		val value = gson.fromJson("{\"type\":\"Point\",\"coordinates\":[100.0,5.0]}", GeoJsonObject::class.java)
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Point)
		val point = value as Point
		assertLngLatAlt(100.0, 5.0, java.lang.Double.NaN, point.coordinates)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeAPointWithAltitude()
	{
		val value = gson.fromJson("{\"coordinates\":[100.0,5.0,123],\"type\":\"Point\"}", GeoJsonObject::class.java)
		val point = value as Point
		assertLngLatAlt(100.0, 5.0, 123.0, point.coordinates)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeAPointWithAltitude()
	{
		val point = Point(100.0, 0.0, 256.0)
		Assert.assertEquals("{\"coordinates\":[100.0,0.0,256.0],\"type\":\"Point\"}", gson.toJson(point))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeLineString()
	{
		val lineString = LineString(LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 1.0))
		Assert.assertEquals("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}", gson.toJson(lineString))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeLineString()
	{
		val lineString = gson.fromJson("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}", LineString::class.java)
		Assert.assertNotNull(lineString)
		Assert.assertTrue(lineString is LineString)

		with (lineString as LineString) {
			val coordinates = lineString.coordinates
			assertLngLatAlt(100.0, 0.0, Double.NaN, coordinates[0])
			assertLngLatAlt(101.0, 1.0, Double.NaN, coordinates[1])
		}
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeMultiLineString()
	{
		val multiLineString = MultiLineString()
		multiLineString.coordinates.add(arrayListOf(LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 1.0)))
		multiLineString.coordinates.add(arrayListOf(LngLatAlt(102.0, 2.0), LngLatAlt(103.0, 3.0)))
		Assert.assertEquals("{\"coordinates\":" + "[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]],\"type\":\"MultiLineString\"}", gson.toJson(multiLineString))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeMultiPoint()
	{
		val multiPoint = MultiPoint(LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 1.0))
		Assert.assertEquals("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"MultiPoint\"}", gson.toJson(multiPoint))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPoint()
	{
		val multiPoint = gson.fromJson("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"MultiPoint\"}", MultiPoint::class.java)
		Assert.assertNotNull(multiPoint)
		val coordinates = multiPoint.coordinates
		assertLngLatAlt(100.0, 0.0, java.lang.Double.NaN, coordinates[0])
		assertLngLatAlt(101.0, 1.0, java.lang.Double.NaN, coordinates[1])
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeMultiPolygon()
	{
		val multiPolygon = MultiPolygon()
		multiPolygon.coordinates.add(Polygon(arrayListOf(
			LngLatAlt(102.0, 2.0),
			LngLatAlt(103.0, 2.0),
			LngLatAlt(103.0, 3.0),
			LngLatAlt(102.0, 3.0),
			LngLatAlt(102.0, 2.0)
		)).coordinates)

		val polygon = Polygon(arrayListOf(
			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
			LngLatAlt(100.0, 0.0)
		))
		polygon.addInteriorRing(arrayListOf(
			LngLatAlt(100.2, 0.2), LngLatAlt(100.8, 0.2),
			LngLatAlt(100.8, 0.8), LngLatAlt(100.2, 0.8),
			LngLatAlt(100.2, 0.2)
		))
		multiPolygon.coordinates.add(polygon.coordinates)
		Assert.assertEquals(
			"{\"coordinates\":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],"
			+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
			+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]],\"type\":\"MultiPolygon\"}",
		gson.toJson(multiPolygon))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPolygon()
	{
		val multiPolygon = gson.fromJson(
		("{\"coordinates\":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],"
		+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
		+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]],\"type\":\"MultiPolygon\"}"), MultiPolygon::class.java
		)
		Assert.assertEquals(2, multiPolygon.coordinates.size)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializePolygon()
	{
		val polygon = Polygon(arrayListOf(
			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
			LngLatAlt(100.0, 0.0)
		))
		Assert.assertEquals("{\"coordinates\":" + "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]],\"type\":\"Polygon\"}", gson.toJson(polygon))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeWithHole()
	{
		val polygon = Polygon(arrayListOf(
			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
			LngLatAlt(100.0, 0.0)
		))
		polygon.addInteriorRing(
			LngLatAlt(100.2, 0.2), LngLatAlt(100.8, 0.2),
			LngLatAlt(100.8, 0.8), LngLatAlt(100.2, 0.8),
			LngLatAlt(100.2, 0.2))
		Assert.assertEquals("{\"coordinates\":"
			+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
			+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]],\"type\":\"Polygon\"}", gson.toJson(polygon))
	}

	@Test(expected = RuntimeException::class)
	@Throws(Exception::class)
	fun itShouldFailOnAddInteriorRingWithoutExteriorRing()
	{
		val polygon = Polygon()
		polygon.addInteriorRing(arrayListOf(
			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
			LngLatAlt(100.0, 0.0)))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializePolygon()
	{
		val polygon = gson.fromJson("{\"coordinates\":"
			+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
			+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]],\"type\":\"Polygon\"}", Polygon::class.java)
		assertListEquals(arrayListOf(
			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
			LngLatAlt(100.0, 0.0)), polygon.getExteriorRing())
		assertListEquals(arrayListOf(
			LngLatAlt(100.2, 0.2), LngLatAlt(100.8, 0.2),
			LngLatAlt(100.8, 0.8), LngLatAlt(100.2, 0.8),
			LngLatAlt(100.2, 0.2)), polygon.getInteriorRing(0))
		assertListEquals(arrayListOf(
			LngLatAlt(100.2, 0.2), LngLatAlt(100.8, 0.2),
			LngLatAlt(100.8, 0.8), LngLatAlt(100.2, 0.8),
			LngLatAlt(100.2, 0.2)), polygon.getInteriorRings()[0])
	}

	private fun assertListEquals(expectedList: List<LngLatAlt>, actualList: List<LngLatAlt>)
	{
		for (x in actualList.indices)
		{
			val expected = expectedList[x]
			val actual = actualList[x]
			assertLngLatAlt(expected.longitude, expected.latitude, expected.altitude, actual)
		}
	}

	private fun assertLngLatAlt(expectedLongitue: Double, expectedLatitude: Double, expectedAltitude: Double?, point: LngLatAlt)
	{
		Assert.assertEquals(expectedLongitue, point.longitude, 0.00001)
		Assert.assertEquals(expectedLatitude, point.latitude, 0.00001)

		if (expectedAltitude?.isNaN() == true)
		{
			Assert.assertFalse(point.hasAltitude())
		}
		else
		{
//			Assert.assertEquals(expectedAltitude as Double, point.altitude, 0.00001)
		}
	}
}
