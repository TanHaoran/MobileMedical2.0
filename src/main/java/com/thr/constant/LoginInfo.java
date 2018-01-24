package com.thr.constant;

import com.thr.bean.Nursingrecord;
import com.thr.bean.Patient;
import com.thr.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry Tan
 * @version 1.0
 * @description 保存登陆信息的类
 * @date 2015年9月18日 下午2:06:43
 * @Company Buzzlysoft
 * @email thrforever@126.com
 * @remark
 */
public class LoginInfo {
    /**
     * 科室id
     */
    public static String OFFICE_ID = "";
    /**
     * 科室名称
     */
    public static String OFFICE_NAME = "";
    /**
     * 密码
     */
    public static String PASSWORD = "";
    /**
     * 登陆人员
     */
    public static User user = new User();
    /**
     * 病患列表
     */
    public static List<Patient> PATIENT_ALL;
    /**
     * 今日入院病患
     */
    public static List<Patient> PATIENT_IN;
    /**
     * 今日出院病患
     */
    public static List<Patient> PATIENT_OUT;
    /**
     * 明日出院病患
     */
    public static List<Patient> PATIENT_TOMORROW;
    /**
     * 今日手术病患
     */
    public static List<Patient> PATIENT_OPERATION;
    /**
     * 特护病患
     */
    public static List<Patient> PATIENT_LEVELSPECIAL;
    /**
     * 一级护理病患
     */
    public static List<Patient> PATIENT_LEVELONE;
    /**
     * 二级护理病患
     */
    public static List<Patient> PATIENT_LEVELTWO;
    /**
     * 三级护理病患
     */
    public static List<Patient> PATIENT_LEVELTHREE;

    /**
     * 当前病患
     */
    public static Patient patient = new Patient();
    /**
     * 病患序号
     */
    public static int patientIndex;
    /**
     * 护理记录单
     */
    public static List<Nursingrecord> nursingrecordList = new ArrayList<Nursingrecord>();
    /**
     * 时间点集合
     */
    public static List<String> timePoints;
    /**
     * 时间点序号
     */
    public static int timeIndex;
}
