package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName

open class GeometryCollection : GeoJsonObject(), Iterable<GeoJsonObject>
{
	@SerializedName("geometries") var geometries: ArrayList<GeoJsonObject> = arrayListOf<GeoJsonObject>()

	init { type = "GeometryCollection" }

	override fun iterator(): Iterator<GeoJsonObject> = geometries.iterator()

	operator fun plusAssign(rhs: GeoJsonObject): Unit
	{
		geometries.add(rhs)
	}
}
