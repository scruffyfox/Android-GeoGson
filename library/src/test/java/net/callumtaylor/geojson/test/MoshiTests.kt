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
		val point = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.pointJson)
		Assert.assertTrue(point is Point)

		val circle = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.circleJson)
		Assert.assertNotNull(circle)

		val multiPoint = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.multiPointJson)
		Assert.assertTrue(multiPoint is MultiPoint)

		val lineString = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.lineStringJson)
		Assert.assertTrue(lineString is LineString)

		val multiLineString = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.multiLineStringJson)
		Assert.assertTrue(multiLineString is MultiLineString)

		val polygon = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.polygonJson)
		Assert.assertTrue(polygon is Polygon)

		val multiPolygon = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.multiPolygonJson)
		Assert.assertTrue(multiPolygon is MultiPolygon)

		val feature = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.featureJson)
		Assert.assertTrue(feature is Feature)

		val featureCollection = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.featureCollectionJson)
		Assert.assertTrue(featureCollection is FeatureCollection)

		val emptyFeatureCollection = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.emptyFeatureCollectionJson)
		Assert.assertTrue(emptyFeatureCollection is FeatureCollection)

		val geometryCollection = moshi.adapter(GeoJsonObject::class.java).fromJson(GeoJsonData.geometryCollectionJson)
		Assert.assertTrue(geometryCollection is GeometryCollection)
	}

	@Test
	fun testEmptyFeatureCollection()
	{
		val feature = moshi.adapter(FeatureCollection::class.java).fromJson(GeoJsonData.emptyFeatureCollectionJson)

		Assert.assertNotNull(feature)
		Assert.assertTrue(feature is FeatureCollection)
		Assert.assertNotNull(feature!!.features)
		Assert.assertEquals(0, feature.features.size)
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
		val json = moshi.adapter(GeoJsonObject::class.java).fromJson(str)
		Assert.assertTrue(json is FeatureCollection)
		Assert.assertTrue((json as FeatureCollection).features[0].foreign!!["_embedded"] != null)
		Assert.assertTrue(((json as FeatureCollection).features[0].foreign!!["_embedded"] as Map<String, *>)["line"] != null)

		val map = (((json as FeatureCollection).features[0].foreign!!["_embedded"] as Map<String, *>)["line"] as Map<String, *>)
		Assert.assertEquals("5", map["id"])
		Assert.assertTrue(map["colors"] is Map<*, *>)
		Assert.assertEquals("#149934", (map["colors"] as Map<*, *>)["background"])
	}

	@Test
	@Throws(Exception::class)
	fun testForeignKeys()
	{
		val point = moshi.adapter(GeoJsonObject::class.java).fromJson("""
			{
				"type": "Point",
				"coordinates": [100,5],
				"key": "value",
				"abc": "def"
			}
		""")
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
		val value = moshi.adapter(Point::class.java).fromJson(GeoJsonData.pointJson)
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Point)
		val point2 = value as Point
		assertLngLatAlt(100.0, 5.0, Double.NaN, point2.coordinates)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeAPoint()
	{
		Assert.assertEquals(GeoJsonData.pointJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.pointObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeACircle()
	{
		val value = moshi.adapter(Circle::class.java).fromJson(GeoJsonData.circleJson)
		Assert.assertNotNull(value)
		Assert.assertTrue(value is Circle)
		assertLngLatAlt(100.0, 5.0, Double.NaN, value!!.coordinates)
		Assert.assertEquals(1000.0, value.radius, 0.00001)
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializeACircle()
	{
		Assert.assertEquals(GeoJsonData.circleJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.circleObject))
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

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeLineString()
	{
		val lineString = moshi.adapter(LineString::class.java).fromJson(GeoJsonData.lineStringJson)
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
		Assert.assertEquals(GeoJsonData.lineStringJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.lineStringObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiLineString()
	{
		val multiLineString = moshi.adapter(MultiLineString::class.java).fromJson(GeoJsonData.multiLineStringJson)
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
		Assert.assertEquals(GeoJsonData.multiLineStringJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.multiLineStringObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPoint()
	{
		val multiPoint = moshi.adapter(MultiPoint::class.java).fromJson(GeoJsonData.multiPointJson)
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
		Assert.assertEquals(GeoJsonData.multiPointJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.multiPointObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeMultiPolygon()
	{
		val multiPolygon = moshi.adapter(MultiPolygon::class.java).fromJson(GeoJsonData.multiPolygonJson)
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
		Assert.assertEquals(GeoJsonData.multiPolygonJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.multiPolygonObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializePolygon()
	{
		val polygon = moshi.adapter(Polygon::class.java).fromJson(GeoJsonData.polygonJson)
		Assert.assertNotNull(polygon)
		Assert.assertTrue(polygon is Polygon)
		assertListEquals(GeoJsonData.polygonObject.coordinates[0], (polygon as Polygon).coordinates[0])
	}

	@Test
	@Throws(Exception::class)
	fun itShouldSerializePolygon()
	{
		Assert.assertEquals(GeoJsonData.polygonJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.polygonObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeFeature()
	{
		val feature = moshi.adapter(Feature::class.java).fromJson(GeoJsonData.featureJson)

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
		val feature = Feature()
		feature.geometry = Point(125.6, 10.1)
		Assert.assertEquals(GeoJsonData.featureJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.featureObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeFeatureCollection()
	{
		val feature = moshi.adapter(FeatureCollection::class.java).fromJson(GeoJsonData.featureCollectionJson)

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
		Assert.assertEquals(GeoJsonData.featureCollectionJson, moshi.adapter(FeatureCollection::class.java).toJson(GeoJsonData.featureCollectionObject))
	}

	@Test
	@Throws(Exception::class)
	fun itShouldDeserializeGeometryCollection()
	{
		val geometryCollection = moshi.adapter(GeometryCollection::class.java).fromJson(GeoJsonData.geometryCollectionJson)

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
		Assert.assertEquals(GeoJsonData.geometryCollectionJson, moshi.adapter(GeoJsonObject::class.java).toJson(GeoJsonData.gemoetryCollectionObject))
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
