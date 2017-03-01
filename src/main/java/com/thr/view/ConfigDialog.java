package com.thr.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.thr.constant.SettingInfo;
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
public class ConfigDialog extends Dialog {

	/**
	 * 保存本地存储的文件名
	 */
	public static final String CONFIG = "config";

	private Context mContext;

	/**
	 * 服务地址
	 */
	private EditText mEditAdd;
	/**
	 * NSIS地址
	 */
	private EditText mEditNsis;
	private EditText mEditAction;
	private EditText mEditExtra;

	/**
	 * 确定按钮
	 */
	private Button mSure;
	/**
	 * 取消按钮
	 */
	private Button mCancle;

	public ConfigDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_config);

		mEditAdd = (EditText) findViewById(R.id.et_add);
		mEditNsis = (EditText) findViewById(R.id.et_nsis);
		mEditAction = (EditText) findViewById(R.id.et_action);
		mEditExtra = (EditText) findViewById(R.id.et_extra);
		mSure = (Button) findViewById(R.id.btn_sure);
		mCancle = (Button) findViewById(R.id.btn_cancle);

		mSure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 保存服务地址并消失dialog
				saveConfig();
				dismiss();
			}
		});
		mCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		// 读取之前保存好的服务地址
		loadConfig();
	}

	/**
	 * 保存服务配置信息
	 */
	private void saveConfig() {
		SharedPreferences sp = mContext.getSharedPreferences(CONFIG,
				Activity.MODE_PRIVATE);
		String add = mEditAdd.getText().toString();
		String nsis = mEditNsis.getText().toString();
		String action = mEditAction.getText().toString();
		String extra = mEditExtra.getText().toString();
		sp.edit().putString("add", add).commit();
		sp.edit().putString("nsis", nsis).commit();
		sp.edit().putString("action", action).commit();
		sp.edit().putString("extra", extra).commit();
		SettingInfo.SERVICE = add;
		SettingInfo.NSIS = nsis;
		SettingInfo.SCAN_FILTER = action;
		SettingInfo.RECEIVE_STRING = extra;
	}

	/**
	 * 读取配置信息
	 */
	public void loadConfig() {
		SharedPreferences sp = mContext.getSharedPreferences(CONFIG,
				Activity.MODE_PRIVATE);
		// 如果不存在就用默认的地址显示
		String add = sp.getString("add",
				"http://192.168.0.100:4010/MMIPService/");
		String nsis = sp.getString("nsis",
				"http://192.168.0.100:4514/NSIS_WebAPI/");
		String action = sp.getString("action", "");
		String extra = sp.getString("extra", "");
		SettingInfo.SERVICE = add;
		SettingInfo.NSIS = nsis;
		SettingInfo.SCAN_FILTER = action;
		SettingInfo.RECEIVE_STRING = extra;

		mEditAdd.setText(add);
		mEditNsis.setText(nsis);
		mEditAction.setText(action);
		mEditExtra.setText(extra);
	}
}
