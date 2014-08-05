package com.groupon.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.gms.maps.model.*;
import com.groupon.*;
import com.groupon.fragments.GrouponMapFragment;
import com.groupon.fragments.ListFragment;
import com.groupon.otherUI.DealListItem;
import groupon.Deal;
import groupon.DealOption;
import groupon.RedemptionLocation;
import groupon.Tag;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MyActivity extends Activity implements GrouponDealRetreiver.DealRetreiverListener, GrouponMapFragment.IGrouponMapFragmentActivity, ListFragment.IDealListActivity
{
  LocationHelper locationHelper;
  GrouponDealRetreiver retreiver;
  GrouponMapFragment myGrouponMap;
  ProgressDialog progressDialog;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    progressDialog =  new ProgressDialog(this);
    setContentView(R.layout.main_screen);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    getFragmentManager().executePendingTransactions();
    myGrouponMap = (GrouponMapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
    locationHelper = new LocationHelper(getApplicationContext());
    sendGrouponRequest();
  }

  private void sendGrouponRequest()
  {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    int searchRadius = Integer.parseInt(sharedPrefs.getString("radius", "10"));
    boolean useCustomLocation = sharedPrefs.getBoolean("use_custom_location", false);
    String customLocation = sharedPrefs.getString("custom_address", "Milwaukee");

    setupRetreiver(searchRadius, useCustomLocation, customLocation);
    retreiver.execute("");
    progressDialog.setMessage("Fetching Groupons...");
    progressDialog.show();
  }

  private void setupRetreiver(int searchRadius, boolean useCustomLocation, String customLocation)
  {
    retreiver = new GrouponDealRetreiver()
        .WithListener(this)
        .WithRadius(searchRadius);
    if (useCustomLocation)
    {
      retreiver.WithGeocodeRequest(customLocation);
    }
    else
    {
      retreiver.WithDealLocation(locationHelper.GetCurrentLocation());
    }
  }

  @Override
  public void NotifyFinished()
  {
    List<Deal> deals = retreiver.GetResults();
    LatLng dealLocation = retreiver.DealLocation;
    if (deals == null)
      return;
    myGrouponMap.UpdateOriginLocation(retreiver.DealLocation);
    Map<LatLng, Deal> uniqueDealLocations = getUniqueDealLocations(deals);
    populateMapWithDeals(uniqueDealLocations, dealLocation);
    progressDialog.dismiss();
  }

  private Map<LatLng, Deal> getUniqueDealLocations(List<Deal> deals)
  {
    Map<LatLng, Deal> uniqueDealLocations = new LinkedHashMap<>();
    for (Deal deal : deals)
    {
      if (!deal.merchant.name.equals("Jazzercise"))
      {
        for (DealOption option : deal.dealOptions)
        {
          for (RedemptionLocation location : option.redemptionLocations)
          {
            LatLng optionLocation = new LatLng(location.lat, location.lng);
            if (!uniqueDealLocations.containsKey(optionLocation))
            {
              uniqueDealLocations.put(optionLocation, deal);
            }
          }
        }
      }
    }
    return uniqueDealLocations;
  }

  private void populateMapWithDeals(Map<LatLng, Deal> uniqueDealLocations, LatLng searchOrigin)
  {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    int searchRadius = Integer.parseInt(sharedPrefs.getString("radius", "10"));
    String categoryFilter = sharedPrefs.getString("category_filter", "All");
    for (LatLng location : uniqueDealLocations.keySet())
    {
      double distance = getDistanceInMiles(searchOrigin, location);
      if (distance < searchRadius)
      {
        Deal dealForLocation = uniqueDealLocations.get(location);
        if (dealIsInCategoryFilter(categoryFilter, dealForLocation))
        {
          myGrouponMap.AddDealToMap(location, dealForLocation);
          addDealToListIfLandscape(location, dealForLocation, distance);
        }
      }
    }
  }

  private boolean dealIsInCategoryFilter(String categoryFilter, Deal dealForLocation)
  {
    boolean dealIsInCategory = false;
    if (!categoryFilter.equals("All"))
    {
      for (Tag tag : dealForLocation.tags)
      {
        if (tag.name.equals(categoryFilter))
        {
          dealIsInCategory = true;
        }
      }
    }
    else
    {
      dealIsInCategory = true;
    }
    return dealIsInCategory;
  }


  private double getDistanceInMiles(LatLng point1, LatLng point2)
  {
    double metersToMiles = 0.000621371;
    float[] distanceResults = new float[3];
    Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, distanceResults);
    return distanceResults[0] * metersToMiles;
  }

  private void addDealToListIfLandscape(LatLng location, Deal dealForLocation, double distanceFromOrigin)
  {
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
    {
      ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
      if (listFragment != null)
      {
        listFragment.AddItem(new DealListItem().withDeal(dealForLocation).withLocation(location).withDistanceFromSearchOrigin(distanceFromOrigin));
      }
    }
  }

  @Override
  public void refreshMap()
  {
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
    {
      ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
      if (listFragment != null)
      {
        listFragment.ClearItems();
      }
    }
    myGrouponMap.ClearMap();
    sendGrouponRequest();
  }

  @Override
  public void onItemClicked(DealListItem clickedItem)
  {
    myGrouponMap.SelectMarkerAtLocation(clickedItem.location);
  }
}
