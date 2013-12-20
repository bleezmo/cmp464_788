package com.lehman.android;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CustomListView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_list);
		ListView lv = (ListView) findViewById(R.id.custom_list);
		ItemModel[] items = new ItemModel[]{
				new ItemModel("Thrall","Son of Durotan. Leader of the orcs"),
				new ItemModel("Jaina Proudmore"," the founder and former ruler of Theramore Isle, the Alliance's major port in southern Kalimdor"),
				new ItemModel("Arthas Menethil","Crown Prince of Lordaeron and Knight of the Silver Hand")
			};
		lv.setAdapter(new CustomAdapter(this,new ArrayList<ItemModel>(Arrays.asList(items))));
	}
	
	public class ItemModel{
		public static final String ITEM_TITLE = "ITEM_TITLE";
		public static final String ITEM_FULL_TEXT = "ITEM_FULL_TEXT";
		private String title;
		private String fullText;
		public ItemModel(String title, String fullText){
			this.title = title;
			this.fullText = fullText;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getFullText() {
			return fullText;
		}
		public void setFullText(String fullText) {
			this.fullText = fullText;
		}
	}
	
	public static class CustomAdapter extends BaseAdapter{
		private ArrayList<ItemModel> list;
		private Activity ctx;
		public CustomAdapter(Activity ctx, ArrayList<ItemModel> list){
			this.list = list;
			this.ctx = ctx;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if(convertView == null){
				LayoutInflater inflater = ctx.getLayoutInflater();
				view = inflater.inflate(R.layout.custom_item_list, null);
			}else{
				view = convertView;
			}
			TextView tv = (TextView) view.findViewById(R.id.item_title);
			Button btn = (Button) view.findViewById(R.id.view_item);
			if(position < list.size()){
				final ItemModel item = list.get(position);
				tv.setText(item.getTitle());
				btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ctx,ItemView.class);
						intent.putExtra(ItemModel.ITEM_TITLE, item.getTitle());
						intent.putExtra(ItemModel.ITEM_FULL_TEXT, item.getFullText());
						ctx.startActivity(intent);
					}
				});
			}
			return view;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
