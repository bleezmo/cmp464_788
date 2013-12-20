package com.lehman.android;

import com.lehman.android.CustomListView.ItemModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ItemView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_view);
		Intent intent = this.getIntent();
		String title = intent.getStringExtra(ItemModel.ITEM_TITLE);
		String fullText = intent.getStringExtra(ItemModel.ITEM_FULL_TEXT);
		TextView tvTitle = (TextView) findViewById(R.id.item_view_title);
		TextView  tvFullText = (TextView) findViewById(R.id.item_view_full_text);
		tvTitle.setText(title);
		tvFullText.setText(fullText);
	}

}
