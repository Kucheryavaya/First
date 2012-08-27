package com.vm.covercam.json;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Kucheryavaya Lyudmila
 */

public class JSONParser {
	
	private JSONObject jObject;
	
	
	public String parseLinkImage(String jString) throws Exception {
		
		String attributeLinks = null;
		String attributeLarge = null;
		
		jObject = new JSONObject(jString);
		
		JSONArray imagesArray = jObject.getJSONArray("images");
		attributeLinks = imagesArray.getJSONObject(0).getString("links").toString();
		JSONObject linksObject = new JSONObject(attributeLinks);
		attributeLarge = linksObject.getString("large");
		
		return attributeLarge;
	}
}
