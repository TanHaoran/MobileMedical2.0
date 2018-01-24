package com.thr.mobilemedical;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.squareup.okhttp.Request;
import com.thr.adapter.ExamAdapter;
import com.thr.bean.Exam;
import com.thr.bean.ExamTitle;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.GsonUtil;
import com.thr.utils.L;
import com.thr.utils.SelectbarUtil;
import com.thr.view.MyProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 病患信息-检验界面
 * @date 2015年9月11日14:12:34
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class ExamFragment extends Fragment {

	private View v;

	private MyProgressDialog mDialog;

	private ExpandableListView mListView;

	private ExamAdapter mAdapter;

	private Map<String, Integer> mTimeMap;
	private List<ExamTitle> mTimeList;
	private List<Exam> mExamList;
	private List<List<Exam>> mExams;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_exam, container, false);
		initView();
//		loadExamList(LoginInfo.patient.getPATIENTHOSID());
		return v;
	}

	private void initView() {

		mDialog = new MyProgressDialog(getActivity());
		initSelectbar();

		mListView = (ExpandableListView) v.findViewById(R.id.elv_exam);
		mListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent intent = new Intent();
				intent.putExtra("title",
						mExams.get(groupPosition).get(childPosition).getNAME());
				intent.putExtra("examid",
						mExams.get(groupPosition).get(childPosition).getID());
				intent.setClass(getActivity(), ExamDetailActivity.class);
				startActivity(intent);
				return false;
			}
		});
	}

	/**
	 * 读取检验报告列表
	 * 
	 * @param patientHosId
	 */
	public void loadExamList(String patientHosId) {
		String url = SettingInfo.SERVICE + Method.GET_LIS + "?PatientHosId="
				+ patientHosId;

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
						L.i("读取检验报告列表------" + response);
						mExamList = GsonUtil.getExamList(response);
						setData();

					}
				});
	}

	/**
	 * 将数据设置到扩展列表上
	 */
	protected void setData() {
		mTimeMap = new HashMap<String, Integer>();
		mExams = new ArrayList<List<Exam>>();
		List<Exam> list = new ArrayList<Exam>();
		if (mExamList != null && mExamList.size() > 0
				&& !"".equals(mExamList.get(0).getID())) {
			for (Exam e : mExamList) {
				// 读取时间字串，如果存在就加一，不存在就新建key
				String time = e.getTIME().split(" ")[0];
				if (!mTimeMap.containsKey(time)) {
					// 如果有0条以上的记录就添加到List集合中
					if (list.size() > 0) {
						mExams.add(list);
					}
					list.clear();
					list.add(e);
					mTimeMap.put(time, 1);
				} else {
					list.add(e);
					Integer num = mTimeMap.get(time);
					mTimeMap.put(time, num + 1);
				}
			}
			mExams.add(list);
			mTimeList = new ArrayList<ExamTitle>();
			Set<String> key = mTimeMap.keySet();
			for (String k : key) {
				ExamTitle title = new ExamTitle();
				title.setTime(k);
				title.setNum(mTimeMap.get(k));
				mTimeList.add(title);
			}
			mAdapter = new ExamAdapter(getActivity(), mTimeList, mExams);
			mListView.setAdapter(mAdapter);
			mListView.setVisibility(View.VISIBLE);
			// 如果第一个有父集合就展开第一项
			if (mListView.getChildAt(0) != null) {
				mListView.expandGroup(0);
			}
		} else {
			mListView.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * 初始化选择条内容
	 */
	public void initSelectbar() {
		SelectbarUtil selectbarUtil = new SelectbarUtil() {

			@Override
			public void data() {
				loadExamList(LoginInfo.patient.getPATIENTHOSID());
			}
		};
		selectbarUtil.initView(getActivity(), v);
	}

}
