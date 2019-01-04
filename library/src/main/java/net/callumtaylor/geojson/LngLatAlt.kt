package net.callumtaylor.geojson

import java.io.Serializable

open class LngLatAlt(
	var longitude: Double = 0.toDouble(),
	var latitude: Double = 0.toDouble(),
	var altitude: Double = Double.NaN
) : Serializable
{
	fun hasAltitude(): Boolean = !java.lang.Double.isNaN(altitude)
}
