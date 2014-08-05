package com.groupon.otherUI;

import com.google.android.gms.maps.model.LatLng;
import groupon.Deal;

/**
 * User: atscott
 * Date: 11/10/13
 * Time: 6:21 PM
 */
public class DealListItem
{
  public Deal deal;
  public LatLng location;
  public double distanceFromSearchOrigin;

  public DealListItem withDeal(Deal deal)
  {
    this.deal = deal;
    return this;
  }

  public DealListItem withLocation(LatLng location)
  {
    this.location = location;
    return this;
  }

  public DealListItem withDistanceFromSearchOrigin(double distance)
  {
    this.distanceFromSearchOrigin = distance;
    return this;
  }

}
