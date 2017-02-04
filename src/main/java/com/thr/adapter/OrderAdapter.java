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
 * @description 医嘱列表适配器
 * @date 2015年9月28日 下午12:14:05
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class OrderAdapter extends CommonAdapter<Order> {

	public OrderAdapter(Context context, List<Order> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, Order item) {
		TextView tName = (TextView) helper.getView(R.id.tv_ordername);
		TextView tUsage = (TextView) helper.getView(R.id.tv_orderusage);
		TextView tType = (TextView) helper.getView(R.id.tv_ordertype);
		TextView tFrequence = (TextView) helper.getView(R.id.tv_orderfrequence);
		TextView tTime = (TextView) helper.getView(R.id.tv_ordertime);
		TextView tState = (TextView) helper.getView(R.id.tv_state);
		ImageView iState = (ImageView) helper.getView(R.id.iv_state);

		tName.setText(item.getORDERCONTENT());
		tUsage.setText(item.getDOSETYPE());
		tType.setText(item.getORDERTYPE());
		tFrequence.setText(item.getFREQUENCY());
		tTime.setText(item.getSTARTTIME());
		// 有执行时间就显示已执行
		if (item.getEXECTIME() != null && !"".equals(item.getEXECTIME())) {
			tState.setText("已执行");
			iState.setImageResource(R.drawable.icon_yzx);
		} else {
			tState.setText("未执行");
			iState.setImageResource(R.drawable.icon_wzx);
		}
	}
}
