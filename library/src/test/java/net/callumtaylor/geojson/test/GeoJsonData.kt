package net.callumtaylor.geojson.test

import net.callumtaylor.geojson.Circle
import net.callumtaylor.geojson.Feature
import net.callumtaylor.geojson.FeatureCollection
import net.callumtaylor.geojson.GeometryCollection
import net.callumtaylor.geojson.LineString
import net.callumtaylor.geojson.LngLatAlt
import net.callumtaylor.geojson.MultiLineString
import net.callumtaylor.geojson.MultiPoint
import net.callumtaylor.geojson.MultiPolygon
import net.callumtaylor.geojson.Point
import net.callumtaylor.geojson.Polygon

/**
 * Mock data for each geojson model
 */
object GeoJsonData
{
	public val pointJson = "{\"coordinates\":[100.0,5.0],\"type\":\"Point\"}"
	public val pointObject = Point().also {
		it.coordinates = LngLatAlt(100.0, 5.0, null)
	}

	public val circleJson = "{\"coordinates\":[100.0,5.0],\"radius\":1000.0,\"type\":\"Circle\"}"
	public val circleObject = Circle().also {
		it.coordinates = LngLatAlt(100.0, 5.0, null)
		it.radius = 1000.0
	}

	public val multiPointJson = "{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"MultiPoint\"}"
	public val multiPointObject = MultiPoint().also {
		it.coordinates = arrayListOf(
			LngLatAlt(100.0, 0.0),
			LngLatAlt(101.0, 1.0)
		)
	}

	public val lineStringJson = "{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}"
	public val lineStringObject = LineString().also {
		it.coordinates = arrayListOf(
			LngLatAlt(100.0, 0.0),
			LngLatAlt(101.0, 1.0)
		)
	}

	public val multiLineStringJson = "{\"coordinates\":[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]],\"type\":\"MultiLineString\"}"
	public val multiLineStringObject = MultiLineString().also {
		it.coordinates = arrayListOf(
			arrayListOf(
				LngLatAlt(100.0, 0.0),
				LngLatAlt(101.0, 1.0)
			),
			arrayListOf(
				LngLatAlt(102.0, 2.0),
				LngLatAlt(103.0, 3.0)
			)
		)
	}

	public val polygonJson = "{\"coordinates\":[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]],\"type\":\"Polygon\"}"
	public val polygonObject = Polygon().also {
		it.coordinates = arrayListOf(
			arrayListOf(
				LngLatAlt(100.0, 0.0),
				LngLatAlt(101.0, 0.0),
				LngLatAlt(101.0, 1.0),
				LngLatAlt(100.0, 1.0),
				LngLatAlt(100.0, 0.0)
			)
		)
	}

	public val multiPolygonJson = "{\"coordinates\":[[[[107.0,7.0],[108.0,7.0],[108.0,8.0],[107.0,8.0],[107.0,7.0]]],[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]]],\"type\":\"MultiPolygon\"}"
	public val multiPolygonObject = MultiPolygon().also {
		it.coordinates = arrayListOf(
			arrayListOf(
				arrayListOf(
					LngLatAlt(107.0, 7.0),
					LngLatAlt(108.0, 7.0),
					LngLatAlt(108.0, 8.0),
					LngLatAlt(107.0, 8.0),
					LngLatAlt(107.0, 7.0)
				)
			),
			arrayListOf(
				arrayListOf(
					LngLatAlt(100.0, 0.0),
					LngLatAlt(101.0, 0.0),
					LngLatAlt(101.0, 1.0),
					LngLatAlt(100.0, 1.0),
					LngLatAlt(100.0, 0.0)
				)
			)
		)
	}

	public val featureJson = "{\"geometry\":{\"coordinates\":[[[-80.724878,35.265454],[-80.722646,35.260338],[-80.720329,35.260618],[-80.718698,35.260267],[-80.715093,35.260548],[-80.71681,35.255361],[-80.710887,35.255361],[-80.703248,35.265033],[-80.704793,35.268397],[-80.70857,35.268257],[-80.712518,35.270359],[-80.715179,35.267696],[-80.721359,35.267276],[-80.724878,35.265454]]],\"type\":\"Polygon\"},\"properties\":{\"name\":\"Plaza Road Park\"},\"type\":\"Feature\"}"
	public val featureObject = Feature().also {
		it.geometry = Polygon().also { polygon ->
			polygon.coordinates = arrayListOf(
				arrayListOf(
					LngLatAlt(-80.724878, 35.265454),
					LngLatAlt(-80.722646, 35.260338),
					LngLatAlt(-80.720329, 35.260618),
					LngLatAlt(-80.718698, 35.260267),
					LngLatAlt(-80.715093, 35.260548),
					LngLatAlt(-80.71681, 35.255361),
					LngLatAlt(-80.710887, 35.255361),
					LngLatAlt(-80.703248, 35.265033),
					LngLatAlt(-80.704793, 35.268397),
					LngLatAlt(-80.70857, 35.268257),
					LngLatAlt(-80.712518, 35.270359),
					LngLatAlt(-80.715179, 35.267696),
					LngLatAlt(-80.721359, 35.267276),
					LngLatAlt(-80.724878, 35.265454)
				)
			)
		}

		it.properties = hashMapOf("name" to "Plaza Road Park")
	}

