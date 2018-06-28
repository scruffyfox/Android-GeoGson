package net.callumtaylor.geojson.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.callumtaylor.geojson.GeoJson;
import net.callumtaylor.geojson.LngLatAlt;
import net.callumtaylor.geojson.MultiPolygon;
import net.callumtaylor.geojson.Polygon;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiPoligonTest
{
	private Gson mapper = GeoJson.registerAdapters(new GsonBuilder()).create();

	@Test
	public void itShouldSerialize() throws Exception
	{
		MultiPolygon multiPolygon = new MultiPolygon();
		multiPolygon.add(new Polygon(new LngLatAlt(102, 2), new LngLatAlt(103, 2), new LngLatAlt(103, 3), new LngLatAlt(102, 3), new LngLatAlt(102, 2)));
		Polygon polygon = new Polygon(MockData.EXTERNAL);
		polygon.addInteriorRing(MockData.INTERNAL);
		multiPolygon.add(polygon);
		assertEquals(
				"{\"coordinates\":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],"
						+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
						+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]],\"type\":\"MultiPolygon\"}",
				mapper.toJson(multiPolygon));
	}

	@Test
	public void itShouldDeserialize() throws Exception
	{
		MultiPolygon multiPolygon = mapper.fromJson(
				"{\"coordinates\":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],"
						+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
						+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]],\"type\":\"MultiPolygon\"}", MultiPolygon.class
		);
		assertEquals(2, multiPolygon.getCoordinates().size());
	}
}
