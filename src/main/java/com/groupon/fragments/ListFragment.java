package com.groupon.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.groupon.otherUI.DealAdapter;
import com.groupon.otherUI.DealListItem;
import com.groupon.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: atscott
 * Date: 9/18/13
 * Time: 3:41 PM
 */
public class ListFragment extends Fragment
{
  DealAdapter adapter;
  List<DealListItem> items;
  public interface IDealListActivity
  {
    public void onItemClicked(DealListItem clickedItem);
  }
  IDealListActivity listener;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_groupon_list, container, false);
    items = new ArrayList<>();
    adapter = new DealAdapter(getActivity(),R.layout.groupon_item_view, items);
    setupList(view);
    return view;
  }

  private void setupList(View view)
  {
    ListView list = (ListView) view.findViewById(R.id.grouponListView);
    list.setAdapter(adapter);
    list.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
      {
        listener.onItemClicked((DealListItem)adapterView.getItemAtPosition(i));
      }
    });
  }

  @Override
  public void onAttach(Activity activity)
  {
    super.onAttach(activity);
    if (activity instanceof IDealListActivity)
    {
      listener = (IDealListActivity) activity;
    }
    else
    {
      throw new ClassCastException(activity.toString() + " must implemenet IDealListActivity");
    }
  }

  public void AddItem(DealListItem item){
    items.add(item);
    Collections.sort(items, new ItemsByDistanceComparer());
    adapter.notifyDataSetChanged();
  }

  public void ClearItems()
  {
    items.clear();
    adapter.notifyDataSetChanged();
  }


  private class ItemsByDistanceComparer implements Comparator<DealListItem>
  {
    @Override
    public int compare(DealListItem dealListItem, DealListItem dealListItem2)
    {
      return dealListItem.distanceFromSearchOrigin < dealListItem2.distanceFromSearchOrigin ? -1 :
          dealListItem.distanceFromSearchOrigin > dealListItem2.distanceFromSearchOrigin ? 1 : 0;
    }
  }

}
