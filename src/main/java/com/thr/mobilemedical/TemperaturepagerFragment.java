package com.thr.mobilemedical;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thr.adapter.TemperatureAdapter;
import com.thr.bean.NursingrecordFiled;
import com.thr.bean.Temperature;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.GsonUtil;
import com.thr.utils.L;
import com.thr.view.MyProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 护理录入-体温单
 * @date 2015年9月11日14:13:33
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
@SuppressLint("HandlerLeak")
public class TemperaturepagerFragment extends Fragment {

	private ListView mListView;
	private TemperatureAdapter mAdapter;

	private TextView mTime1;
	private TextView mTime2;
	private TextView mTime3;
	private TextView mTime4;
	private TextView mTime5;
	private TextView mTime6;

	private List<Temperature> mTemperatureList;
	// 护理记录单字段结构
	private List<NursingrecordFiled> mFileds;

	private MyProgressDialog mDialog;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			updateOnListView();
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_temperaturepager,
				container, false);

		initView(v);
		// 读取护理记录单字段以获取时间段
//		if (LoginInfo.nursingrecordList.get(0) != null) {
//			loadNursingRecordFiled(LoginInfo.nursingrecordList.get(0)
//					.getNURSINGID());
//		}
//		loadPatientTemperature(LoginInfo.OFFICE_ID);
		return v;
	}

	/**
	 * 读取所有病患的体温
	 */
	private void loadPatientTemperature(String officeId) {
		String url = SettingInfo.SERVICE + Method.GET_PATIENT_TEMPERATURE
				+ "?OfficeID=" + officeId;

		OkHttpUtils
				.get()
				.url(url)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						L.i("病患体温单------" + response);
						mTemperatureList = GsonUtil.getPatientTemperature(response);
						setOnListView();
					}
				});

	}

	/**
	 * 读取护理记录单结构
	 */
	/**
	 * 读取护理记录单结构
	 */
	private void loadNursingRecordFiled(String nursignId) {
		String url = SettingInfo.SERVICE + Method.GET_NURSING_FILED
				+ "?NursingId=" + nursignId;

		OkHttpUtils
				.get()
				.url(url)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						L.i("护理记录单字段------" + response);
						mFileds = GsonUtil.getNursingStructureList(response);
						getTimePoint();
						setTimePotin();
					}
				});


	}

	/**
	 * 设置好时间点
	 */
	protected void setTimePotin() {
		if (LoginInfo.timePoints != null) {
			mTime1.setText(LoginInfo.timePoints.get(0).substring(2));
			mTime2.setText(LoginInfo.timePoints.get(1).substring(2));
			mTime3.setText(LoginInfo.timePoints.get(2).substring(2));
			mTime4.setText(LoginInfo.timePoints.get(3).substring(2));
			mTime5.setText(LoginInfo.timePoints.get(4).substring(2));
			mTime6.setText(LoginInfo.timePoints.get(5).substring(2));
		}
	}

	/**
	 * 根据护理记录单结构获取测量时间点
	 */
	private void getTimePoint() {
		if (mFileds != null && mFileds.size() > 0) {
			for (NursingrecordFiled filed : mFileds) {
				if ("TENDTIME".equals(filed.getCOLNAMES())) {
					if (filed.getNURSINGCONTENT() != null) {
						String[] times = filed.getNURSINGCONTENT().split("\\|");
						if (times != null) {
							List<String> timpPotins = new ArrayList<String>();
							for (String time : times) {
								if (!"空".equals(time)) {
									timpPotins.add(time);
								}
							}
							LoginInfo.timePoints = timpPotins;
						}
					}
				}
			}
		}
	}

	/**
	 * 在界面上显示出来体温数据
	 */
	protected void setOnListView() {
		if (mTemperatureList != null && mTemperatureList.size() > 0) {
			mAdapter = new TemperatureAdapter(getActivity(), mTemperatureList,
					R.layout.item_temperature);
			mListView.setAdapter(mAdapter);
		}
	}

	/**
	 * 更新体温表
	 */
	private void updateOnListView() {
		if (mAdapter == null) {
			mAdapter = new TemperatureAdapter(getActivity(), mTemperatureList,
					R.layout.item_temperature);
		}
		mAdapter.setDatas(mTemperatureList);
		mAdapter.notifyDataSetChanged();
	}

	private void initView(View v) {

		mTime1 = (TextView) v.findViewById(R.id.tv_time1);
		mTime2 = (TextView) v.findViewById(R.id.tv_time2);
		mTime3 = (TextView) v.findViewById(R.id.tv_time3);
		mTime4 = (TextView) v.findViewById(R.id.tv_time4);
		mTime5 = (TextView) v.findViewById(R.id.tv_time5);
		mTime6 = (TextView) v.findViewById(R.id.tv_time6);
		mListView = (ListView) v.findViewById(R.id.lv_temperature_normal);

		mTemperatureList = new ArrayList<Temperature>();
		mDialog = new MyProgressDialog(getActivity());
	}

	/**
	 * 更新体温列表
	 */
	private void updateTemperatureForm() {
		String url = SettingInfo.SERVICE + Method.GET_PATIENT_TEMPERATURE
				+ "?OfficeID=" + LoginInfo.OFFICE_ID;


		OkHttpUtils
				.get()
				.url(url)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						L.i("护理记录单单条记录------" + response);
						mTemperatureList = GsonUtil.getPatientTemperature(response);
						handler.sendEmptyMessage(0);
					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
//		updateTemperatureForm();
	}
}
