package net.callumtaylor.geojson

/**
 * Checks if a point exists within a polygon
 */
fun Polygon?.contains(point: Point?): Boolean = this.contains(point?.coordinates) ?: false

/**
 * Checks if a coordinate exists within a polygon
 */
fun Polygon?.contains(lngLatAlt: LngLatAlt?): Boolean
{
	if (this == null || lngLatAlt == null) return false

	var intersections = 0
	for (coordinate in this.coordinates)
	{
		for (i in 1 until coordinate.size)
		{
			val v1 = coordinate[i - 1]
			val v2 = coordinate[i]

			if (lngLatAlt== v2)
			{
				return true
			}

			if (v1.latitude == v2.latitude
				&& v1.latitude == lngLatAlt.latitude
				&& lngLatAlt.longitude > (if (v1.longitude > v2.longitude) v2.longitude else v1.longitude)
				&& lngLatAlt.longitude < if (v1.longitude < v2.longitude) v2.longitude else v1.longitude)
			{
				// Is horizontal polygon boundary
				return true
			}

			if (lngLatAlt.latitude > (if (v1.latitude < v2.latitude) v1.latitude else v2.latitude)
				&& lngLatAlt.latitude <= (if (v1.latitude < v2.latitude) v2.latitude else v1.latitude)
				&& lngLatAlt.longitude <= (if (v1.longitude < v2.longitude) v2.longitude else v1.longitude)
				&& v1.latitude != v2.latitude)
			{
				val intersection = (lngLatAlt.latitude - v1.latitude) * (v2.longitude - v1.longitude) / (v2.latitude - v1.latitude) + v1.longitude

				if (intersection == lngLatAlt.longitude)
				{
					// Is other boundary
					return true
				}

				if (v1.longitude == v2.longitude || lngLatAlt.longitude <= intersection)
				{
					intersections++
				}
			}
		}
	}

	return intersections % 2 != 0
}
