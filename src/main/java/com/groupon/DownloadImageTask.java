package com.groupon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
  private final BitmapDownloadListener listener;
  private Object extraIdentifier;

  public interface BitmapDownloadListener
  {
    public void receiveBitmap(Bitmap image, Object extra);
  }

  public DownloadImageTask(BitmapDownloadListener listener, Object extraIdentifier)
  {
    this.listener = listener;
    this.extraIdentifier = extraIdentifier;
  }

  @Override
  protected Bitmap doInBackground(String... urls)
  {
    String urldisplay = urls[0];
    Bitmap mIcon11 = null;
    try
    {
      InputStream in = new java.net.URL(urldisplay).openStream();
      mIcon11 = BitmapFactory.decodeStream(in);
    } catch (Exception e)
    {
      Log.e("Error", e.getMessage());
      e.printStackTrace();
    }
    return mIcon11;
  }

  @Override
  protected void onPostExecute(Bitmap result)
  {
    listener.receiveBitmap(result, extraIdentifier);
  }
}