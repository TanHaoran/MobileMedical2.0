package com.thr.mobilemedical;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.thr.adapter.DropAdapter;
import com.thr.adapter.NursinglistAdapter;
import com.thr.adapter.PatientAdapter;
import com.thr.bean.Order;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.GsonUtil;
import com.thr.utils.HttpGetUtil;
import com.thr.utils.L;
import com.thr.utils.SelectbarUtil;
import com.thr.view.LeftMenuPopupWindow;
import com.thr.view.MyProgressDialog;
import com.thr.view.PatientPopupWindow;
import com.thr.view.TitleBar;
import com.thr.view.TitleBar.OnLeftClickListener;
import com.thr.view.TitleBar.OnRightClickListener;

/**
 * @description DropdetailActivity.java
 * @date 2015年10月21日 下午4:30:09
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */

@SuppressLint("HandlerLeak")
@ContentView(R.layout.activity_dropdetail)
public class DropdetailActivity extends Activity {

	// 左侧菜单
	private LeftMenuPopupWindow mLeftWindow;
	// 病患菜单
	private PatientPopupWindow mPatientWindow;

	// 标题栏
	@ViewInject(R.id.titlebar)
	private TitleBar mTitleBar;

	@ViewInject(R.id.tv_total)
	private TextView mTotal;
	@ViewInject(R.id.tv_executed)
	private TextView mExecuted;
	@ViewInject(R.id.tv_notexe)
	private TextView mNotexe;

	@SuppressWarnings("unused")
	private ListView mListView;
	@SuppressWarnings("unused")
	private DropAdapter mAdapter;

	private PatientAdapter mPatientAdapter;

	@SuppressWarnings("unused")
	private BroadcastReceiver mReceiver;

	private MyProgressDialog mDialog;

	// 已执行医嘱列表
	@SuppressWarnings("unused")
	private List<Order> mExecOrderList;

	// 医嘱类型，1：长期；0：临时
	private String mCodeOrderClassify;
	// 父医嘱号
	@SuppressWarnings("unused")
	private String mCodeParentOrder;
	// 执行次数
	@SuppressWarnings("unused")
	private String mCodeExecTimes;
	// 计划执行日期
	@SuppressWarnings("unused")
	private String mCodeExecPlanDay;
	// 计划执行时间
	@SuppressWarnings("unused")
	private String mCodeExecPlanTime;

	// 显示的医嘱
	protected List<Order> mOrderList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		initView();
		setPatientListView();
		// 读取所有静滴医嘱
		loadOrder(LoginInfo.OFFICE_ID, LoginInfo.patient.getPATIENTHOSID(),
				Method.DROP);
	}

	/**
	 * 初始化病人列表信息
	 */
	private void setPatientListView() {
		ListView mPatientListView = mPatientWindow.getListView();
		if (LoginInfo.PATIENT_ALL != null) {
			mPatientAdapter = new PatientAdapter(this, LoginInfo.PATIENT_ALL,
					R.layout.item_patient);
			mPatientListView.setAdapter(mPatientAdapter);
		}
		mPatientListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPatientWindow.dismiss();
				changePatient(position);
			}
		});
	}

	private void initView() {
		initSelectbar();

		mDialog = new MyProgressDialog(this);

		mLeftWindow = new LeftMenuPopupWindow(this);
		mPatientWindow = new PatientPopupWindow(this);

		mTitleBar.setLeftImage(R.drawable.top_fh);
		mTitleBar.setRightImage(R.drawable.top_bh);
		mTitleBar.setOnLeftClickListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {
				finish();
			}
		});
		mTitleBar.setOnRightClickListener(new OnRightClickListener() {

			@Override
			public void onRightClick(View v) {
				mPatientWindow.showAtLocation(findViewById(R.id.ll_main),
						Gravity.BOTTOM, 0, 0);
			}
		});

		mListView = (ListView) findViewById(R.id.lv_order);

	}

	/**
	 * 通过点击右上角的病患列表来改变病患的显示
	 */
	private void changePatient(int position) {
		LoginInfo.patientIndex = position;
		LoginInfo.patient = LoginInfo.PATIENT_ALL.get(LoginInfo.patientIndex);
		initSelectbar();
		// 读取所有静滴医嘱
		loadOrder(LoginInfo.OFFICE_ID, LoginInfo.patient.getPATIENTHOSID(),
				Method.DROP);
	}

	/**
	 * 初始化选择条内容
	 */
	public void initSelectbar() {
		SelectbarUtil selectbarUtil = new SelectbarUtil() {

			@Override
			public void data() {
				// 读取所有静滴医嘱
				loadOrder(LoginInfo.OFFICE_ID,
						LoginInfo.patient.getPATIENTHOSID(), Method.DROP);
			}
		};
		selectbarUtil.initView(this, this);
	}

	/**
	 * 读取所有医嘱
	 */
	private void loadOrder(String officeId, String patientHosId, String execType) {
		String url = SettingInfo.SERVICE
				+ Method.GET_PATIENT_INFO_DOCTOR_ORDER_BY_PATIENTHOSID
				+ "?OfficeID=" + officeId + "&PatientHosId=" + patientHosId
				+ "&MedType=" + execType;
		HttpGetUtil httpGet = new HttpGetUtil(this) {

			@Override
			public void success(String json) {
				mOrderList = GsonUtil.getDropOrderList(json);
				setListData();
			}

		};
		httpGet.doGet(url, mDialog, this, "所有医嘱");
	}

	/**
	 * 设置ListView列表数据
	 */
	private void setListData() {
		if (mOrderList != null && mOrderList.size() > 0
				&& !"".equals(mOrderList.get(0).getORDERCONTENT())) {
			mListView.setVisibility(View.VISIBLE);
			mAdapter = new DropAdapter(this, mOrderList, R.layout.item_drop);
			mListView.setAdapter(mAdapter);
			int total = mOrderList.size();
			int executed = 0;
			for (Order o : mOrderList) {
				if (!"".equals(o.getEXECTIME())) {
					executed++;
				}
			}
			mTotal.setText(String.valueOf(total));
			mExecuted.setText(String.valueOf(executed));
			mNotexe.setText(String.valueOf(total - executed));
		} else {
			mListView.setVisibility(View.INVISIBLE);
			mTotal.setText("0");
			mExecuted.setText("0");
			mNotexe.setText("0");
		}

	}

}
