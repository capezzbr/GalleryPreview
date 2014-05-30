package com.brunocapezzali.gallerypreview;

import java.io.InputStream;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			ImageView imgPhoto = (ImageView)rootView.findViewById(R.id.imgPhoto);
			customizeImagePreview(imgPhoto, "img0.JPG");
			imgPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View image) {
					ImageView imgView = (ImageView)image;
					new GalleryPreview(getActivity(), (BitmapDrawable)imgView.getDrawable()).show();
				}
			});
			
			return rootView;
		}
		
		public Bitmap readImageFromAssets(Context ctx, String filename) {
			InputStream is = null;
			try {
				is = ctx.getAssets().open(filename);
				return BitmapFactory.decodeStream(is);
			} catch (Exception ex) {}
			return null;
		}
		
		public void customizeImagePreview(ImageView imageView, String imageFilename) {
			if ( imageView == null || (imageFilename == null || imageFilename.length() == 0 ) ) {
				return;
			}
			
			Bitmap bitmap = readImageFromAssets(imageView.getContext(), imageFilename);
			if ( bitmap != null ) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}

}
