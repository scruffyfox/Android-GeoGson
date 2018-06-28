package net.callumtaylor.geojson.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.callumtaylor.geojson.GeoJson;
import net.callumtaylor.geojson.GeometryCollection;
import net.callumtaylor.geojson.LineString;
import net.callumtaylor.geojson.LngLatAlt;
import net.callumtaylor.geojson.Point;

import org.junit.Assert;
import org.junit.Test;

public class GeometryCollectionTest
{
	private Gson mapper = GeoJson.registerAdapters(new GsonBuilder()).create();

	@Test
	public void itShouldSerialize() throws Exception
	{
		GeometryCollection gc = new GeometryCollection();
		gc.add(new Point(100, 0));
		gc.add(new LineString(new LngLatAlt(101, 0), new LngLatAlt(102, 1)));

		String expected = "{\"geometries\":[{\"coordinates\":[100.0,0.0],\"type\":\"Point\"},"
				+ "{\"coordinates\":[[101.0,0.0],[102.0,1.0]],\"type\":\"LineString\"}],"
				+ "\"type\":\"GeometryCollection\"}";

		String generated = mapper.toJson(gc);

		Assert.assertEquals(expected, generated);
	}
}
