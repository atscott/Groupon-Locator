package com.groupon;

import android.location.Location;
import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: atscott
 * Date: 11/9/13
 * Time: 6:38 PM
 */
public class GeocodeRetreiver
{
  private final String baseURL = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=";

  public LatLng RequestGeocode(String request)
  {
    request = request.replace(" ", "+");
    try
    {
      JSONObject response = getResponse(baseURL + request);
      JSONArray results = response.getJSONArray("results");
      for(int i=0;i<results.length();i++)
      {
        if(results.getJSONObject(i).has("geometry"))
        {
          JSONObject location = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
          double lat = location.getDouble("lat");
          double lng = location.getDouble("lng");
          return new LatLng(lat,lng);
        }
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return new LatLng(0, 0);
  }

  private JSONObject getResponse(String link) throws IOException, JSONException
  {
    URL url = new URL(link);
    URLConnection con = url.openConnection();
    InputStream in = con.getInputStream();
    String encoding = con.getContentEncoding();
    encoding = encoding == null ? "UTF-8" : encoding;
    String body = IOUtils.toString(in, encoding);
    return new JSONObject(body);
  }

  public static void main(String args[])
  {
    GeocodeRetreiver retreiver = new GeocodeRetreiver();
    retreiver.RequestGeocode("Milwaukee");
  }
}
