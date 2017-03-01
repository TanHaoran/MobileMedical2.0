package com.thr.mobilemedical;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Request;
import com.thr.bean.Office;
import com.thr.bean.User;
import com.thr.constant.LoginInfo;
import com.thr.constant.Method;
import com.thr.constant.SettingInfo;
import com.thr.utils.DeviceUtil;
import com.thr.utils.GsonUtil;
import com.thr.utils.KeyBoardUtils;
import com.thr.utils.L;
import com.thr.view.ConfigDialog;
import com.thr.view.MyAlertDialog;
import com.thr.view.MyProgressDialog;
import com.thr.view.OfficeDialog;
import com.thr.view.WheelView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * 登陆界面
 *
 * @author Jerry Tan
 * @version 1.0
 * @description LoginActivity.java
 * @date 2015年9月9日 下午1:45:37
 * @Company Buzzlysoft
 * @email thrforever@126.com
 * @remark
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends Activity {

    @ViewInject(R.id.et_username)
    private EditText mEditUsername;
    @ViewInject(R.id.et_password)
    private EditText mEditPassword;

    private MyProgressDialog mDialog;

    // 科室集合
    private List<Office> mOffices;
    // 人员列表集合
    private List<User> mUsers;
    // 用户名集合
    private List<String> mUsernames;

    // 人员选择弹出框
    private PopupWindow mPopWindow;

    private BroadcastReceiver scanReceiver;
    private SoundPool soundpool;
    private int soundid;

    // 选择人员序号
    int mIndex = -1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String result = (String) msg.obj;
            verifyLoginInfo(result);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().finishAllActivity();
        MyApplication.getInstance().addActivity(this);
        initView();
        loadConfig();
//        loadDeviceInfo();
    }

    /**
     * 读取科室内的人员
     */
    private void loadUser(String officeId) {
        String url = SettingInfo.SERVICE + Method.GET_OPERATOR_BY_OFFICEID
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
                        mUsers = GsonUtil.getUsers(response);
                        showUserPopWindow();
                    }
                });
    }

    private void showUserPopWindow() {
        initPopupData();
        // 设置layout在PopupWindow中显示的位置
        mPopWindow.showAtLocation(
                LoginActivity.this.findViewById(R.id.rl_main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initView() {
        // 隐藏软键盘的弹出
        mEditUsername.setInputType(InputType.TYPE_NULL);
        mEditUsername.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditUsername
                        .setBackgroundResource(R.drawable.edit_login_bg_focuse);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (LoginInfo.OFFICE_ID == null) {
                        MyAlertDialog alertDialog = new MyAlertDialog(
                                LoginActivity.this, "请先选择科室");
                        alertDialog.show();
                    } else {
                        KeyBoardUtils.closeKeybord(mEditPassword,
                                LoginActivity.this);
                        loadUser(LoginInfo.OFFICE_ID);
                    }
                    return true;
                }
                return false;
            }
        });
        mPopWindow = initPopupWindow();
        mDialog = new MyProgressDialog(this);
    }

    /**
     * 读取服务配置信息
     */
    private void loadConfig() {
        SharedPreferences sp = getSharedPreferences(ConfigDialog.CONFIG,
                Activity.MODE_PRIVATE);
        String add = sp.getString("add",
                "http://192.168.0.100:4010/MMIPService/");
        String nsis = sp.getString("nsis",
                "http://192.168.0.100:4514/NSIS_WebAPI/");
        String user = sp.getString("user", "");
        String officeId = sp.getString("office_id", null);
        String officeName = sp.getString("office_name", null);
        SettingInfo.SERVICE = add;
        SettingInfo.NSIS = nsis;
        LoginInfo.OFFICE_ID = officeId;
        LoginInfo.OFFICE_NAME = officeName;
        if (!"".equals(user)) {
            User u = GsonUtil.getLoginInfo(user);
            mEditUsername.setText(u.getNAME());
            saveLoginInfo(u);
        }
    }

    /**
     * 初始化底布弹出框
     */
    @SuppressWarnings("deprecation")
    private PopupWindow initPopupWindow() {
        // 初始化弹出窗口
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater
                .inflate(R.layout.pop_selection, null);
        mPopWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setFocusable(true);
        // 设置动画效果
        mPopWindow.setAnimationStyle(R.style.pop_changehead);

        return mPopWindow;
    }

    /**
     * 设置弹出框的内容数据、监听等
     */
    private void initPopupData() {
        // 设置监听事件
        TextView ensure = (TextView) mPopWindow.getContentView().findViewById(
                R.id.tv_ensure);
        TextView cancle = (TextView) mPopWindow.getContentView().findViewById(
                R.id.tv_cancle);

        final WheelView wheelView = (WheelView) mPopWindow.getContentView()
                .findViewById(R.id.wv_selection);
        wheelView.setOffset(2);
        // 如果之前选择好的，弹出式就显示之前选择的框
        if (mIndex == -1) {
            wheelView.setSeletion(0);
        } else {
            wheelView.setSeletion(mIndex);
        }
        if (mUsers != null && mUsers.size() > 0) {
            mUsernames = new ArrayList<String>();
            for (User user : mUsers) {
                mUsernames.add(user.getNAME());
            }
            wheelView.setItems(mUsernames);
        }
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                L.i("selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
        // 确认事件
        ensure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取选中的用户并显示到界面上
                mIndex = wheelView.getSeletedIndex();
                if (mUsers != null) {
                    User select = mUsers.get(mIndex);
                    saveLoginInfo(select);
                    mEditUsername.setText(select.getNAME());
                    // 选好操作员后，焦点跳转到密码框，弹出输入法
                    mEditPassword.setFocusable(true);
                    mEditPassword.requestFocus();
                    KeyBoardUtils
                            .openKeybord(mEditPassword, LoginActivity.this);
                }
                mEditUsername
                        .setBackgroundResource(R.drawable.edit_login_bg_normal);
                mPopWindow.dismiss();
            }
        });
        // 取消事件
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEditUsername
                        .setBackgroundResource(R.drawable.edit_login_bg_normal);
                mPopWindow.dismiss();
            }
        });
    }

    /**
     * 保存登录信息
     *
     * @param user
     */
    protected void saveLoginInfo(User user) {
        LoginInfo.user = user;
    }

    /**
     * 保存登录信息
     *
     * @param userJson
     */
    protected void saveLoginInfo(String userJson) {
        LoginInfo.user = GsonUtil.getLoginInfo(userJson);
        SharedPreferences sp = getSharedPreferences(ConfigDialog.CONFIG,
                Activity.MODE_PRIVATE);
        sp.edit().putString("user", userJson).commit();
    }

    @OnClick(R.id.btn_login)
    public void loginBtn(View view) {
        if (TextUtils.isEmpty(mEditUsername.getText().toString())) {
            MyAlertDialog alertDialog = new MyAlertDialog(this, "请选择登陆用户");
            alertDialog.show();
            return;
        }
        if (TextUtils.isEmpty(mEditPassword.getText().toString())) {
            MyAlertDialog alertDialog = new MyAlertDialog(this, "请输入密码");
            alertDialog.show();
            return;
        }
        String username = LoginInfo.user.getLOGINNAME();
        String password = mEditPassword.getText().toString();
        login(username, password);
    }

    /**
     * 根据用户名和密码判断登陆
     *
     * @param username
     * @param password
     */
    private void login(final String username, String password) {

        String url = SettingInfo.SERVICE + Method.LOGIN_NURSE + "?LoginName="
                + username + "&Password=" + password;


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
                        Message msg = new Message();
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 根据返回值判断登陆信息是否正确
     *
     * @param json
     */
    private void verifyLoginInfo(String json) {
        String content = null;
        if ("\"-1\"".equals(json)) {
            content = "用户名不存在";
        } else if ("\"-2\"".equals(json)) {
            content = "密码错误";
        } else if (json != null && (json.trim().length() > 3)) {
            saveLoginInfo(json);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        MyAlertDialog alertDialog = new MyAlertDialog(this, content);
        alertDialog.show();
    }

    @OnClick(R.id.iv_logo)
    public void onOffice(View v) {
        loadOffice();
    }

    /**
     * 读取可用科室
     */
    private void loadOffice() {

        String url = SettingInfo.SERVICE + Method.GET_REGIST_OFFICE;

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
                        mOffices = GsonUtil.getOfficeList(response);
                        showOfficeList();
                    }
                });

    }

    /**
     * 显示科室列表
     */
    protected void showOfficeList() {
        OfficeDialog dialog = new OfficeDialog(this);
        dialog.show();
        dialog.setItems(mOffices);
    }

    @OnClick(R.id.tv_logo)
    public void onConfig(View v) {
        ConfigDialog dialog = new ConfigDialog(this);
        dialog.show();
    }

    @Override
    protected void onResume() {
        registerScanReceiver();
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (scanReceiver != null) {
            unregisterReceiver(scanReceiver);
        }
        super.onDestroy();
    }

    private void registerScanReceiver() {

        scanReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "扫描到了", Toast.LENGTH_SHORT).show();
                int barocodelen = intent.getIntExtra("length", -1);
                String result = new String();
                if (barocodelen == -1) {
                    result = intent.getStringExtra(SettingInfo.RECEIVE_STRING)
                            .toString().replace("#", "");
                } else {
                    soundpool.play(soundid, 1, 1, 0, 0, 1);
                    byte[] barcode = intent.getByteArrayExtra("barcode");
                    byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                    android.util.Log.i("debug", "----codetype--" + temp);
                    result = new String(barcode, 0, barocodelen).replace("#",
                            "");
                }

                L.i(result);
                String[] resultArray = result.split("@");
                if (resultArray.length == 3) {
                    String username = resultArray[0];
                    String password = resultArray[1];
                    String officeId = resultArray[2];
                    if (LoginInfo.OFFICE_ID.equals(officeId)) {
                        login(username, password);
                    } else {
                        new MyAlertDialog(LoginActivity.this, "科室不相符，请重新选择科室！")
                                .show();
                    }
                }

            }
        };

        IntentFilter filter = new IntentFilter("android.provider.sdlMessage");

        registerReceiver(scanReceiver, filter);

    }

    /**
     * 读取设备的各种信息，用来区别不同的接收器和广播
     */
    private void loadDeviceInfo() {
        String info = DeviceUtil.getCPUSerial();
        L.i(info);
//        if (info.trim().startsWith("ARM")) {
//            SettingInfo.SCAN_FILTER = SettingInfo.T8_SCANFILTER;
//            SettingInfo.RECEIVE_STRING = SettingInfo.T8_RECEIVE_STRING;
//        } else {
//            SettingInfo.SCAN_FILTER = SettingInfo.NEW_SCAN_FILTER;
//            SettingInfo.RECEIVE_STRING = SettingInfo.NEW_RECEIVE_STRING;
//        }
        SettingInfo.SCAN_FILTER = "android.provider.sdlMessage";
        SettingInfo.RECEIVE_STRING = "msg";
    }
}
