package com.thr.mobilemedical;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.thr.adapter.BloodAdapter;
import com.thr.bean.Blood;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.GsonUtil;
import com.thr.utils.HttpGetUtil;
import com.thr.utils.L;
import com.thr.view.MyProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * @description 护理录入-血压
 * @date 2015年9月11日14:14:46
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class BloodpressureFragment extends Fragment {

	private ListView mListView;

	private MyProgressDialog mDialog;

	private List<Blood> mBloodpressureList;

	private BloodAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_bloodpressure, container,
				false);
		initView(v);
//		loadBloodoxygenData("2037");
		return v;
	}

	/**
	 * 读取血氧的数据
	 * 
	 * @param string
	 */
	private void loadBloodoxygenData(String officeId) {
		String url = SettingInfo.NSIS
				+ Method.QUERY_NURSE_ITEM_MOBILE_BY_BLOODPRESSURE
				+ "?officeid=" + officeId;

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
						L.i("返回值------" + response);
						mBloodpressureList = GsonUtil.getBloodData(response);
						setBloodpressureData();
					}
				});

	}

	protected void setBloodpressureData() {
		if (mBloodpressureList != null && mBloodpressureList.size() > 0) {
			mAdapter = new BloodAdapter(getActivity(), mBloodpressureList,
					R.layout.item_blood);
			mListView.setAdapter(mAdapter);
		}
	}

	private void initView(View v) {
		mDialog = new MyProgressDialog(getActivity());
		mListView = (ListView) v.findViewById(R.id.lv_bloodpressure);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LoginInfo.patientIndex = position;
				LoginInfo.patient = LoginInfo.PATIENT_ALL.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(),
						BloodpressuredetailActivity.class);
				startActivity(intent);
			}
		});

	}
}
