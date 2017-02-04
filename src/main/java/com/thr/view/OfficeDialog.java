package com.thr.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.thr.bean.Office;
import com.thr.constant.LoginInfo;
import com.thr.mobilemedical.R;

/**
 * @description 自定义Dialog弹出框
 * @date 2015年9月30日 上午9:40:13
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class OfficeDialog extends Dialog {

	private Context mContext;

	private WheelView mWheel;
	private List<Office> mOffices;

	private Button mSure;
	private Button mCancle;

	public OfficeDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_office);

		mSure = (Button) findViewById(R.id.btn_sure);
		mCancle = (Button) findViewById(R.id.btn_cancle);
		mWheel = (WheelView) findViewById(R.id.wv_office);

		mWheel.setOffset(2);

		mSure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveInfo();
				dismiss();
			}
		});
		mCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	/**
	 * 保存配置信息
	 */
	protected void saveInfo() {
		int index = mWheel.getSeletedIndex();
		if (index == -1) {
			index = 0;
		}
		if (mOffices != null) {
			Office office = mOffices.get(index);
			SharedPreferences sp = mContext.getSharedPreferences(
					ConfigDialog.CONFIG, Activity.MODE_PRIVATE);
			String officeId = office.getOFFICEHISID();
			String officeName = office.getOFFICENAME();
			sp.edit().putString("office_id", officeId)
					.putString("office_name", officeName).commit();
			LoginInfo.OFFICE_ID = officeId;
			LoginInfo.OFFICE_NAME = officeName;
		}
	}

	/**
	 * 设置科室数据
	 * 
	 * @param items
	 */
	public void setItems(List<Office> offices) {
		mOffices = offices;
		List<String> items = new ArrayList<String>();
		for (Office o : offices) {
			items.add(o.getOFFICENAME());
		}
		mWheel.setItems(items);
	}

}