	public val featureCollectionJson = "{\"features\":[{\"geometry\":{\"coordinates\":[-80.870885,35.215151],\"type\":\"Point\"},\"properties\":{\"name\":\"ABBOTT NEIGHBORHOOD PARK\",\"address\":\"1300  SPRUCE ST\"},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[-80.724878,35.265454],[-80.722646,35.260338],[-80.720329,35.260618],[-80.718698,35.260267],[-80.715093,35.260548],[-80.71681,35.255361],[-80.710887,35.255361],[-80.703248,35.265033],[-80.704793,35.268397],[-80.70857,35.268257],[-80.712518,35.270359],[-80.715179,35.267696],[-80.721359,35.267276],[-80.724878,35.265454]]],\"type\":\"Polygon\"},\"properties\":{\"name\":\"Plaza Road Park\"},\"type\":\"Feature\"}],\"type\":\"FeatureCollection\"}"
	public val featureCollectionObject = FeatureCollection().also {
		it.features = arrayListOf(
			Feature().also { feature ->
				feature.geometry = Point().also { point ->
					point.coordinates = LngLatAlt(-80.870885, 35.215151)
				}
				feature.properties = hashMapOf(
					"name" to "ABBOTT NEIGHBORHOOD PARK",
					"address" to "1300  SPRUCE ST"
				)
			},
			featureObject
		)
	}

	public val geometryCollectionJson = "{\"geometries\":[{\"coordinates\":[-80.660805,35.049392],\"type\":\"Point\"},{\"coordinates\":[[[-80.664582,35.044965],[-80.663874,35.04428],[-80.662586,35.04558],[-80.663444,35.046036],[-80.664582,35.044965]]],\"type\":\"Polygon\"},{\"coordinates\":[[-80.662372,35.059509],[-80.662693,35.059263],[-80.662844,35.05893],[-80.66308,35.058332],[-80.663595,35.057753],[-80.663874,35.057401],[-80.66441,35.057033],[-80.664861,35.056787],[-80.665419,35.056506],[-80.665633,35.056312],[-80.666019,35.055891],[-80.666191,35.055452],[-80.666191,35.055171],[-80.666255,35.05489],[-80.666213,35.054222],[-80.666213,35.053924],[-80.665955,35.052905],[-80.665698,35.052044],[-80.665504,35.051482],[-80.665762,35.050481],[-80.66617,35.049725],[-80.666513,35.049286],[-80.666921,35.048531],[-80.667006,35.048215],[-80.667071,35.047775],[-80.667049,35.047389],[-80.666964,35.046985],[-80.666813,35.046353],[-80.666599,35.045966],[-80.666406,35.045615],[-80.665998,35.045193],[-80.665526,35.044877],[-80.664989,35.044543],[-80.664496,35.044174],[-80.663852,35.043876],[-80.663037,35.043717]],\"type\":\"LineString\"}],\"properties\":{\"foo\":\"value\"},\"type\":\"GeometryCollection\"}"
	public val gemoetryCollectionObject = GeometryCollection().also {
		it.properties = hashMapOf("foo" to "value")
		it.geometries = arrayListOf(
			Point().also { point ->
				point.coordinates = LngLatAlt(-80.660805, 35.049392)
			},
			Polygon().also { polygon ->
				polygon.coordinates = arrayListOf(
					arrayListOf(
						LngLatAlt(-80.664582, 35.044965),
						LngLatAlt(-80.663874, 35.04428),
						LngLatAlt(-80.662586, 35.04558),
						LngLatAlt(-80.663444, 35.046036),
						LngLatAlt(-80.664582, 35.044965)
					)
				)
			},
			LineString().also { lineString ->
				lineString.coordinates = arrayListOf(
					LngLatAlt(-80.662372, 35.059509),
					LngLatAlt(-80.662693, 35.059263),
					LngLatAlt(-80.662844, 35.05893),
					LngLatAlt(-80.66308, 35.058332),
					LngLatAlt(-80.663595, 35.057753),
					LngLatAlt(-80.663874, 35.057401),
					LngLatAlt(-80.66441, 35.057033),
					LngLatAlt(-80.664861, 35.056787),
					LngLatAlt(-80.665419, 35.056506),
					LngLatAlt(-80.665633, 35.056312),
					LngLatAlt(-80.666019, 35.055891),
					LngLatAlt(-80.666191, 35.055452),
					LngLatAlt(-80.666191, 35.055171),
					LngLatAlt(-80.666255, 35.05489),
					LngLatAlt(-80.666213, 35.054222),
					LngLatAlt(-80.666213, 35.053924),
					LngLatAlt(-80.665955, 35.052905),
					LngLatAlt(-80.665698, 35.052044),
					LngLatAlt(-80.665504, 35.051482),
					LngLatAlt(-80.665762, 35.050481),
					LngLatAlt(-80.66617, 35.049725),
					LngLatAlt(-80.666513, 35.049286),
					LngLatAlt(-80.666921, 35.048531),
					LngLatAlt(-80.667006, 35.048215),
					LngLatAlt(-80.667071, 35.047775),
					LngLatAlt(-80.667049, 35.047389),
					LngLatAlt(-80.666964, 35.046985),
					LngLatAlt(-80.666813, 35.046353),
					LngLatAlt(-80.666599, 35.045966),
					LngLatAlt(-80.666406, 35.045615),
					LngLatAlt(-80.665998, 35.045193),
					LngLatAlt(-80.665526, 35.044877),
					LngLatAlt(-80.664989, 35.044543),
					LngLatAlt(-80.664496, 35.044174),
					LngLatAlt(-80.663852, 35.043876),
					LngLatAlt(-80.663037, 35.043717)
				)
			}
		)
	}
}
