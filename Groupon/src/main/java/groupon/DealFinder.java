package groupon;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * User: atscott
 * Date: 11/9/13
 * Time: 11:22 AM
 */
public class DealFinder
{
  private final String CLIENT_KEY = "4b1f99001fcf62f09e58763a8a8e39b337f44bd4";

  public List<Deal> GetDealsForDivision(String division)
  {
    JSONArray dealsJsonArray = GetDealsJsonArray(division);
    List<Deal> deals = new Gson().fromJson(dealsJsonArray.toString(), new TypeToken<List<Deal>>(){}.getType());
    return deals;
  }
  public List<Deal> GetDealsNearLocation(double lat, double lng, double radius)
  {
    JSONArray dealsJsonArray = GetDealsJsonArray(lat,lng,radius);
    List<Deal> deals = new Gson().fromJson(dealsJsonArray.toString(), new TypeToken<List<Deal>>(){}.getType());
    return deals;
  }

  private JSONArray GetDealsJsonArray(String division)
  {
    JSONArray deals = new JSONArray();
    try
    {
      JSONObject response = getResponse("https://api.groupon.com/v2/deals.json?client_id=" + CLIENT_KEY + "&division_id=" + division);
      deals = response.getJSONArray("deals");
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    return deals;
  }

  private JSONArray GetDealsJsonArray(double lat, double lng, double radius)
  {
    JSONArray deals = new JSONArray();
    try
    {
      JSONObject response = getResponse("https://api.groupon.com/v2/deals.json?client_id=" + CLIENT_KEY + "&lat=" + lat + "&lng=" + lng + "&radius=" +radius);
      deals = response.getJSONArray("deals");
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    return deals;
  }

  public List<Division> GetDivisions()
  {
    List<Division> divisions;
    try
    {
      URL url = new URL("https://api.groupon.com/v2/divisions.json?client_id=" + CLIENT_KEY);
      URLConnection con = url.openConnection();
      InputStream in = con.getInputStream();
      String encoding = con.getContentEncoding();
      encoding = encoding == null ? "UTF-8" : encoding;
      String body = IOUtils.toString(in, encoding);
      JSONObject response = new JSONObject(body);
      JSONArray divisionsArray = response.getJSONArray("divisions");
      divisions = new Gson().fromJson(divisionsArray.toString(), new TypeToken<List<Division>>(){}.getType());
    } catch (Exception e)
    {
      divisions = new ArrayList<>();
    }
    return divisions;
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
}
