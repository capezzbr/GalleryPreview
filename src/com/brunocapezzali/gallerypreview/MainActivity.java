package com.brunocapezzali.gallerypreview;

import java.io.InputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

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
			setImageAsPreview(imgPhoto, "img0.JPG");
			
			LinearLayout galleryContainer = (LinearLayout)rootView.findViewById(R.id.layoutGallery);
			createGallery(galleryContainer, new String[]{"img1.JPG", "img2.JPG", "img3.JPG", "img4.JPG"}, 90);
			
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
		
		public void setImageAsPreview(ImageView imageView, String imageFilename) {
			if ( imageView == null || (imageFilename == null || imageFilename.length() == 0 ) ) {
				return;
			}
			
			final Bitmap bitmap = readImageFromAssets(imageView.getContext(), imageFilename);
			if ( bitmap != null ) {
				imageView.setImageBitmap(bitmap);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View image) {
						GalleryPreview gallery = new GalleryPreview(getActivity(), 
								new BitmapDrawable(image.getResources(), bitmap));
						gallery.show();
					}
				});
			}
		}
		
		public void createGallery(LinearLayout container, String images[], int thumbnailSize) {
			if ( images.length == 0 ) {
				container.getLayoutParams().height = 0;
				return;
			}
			
			final ArrayList<Drawable> drawableImages = new ArrayList<Drawable>();
			OnClickListener onClick = new OnClickListener() {
				@Override
				public void onClick(View v) {
					GalleryPreview gallery = new GalleryPreview(getActivity(), drawableImages);
					gallery.showWithImageIndex((Integer) v.getTag());
				}
			};
			
			for ( int i=0; i<images.length; i++ ) {
				Bitmap bitmap = readImageFromAssets(container.getContext(), images[i]);
				if ( bitmap != null ) {
					drawableImages.add(new BitmapDrawable(container.getResources(), bitmap));	
					
					// create a thumbnail ImageView for every image 
					ImageView image = new ImageView(container.getContext());
					image.setScaleType(ScaleType.FIT_CENTER);
					image.setBackgroundResource(R.drawable.gallery_preview_button);
					image.setClickable(true);
					LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
					parms.setMargins(6, 0, 6, 0);
					image.setLayoutParams(parms);
					image.setImageBitmap(bitmap);

					container.addView(image);
					image.setTag(i); // for the click event
					image.setOnClickListener(onClick);
				}
			}
		}
	}

}
