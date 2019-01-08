package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

open class FeatureCollection : GeoJsonObject(), Iterable<Feature>
{
	@field:Json(name = "features")
	@SerializedName("features")
	var features: ArrayList<Feature> = arrayListOf<Feature>()

	init { type = "FeatureCollection" }

	fun addFeature(feature: Feature): FeatureCollection
	{
		features.add(feature)
		return this
	}

	override fun iterator(): Iterator<Feature> = features.iterator()
}
