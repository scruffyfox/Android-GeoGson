package net.callumtaylor.geojson.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.callumtaylor.geojson.GeoJson;
import net.callumtaylor.geojson.LineString;
import net.callumtaylor.geojson.LngLatAlt;
import net.callumtaylor.geojson.MultiPoint;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class LineStringTest
{
	private Gson mapper = GeoJson.registerAdapters(new GsonBuilder()).create();

	@Test
	public void itShouldSerializeMultiPoint() throws Exception
	{
		MultiPoint lineString = new LineString(new LngLatAlt(100, 0), new LngLatAlt(101, 1));
		Assert.assertEquals("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}", mapper.toJson(lineString));
	}

	@Test
	public void itShouldDeserializeLineString() throws Exception
	{
		LineString lineString = mapper.fromJson("{\"coordinates\":[[100.0,0.0],[101.0,1.0]],\"type\":\"LineString\"}", LineString.class);
		assertNotNull(lineString);
		List<LngLatAlt> coordinates = lineString.getCoordinates();
		PointTest.assertLngLatAlt(100, 0, Double.NaN, coordinates.get(0));
		PointTest.assertLngLatAlt(101, 1, Double.NaN, coordinates.get(1));
	}
}
