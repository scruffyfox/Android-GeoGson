package net.callumtaylor.geojson

import com.google.gson.annotations.SerializedName
import java.util.*

open class Crs
{
	@SerializedName("type") var type: String = "name"
	@SerializedName("properties") var properties: HashMap<String, Any>? = null
}
