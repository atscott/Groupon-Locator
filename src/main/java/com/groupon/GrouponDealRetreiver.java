package com.groupon;

import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import groupon.Deal;
import groupon.DealFinder;
import groupon.Division;
import location.DealLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * User: atscott
 * Date: 11/9/13
 * Time: 4:00 PM
 */
public class GrouponDealRetreiver extends AsyncTask<String, Void, String>
{
  private List<Deal> results;
  public LatLng DealLocation;
  private String geocodeRequest;
  private List<DealRetreiverListener> listeners = new ArrayList<>();
  private int searchRadius = 50;

  public void addListener(DealRetreiverListener listener)
  {
    listeners.add(listener);
  }

  public List<Deal> GetResults()
  {
    return results;
  }

  public interface DealRetreiverListener
  {
    public void NotifyFinished();
  }

  public GrouponDealRetreiver WithListener(DealRetreiverListener listener)
  {
    listeners.add(listener);
    return this;
  }

  public GrouponDealRetreiver WithDealLocation(LatLng location)
  {
    DealLocation = location;
    return this;
  }

  public GrouponDealRetreiver WithGeocodeRequest(String request)
  {
    geocodeRequest = request;
    return this;
  }

  public GrouponDealRetreiver WithRadius(int radius)
  {
    searchRadius = radius;
    return this;
  }

  protected String doInBackground(String... urls)
  {
    updateDealLocationIfGeocodeRequest();
    if (DealLocation != null)
    {
      results = new ArrayList<>();
      DealFinder finder = new DealFinder();
      results = finder.GetDealsNearLocation(DealLocation.latitude, DealLocation.longitude, searchRadius);
      return "OK";
    }
    else
    {
      return "FAIL";
    }
  }

  private void updateDealLocationIfGeocodeRequest()
  {
    if (geocodeRequest != null)
    {
      GeocodeRetreiver retreiver = new GeocodeRetreiver();
      DealLocation = retreiver.RequestGeocode(geocodeRequest);
    }

  }

  @Override
  protected void onPostExecute(String result)
  {
    for (DealRetreiverListener listener : listeners)
    {
      listener.NotifyFinished();
    }
  }
}
