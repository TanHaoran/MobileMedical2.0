package com.thr.mobilemedical;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.thr.adapter.BloodAdapter;
import com.thr.bean.Blood;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.GsonUtil;
import com.thr.utils.HttpGetUtil;
import com.thr.utils.L;
import com.thr.view.MyProgressDialog;

/**
 * @description 护理录入-血糖
 * @date 2015年9月11日14:14:36
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class BloodsugerFragment extends Fragment {

	private ListView mListView;

	private MyProgressDialog mDialog;

	private List<Blood> mBloodsugerList;

	private BloodAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_bloodsuger, container,
				false);
		initView(v);
		loadBloodsugerData("2037");
		return v;
	}

	/**
	 * 读取血糖的数据
	 * 
	 * @param string
	 */
	private void loadBloodsugerData(String officeId) {
		String url = SettingInfo.NSIS
				+ Method.QUERY_NURSE_ITEM_MOBILE_BY_BLOODSUGAR + "?officeid="
				+ officeId;
		HttpGetUtil httpGet = new HttpGetUtil(getActivity()) {
			@Override
			public void success(String json) {
				L.i("返回值------" + json);
				mBloodsugerList = GsonUtil.getBloodData(json);
				setBloodsugerData();
			}
		};
		httpGet.doGet(url, mDialog, getActivity(), "血糖数据");
	}

	protected void setBloodsugerData() {
		if (mBloodsugerList != null && mBloodsugerList.size() > 0) {
			mAdapter = new BloodAdapter(getActivity(), mBloodsugerList,
					R.layout.item_blood);
			mListView.setAdapter(mAdapter);
		}
	}

	private void initView(View v) {
		mDialog = new MyProgressDialog(getActivity());
		mListView = (ListView) v.findViewById(R.id.lv_bloodsuger);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LoginInfo.patientIndex = position;
				LoginInfo.patient = LoginInfo.PATIENT_ALL.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), BloodsugerdetailActivity.class);
				startActivity(intent);
			}
		});
	}
}
