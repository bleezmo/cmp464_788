package com.lehman.android;

import com.lehman.android.Global.ListItem;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RssListFragment extends ListFragment{
	private boolean dualPaneMode;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ArrayAdapter<ListItem> adapter = new ArrayAdapter<ListItem>(getActivity(),android.R.layout.simple_list_item_1,Global.rsslist);
		setListAdapter(adapter);
		return inflater.inflate(R.layout.rss_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        View detailsFrame = getActivity().findViewById(R.id.rss_details_fragment);
        dualPaneMode = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        if(dualPaneMode){
        	showDetails(0);
        }
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(dualPaneMode){
			showDetails(position);
		}
	}
	
	private void showDetails(int i) {
		if(dualPaneMode){
			//first, show that the list item was clicked
			getListView().setItemChecked(i, true);
			RssDetailsFragment detailsFragment = (RssDetailsFragment) getFragmentManager().findFragmentById(R.id.rss_details_fragment);
			if(detailsFragment == null){
				
			}
		}
	}
	
}
