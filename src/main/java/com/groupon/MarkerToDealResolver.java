package com.groupon;

import com.google.android.gms.maps.model.Marker;
import groupon.Deal;

/**
 * User: atscott
 * Date: 11/12/13
 * Time: 9:31 PM
 */
public interface MarkerToDealResolver
{
    public Deal getDealFromMarker(Marker marker);
}
