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

	private Activity mActivity;
	private ArrayList<Drawable> mImages;
	private int mImagesCount;
	private int mCurrentImageIndex;

	private ImageView mImgView;
	private TextView mTxtInfo;
	private Button.OnClickListener mClickListener;
	private ImageButton mBtnPrev, mBtnNext, mBtnClose;

	public GalleryPreview(Activity activity, Drawable image) {
		super();
		ArrayList<Drawable> images = new ArrayList<Drawable>();
		images.add(image);
		initGalleryPopup(activity, images);
	}

	public GalleryPreview(Activity activity, ArrayList<Drawable> images) {
		super();
		initGalleryPopup(activity, images);
	}

	private void initGalleryPopup(Activity activity, ArrayList<Drawable> images) {
		mActivity = activity;
		mImages = images;
		mImagesCount = mImages.size();
		mCurrentImageIndex = 0;

		LayoutInflater layoutInflater = (LayoutInflater)mActivity.getBaseContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(R.layout.gallery_preview, null); 
		setContentView(contentView);

		setHeight(LayoutParams.MATCH_PARENT);
		setWidth(LayoutParams.MATCH_PARENT);

		mImgView = (ImageView)contentView.findViewById(R.id.imgPhoto);
		mTxtInfo = (TextView)contentView.findViewById(R.id.txtInfo);

		final GalleryPreview instance = this;
		mClickListener = new Button.OnClickListener() {
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

		mBtnClose = (ImageButton)contentView.findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(mClickListener);

		mBtnPrev = (ImageButton)contentView.findViewById(R.id.btnPrev);
		mBtnNext = (ImageButton)contentView.findViewById(R.id.btnNext);

		if ( mImagesCount == 1 ) {
			mTxtInfo.setVisibility(View.GONE);
			mBtnPrev.setVisibility(View.GONE);
			mBtnNext.setVisibility(View.GONE);
		} else {
			mBtnPrev.setOnClickListener(mClickListener);
			mBtnNext.setOnClickListener(mClickListener);
		}

	} 

	private void updatePicture(int pictureIndex) {
		mCurrentImageIndex = pictureIndex < 0 ? mImagesCount-1 : pictureIndex % mImagesCount;
		mImgView.setImageDrawable(mImages.get(mCurrentImageIndex));
		mTxtInfo.setText( (mCurrentImageIndex+1) +" / "+ mImagesCount );
	}

	public void showWithImageIndex(int pictureIndex) {
		View mainView = mActivity.findViewById(android.R.id.content);
		showAtLocation(mainView, Gravity.CENTER, 0, 0);
		updatePicture(pictureIndex);
	}

	public void show() {
		showWithImageIndex(0);
	}
}
