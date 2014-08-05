package location;

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

  public void RequestGeocode(String request)
  {

    request = request.replace(" ", "+");
    try
    {
      JSONObject response = getResponse(baseURL + request);
      JSONArray test = response.getJSONArray("results");
      for(int i=0;i<test.length();i++)
      {
        if(test.getJSONObject(i).has("geometry"))
        {
          JSONObject location = test.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
          double lat = location.getDouble("lat");
          double lng = location.getDouble("lng");
        }
      }
      JSONObject location = response.getJSONObject("results").getJSONObject("geometry").getJSONObject("location");
//      return new LatLng(Double.parseDouble(location.getString("lat")),Double.parseDouble(location.getString("lng")));
    } catch (Exception e)
    {
      e.printStackTrace();
//      return new LatLng(0, 0);
    }
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
