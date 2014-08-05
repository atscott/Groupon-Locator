package com.groupon;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created with IntelliJ IDEA.
 * User: atscott
 * Date: 11/9/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationHelper
{
  private Context context;

  public LocationHelper(Context context)
  {
    this.context = context;
  }

  public LatLng GetCurrentLocation()
  {
    LocationManager locationManager;
    String	svcName=	Context.LOCATION_SERVICE;
    locationManager	=	(LocationManager)	context.getSystemService(svcName);

    //	Set	location	criteria
    Criteria criteria	=	new	Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    criteria.setPowerRequirement(Criteria.POWER_LOW);
    criteria.setAltitudeRequired(false);
    criteria.setBearingRequired(false);
    criteria.setSpeedRequired(false);
    criteria.setCostAllowed(true);

    //	Obtain	reference	to	location	provider
    String	provider	=	locationManager.getBestProvider(criteria,	true);
    Location l = locationManager.getLastKnownLocation(provider);
    if(l!= null){
    return	new LatLng(l.getLatitude(),l.getLongitude());
    }
    else{
      return new LatLng(0,0);
    }
  }
}
