package com.thr.mobilemedical;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thr.constant.LoginInfo;
import com.thr.utils.SelectbarUtil;

/**
 * @author Jerry Tan
 * @version 1.0
 * @description 病患信息-花费查询界面
 * @date 2015年9月11日14:12:14
 * @Company Buzzlysoft
 * @email thrforever@126.com
 * @remark
 */
public class CostFragment extends Fragment {

    private View v;

    private TextView mInhos;
    private TextView mName;
    private TextView mBed;
    private TextView mTotal;
    private TextView mBalance;
    private TextView mArrearage;
    private TextView mPayway;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cost, container, false);
        initView();
        return v;
    }

    private void initView() {

        initSelectbar();

        mInhos = (TextView) v.findViewById(R.id.tv_inhosid);
        mName = (TextView) v.findViewById(R.id.tv_nametext);
        mBed = (TextView) v.findViewById(R.id.tv_bedtext);
        mTotal = (TextView) v.findViewById(R.id.tv_total);
        mBalance = (TextView) v.findViewById(R.id.tv_balance);
        mArrearage = (TextView) v.findViewById(R.id.tv_arrearage);
        mPayway = (TextView) v.findViewById(R.id.tv_payway);

        initData();
    }

    public void initSelectbar() {
        SelectbarUtil selectbarUtil = new SelectbarUtil() {

            @Override
            public void data() {
                initData();
            }
        };
        selectbarUtil.initView(getActivity(), v);
    }

    public void initData() {
        if (LoginInfo.patient != null) {
            mInhos.setText(LoginInfo.patient.getPATIENTHOSID());
            mName.setText(LoginInfo.patient.getNAME());
            mBed.setText(LoginInfo.patient.getBEDNO());
            mTotal.setText("¥ " + LoginInfo.patient.getTOTALCOST());
            mBalance.setText("¥ " + LoginInfo.patient.getBALANCE());


            try {
                if (Double.parseDouble(LoginInfo.patient.getBALANCE()) < 0) {
                    mArrearage.setText("¥ " + (-(Double.parseDouble(LoginInfo.patient.getBALANCE()))));
                } else {
                    mArrearage.setText("¥ 0");
                }
            } catch (NumberFormatException e) {
                mArrearage.setText("¥ 0");
                e.printStackTrace();
            }
            mPayway.setText(LoginInfo.patient.getMEDICARETYPE());
        }
    }
}
