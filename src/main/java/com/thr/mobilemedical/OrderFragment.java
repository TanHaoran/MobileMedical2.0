package com.thr.mobilemedical;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thr.adapter.OrderAdapter;
import com.thr.bean.Order;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.GsonUtil;
import com.thr.utils.L;
import com.thr.utils.SelectbarUtil;
import com.thr.view.MyProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 * @description 病患信息-医嘱界面
 * @date 2015年9月11日14:12:45
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class OrderFragment extends Fragment {

	private View v;

	private LinearLayout mLong;
	private LinearLayout mTemp;
	private View mLongline;
	private View mTempline;

	private TextView mTextLong;
	private TextView mTextTemp;

	private TextView mTotal;
	private TextView mExecute;
	private TextView mNotExe;

	private ListView mListView;
	private List<Order> mLongList;
	private List<Order> mTempList;
	private OrderAdapter mAdapter;

	private MyProgressDialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_order, container, false);
		initView();
		loadLongOrder(LoginInfo.patient.getPATIENTHOSID(),
				LoginInfo.patient.getPATIENTINTIMES(), "1");
		loadTempOrder(LoginInfo.patient.getPATIENTHOSID(),
				LoginInfo.patient.getPATIENTINTIMES(), "0");
		return v;
	}

	private void initView() {
		mDialog = new MyProgressDialog(getActivity());
		initSelectbar();

		mLong = (LinearLayout) v.findViewById(R.id.ll_longorder);
		mTemp = (LinearLayout) v.findViewById(R.id.ll_temporder);
		mTextLong = (TextView) v.findViewById(R.id.tv_longorder);
		mTextTemp = (TextView) v.findViewById(R.id.tv_temporder);
		mLongline = (View) v.findViewById(R.id.v_longline);
		mTempline = (View) v.findViewById(R.id.v_templine);

		mTotal = (TextView) v.findViewById(R.id.tv_total);
		mExecute = (TextView) v.findViewById(R.id.tv_execute);
		mNotExe = (TextView) v.findViewById(R.id.tv_notexe);

		mListView = (ListView) v.findViewById(R.id.lv_order);

		mLong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeToLong();
			}

		});
		mTemp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chagneToTemp();
			}

		});

	}

	/**
	 * 初始化选择条内容
	 */
	public void initSelectbar() {
		SelectbarUtil selectbarUtil = new SelectbarUtil() {

			@Override
			public void data() {
				loadLongOrder(LoginInfo.patient.getPATIENTHOSID(),
						LoginInfo.patient.getPATIENTINTIMES(), "1");
				loadTempOrder(LoginInfo.patient.getPATIENTHOSID(),
						LoginInfo.patient.getPATIENTINTIMES(), "0");
				changeToLong();
			}
		};
		selectbarUtil.initView(getActivity(), v);
	}

	/**
	 * 切换到长期医嘱
	 */
	private void changeToLong() {
		initOrderList(mLongList);
		mLongline.setVisibility(View.VISIBLE);
		mTempline.setVisibility(View.INVISIBLE);
		mTextLong.setTextColor(getResources().getColor(
				R.color.login_btn_bg_normal));
		mTextTemp
				.setTextColor(getResources().getColor(R.color.bottom_tab_text));
		loadLongOrder(LoginInfo.patient.getPATIENTHOSID(),
				LoginInfo.patient.getPATIENTINTIMES(), "1");
	}

	/**
	 * 切换到临时医嘱
	 */
	private void chagneToTemp() {
		initOrderList(mTempList);
		mLongline.setVisibility(View.INVISIBLE);
		mTempline.setVisibility(View.VISIBLE);
		mTextTemp.setTextColor(getResources().getColor(
				R.color.login_btn_bg_normal));
		mTextLong
				.setTextColor(getResources().getColor(R.color.bottom_tab_text));
		loadTempOrder(LoginInfo.patient.getPATIENTHOSID(),
				LoginInfo.patient.getPATIENTINTIMES(), "0");
	}

	/**
	 * 初始化医嘱数据
	 * 
	 * @param orderList
	 */
	private void initOrderList(List<Order> orderList) {
		mAdapter = new OrderAdapter(getActivity(), orderList,
				R.layout.item_order);
		if (orderList != null && orderList.size() > 0
				&& !orderList.get(0).getPATIENTHOSID().trim().equals("")) {
			mListView.setVisibility(View.VISIBLE);
			mListView.setAdapter(mAdapter);
			mTotal.setText(String.valueOf(orderList.size()));
			int execute = 0;
			for (Order o : orderList) {
				if (o.getEXECTIME() != null && !"".equals(o.getEXECTIME())) {
					execute++;
				}
			}
			mExecute.setText(String.valueOf(execute));
			mNotExe.setText(String.valueOf(orderList.size() - execute));
		} else {
			mListView.setVisibility(View.INVISIBLE);
			mTotal.setText("0");
			mExecute.setText("0");
			mNotExe.setText("0");
		}
	}

	/**
	 * 读取病患长期医嘱信息
	 * 
	 * @param patientHosId
	 * @param patientInTimes
	 * @param orderType
	 *            1：长期医嘱；0：临时医嘱
	 */
	public void loadLongOrder(String patientHosId, String patientInTimes,
			String orderType) {
		String url = SettingInfo.SERVICE + Method.DOCTOR_ORDER_BY_PATIENTHOSID
				+ "?PatientHosId=" + patientHosId + "&PatientInTimes="
				+ patientInTimes + "&OrderType=" + orderType;

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
						L.i("长期医嘱------" + response);
						mLongList = GsonUtil.getOrderList(response);
						initOrderList(mLongList);
						if (mLongList != null
								&& mLongList.size() > 0
								&& !mLongList.get(0).getPATIENTHOSID().trim()
								.equals("")) {
							mTextLong.setText("长期医嘱" + "(" + mLongList.size() + ")");
						} else {
							mTextLong.setText("长期医嘱");
						}
					}
				});

	}

	/**
	 * 读取病患长期医嘱信息
	 */
	private void loadTempOrder(String patientHosId, String patientInTimes,
			String orderType) {
		String url = SettingInfo.SERVICE + Method.DOCTOR_ORDER_BY_PATIENTHOSID
				+ "?PatientHosId=" + patientHosId + "&PatientInTimes="
				+ patientInTimes + "&OrderType=" + orderType;

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

						L.i("临时医嘱------" + response);
						mTempList = GsonUtil.getOrderList(response);
						if (mTempList != null
								&& mTempList.size() > 0
								&& !mTempList.get(0).getPATIENTHOSID().trim()
								.equals("")) {
							mTextTemp.setText("临时医嘱" + "(" + mTempList.size() + ")");
						} else {
							mTextTemp.setText("临时医嘱");
						}
					}
				});

	}
}
