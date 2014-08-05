package com.groupon.otherUI;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.groupon.DownloadImageTask;
import com.groupon.MarkerToDealResolver;
import com.groupon.R;
import groupon.Deal;

/**
 * User: atscott
 * Date: 11/12/13
 * Time: 8:24 PM
 */
public class GrouponInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, DownloadImageTask.BitmapDownloadListener
{
  private Marker myMarker;
  private View view;
  private Bitmap image;
  private MarkerToDealResolver resolver;
  private Deal dealForCurrentImage;
  private Deal dealForLastMarkerRequested;

  @Override
  public void receiveBitmap(Bitmap image, Object extraIdentifier)
  {
    dealForCurrentImage = (Deal) extraIdentifier;
    this.image = image;
    if(dealForCurrentImage.mediumImageUrl.equals(dealForLastMarkerRequested.mediumImageUrl)){
      myMarker.showInfoWindow();
    }
  }

  public GrouponInfoWindowAdapter(Context context, MarkerToDealResolver resolver)
  {
    LayoutInflater inflater = LayoutInflater.from(context);
    view = inflater.inflate(R.layout.groupon_info_window, null);
    this.resolver = resolver;
  }

  @Override
  public View getInfoWindow(Marker marker)
  {
    myMarker = marker;
    dealForLastMarkerRequested = resolver.getDealFromMarker(marker);
    ((TextView) view.findViewById(R.id.titleText)).setText(marker.getTitle());
    ((TextView) view.findViewById(R.id.snippetText)).setText(marker.getSnippet());
    ((TextView) view.findViewById(R.id.soldText)).setText(dealForLastMarkerRequested.soldQuantity + " sold");
    if (image != null && dealForLastMarkerRequested.mediumImageUrl.equals(dealForCurrentImage.mediumImageUrl))
    {
      ((ImageView) view.findViewById(R.id.dealImage)).setImageBitmap(image);
    }
    else
    {
      ((ImageView)view.findViewById(R.id.dealImage)).setImageResource(android.R.color.transparent);
      downloadImage(marker);
    }
    return view;
  }

  private void downloadImage(Marker marker)
  {
    Deal dealForMarker = resolver.getDealFromMarker(marker);
    DownloadImageTask task = new DownloadImageTask(this, dealForMarker);
    task.execute(dealForMarker.mediumImageUrl);
  }

  @Override
  public View getInfoContents(Marker marker)
  {
    return view;
  }
}
