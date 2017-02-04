package com.thr.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.thr.constant.ActivityName;
import com.thr.constant.LoginInfo;
import com.thr.mobilemedical.LoginActivity;
import com.thr.mobilemedical.MainActivity;
import com.thr.mobilemedical.MyApplication;
import com.thr.mobilemedical.NursinglistActivity;
import com.thr.mobilemedical.NursingpatrolActivity;
import com.thr.mobilemedical.NursingrecordActivity;
import com.thr.mobilemedical.DropexecuteActivity;
import com.thr.mobilemedical.PatientinfoActivity;
import com.thr.mobilemedical.R;
import com.thr.mobilemedical.SettingAcitivity;
import com.thr.utils.ActivityUtil;
import com.thr.utils.ScreenUtils;
import com.thr.view.MyDialog.SureClickListener;

/**
 * @description 左侧菜单弹出框
 * @date 2015-9-24 13:45:29
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class LeftMenuPopupWindow extends PopupWindow implements OnClickListener {

	private Context mContext;

	/**
	 * 主View
	 */
	private View mMenu;
	/**
	 * 姓名TextView
	 */
	private TextView mName;
	/**
	 * 科室TextView
	 */
	private TextView mOffice;

	private LinearLayout mMenuHomepage; // 主页菜单
	private LinearLayout mMenuPatientinfo; // 病患信息菜单
	private LinearLayout mMenuNursingrecord; // 护理录入菜单
	private LinearLayout mMenuOrderexecute; // 医嘱执行菜单
	private LinearLayout mMenuNursinglist; // 护理清单菜单
	private LinearLayout mMenuNursingpatrol; // 护理巡视菜单
	private LinearLayout mMenuSetting; // 设置菜单
	private LinearLayout mMenuQuit; // 退出菜单

	private ImageView mImgHomepage;
	private ImageView mImgPatientinfo;
	private ImageView mImgNursingrecord;
	private ImageView mImgOrderexecute;
	private ImageView mImgNursinglist;
	private ImageView mImgNursingpatrol;

	private TextView mTextHomepage;
	private TextView mTextPatientinfo;
	private TextView mTextNursingrecord;
	private TextView mTextOrderexecute;
	private TextView mTextNursinglist;
	private TextView mTextNursingpatrol;

	private int mStatusHeight; // 状态栏高度
	private int mScreenHeight; // 屏幕的高度

	public LeftMenuPopupWindow(Context context) {
		super();
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenu = inflater.inflate(R.layout.view_menu, null);
		getScreenSize();
		// 设置SelectPicPopupWindow的View
		setContentView(mMenu);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(mScreenHeight - mStatusHeight);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 设置出入动画
		setAnimationStyle(R.style.leftmenu_style);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
		initView();
	}

	/**
	 * 取得屏幕中所需的各项高度
	 */
	private void getScreenSize() {
		mStatusHeight = ScreenUtils.getStatusHeight(mContext);
		mScreenHeight = ScreenUtils.getScreenHeight(mContext);
	}

	/**
	 * 初始化显示和监听器
	 */
	private void initView() {

		mName = (TextView) mMenu.findViewById(R.id.tv_name);
		mOffice = (TextView) mMenu.findViewById(R.id.tv_office);

		mMenuHomepage = (LinearLayout) mMenu.findViewById(R.id.ll_homepage);
		mMenuPatientinfo = (LinearLayout) mMenu
				.findViewById(R.id.ll_patientinfo);
		mMenuNursingrecord = (LinearLayout) mMenu
				.findViewById(R.id.ll_nursingrecord);
		mMenuOrderexecute = (LinearLayout) mMenu
				.findViewById(R.id.ll_orderexecute);
		mMenuNursinglist = (LinearLayout) mMenu
				.findViewById(R.id.ll_nursinglist);
		mMenuNursingpatrol = (LinearLayout) mMenu
				.findViewById(R.id.ll_nursingpatrol);
		mMenuSetting = (LinearLayout) mMenu.findViewById(R.id.ll_setting);
		mMenuQuit = (LinearLayout) mMenu.findViewById(R.id.ll_quit);

		mImgHomepage = (ImageView) mMenu.findViewById(R.id.iv_homepage);
		mImgPatientinfo = (ImageView) mMenu.findViewById(R.id.iv_patientinfo);
		mImgNursingrecord = (ImageView) mMenu
				.findViewById(R.id.iv_nursingrecord);
		mImgOrderexecute = (ImageView) mMenu.findViewById(R.id.iv_orderexecute);
		mImgNursinglist = (ImageView) mMenu.findViewById(R.id.iv_nursinglist);
		mImgNursingpatrol = (ImageView) mMenu
				.findViewById(R.id.iv_nursingpatrol);

		mTextHomepage = (TextView) mMenu.findViewById(R.id.tv_homepage);
		mTextPatientinfo = (TextView) mMenu.findViewById(R.id.tv_patientinfo);
		mTextNursingrecord = (TextView) mMenu
				.findViewById(R.id.tv_nursingrecord);
		mTextOrderexecute = (TextView) mMenu.findViewById(R.id.tv_orderexecute);
		mTextNursinglist = (TextView) mMenu.findViewById(R.id.tv_nursinglist);
		mTextNursingpatrol = (TextView) mMenu
				.findViewById(R.id.tv_nursingpatrol);

		mName.setText(LoginInfo.user.getNAME());
		mOffice.setText(LoginInfo.OFFICE_NAME);

		String activityName = ActivityUtil.getCurrentActivity(mContext);
		if (activityName.endsWith(ActivityName.MAIN_ACTIVITY)) {
			mImgHomepage.setImageResource(R.drawable.left_menu_sy_n);
			makeTextHighlight(mTextHomepage);
		} else if (activityName.endsWith(ActivityName.PATIENTINFO_ACTIVITY)) {
			mImgPatientinfo.setImageResource(R.drawable.left_menu_bhxx_n);
			makeTextHighlight(mTextPatientinfo);
		} else if (activityName.endsWith(ActivityName.NURSINGRECORD_ACTIVITY)) {
			mImgNursingrecord.setImageResource(R.drawable.left_menu_hllr_n);
			makeTextHighlight(mTextNursingrecord);
		} else if (activityName.endsWith(ActivityName.ORDEREXECUTE_ACTIVITY)) {
			mImgOrderexecute.setImageResource(R.drawable.left_menu_yzzx_n);
			makeTextHighlight(mTextOrderexecute);
		} else if (activityName.endsWith(ActivityName.NURSINGLIST_ACTIVITY)) {
			mImgNursinglist.setImageResource(R.drawable.left_menu_hlqd_n);
			makeTextHighlight(mTextNursinglist);
		} else if (activityName.endsWith(ActivityName.NURSINGPATROL_ACTIVITY)) {
			mImgNursingpatrol.setImageResource(R.drawable.left_menu_hlxs_n);
			makeTextHighlight(mTextNursingpatrol);
		}

		mMenuHomepage.setOnClickListener(this);
		mMenuPatientinfo.setOnClickListener(this);
		mMenuNursingrecord.setOnClickListener(this);
		mMenuOrderexecute.setOnClickListener(this);
		mMenuNursinglist.setOnClickListener(this);
		mMenuNursingpatrol.setOnClickListener(this);
		mMenuSetting.setOnClickListener(this);
		mMenuQuit.setOnClickListener(this);

		mMenu.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int width = mMenu.findViewById(R.id.rl_main).getRight();
				int x = (int) event.getX();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (x > width) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	/**
	 * 使当前标签高亮
	 * 
	 * @param textView
	 */
	private void makeTextHighlight(TextView textView) {
		textView.setTextColor(mContext.getResources().getColor(
				R.color.slidingmenu_text_select));
	}

	@Override
	public void onClick(View v) {
		// 首先获取当前Activity类名，如果一致，只需关闭菜单即可
		String activityName = ActivityUtil.getCurrentActivity(mContext);
		Intent intent = new Intent();
		switch (v.getId()) {
		// 主页
		case R.id.ll_homepage:
			if (activityName.endsWith(ActivityName.MAIN_ACTIVITY)) {
				dismiss();
				return;
			} else {
				intent.setClass(mContext, MainActivity.class);
			}
			break;
		// 病患信息
		case R.id.ll_patientinfo:
			if (activityName.endsWith(ActivityName.PATIENTINFO_ACTIVITY)) {
				dismiss();
				return;
			} else {
				intent.setClass(mContext, PatientinfoActivity.class);
			}
			break;
		// 护理录入
		case R.id.ll_nursingrecord:
			if (activityName.endsWith(ActivityName.NURSINGRECORD_ACTIVITY)) {
				dismiss();
				return;
			} else {
				intent.setClass(mContext, NursingrecordActivity.class);
			}
			break;
		// 医嘱执行
		case R.id.ll_orderexecute:
			if (activityName.endsWith(ActivityName.ORDEREXECUTE_ACTIVITY)) {
				dismiss();
				return;
			} else {
				intent.setClass(mContext, DropexecuteActivity.class);
			}
			break;
		// 护理清单
		case R.id.ll_nursinglist:
			if (activityName.endsWith(ActivityName.NURSINGLIST_ACTIVITY)) {
				dismiss();
				return;
			} else {
				intent.setClass(mContext, NursinglistActivity.class);
			}
			break;
		// 护理巡视
		case R.id.ll_nursingpatrol:
			if (activityName.endsWith(ActivityName.NURSINGPATROL_ACTIVITY)) {
				dismiss();
				return;
			} else {
				intent.setClass(mContext, NursingpatrolActivity.class);
			}
			break;
		// 设置
		case R.id.ll_setting:
			dismiss();
			intent.setClass(mContext, SettingAcitivity.class);
			break;
		// 退出
		case R.id.ll_quit:
			showExitDialog();
			return;
		}
		mContext.startActivity(intent);
	}

	/**
	 * 显示退出提示框
	 */
	private void showExitDialog() {

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_logout_text, null);

		final MyDialog dialog = new MyDialog(mContext, view,
				new SureClickListener() {

					@Override
					public void onSureClick() {
						MyApplication.getInstance().finishAllActivity();
						// 退出后跳转到登陆页面
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						mContext.startActivity(intent);
					}
				});
		dialog.show();
	}
}
