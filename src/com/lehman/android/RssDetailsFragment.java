package com.lehman.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RssDetailsFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.rss_details, container, false);
		TextView tv = (TextView) v.findViewById(R.id.rss_details_text);
		tv.setText(Global.rsslist[currentListItemIndex()].description);
		return v;
	}

	public static RssDetailsFragment newInstance(int listItemIndex){
		RssDetailsFragment fragment = new RssDetailsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("index", listItemIndex);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	public int currentListItemIndex(){
		return getArguments().getInt("index");
	}
	
}
