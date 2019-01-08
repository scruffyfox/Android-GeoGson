package net.callumtaylor.geojson.test

import com.squareup.moshi.Moshi
import net.callumtaylor.geojson.*
import org.junit.Assert
import org.junit.Test

public class MoshiTests
{
	private val moshi = GeoMoshi.registerAdapters(Moshi.Builder()).build()

	@Test
	@Throws(Exception::class)
	fun intelligentDeserialisation()
	{
		val point = moshi.adapter(GeoJsonObject::class.java).fromJson("{\"type\":\"Point\",\"coordinates\":[100.0,5.0]}")
		Assert.assertTrue(point is Point)

		val circle = moshi.adapter(GeoJsonObject::class.java).fromJson("{\"type\":\"Circle\",\"coordinates\":[100.0,5.0],\"radius\":1000.0}")
		Assert.assertNotNull(circle)

		val multiPoint = moshi.adapter(GeoJsonObject::class.java).fromJson("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"MultiPoint\"}")
		Assert.assertTrue(multiPoint is MultiPoint)

		val lineString = moshi.adapter(GeoJsonObject::class.java).fromJson("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}")
		Assert.assertTrue(lineString is LineString)

		val multiLineString = moshi.adapter(GeoJsonObject::class.java).fromJson("{\"type\":\"MultiLineString\",\"coordinates\":[[[100,0],[101,1]],[[102,2],[103,3]]]}")
		Assert.assertTrue(multiLineString is MultiLineString)

		val polygon = moshi.adapter(GeoJsonObject::class.java).fromJson("{\"coordinates\":[],\"type\":\"Polygon\"}")
		Assert.assertTrue(polygon is Polygon)

		val multiPolygon = moshi.adapter(GeoJsonObject::class.java).fromJson("{\"coordinates\":[],\"type\":\"MultiPolygon\"}")
		Assert.assertTrue(multiPolygon is MultiPolygon)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeAPoint()
	{
		val value = moshi.adapter(Point::class.java).fromJson("{\"type\":\"Point\",\"coordinates\":[100.0,5.0]}")
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Point)
		val point2 = value as Point
		assertLngLatAlt(100.0, 5.0, Double.NaN, point2.coordinates)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeAPoint()
	{
		val point = Point(100.0, 0.0)
		Assert.assertEquals("{\"coordinates\":[100.0,0.0],\"type\":\"Point\"}", moshi.adapter(GeoJsonObject::class.java).toJson(point))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeACircle()
	{
		val value = moshi.adapter(Circle::class.java).fromJson("{\"type\":\"Circle\",\"coordinates\":[100.0,5.0],\"radius\":1000.0}")
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Circle)
		assertLngLatAlt(100.0, 5.0, Double.NaN, value!!.coordinates)
		Assert.assertEquals(1000.0, value.radius, 0.00001)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeACircle()
	{
		val circle = Circle(100.0, 0.0, null, 1000.0)
		Assert.assertEquals("{\"coordinates\":[100.0,0.0],\"radius\":1000.0,\"type\":\"Circle\"}", moshi.adapter(GeoJsonObject::class.java).toJson(circle))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeAPointWithProperties()
	{
		val value = moshi.adapter(Point::class.java).fromJson("""
			{
				"type":"Point",
				"coordinates":[100.0,5.0],
				"properties": {
					"name": "value",
					"name2": 2
				},
				"bbox": [100.0, 0.0, -100.0, 105.0, 1.0, 0.0],
				"other": "foreign"
			}
		""")
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Point)
		assertLngLatAlt(100.0, 5.0, Double.NaN, value!!.coordinates)
		Assert.assertTrue(!value.properties.isNullOrEmpty())
		Assert.assertEquals("value", value.properties!!["name"] as String)
		Assert.assertEquals(2.0, value.properties!!["name2"] as Double, 0.00001)
		Assert.assertTrue(!value.foreign.isNullOrEmpty())
		Assert.assertEquals("foreign", value.foreign!!["other"] as String)
		Assert.assertNotNull(value.bbox)
		Assert.assertEquals(100.0, value.bbox!![0], 0.00001)
		Assert.assertEquals(0.0, value.bbox!![1], 0.00001)
		Assert.assertEquals(-100.0, value.bbox!![2], 0.00001)
		Assert.assertEquals(105.0, value.bbox!![3], 0.00001)
		Assert.assertEquals(1.0, value.bbox!![4], 0.00001)
		Assert.assertEquals(0.0, value.bbox!![5], 0.00001)
	}

//	@Test
//	@Throws(Exception::class)
//	fun itShouldDeserializeAPointWithAltitude()
//	{
//		val value = gson.fromJson("{\"coordinates\":[100.0,5.0,123],\"type\":\"Point\"}", GeoJsonObject::class.java)
//		val point = value as Point
//		assertLngLatAlt(100.0, 5.0, 123.0, point.coordinates)
//	}
//
//	@Test
//	@Throws(Exception::class)
//	fun itShouldSerializeAPointWithAltitude()
//	{
//		val point = Point(100.0, 0.0, 256.0)
//		Assert.assertEquals("{\"coordinates\":[100.0,0.0,256.0],\"type\":\"Point\"}", gson.toJson(point))
//	}
//
	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeLineString()
	{
		val lineString = moshi.adapter(LineString::class.java).fromJson("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}")
		Assert.assertNotNull(lineString)
		Assert.assertTrue(lineString is LineString)

		with (lineString as LineString) {
			val coordinates = lineString.coordinates
			assertLngLatAlt(100.0, 0.0, null, coordinates[0])
			assertLngLatAlt(101.0, 1.0, null, coordinates[1])
		}
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeLineString()
	{
		val lineString = LineString(LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 1.0))
		Assert.assertEquals("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}", moshi.adapter(GeoJsonObject::class.java).toJson(lineString))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiLineString()
	{
		val multiLineString = moshi.adapter(MultiLineString::class.java).fromJson("""
			{
				"type": "MultiLineString",
				"coordinates": [
				 	[
						[100.0, 0.0],
						[101.0, 1.0]
					],
					[
						[102.0, 2.0],
						[103.0, 3.0]
					]
				]
			}
		""")
		Assert.assertNotNull(multiLineString)
		Assert.assertTrue(multiLineString is MultiLineString)

		with (multiLineString as MultiLineString) {
			val coordinates = multiLineString.coordinates
			assertLngLatAlt(100.0, 0.0, null, coordinates[0][0])
			assertLngLatAlt(101.0, 1.0, null, coordinates[0][1])
			assertLngLatAlt(102.0, 2.0, null, coordinates[1][0])
			assertLngLatAlt(103.0, 3.0, null, coordinates[1][1])
		}
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeMultiLineString()
	{
		val multiLineString = MultiLineString()
		multiLineString.coordinates.add(arrayListOf(LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 1.0)))
		multiLineString.coordinates.add(arrayListOf(LngLatAlt(102.0, 2.0), LngLatAlt(103.0, 3.0)))
		Assert.assertEquals("{\"coordinates\":[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]],\"type\":\"MultiLineString\"}", moshi.adapter(GeoJsonObject::class.java).toJson(multiLineString))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPoint()
	{
		val multiPoint = moshi.adapter(MultiPoint::class.java).fromJson("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"MultiPoint\"}")
		Assert.assertTrue(multiPoint is MultiPoint)
		Assert.assertNotNull(multiPoint)
		val coordinates = multiPoint!!.coordinates
		assertLngLatAlt(100.0, 0.0, null, coordinates[0])
		assertLngLatAlt(101.0, 1.0, null, coordinates[1])
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeMultiPoint()
	{
		val multiPoint = MultiPoint(LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 1.0))
		Assert.assertEquals("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"MultiPoint\"}", moshi.adapter(GeoJsonObject::class.java).toJson(multiPoint))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPolygon()
	{
		val multiPolygon = moshi.adapter(MultiPolygon::class.java).fromJson("""
			 {
				"type": "MultiPolygon",
				"coordinates": [
					[
						[
							[102.0, 2.0],
							[103.0, 2.0],
							[103.0, 3.0],
							[102.0, 3.0],
							[102.0, 2.0]
						]
					],
					[
						[
							[100.0, 0.0],
							[101.0, 0.0],
							[101.0, 1.0],
							[100.0, 1.0],
							[100.0, 0.0]
						],
						[
							[100.2, 0.2],
							[100.2, 0.8],
							[100.8, 0.8],
							[100.8, 0.2],
							[100.2, 0.2]
						]
					]
				]
			 }
		""")
		Assert.assertNotNull(multiPolygon)
		Assert.assertTrue(multiPolygon is MultiPolygon)

		Assert.assertEquals(2, multiPolygon!!.coordinates.size)
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
		moshi.adapter(GeoJsonObject::class.java).toJson(multiPolygon))
	}

//	@Test
//	@Throws(Exception::class)
//	fun itShouldSerializeWithHole()
//	{
//		val polygon = Polygon(arrayListOf(
//			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
//			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
//			LngLatAlt(100.0, 0.0)
//		))
//		polygon.addInteriorRing(
//			LngLatAlt(100.2, 0.2), LngLatAlt(100.8, 0.2),
//			LngLatAlt(100.8, 0.8), LngLatAlt(100.2, 0.8),
//			LngLatAlt(100.2, 0.2))
//		Assert.assertEquals("{\"coordinates\":"
//			+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
//			+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]],\"type\":\"Polygon\"}", gson.toJson(polygon))
//	}
//
//	@Test(expected = RuntimeException::class)
//	@Throws(Exception::class)
//	fun itShouldFailOnAddInteriorRingWithoutExteriorRing()
//	{
//		val polygon = Polygon()
//		polygon.addInteriorRing(arrayListOf(
//			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
//			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
//			LngLatAlt(100.0, 0.0)))
//	}
//
	@Test
	@Throws(Exception::class)
	fun itShouldDeserializePolygon()
	{
		val polygon = moshi.adapter(Polygon::class.java).fromJson("""
			{"type":"Polygon","coordinates":[[[100,0],[101,0],[101,1],[100,1],[100,0]]]}
		""")
		Assert.assertNotNull(polygon)
		Assert.assertTrue(polygon is Polygon)
		assertListEquals(arrayListOf(
			LngLatAlt(100.0, 0.0), LngLatAlt(101.0, 0.0),
			LngLatAlt(101.0, 1.0), LngLatAlt(100.0, 1.0),
			LngLatAlt(100.0, 0.0)), polygon!!.coordinates[0])
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
		Assert.assertEquals("{\"coordinates\":[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]],\"type\":\"Polygon\"}", moshi.adapter(GeoJsonObject::class.java).toJson(polygon))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeFeature()
	{
		val feature = moshi.adapter(Feature::class.java).fromJson("""
			{
				"type": "Feature",
				"geometry": {
					"type": "Point",
					"coordinates": [
						125.6,
						10.1
					]
				},
				"properties": {
					"name": "Dinagat Islands"
				}
			}
		""")

		Assert.assertNotNull(feature)
		Assert.assertTrue(feature is Feature)
		Assert.assertNotNull(feature!!.properties)
		Assert.assertEquals("Dinagat Islands", feature.properties!!["name"])
		Assert.assertNotNull(feature.geometry)
		Assert.assertTrue(feature.geometry is Point)
		Assert.assertEquals(125.6, (feature.geometry as Point).coordinates.longitude, 0.00001)
		Assert.assertEquals(10.1, (feature.geometry as Point).coordinates.latitude, 0.00001)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeFeature()
	{
		val feature = Feature()
		feature.geometry = Point(125.6, 10.1)
		Assert.assertEquals("{\"geometry\":{\"coordinates\":[125.6,10.1],\"type\":\"Point\"},\"type\":\"Feature\"}", moshi.adapter(GeoJsonObject::class.java).toJson(feature))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeFeatureCollection()
	{
		val feature = moshi.adapter(FeatureCollection::class.java).fromJson("""
			 {
				"type": "FeatureCollection",
				"bbox": [100, 0, 105, 1],
				"features": [
					{
						"type": "Feature",
						"geometry": {
							"type": "Point",
							"coordinates": [125.6, 10.1]
						},
						"properties": {
							"name": "Dinagat Islands"
						}
					}
				]
			}
		""")

		Assert.assertNotNull(feature)
		Assert.assertTrue(feature is FeatureCollection)
		Assert.assertNotNull(feature!!.features)
		Assert.assertEquals(1, feature.features.size)
		Assert.assertEquals("Dinagat Islands", feature.features[0].properties!!["name"])
		Assert.assertNotNull(feature.features[0].geometry)
		Assert.assertTrue(feature.features[0].geometry is Point)
		Assert.assertEquals(125.6, (feature.features[0].geometry as Point).coordinates.longitude, 0.00001)
		Assert.assertEquals(10.1, (feature.features[0].geometry as Point).coordinates.latitude, 0.00001)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeGeometryCollection()
	{
		val geometry = moshi.adapter(GeometryCollection::class.java).fromJson("""
			{
				"type": "GeometryCollection",
				"properties": {
					"value": "foo"
				},
				"geometries": [
					{
						"type": "Point",
						"coordinates": [0,0]
					},
					{
						"type": "Polygon",
						"coordinates": [[[45,45],[45,-45],[-45,-45],[-45,45],[45,45]]]
					}
				]
			}
		""")

		Assert.assertNotNull(geometry)
		Assert.assertTrue(geometry is GeometryCollection)
		Assert.assertNotNull(geometry!!.geometries)
		Assert.assertEquals(2, geometry.geometries.size)
		Assert.assertEquals("foo", geometry.properties!!["value"])
		Assert.assertTrue(geometry.geometries[0] is Point)
		Assert.assertTrue(geometry.geometries[1] is Polygon)
		Assert.assertEquals(0.0, (geometry.geometries[0] as Point).coordinates.longitude, 0.00001)
		Assert.assertEquals(0.0, (geometry.geometries[0] as Point).coordinates.latitude, 0.00001)
		Assert.assertEquals(1, (geometry.geometries[1] as Polygon).coordinates.size)
		Assert.assertEquals(5, (geometry.geometries[1] as Polygon).coordinates[0].size)
		Assert.assertEquals(45.0, (geometry.geometries[1] as Polygon).coordinates[0][0].longitude, 0.00001)
		Assert.assertEquals(45.0, (geometry.geometries[1] as Polygon).coordinates[0][0].latitude, 0.00001)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeGeometryCollection()
	{
		val collection = GeometryCollection()
		collection.geometries.add(Point(0.0, 0.0))
		collection.geometries.add(Point(125.0, -10.0))

		val str = "{\"geometries\":[{\"coordinates\":[0.0,0.0],\"type\":\"Point\"},{\"coordinates\":[125.0,-10.0],\"type\":\"Point\"}],\"type\":\"GeometryCollection\"}"
		Assert.assertEquals(str, moshi.adapter(GeoJsonObject::class.java).toJson(collection))
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
