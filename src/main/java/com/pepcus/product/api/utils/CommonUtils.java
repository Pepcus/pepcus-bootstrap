package com.pepcus.product.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.pepcus.product.api.constant.ApplicationConstant;
import com.pepcus.product.api.exception.APIErrorCodes;
import com.pepcus.product.api.exception.ApplicationException;

public class CommonUtils {

  /**
   * Method return jsonobject fetch by bufferreader 
   * 
   * @param reader
   * @return
   */
  public static JSONObject readJsonObject(BufferedReader reader) {

    JSONParser parser = new JSONParser();
    JSONObject jsonObject = new JSONObject();
    StringBuffer fileContent = new StringBuffer();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        fileContent.append(line).append("\n");
      }
      parser = new JSONParser();
      jsonObject = (JSONObject) parser.parse(fileContent.toString());
    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        // TODO
        e.printStackTrace();
      }
    }
    return jsonObject;
  }


  /**
   * This method parse JSON STring to JSON Object
   * @param jsonString
   * @return
   */
  public static JSONObject readJsonObject(String jsonString){
    JSONParser parser = new JSONParser();
    try {
      return (JSONObject) parser.parse(jsonString);
    } catch (ParseException e) {
      //TODO : Log Exception
      throw new ApplicationException(APIErrorCodes.APPLICATION_ERROR);
    }
  }

  /**
   * This method parse JSON STring to JSON Array
   * @param jsonString
   * @return
   */
  public static JSONArray readJsonArray(String jsonString){
    JSONParser parser = new JSONParser();
    try {
      return (JSONArray) parser.parse(jsonString);
    } catch (ParseException e) {
      //TODO : Log Exception
      throw new ApplicationException(APIErrorCodes.APPLICATION_ERROR);
    }
  }

  /**
   * Method to fetch JsonArray from JsonObject
   * 
   * @param jsonObject
   * @param jsonArrayName
   * @return JSONArray object
   */
  public static JSONArray readJsonArrayFromJSON(JSONObject jsonObject, String jsonArrayName) {

    JSONArray jsonArray = (JSONArray) jsonObject.get(jsonArrayName);
    return jsonArray;
  }

 

}
