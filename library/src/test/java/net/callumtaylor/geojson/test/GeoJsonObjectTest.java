package net.callumtaylor.geojson.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.callumtaylor.geojson.GeoJson;
import net.callumtaylor.geojson.GeoJsonObject;

import org.junit.Assert;
import org.junit.Test;

public class GeoJsonObjectTest
{
	private Gson mapper = GeoJson.registerAdapters(new GsonBuilder()).create();

	private class TestGeoJsonObject extends GeoJsonObject
	{
		@Override public void finishPopulate()
		{
		}
	}

	@Test public void itShouldHaveProperties() throws Exception
	{
		TestGeoJsonObject testObject = new TestGeoJsonObject();
		testObject.setProperty("property", "value");
		Assert.assertNotNull(testObject.getProperties());
	}

	@Test public void itShouldNotSerializeEmptyProperties() throws Exception
	{
		TestGeoJsonObject testObject = new TestGeoJsonObject();
		Assert.assertEquals("{\"type\":\"TestGeoJsonObject\"}", mapper.toJson(testObject));
	}
}
