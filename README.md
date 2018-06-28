# GeoGson

GeoGson is a Gson implementation of the GeoJson specification (with some unofficial object support)

## Types supported

1. Circle (unofficial support)
1. Feature
1. LineString
1. MultiLineString
1. Polygon
1. MultiPolygon
1. Point
1. MultiPoint

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
compile 'net.callumtaylor:GeoGson:1.6'
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
