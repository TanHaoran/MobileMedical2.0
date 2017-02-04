package com.thr.constant;

/**
 * @description 保存设置信息的类
 * @date 2015年9月18日 下午2:06:43
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class SettingInfo {
	/**
	 * 服务地址
	 */
	public static String SERVICE = "";
	/**
	 * NSIS服务
	 */
	public static String NSIS = "";

	/**
	 * T8过滤器
	 */
	public static final String T8_SCANFILTER = "com.android.scanner.result";
	/**
	 * 联想（黄色）过滤器
	 */
	public static final String NEW_SCAN_FILTER = "action.barcode.reader.value";
	/**
	 * EMH过滤器
	 */
	public static final String EMH_SCANFILTER = "com.ge.action.barscan";
	/**
	 * 过滤器
	 */
	public static String SCAN_FILTER = "";

	/**
	 * T8接受字串
	 */
	public static final String T8_RECEIVE_STRING = "result";
	/**
	 * 联想（黄色）接受字串
	 */
	public static final String NEW_RECEIVE_STRING = "borcode_value";
	/**
	 * EMH接受字串
	 */
	public static final String EMH_RECEIVE_STRING = "value";
	/**
	 * 接受字串
	 */
	public static String RECEIVE_STRING = "";

}
