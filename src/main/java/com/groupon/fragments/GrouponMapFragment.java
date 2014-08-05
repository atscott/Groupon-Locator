package com.groupon.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import com.groupon.MarkerToDealResolver;
import com.groupon.R;
import com.groupon.activities.SettingsActivity;
import com.groupon.otherUI.GrouponInfoWindowAdapter;
import groupon.Deal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: atscott
 * Date: 9/18/13
 * Time: 3:41 PM
 */
public class GrouponMapFragment extends Fragment implements MarkerToDealResolver
{
  private GoogleMap googleMap;
  Map<Marker, Deal> markersOnMap;
  private IGrouponMapFragmentActivity listener;

  public interface IGrouponMapFragmentActivity
  {
    public void refreshMap();
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {

    View view = inflater.inflate(R.layout.fragment_map, container, false);
    view = initMap(inflater, container, view);
    Fragment fragment = getFragmentManager().findFragmentById(R.id.map);
    googleMap = ((MapFragment)fragment).getMap();
    googleMap.setInfoWindowAdapter(new GrouponInfoWindowAdapter(view.getContext(), this));
    setupSettingsButton(view);
    setupRefreshButton(view);
    setupOnInfoWindowClick();
    markersOnMap = new LinkedHashMap<>();
    return view;
  }

  private View initMap(LayoutInflater inflater, ViewGroup container, View view)
  {
    if (view != null) {
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null)
        parent.removeView(view);
    }
    try {
      view = inflater.inflate(R.layout.fragment_map, container, false);
    } catch (InflateException e) {
        /* map is already there, just return view as it is */
    }
    return view;
  }

  @Override
  public Deal getDealFromMarker(Marker marker)
  {
    return markersOnMap.get(marker);
  }

  public void AddDealToMap(LatLng location, Deal dealForLocation)
  {
    Marker marker = googleMap.addMarker(new MarkerOptions()
        .position(location)
        .title(dealForLocation.shortAnnouncementTitle)
        .icon(BitmapDescriptorFactory.fromAsset("grouponMapIcon.png"))
        .snippet("Merchant: " + dealForLocation.merchant.name));
    markersOnMap.put(marker,dealForLocation);
  }

  private void setupOnInfoWindowClick()
  {
    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
      @Override
      public void onInfoWindowClick(Marker marker) {
        Deal dealForMarker = markersOnMap.get(marker);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(dealForMarker.dealUrl));
        startActivity(i);
      }
    });

  }

  private void setupSettingsButton(View view)
  {
    ImageButton settingsButton = (ImageButton) view.findViewById(R.id.settingsButton);
    settingsButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        showPreferences();
      }
    });
  }

  private void showPreferences()
  {
    Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
    startActivity(intent);
  }

  private void setupRefreshButton(View view)
  {
    ImageButton refreshButton = (ImageButton) view.findViewById(R.id.refreshButton);
    refreshButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        listener.refreshMap();
      }
    });
  }

  public void ClearMap()
  {
    googleMap.clear();
    markersOnMap.clear();
  }

  @Override
  public void onAttach(Activity activity)
  {
    super.onAttach(activity);
    if (activity instanceof IGrouponMapFragmentActivity)
    {
      listener = (IGrouponMapFragmentActivity) activity;
    }
    else
    {
      throw new ClassCastException(activity.toString() + " must implemenet IGrouponMapFragmentActivity");
    }
  }

  public void UpdateOriginLocation(LatLng location)
  {

    if (location != null)
    {
      CircleOptions currentLocationCircle = new CircleOptions()
          .center(location)
          .radius(20)
          .strokeColor(Color.BLUE)
          .fillColor(0xff6666ff);
      googleMap.addCircle(currentLocationCircle);
      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 12, 0, 0)));
    }
  }

  public void SelectMarkerAtLocation(LatLng location)
  {
    Marker[] markers = markersOnMap.keySet().toArray(new Marker[markersOnMap.size()]);
    double epsilon = .000001;
    for(Marker marker : markers)
    {
      boolean sameLats = Math.abs(marker.getPosition().latitude - location.latitude) < epsilon;
      boolean sameLngs = Math.abs(marker.getPosition().longitude - location.longitude) < epsilon;
        if(sameLats && sameLngs)
        {
          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 13, 0, 0)));
          marker.showInfoWindow();

          break;
        }
    }
  }
}
