package com.thr.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thr.mobilemedical.R;

/**
 * 标题栏自定义控件
 * 
 * @author Jerry
 *
 */
public class TitleBar extends RelativeLayout {

	private RelativeLayout mLeftLayout;
	private RelativeLayout mRightLayout;

	private TextView mTitle;
	private TextView mLeft;
	private TextView mRight;
	
	private ImageView mLeftImg;
	private ImageView mRightImg;

	private OnLeftClickListener leftClickListener;
	private OnRightClickListener rightClickListener;

	@SuppressLint("Recycle")
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.isInEditMode();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_titlebar, this);
		mLeftLayout = (RelativeLayout) findViewById(R.id.rl_left);
		mRightLayout = (RelativeLayout) findViewById(R.id.rl_right);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mLeft = (TextView) findViewById(R.id.tv_left);
		mRight = (TextView) findViewById(R.id.tv_right);
		mLeftImg = (ImageView) findViewById(R.id.iv_left);
		mRightImg = (ImageView) findViewById(R.id.iv_right);
	

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.TitleBar);
		int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.TitleBar_titleText:
				// 避免编译器报错
				if (!isInEditMode()) {
					mTitle.setText(typedArray.getString(attr));
				}
				break;
			case R.styleable.TitleBar_leftText:
				if (!isInEditMode()) {
					mLeft.setText(typedArray.getString(attr));
				}
				break;
			case R.styleable.TitleBar_rightText:
				mRight.setText(typedArray.getString(attr));
				break;
			}
		}
		// 避免编译器报错
		if (!isInEditMode()) {
			mLeftLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (leftClickListener != null) {
						leftClickListener.onLeftClick(v);
					}
				}
			});
			mRightLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (rightClickListener != null) {
						rightClickListener.onRightClick(v);
					}
				}
			});
		}
	}

	/**
	 * 设置标题内容
	 * 
	 * @param title
	 *            字符串内容
	 */
	public void setTitle(String title) {
		mTitle.setText(title);
	}

	/**
	 * 设置标题内容
	 * 
	 * @param resid
	 *            资源id
	 */
	public void setTitle(int resid) {
		mTitle.setText(resid);
	}

	/**
	 * 设置左侧内容
	 * 
	 * @param title
	 *            字符串内容
	 */
	public void setLeftText(String title) {
		mLeft.setText(title);
	}

	/**
	 * 设置左侧内容
	 * 
	 * @param resid
	 *            资源id
	 */
	public void setLeftText(int resid) {
		mLeft.setText(resid);
	}

	/**
	 * 设置右侧内容
	 * 
	 * @param title
	 *            字符串内容
	 */
	public void setRightText(String title) {
		mRight.setText(title);
	}

	/**
	 * 设置右侧内容
	 * 
	 * @param resid
	 *            资源id
	 */
	public void setRightText(int resid) {
		mRight.setText(resid);
	}

	public void setOnLeftClickListener(OnLeftClickListener leftClickListener) {
		this.leftClickListener = leftClickListener;
	}

	public void setOnRightClickListener(OnRightClickListener rightClickListener) {
		this.rightClickListener = rightClickListener;
	}

	public void setLeftImage(int resId) {
		mLeftImg.setImageResource(resId);
	}

	public void setRightImage(int resId) {
		mRightImg.setImageResource(resId);
	}

	public interface OnLeftClickListener {
		void onLeftClick(View v);
	}

	public interface OnRightClickListener {
		void onRightClick(View v);
	}

}
