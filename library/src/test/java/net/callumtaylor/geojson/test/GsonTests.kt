package net.callumtaylor.geojson.test

import com.google.gson.GsonBuilder
import net.callumtaylor.geojson.*
import org.junit.Assert
import org.junit.Test

public class GsonTests
{
	private val gson = GeoGson.registerAdapters(GsonBuilder()).create()

	@Test
	fun testEmptyFeatureCollection()
	{
		val feature = gson.fromJson(GeoJsonData.emptyFeatureCollectionJson, FeatureCollection::class.java)

		Assert.assertNotNull(feature)
		Assert.assertTrue(feature is FeatureCollection)
		Assert.assertNotNull(feature!!.features)
		Assert.assertEquals(0, feature.features.size)
	}

	@Test
	@Throws(Exception::class)
	fun intelligentDeserialisation()
	{
		val point = gson.fromJson(GeoJsonData.pointJson, GeoJsonObject::class.java)
		Assert.assertTrue(point is Point)

		val circle = gson.fromJson(GeoJsonData.circleJson, GeoJsonObject::class.java)
		Assert.assertNotNull(circle)

		val multiPoint = gson.fromJson(GeoJsonData.multiPointJson, GeoJsonObject::class.java)
		Assert.assertTrue(multiPoint is MultiPoint)

		val lineString = gson.fromJson(GeoJsonData.lineStringJson, GeoJsonObject::class.java)
		Assert.assertTrue(lineString is LineString)

		val multiLineString = gson.fromJson(GeoJsonData.multiLineStringJson, GeoJsonObject::class.java)
		Assert.assertTrue(multiLineString is MultiLineString)

		val polygon = gson.fromJson(GeoJsonData.polygonJson, GeoJsonObject::class.java)
		Assert.assertTrue(polygon is Polygon)

		val multiPolygon = gson.fromJson(GeoJsonData.multiPolygonJson, GeoJsonObject::class.java)
		Assert.assertTrue(multiPolygon is MultiPolygon)

		val feature = gson.fromJson(GeoJsonData.featureJson, GeoJsonObject::class.java)
		Assert.assertTrue(feature is Feature)

		val featureCollection = gson.fromJson(GeoJsonData.featureCollectionJson, GeoJsonObject::class.java)
		Assert.assertTrue(featureCollection is FeatureCollection)

		val emptyFeatureCollection = gson.fromJson(GeoJsonData.emptyFeatureCollectionJson, GeoJsonObject::class.java)
		Assert.assertTrue(emptyFeatureCollection is FeatureCollection)

		val geometryCollection = gson.fromJson(GeoJsonData.geometryCollectionJson, GeoJsonObject::class.java)
		Assert.assertTrue(geometryCollection is GeometryCollection)
	}

	@Test
	fun testFeatureForeign()
	{
		val str = """
			{
				"type": "FeatureCollection",
				"features": [{
					"type": "Feature",
					"geometry": {
						"type": "Point",
						"coordinates": [-1.90255, 50.7689]
					},
					"properties": {
						"direction": "outbound",
						"line": "5"
					},
					"_embedded": {
						"line": {
							"id": "5",
							"name": "5",
							"title": "5",
							"description": "Test model",
							"colors": {
								"background": "#149934",
								"foreground": "#FFFFFF"
							},
							"href": "http://google.com"
						}
					}
				}]
			}
		"""
		val json = gson.fromJson(str, GeoJsonObject::class.java)
		val feature = (json as FeatureCollection).features[0]
		Assert.assertTrue(feature.foreign!!["_embedded"] != null)
		Assert.assertTrue((feature.foreign!!["_embedded"] as Map<String, *>)["line"] != null)

		val map = (((feature as Feature).foreign!!["_embedded"] as Map<String, *>)["line"] as Map<String, *>)
		Assert.assertEquals("5", map["id"])
		Assert.assertTrue(map["colors"] is Map<*, *>)
		Assert.assertEquals("#149934", (map["colors"] as Map<*, *>)["background"])
	}

