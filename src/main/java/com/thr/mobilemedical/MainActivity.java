package com.thr.mobilemedical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Request;
import com.thr.adapter.PatientAdapter;
import com.thr.bean.NewOrder;
import com.thr.bean.Patient;
import com.thr.bean.UnusualTemper;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.DateUtil;
import com.thr.utils.DensityUtils;
import com.thr.utils.GsonUtil;
import com.thr.utils.L;
import com.thr.utils.ScanUtil;
import com.thr.utils.TopActivityListener;
import com.thr.utils.TopExitImpl;
import com.thr.view.LeftMenuPopupWindow;
import com.thr.view.MyAlertDialog;
import com.thr.view.MyProgressDialog;
import com.thr.view.PatientPopupWindow;
import com.thr.view.TitleBar;
import com.thr.view.TitleBar.OnLeftClickListener;
import com.thr.view.TitleBar.OnRightClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry Tan
 * @version 1.0
 * @description 主界面
 * @date 2015年8月25日 上午10:09:27
 * @Company Buzzlysoft
 * @email thrforever@126.com
 * @remark
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends Activity implements OnClickListener {

    // 左侧菜单
    private LeftMenuPopupWindow mLeftWindow;
    // 病患菜单
    private PatientPopupWindow mPatientWindow;

    // 标题栏
    @ViewInject(R.id.titlebar)
    private TitleBar mTitleBar;

    // 科室名字
    @ViewInject(R.id.tv_officename)
    private TextView mOfficeName;

    // 所有病患
    @ViewInject(R.id.ll_allpatient)
    private RelativeLayout mGroupAll;
    // 今日入院
    @ViewInject(R.id.ll_inpatient)
    private RelativeLayout mGroupIn;
    // 今日出院
    @ViewInject(R.id.ll_outpatient)
    private RelativeLayout mGroupOut;
    // 今日手术
    @ViewInject(R.id.ll_operationpatient)
    private RelativeLayout mGroupOperation;
    // 特级护理
    @ViewInject(R.id.ll_speciallevelpatient)
    // 一级护理
    private RelativeLayout mGroupSpecialLevel;
    @ViewInject(R.id.ll_onelevelpatient)
    private RelativeLayout mGroupOneLevel;
    // 二级护理
    @ViewInject(R.id.ll_twolevelpatient)
    private RelativeLayout mGroupTwoLevel;
    // 三级护理
    @ViewInject(R.id.ll_threelevelpatient)
    private RelativeLayout mGroupThreeLevel;

    // 所有病患
    @ViewInject(R.id.tv_allpatient)
    private TextView mTextAll;
    // 今日入院
    @ViewInject(R.id.tv_inpatient)
    private TextView mTextIn;
    // 今日出院
    @ViewInject(R.id.tv_outpatient)
    private TextView mTextOut;
    // 今日手术
    @ViewInject(R.id.tv_operationpatient)
    private TextView mTextOperation;
    // 特级护理
    @ViewInject(R.id.tv_speciallevelpatient)
    private TextView mTextSpecialLevel;
    // 一级护理
    @ViewInject(R.id.tv_onelevelpatient)
    private TextView mTextOneLevel;
    // 二级护理
    @ViewInject(R.id.tv_twolevelpatient)
    private TextView mTextTwoLevel;
    // 三级护理
    @ViewInject(R.id.tv_threelevelpatient)
    private TextView mTextThreeLevel;
    @ViewInject(R.id.ll_infocenter)
    private LinearLayout mInfoLayout;

    private ListView mListView; // 病患列表
    private PatientAdapter mAdapter;

    private MyProgressDialog mDialog; // 等待框

    private List<Patient> mPatientList = new ArrayList<Patient>(); // 病患列表
    private List<Patient> mPatientShow = new ArrayList<Patient>(); // 显示病患列表

    // 所有病患
    private int mAll;
    // 今日入院
    private int mIn;
    // 无床等待
    @SuppressWarnings("unused")
    private int mWait;
    // 24小时出院
    private int mOut;
    // 明日出院
    @SuppressWarnings("unused")
    private int mTomorrow;
    // 今日手术
    private int mOperation;
    // 特级护理
    private int mSpecialLevel;
    // 一级护理
    private int mOneLevel;
    // 二级护理
    private int mTwoLevel;
    // 三级护理
    private int mThreeLevel;

    private TopActivityListener mTopListener;
    // 体温异常的病患
    private List<UnusualTemper> mUnusualList;
    // 新开的医嘱
    protected List<NewOrder> mNewOrderList;

    // 刷新新医嘱的分钟数
    private static final int MINUTE = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        initView();


        for (int i = 0; i < 20; i++) {
            Patient p = new Patient();
            p.setNAME("陈小兵" + i);
            p.setBEDNO(i + "");
            p.setBIRTHDAY("1988-1-24");
            p.setDOCTOR("李医生");
            mPatientList.add(p);
        }

        LoginInfo.PATIENT_ALL = mPatientList;


//        // 读取病患列表
//        loadPatientList(LoginInfo.OFFICE_ID);
//        // 读取护理记录单个数
//        loadNursingRecord(LoginInfo.OFFICE_ID);
//        // 读取体温异常的病患
//        loadUnusualTemper(LoginInfo.OFFICE_ID);
//        // 读取新开的医嘱
//        loadNewOrder(LoginInfo.OFFICE_ID, MINUTE);
    }

    private void initView() {
        mTitleBar.setLeftImage(R.drawable.top_list);
        mTitleBar.setRightImage(R.drawable.top_bh);

        mLeftWindow = new LeftMenuPopupWindow(this);
        mPatientWindow = new PatientPopupWindow(this);

        mTitleBar.setOnLeftClickListener(new OnLeftClickListener() {

            @Override
            public void onLeftClick(View v) {
                mLeftWindow.showAtLocation(findViewById(R.id.ll_main),
                        Gravity.BOTTOM, 0, 0);
            }
        });
        mTitleBar.setOnRightClickListener(new OnRightClickListener() {

            @Override
            public void onRightClick(View v) {
                mPatientShow = LoginInfo.PATIENT_ALL;
                mAdapter.setDatas(mPatientShow);
                mAdapter.notifyDataSetChanged();
                mPatientWindow.showAtLocation(findViewById(R.id.ll_main),
                        Gravity.BOTTOM, 0, 0);
            }
        });

        mOfficeName.setText(LoginInfo.OFFICE_NAME);
        mDialog = new MyProgressDialog(this);
        // 设置最上层退出处理
        setTopListener(new TopExitImpl());
    }

    /**
     * 弹出右侧病人列表
     */
    private void showPopupWindow() {
        mAdapter.setDatas(mPatientShow);
        mAdapter.notifyDataSetChanged();
        mPatientWindow.showAtLocation(findViewById(R.id.sv_main),
                Gravity.RIGHT, 0, DensityUtils.dp2px(MainActivity.this,
                        getResources().getDimension(R.dimen.titlebar_height)));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            // 跳转病患列表
            case R.id.ll_patientinfo:
                intent.setClass(this, PatientinfoActivity.class);
                break;
            // 跳转护理录入
            case R.id.ll_nursingrecord:
                intent.setClass(this, NursingrecordActivity.class);
                break;
        }
        startActivity(intent);
    }

    public void setTopListener(TopActivityListener topListener) {
        mTopListener = topListener;
    }

    @Override
    public void onBackPressed() {
        mTopListener.onExit(this);
    }

    @OnClick({R.id.ll_allpatient, R.id.ll_inpatient, R.id.ll_outpatient,
            R.id.ll_operationpatient, R.id.ll_speciallevelpatient,
            R.id.ll_onelevelpatient, R.id.ll_twolevelpatient,
            R.id.ll_threelevelpatient})
    public void showPatient(View view) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(this, "无病患");
        switch (view.getId()) {
            // 总病患
            case R.id.ll_allpatient:
                if (mAll == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_ALL;
                break;
            // 今日入院
            case R.id.ll_inpatient:
                if (mIn == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_IN;
                break;
            // 今日出院
            case R.id.ll_outpatient:
                if (mOut == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_OUT;
                break;
            // 今日手术
            case R.id.ll_operationpatient:
                if (mOperation == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_OPERATION;
                break;
            // 特级护理
            case R.id.ll_speciallevelpatient:
                if (mSpecialLevel == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_LEVELSPECIAL;
                break;
            // 一级护理
            case R.id.ll_onelevelpatient:
                if (mOneLevel == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_LEVELONE;
                break;
            // 二级护理
            case R.id.ll_twolevelpatient:
                if (mTwoLevel == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_LEVELTWO;
                break;
            // 三级护理
            case R.id.ll_threelevelpatient:
                if (mThreeLevel == 0) {
                    myAlertDialog.show();
                    return;
                }
                mPatientShow = LoginInfo.PATIENT_LEVELTHREE;
                break;
        }
        showPopupWindow();
    }

    /**
     * 读取所有病患信息
     */
    private void loadPatientList(String officeId) {
//		String url = SettingInfo.SERVICE + Method.PATIENT_INFO_BY_OFFICEID
//				+ "?OfficeId=" + officeId;
//
//
//		OkHttpUtils
//				.get()
//				.url(url)
//				.build()
//				.execute(new StringCallback() {
//					@Override
//					public void onError(Request request, Exception e) {
//
//					}
//
//					@Override
//					public void onResponse(String response) {
//						mPatientList = GsonUtil.getPatientInfoList(response);
//						countPatientNumber();
//						setPatientNumber();
//						setPatientListView();
//					}
//				});
    }

    /**
     * 读取护理记录单
     */
    private void loadNursingRecord(String officeId) {
        String url = SettingInfo.SERVICE + Method.GET_NURSING_RECORD
                + "?OfficeId=" + officeId;

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
                        L.i("护理记录单------" + response);
                        LoginInfo.nursingrecordList = GsonUtil
                                .getNursingRecordList(response);
                    }
                });


    }

    /**
     * 读取异常体温病患
     *
     * @param officeId
     */
    private void loadUnusualTemper(String officeId) {
        String url = SettingInfo.SERVICE
                + Method.GET_NURSING_LIST_YCTW_BY_OFFICEID + "?OfficeId="
                + officeId;


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
                        L.i("异常体温------" + response);
                        mUnusualList = GsonUtil.getUnusualTemper(response);
                        addTemperToInfoceter(mUnusualList);
                    }
                });
    }

    /**
     * 读取60分钟内新开的医嘱
     *
     * @param officeId
     */
    private void loadNewOrder(String officeId, int minute) {
        String url = SettingInfo.NSIS + Method.QUERY_MEDORDER_MOBILE_BY_OFFICE
                + "?OfficeId=" + officeId + "&minute=" + minute;

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
                        L.i("新开医嘱------" + response);
                        mNewOrderList = GsonUtil.getNewOrderData(response);
                        addOrderToInfoceter(mNewOrderList);
                    }
                });

    }

    /**
     * 将读取到的消息显示在消息中心
     */
    private void addTemperToInfoceter(List<UnusualTemper> list) {
        if (list != null && list.size() > 0) {
            for (UnusualTemper unusual : list) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View child = inflater.inflate(R.layout.item_infocenter,
                        mInfoLayout, false);
                TextView tBed = (TextView) child.findViewById(R.id.tv_bedno);
                TextView tName = (TextView) child.findViewById(R.id.tv_name);
                TextView tDesc = (TextView) child.findViewById(R.id.tv_desc);
                ImageView iIcon = (ImageView) child.findViewById(R.id.iv_icon);
                tBed.setText(unusual.getBEDNO());
                tName.setText(unusual.getNAME());
                tDesc.setText("体温异常");
                iIcon.setImageResource(R.drawable.sy_twyc);
                mInfoLayout.addView(child);
            }
        }
    }

    /**
     * 将读取到的消息显示在消息中心
     */
    private void addOrderToInfoceter(List<NewOrder> list) {
        if (list != null && list.size() > 0) {
            for (NewOrder order : list) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View child = inflater.inflate(R.layout.item_infocenter,
                        mInfoLayout, false);
                TextView tBed = (TextView) child.findViewById(R.id.tv_bedno);
                TextView tName = (TextView) child.findViewById(R.id.tv_name);
                TextView tDesc = (TextView) child.findViewById(R.id.tv_desc);
                ImageView iIcon = (ImageView) child.findViewById(R.id.iv_icon);
                tBed.setText(order.getBEDNO());
                tName.setText(order.getPATIENTNAME());
                tDesc.setText("新开医嘱");
                iIcon.setImageResource(R.drawable.sy_jrxk);
                mInfoLayout.addView(child);
            }
        }
    }

    /**
     * 设置显示病患数目相关显示
     *
     * @param json
     */
    private void setPatientNumber() {
        mTextAll.setText(String.valueOf(mAll));
        mTextIn.setText(String.valueOf(mIn));
        mTextOut.setText(String.valueOf(mOut));
        mTextOperation.setText(String.valueOf(mOperation));
        mTextSpecialLevel.setText(String.valueOf(mSpecialLevel));
        mTextOneLevel.setText(String.valueOf(mOneLevel));
        mTextTwoLevel.setText(String.valueOf(mTwoLevel));
        mTextThreeLevel.setText(String.valueOf(mThreeLevel));
    }

    /**
     * 统计相关分类下的病患数目
     *
     * @param json
     */
    private void countPatientNumber() {
        if (mPatientList == null || mPatientList.size() == 0) {
            new MyAlertDialog(this, "病患列表为空").show();
        } else {
            mAll = mPatientList.size();
            mPatientShow = mPatientList;
            List<Patient> patientIn = new ArrayList<Patient>();
            List<Patient> patientOut = new ArrayList<Patient>();
            List<Patient> patientTomorrow = new ArrayList<Patient>();
            List<Patient> patientOperation = new ArrayList<Patient>();
            List<Patient> patientLevelSpecial = new ArrayList<Patient>();
            List<Patient> patientLevelOne = new ArrayList<Patient>();
            List<Patient> patientLevelTwo = new ArrayList<Patient>();
            List<Patient> patientLevelThree = new ArrayList<Patient>();
            for (Patient info : mPatientList) {
                String bedNo = info.getBEDNO();
                String inDate = info.getPATIENTINHOSDATE();
                String leaveState = info.getLEAVEHOSPITALSTATE();
                String nursingLevel = info.getNURSELEVEL();
                String operationState = info.getOPERATIONSTATE();
                if ("".equals(bedNo)) {
                    mWait++;
                }
                if (DateUtil.isToday(inDate)) {
                    mIn++;
                    patientIn.add(info);
                }
                if ("今日出院".equals(leaveState)) {
                    mOut++;
                    patientOut.add(info);
                } else if ("明日出院".equals(leaveState)) {
                    mTomorrow++;
                    patientTomorrow.add(info);
                }
                if ("今日手术".equals(operationState)) {
                    mOperation++;
                    patientOperation.add(info);
                }
                if (Method.isNursingSpecial(nursingLevel)) {
                    mSpecialLevel++;
                    patientLevelSpecial.add(info);
                } else if (Method.isNursingOne(nursingLevel)) {
                    mOneLevel++;
                    patientLevelOne.add(info);
                } else if (Method.isNursingTwo(nursingLevel)) {
                    mTwoLevel++;
                    patientLevelTwo.add(info);
                } else {
                    mThreeLevel++;
                    patientLevelThree.add(info);
                }
            }
            LoginInfo.PATIENT_ALL = mPatientList;
            LoginInfo.PATIENT_IN = patientIn;
            LoginInfo.PATIENT_OUT = patientOut;
            LoginInfo.PATIENT_TOMORROW = patientTomorrow;
            LoginInfo.PATIENT_OPERATION = patientOperation;
            LoginInfo.PATIENT_LEVELSPECIAL = patientLevelSpecial;
            LoginInfo.PATIENT_LEVELONE = patientLevelOne;
            LoginInfo.PATIENT_LEVELTWO = patientLevelTwo;
            LoginInfo.PATIENT_LEVELTHREE = patientLevelThree;
            // 初始化默认数据
            LoginInfo.patientIndex = 0;
            LoginInfo.patient = LoginInfo.PATIENT_ALL
                    .get(LoginInfo.patientIndex);
        }
    }

    /**
     * 初始化病人列表信息
     */
    protected void setPatientListView() {
        mListView = mPatientWindow.getListView();
        if (mPatientShow != null && mPatientShow.size() > 0) {
            mAdapter = new PatientAdapter(this, mPatientShow,
                    R.layout.item_patient);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(MainActivity.this,
                            PatientinfoActivity.class);
                    LoginInfo.patientIndex = position;
                    LoginInfo.patient = LoginInfo.PATIENT_ALL.get(position);
                    startActivity(intent);
                }
            });
        }
    }

    @OnClick(R.id.iv_refresh)
    public void onRefresh(View v) {
        mInfoLayout.removeAllViews();
        // 读取体温异常的病患
        loadUnusualTemper(LoginInfo.OFFICE_ID);
        // 读取新开的医嘱
        loadNewOrder(LoginInfo.OFFICE_ID, MINUTE);
    }

    @Override
    protected void onResume() {
        ScanUtil.registerScanReceiver(this);
        super.onResume();
    }

}