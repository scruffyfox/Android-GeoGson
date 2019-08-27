# GeoGson

GeoGson is a Gson and Moshi implementation of the GeoJson specification (with some unofficial object support)

## Types supported

1. Circle (unofficial support)
1. Feature
1. FeatureCollection
1. LineString
1. MultiLineString
1. Polygon
1. MultiPolygon
1. Point
1. MultiPoint
1. GeometryCollection

## Usage with Moshi
Simply pass your moshi builder to `GeoMoshi.registerAdapters()`, or use the following code to register the adapters manually

```kotlin
	Moshi.Builder().add(Point::class.java, PointJsonAdapter())
	.add(Circle::class.java, CircleJsonAdapter())
	.add(MultiPoint::class.java, MultiPointJsonAdapter())
	.add(LineString::class.java, LineStringJsonAdapter())
	.add(MultiLineString::class.java, MultiLineStringJsonAdapter())
	.add(Polygon::class.java, PolygonJsonAdapter())
	.add(MultiPolygon::class.java, MultiPolygonJsonAdapter())
	.add(Feature::class.java, FeatureJsonAdapter())
	.add(FeatureCollection::class.java, FeatureCollectionJsonAdapter())
	.add(GeometryCollection::class.java, GeometryCollectionJsonAdapter())
	.add(GeoJsonObject::class.java, GeoJsonObjectMoshiAdapter())
	.add(LngLatAlt::class.java, LngLatAltMoshiAdapter())
	.build()
```

To deserialise json, you can either deserialise to a known class type, or use `GeoJsonObject` as the class type and it will auto decode into the correct object, but will return as type `GeoJsonObject`

example:

```kotlin
GeoMoshi.registerAdapters(Moshi.Builder()).build()
	.adapter(GeoJsonObject::class.java).fromJson(/* json */)
```

## Usage with custom gson parsers

In order for the gson to correctly inflate into geojson objects, you must make sure to include the type adapter so that gson can detect and inflate the geojson objects correctly.

```java
GsonBuilder builder = new GsonBuilder();
builder.registerTypeAdapter(GeoJsonObject.class, new GeoJsonObjectAdapter());
builder.registerTypeAdapter(LngLatAlt.class, new LngLatAltAdapter());
```

You can use the methods found in `GeoGson` to automatically add these adapters to your builder object, or create a new gson instance with the adapters already added

```java
GeoJson.getGson()
GeoJson.registerAdapters(GsonBuilder builder)
```

## Example

```java
GsonBuilder builder = new GsonBuilder();
GeoJson.registerAdapters(builder);

GeoJsonObject point = builder.create().fromJson("{ \"type\": \"Point\", \"coordinates\": [100.0, 0.0] }", GeoJsonObject.class);
System.out.println(point instanceof Point);
```

This will automatically parse the provided JSON string into its correct class (in this case, a point)

## Maven

To include the project, add the following to your `build.gradle`

```
compile 'net.callumtaylor:geogson:2.0.2'
```

## License

	Copyright [2018] [Callum Taylor]

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

See [LICENSE](LICENSE)