	@Test
	@Throws(Exception::class)
	fun testForeignKeys()
	{
		val point = gson.fromJson("""
			{
				"type": "Point",
				"coordinates": [100,5],
				"key": "value",
				"abc": "def"
			}
		""", GeoJsonObject::class.java)
		Assert.assertTrue(point is Point)
		Assert.assertNotNull(point!!.foreign)
		Assert.assertNotNull(point.foreign!!["key"])
		Assert.assertNotNull(point.foreign!!["abc"])
		Assert.assertEquals("value", point.foreign!!["key"])
		Assert.assertEquals("def", point.foreign!!["abc"])
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeAPoint()
	{
		val value = gson.fromJson(GeoJsonData.pointJson, GeoJsonObject::class.java)
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Point)
		val point2 = value as Point
		assertLngLatAlt(100.0, 5.0, Double.NaN, point2.coordinates)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeAPoint()
	{
		Assert.assertEquals(GeoJsonData.pointJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.pointObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeACircle()
	{
		val value = gson.fromJson(GeoJsonData.circleJson, GeoJsonObject::class.java) as Circle?
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Circle)
		assertLngLatAlt(100.0, 5.0, Double.NaN, value!!.coordinates)
		Assert.assertEquals(1000.0, value.radius, 0.00001)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeACircle()
	{
		Assert.assertEquals(GeoJsonData.circleJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.circleObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeAPointWithProperties()
	{
		val value = gson.fromJson("""
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
		""", GeoJsonObject::class.java) as Point?
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

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeLineString()
	{
		val lineString = gson.fromJson(GeoJsonData.lineStringJson, GeoJsonObject::class.java) as LineString?
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
		Assert.assertEquals(GeoJsonData.lineStringJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.lineStringObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiLineString()
	{
		val multiLineString = gson.fromJson(GeoJsonData.multiLineStringJson, GeoJsonObject::class.java) as MultiLineString?
		Assert.assertNotNull(multiLineString)
		Assert.assertTrue(multiLineString is MultiLineString)

		with (multiLineString as MultiLineString) {
			assertListEquals(GeoJsonData.multiLineStringObject.coordinates[0], coordinates[0])
		}
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeMultiLineString()
	{
		Assert.assertEquals(GeoJsonData.multiLineStringJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.multiLineStringObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPoint()
	{
		val multiPoint = gson.fromJson(GeoJsonData.multiPointJson, GeoJsonObject::class.java) as MultiPoint?
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
		Assert.assertEquals(GeoJsonData.multiPointJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.multiPointObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPolygon()
	{
		val multiPolygon = gson.fromJson(GeoJsonData.multiPolygonJson, GeoJsonObject::class.java) as MultiPolygon?
		Assert.assertNotNull(multiPolygon)
		Assert.assertTrue(multiPolygon is MultiPolygon)

		Assert.assertEquals(2, multiPolygon!!.coordinates.size)
		Assert.assertEquals(1, multiPolygon.coordinates[0].size)
		Assert.assertEquals(5, multiPolygon.coordinates[0][0].size)
		Assert.assertEquals(5, multiPolygon.coordinates[1][0].size)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeMultiPolygon()
	{
		Assert.assertEquals(GeoJsonData.multiPolygonJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.multiPolygonObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializePolygon()
	{
		val polygon = gson.fromJson(GeoJsonData.polygonJson, GeoJsonObject::class.java) as Polygon?
		Assert.assertNotNull(polygon)
		Assert.assertTrue(polygon is Polygon)
		assertListEquals(GeoJsonData.polygonObject.coordinates[0], (polygon as Polygon).coordinates[0])
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializePolygon()
	{
		Assert.assertEquals(GeoJsonData.polygonJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.polygonObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeFeature()
	{
		val feature = gson.fromJson(GeoJsonData.featureJson, GeoJsonObject::class.java) as Feature?

		Assert.assertNotNull(feature)
		Assert.assertTrue(feature is Feature)
		Assert.assertNotNull(feature!!.properties)
		Assert.assertEquals("Plaza Road Park", feature.properties!!["name"])
		Assert.assertNotNull(feature.geometry)
		Assert.assertTrue(feature.geometry is Polygon)
		assertListEquals((GeoJsonData.featureObject.geometry as Polygon).coordinates[0], (feature.geometry as Polygon).coordinates[0])
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeFeature()
	{
		Assert.assertEquals(GeoJsonData.featureJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.featureObject).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeFeatureCollection()
	{
		val feature = gson.fromJson(GeoJsonData.featureCollectionJson, GeoJsonObject::class.java) as FeatureCollection?

		Assert.assertNotNull(feature)
		Assert.assertTrue(feature is FeatureCollection)
		Assert.assertNotNull(feature!!.features)
		Assert.assertEquals(2, feature.features.size)
		Assert.assertEquals("ABBOTT NEIGHBORHOOD PARK", feature.features[0].properties!!["name"])
		Assert.assertEquals("1300  SPRUCE ST", feature.features[0].properties!!["address"])
		Assert.assertNotNull(feature.features[0].geometry)
		Assert.assertTrue(feature.features[0].geometry is Point)
		Assert.assertEquals(-80.870885, (feature.features[0].geometry as Point).coordinates.longitude, 0.00001)
		Assert.assertEquals(35.215151, (feature.features[0].geometry as Point).coordinates.latitude, 0.00001)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeFeatureCollection()
	{
		Assert.assertEquals(GeoJsonData.featureCollectionJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.featureCollectionObject, FeatureCollection::class.java).toCharArray().sort().toString())
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeGeometryCollection()
	{
		val geometryCollection = gson.fromJson(GeoJsonData.geometryCollectionJson, GeoJsonObject::class.java) as GeometryCollection?

		Assert.assertNotNull(geometryCollection)
		Assert.assertTrue(geometryCollection is GeometryCollection)
		Assert.assertNotNull(geometryCollection!!.geometries)
		Assert.assertEquals(3, geometryCollection.geometries.size)
		Assert.assertEquals("value", geometryCollection.properties!!["foo"])
		Assert.assertTrue(geometryCollection.geometries[0] is Point)
		Assert.assertTrue(geometryCollection.geometries[1] is Polygon)
		Assert.assertTrue(geometryCollection.geometries[2] is LineString)
		Assert.assertEquals(-80.660805, (geometryCollection.geometries[0] as Point).coordinates.longitude, 0.00001)
		Assert.assertEquals(35.049392, (geometryCollection.geometries[0] as Point).coordinates.latitude, 0.00001)
		Assert.assertEquals(1, (geometryCollection.geometries[1] as Polygon).coordinates.size)
		Assert.assertEquals(5, (geometryCollection.geometries[1] as Polygon).coordinates[0].size)
		Assert.assertEquals(36, (geometryCollection.geometries[2] as LineString).coordinates.size)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeGeometryCollection()
	{
		Assert.assertEquals(GeoJsonData.geometryCollectionJson.toCharArray().sort().toString(), gson.toJson(GeoJsonData.gemoetryCollectionObject).toCharArray().sort().toString())
	}

	private fun assertListEquals(expectedList: ArrayList<LngLatAlt>, actualList: ArrayList<LngLatAlt>)
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

		if (expectedAltitude?.isNaN() == true || expectedAltitude == null)
		{
			Assert.assertFalse(point.hasAltitude())
		}
		else
		{
			Assert.assertEquals(expectedAltitude as Double, point.altitude!!, 0.00001)
		}
	}
}
