package com.brunocapezzali.gallerypreview;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GalleryPreview extends PopupWindow {

	/**
	 * Is the activity in which we want to show the <code>GalleryPreview</code>
	 */
	private Activity mActivity;
	
	/** 
	 * Are the images that we want to show into the <code>GalleryPreview</code>
	 */
	private ArrayList<Drawable> mImages;
	
	/**
	 * The number of images into the gallery
	 */
	private int mImagesCount;
	
	/**
	 * The index of the current image that we are showing into the <code>GalleryPreview</code>
	 */
	private int mCurrentImageIndex;

	/**
	 * Main <code>ImageView</code> that display the current photo inside the <code>GalleryPreview</code>
	 */
	private ImageView mImgView;
	
	/**
	 * This <code>TextView</code> shows the number of photos inside the <code>GalleryPreview</code>
	 */
	private TextView mTxtInfo;
	
	/**
	 * <code>ImageButton</code>s that allow the user to navigate the photos inside the 
	 * <code>GalleryPreview</code> 
	 */
	private ImageButton mBtnPrev, mBtnNext;

	/**
	 * <code>ImageButton</code> that allows the user to close the <code>GalleryPreview</code> 
	 */
	private ImageButton mBtnClose;

	/**
	 * Allow the creation of a <code>GalleryPreview</code> with a single image.
	 * @param activity where we want to open the <code>GalleryPreview</code>
	 * @param image that we want to display.
	 */
	public GalleryPreview(Activity activity, Drawable image) {
		super();
		ArrayList<Drawable> images = new ArrayList<Drawable>();
		images.add(image);
		initGalleryPopup(activity, images);
	}

	/**
	 * Allow the creation of a <code>GalleryPreview</code> with a sequence of images (gallery).
	 * @param activity where we want to open the <code>GalleryPreview</code>
	 * @param images that we want to display.
	 */
	public GalleryPreview(Activity activity, ArrayList<Drawable> images) {
		super();
		initGalleryPopup(activity, images);
	}

	/**
	 * Allow the basic creation of the <code>GalleryPreview</code>. This method is called
	 * from high-level constructors and do the real job.
	 * @param activity where we want to open the <code>GalleryPreview</code>
	 * @param images that we want to display.
	 */
	private void initGalleryPopup(Activity activity, ArrayList<Drawable> images) {
		mActivity = activity;
		mImages = images;
		mImagesCount = mImages.size();
		mCurrentImageIndex = 0;

		// create the basic structure of the layout
		LayoutInflater layoutInflater = (LayoutInflater)mActivity.getBaseContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(R.layout.gallery_preview, null); 
		setContentView(contentView);

		// animate the window when enter / exit from the activity
		setAnimationStyle(R.style.AnimationPopup);

		// we want a popup that fills the entire window
		setHeight(LayoutParams.MATCH_PARENT);
		setWidth(LayoutParams.MATCH_PARENT);

		// take a reference of the main imageView and the TextView for the status
		mImgView = (ImageView)contentView.findViewById(R.id.imgPhoto);
		mTxtInfo = (TextView)contentView.findViewById(R.id.txtInfo);

		// Create the click listener for the three buttons (prev, next, close)
		final GalleryPreview instance = this;
		Button.OnClickListener onClick = new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnPrev:
					updatePicture(--mCurrentImageIndex);
					break;

				case R.id.btnNext:
					updatePicture(++mCurrentImageIndex);
					break;

				case R.id.btnClose:
					instance.dismiss();
					break;
				}
			}
		};

		// take the reference for the close button and add the close action
		mBtnClose = (ImageButton)contentView.findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(onClick);

		// take the reference to the buttons prev and next
		mBtnPrev = (ImageButton)contentView.findViewById(R.id.btnPrev);
		mBtnNext = (ImageButton)contentView.findViewById(R.id.btnNext);

		/* The GalleryPreview window hide the prev, next buttons and the information text if
		 * we are managing a single image. */
		if ( mImagesCount == 1 ) {
			mTxtInfo.setVisibility(View.GONE);
			mBtnPrev.setVisibility(View.GONE);
			mBtnNext.setVisibility(View.GONE);
		} else {
			mBtnPrev.setOnClickListener(onClick);
			mBtnNext.setOnClickListener(onClick);
		}
	} 

	/**
	 * Allow to change the current picture displayed into the <code>GalleryPreview</code>.
	 * This method is called when the user tap on the prev and next buttons and if we need
	 * to open the <code>GalleryPreview</code> with a particular image instead the first.
	 * @param pictureIndex indicates which photo we want to show.
	 */
	private void updatePicture(int pictureIndex) {
		mCurrentImageIndex = pictureIndex < 0 ? mImagesCount-1 : pictureIndex % mImagesCount;
		mImgView.setImageDrawable(mImages.get(mCurrentImageIndex));
		mTxtInfo.setText( (mCurrentImageIndex+1) +" / "+ mImagesCount );
	}

	/**
	 * Open the <code>GalleryPreview</code> showing a particular photo.
	 * @param pictureIndex indicates which photo we want to show.
	 */
	public void showWithImageIndex(int pictureIndex) {
		View mainView = mActivity.findViewById(android.R.id.content);
		showAtLocation(mainView, Gravity.CENTER, 0, 0);
		updatePicture(pictureIndex);
	}

	/**
	 * Open the <code>GalleryPreview</code> showing the firts photo.
	 */
	public void show() {
		showWithImageIndex(0);
	}
}
