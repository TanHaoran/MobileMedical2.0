package com.thr.mobilemedical;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.thr.adapter.NursinglistAdapter;
import com.thr.bean.Nursingpaper;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.GsonUtil;
import com.thr.utils.HttpGetUtil;
import com.thr.utils.L;
import com.thr.utils.ScanUtil;
import com.thr.utils.TopActivityListener;
import com.thr.utils.TopExitImpl;
import com.thr.view.LeftMenuPopupWindow;
import com.thr.view.MyProgressDialog;
import com.thr.view.TitleBar;
import com.thr.view.TitleBar.OnLeftClickListener;

/**
 * @description 护理清单（静滴清单）界面
 * @date 2015年9月25日 下午3:35:11
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
@ContentView(R.layout.activity_nursinglist)
public class NursinglistActivity extends Activity {

	// 左侧菜单
	private LeftMenuPopupWindow mLeftWindow;

	// 标题栏
	@ViewInject(R.id.titlebar)
	private TitleBar mTitleBar;

	@ViewInject(R.id.tv_all)
	private TextView mTextAll;
	@ViewInject(R.id.tv_executed)
	private TextView mTextExecuted;
	@ViewInject(R.id.tv_notexecute)
	private TextView mTextNotexe;

	@ViewInject(R.id.lv_nursinglist)
	private ListView mListView;

	private TopActivityListener mTopListener;

	private List<Nursingpaper> mNursingList;

	private NursinglistAdapter mAdapter;

	private MyProgressDialog mDialog;

	public void setTopListener(TopActivityListener topListener) {
		mTopListener = topListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		initView();
		loadNursinglistData(LoginInfo.OFFICE_ID);
	}

	private void initView() {
		mDialog = new MyProgressDialog(this);
		mLeftWindow = new LeftMenuPopupWindow(this);
		mTitleBar.setLeftImage(R.drawable.top_list);
		mTitleBar.setOnLeftClickListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {
				mLeftWindow.showAtLocation(findViewById(R.id.ll_main),
						Gravity.BOTTOM, 0, 0);
			}
		});

		// 设置最上层退出处理
		setTopListener(new TopExitImpl());
	}

	/**
	 * 读取护理清单数据
	 * 
	 * @param string
	 */
	private void loadNursinglistData(String officeId) {
		String url = SettingInfo.NSIS
				+ Method.QUERY_NURSE_ITEM_MOBILE_BY_STATISTICAL + "?officeid="
				+ officeId;
		HttpGetUtil httpGet = new HttpGetUtil(this) {
			@Override
			public void success(String json) {
				L.i("返回值------" + json);
				mNursingList = GsonUtil.getNursingpaperData(json);
				setNursingpaperData();
			}
		};
		httpGet.doGet(url, mDialog, this, "血氧数据");
	}

	/**
	 * 设置护理清单记录
	 */
	protected void setNursingpaperData() {
		int all = 0; // 全部清单
		int done = 0; // 已执行清单
		if (mNursingList != null) {
			for (Nursingpaper p : mNursingList) {
				all += Integer.parseInt(p.getPERFROMTIMES());
				done += Integer.parseInt(p.getPERFORMEDTIMES());
			}
		}
		mTextAll.setText(String.valueOf(all));
		mTextExecuted.setText(String.valueOf(done));
		mTextNotexe.setText(String.valueOf(all - done));

		if (mNursingList != null && mNursingList.size() > 0) {
			mAdapter = new NursinglistAdapter(this, mNursingList,
					R.layout.item_nursingpaper);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					LoginInfo.patientIndex = position;
					LoginInfo.patient = LoginInfo.PATIENT_ALL.get(position);
					Intent intent = new Intent(NursinglistActivity.this,
							DropdetailActivity.class);
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		mTopListener.onExit(this);
	}

	@Override
	protected void onResume() {
		ScanUtil.registerScanReceiver(this);
		super.onResume();
	}

}
