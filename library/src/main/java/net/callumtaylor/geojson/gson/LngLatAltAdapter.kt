package net.callumtaylor.geojson.gson

import com.google.gson.*
import net.callumtaylor.geojson.LngLatAlt
import java.lang.reflect.Type

/**
 * LngLatAlt de-serialization in Gson
 */
open class LngLatAltAdapter : JsonSerializer<LngLatAlt>, JsonDeserializer<LngLatAlt>
{
	override fun serialize(src: LngLatAlt, typeOfSrc: Type, context: JsonSerializationContext): JsonElement
	{
		val array = JsonArray()
		array.add(JsonPrimitive(src.longitude))
		array.add(JsonPrimitive(src.latitude))
		if (src.hasAltitude())
		{
			array.add(JsonPrimitive(src.altitude))
		}

		return array
	}

	@Throws(JsonParseException::class)
	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LngLatAlt
	{
		val data = json.asJsonArray
		val node = LngLatAlt()

		node.longitude = data.get(0).asDouble
		node.latitude = data.get(1).asDouble
		node.altitude = data.get(2)?.asDouble ?: Double.NaN

		return node
	}
}
