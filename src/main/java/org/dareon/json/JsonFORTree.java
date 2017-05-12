package org.dareon.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.dareon.domain.FOR;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonFORTree
{

    public static JSONObject addChildren(JSONObject fOR, List<FOR> fORChildren)
    {
//	System.out.println(fOR);
	JSONArray fORChildrenArray = new JSONArray();
	for(FOR a : fORChildren)
	{
	    JSONObject fORChildObject = new JSONObject();
	    fORChildObject.put("text", a.getCode() + " | " + a.getName());
	    fORChildObject.put("id", a.getId().toString());
	    fORChildObject.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
	    if(a.getChildren().size() > 0)
		addChildren(fORChildObject, a.getChildren());
//	    System.out.println(fORChildObject);
	    fORChildrenArray.put(fORChildObject);
	}
	
	fOR.put("nodes", fORChildrenArray);
	System.out.println("final: "+fOR);
	return fOR;
    }
   

}