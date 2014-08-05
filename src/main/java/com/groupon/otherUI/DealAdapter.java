package com.groupon.otherUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.groupon.R;

import java.util.List;

/**
 * User: atscott
 * Date: 11/10/13
 * Time: 4:13 PM
 */
public class DealAdapter extends ArrayAdapter<DealListItem>
{
  int layoutResourceId;
  Context context;
  List<DealListItem> data;

  public DealAdapter(Context context, int textViewResourceId, List<DealListItem> data)
  {
    super(context, textViewResourceId, data);
    layoutResourceId = textViewResourceId;
    this.context = context;
    this.data = data;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = convertView;

    DealHolder holder;
    if(row == null)
    {
      LayoutInflater inflater = ((Activity)context).getLayoutInflater();
      row = inflater.inflate(layoutResourceId, parent, false);

      holder = new DealHolder();
      holder.dealInfo = (TextView)row.findViewById(R.id.dealText);
      holder.sold = (TextView)row.findViewById(R.id.soldText);
      holder.merchant = (TextView)row.findViewById(R.id.merchantText);
      holder.distance = (TextView)row.findViewById(R.id.distanceText);

      row.setTag(holder);
    }
    else
    {
      holder = (DealHolder)row.getTag();
    }

    DealListItem dealListItem = data.get(position);
    holder.dealInfo.setText(dealListItem.deal.shortAnnouncementTitle);
    holder.merchant.setText(dealListItem.deal.merchant.name);
    holder.distance.setText(String.format("%.2f miles", dealListItem.distanceFromSearchOrigin));
    holder.sold.setText(Integer.toString(dealListItem.deal.soldQuantity));

    return row;
  }

  private class DealHolder
  {
    TextView sold;
    TextView dealInfo;
    TextView distance;
    TextView merchant;
  }
}