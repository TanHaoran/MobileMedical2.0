package com.thr.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.thr.bean.Order;
import com.thr.mobilemedical.R;
import com.thr.utils.CommonAdapter;
import com.thr.utils.ViewHolder;

/**
 * @description 静滴医嘱适配器
 * @date 2015年10月19日 下午4:09:12
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class DropAdapter extends CommonAdapter<Order> {

	public DropAdapter(Context context, List<Order> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, Order item) {
		// 医嘱名
		TextView tName = (TextView) helper.getView(R.id.tv_ordername);
		// 用量
		TextView tUsage = (TextView) helper.getView(R.id.tv_orderusage);
		// 频率
		TextView tFrequence = (TextView) helper.getView(R.id.tv_orderfrequence);
		// 执行时间
		TextView tTime = (TextView) helper.getView(R.id.tv_ordertime);
		// 状态
		TextView tState = (TextView) helper.getView(R.id.tv_state);
		// 状态图标
		ImageView iState = (ImageView) helper.getView(R.id.iv_state);

		tName.setText(item.getORDERCONTENT());
		tUsage.setText(item.getDOSETYPE());
		tFrequence.setText(item.getFREQUENCY());
		tTime.setText(item.getEXECTIME());
		// 如果有执行时间就显示已执行
		if (item.getEXECTIME() != null && !"".equals(item.getEXECTIME())) {
			tState.setText("已执行");
			iState.setImageResource(R.drawable.icon_yzx);
		} else {
			tState.setText("未执行");
			iState.setImageResource(R.drawable.icon_wzx);
		}
	}
}