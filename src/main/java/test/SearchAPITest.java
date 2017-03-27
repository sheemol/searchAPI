package test;

import java.io.IOException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SearchAPITest {
	// Test SearchAPI

	String url = "https://itunes.apple.com/search?";

	// search with term itunes

	@Test
	@Produces("application/json")
	public void testSearchbyTerm() throws JsonParseException,
			JsonMappingException, IOException {
		Client client = Client.create();
		String key = "term";
		String value = "iTunes";

		WebResource webResource = client.resource(
				"https://itunes.apple.com/search?").queryParam(key, value);

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);
		String res = response.getEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		ResultResponse result = mapper.readValue(res, ResultResponse.class);
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertEquals(result.getResultCount(), 50);

	}

	// search with no query param
	@Test
	public void testSearchbyNoQueryParam() throws JsonParseException,
			JsonMappingException, IOException {

		Client client = Client.create();

		WebResource webResource = client
				.resource("https://itunes.apple.com/search?");

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);

		String res = response.getEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		ResultResponse result = mapper.readValue(res, ResultResponse.class);
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertEquals(result.getResultCount(), 0);
	}

	// search with term with invalid parameter
	@Test
	public void testSearchbyTermInvalid() throws JsonParseException,
			JsonMappingException, IOException {

		Client client = Client.create();
		String key = "key";
		String value = "INVALID";
		WebResource webResource = client.resource(
				"https://itunes.apple.com/search?").queryParam(key, value);

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);
		String res = response.getEntity(String.class);

		ObjectMapper mapper = new ObjectMapper();
		ResultResponse result = mapper.readValue(res, ResultResponse.class);
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertEquals(result.getResultCount(), 0);
	}

	// search with valid term with valid country
	@Test
	public void testSearchbyTermCountry() throws JsonParseException,
			JsonMappingException, IOException {

		Client client = Client.create();
		MultivaluedMap<String, String> m = new MultivaluedHashMap<String, String>();
		m.add("term", "itunes");
		m.add("country", "US");
		m.add("media", "music");
		WebResource webResource = client.resource(
				"https://itunes.apple.com/search?").queryParams(m);

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);
		String res = response.getEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		ResultResponse result = mapper.readValue(res, ResultResponse.class);
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertEquals(result.getResultCount(), 50);
	}

	// search with valid term with invalid country
	@Test
	public void testSearchbyTermInValidCountry() throws Exception {

		Client client = Client.create();
		MultivaluedMap<String, String> m = new MultivaluedHashMap<String, String>();
		m.add("term", "itunes");
		m.add("country", "UP");

		WebResource webResource = client.resource(
				"https://itunes.apple.com/searches?").queryParams(m);

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);
		Assert.assertEquals(response.getStatusInfo().getStatusCode(), 404);
	}

}
