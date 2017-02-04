package com.thr.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thr.bean.Blood;
import com.thr.bean.Exam;
import com.thr.bean.ExamItem;
import com.thr.bean.NewOrder;
import com.thr.bean.Nursingpaper;
import com.thr.bean.Nursingrecord;
import com.thr.bean.NursingrecordFiled;
import com.thr.bean.Office;
import com.thr.bean.Order;
import com.thr.bean.Patient;
import com.thr.bean.Temperature;
import com.thr.bean.UnusualTemper;
import com.thr.bean.User;

public class GsonUtil {

	private static Gson gson = new Gson();

	/**
	 * 去掉多余的反斜杠和引号
	 * 
	 * @param json
	 * @return
	 */
	public static String cutSlash(String json) {
		int length = json.length();
		json = json.substring(1, length - 1);
		json = json.replace("\\", "");
		return json;
	}

	/**
	 * 去掉多余的引号
	 * 
	 * @param json
	 * @return
	 */
	public static String cutMark(String json) {
		int length = json.length();
		json = json.substring(1, length - 1);
		return json;
	}

	/**
	 * 解析人员信息
	 * 
	 * @param json
	 * @return
	 */
	public static List<User> getUsers(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<User>>() {
		}.getType();
		List<User> userList = gson.fromJson(json, listType);
		return userList;
	}

	/**
	 * 解析登陆信息
	 * 
	 * @param json
	 * @return
	 */
	public static User getLoginInfo(String json) {
		json = cutSlash(json);
		json = cutMark(json);
		return gson.fromJson(json, User.class);
	}

	/**
	 * 解析病患信息
	 * 
	 * @param json
	 * @return
	 */
	public static List<Patient> getPatientInfoList(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<Patient>>() {
		}.getType();
		List<Patient> patientList = gson.fromJson(json, listType);
		return patientList;
	}

	/**
	 * 解析护理记录单信息
	 * 
	 */
	public static List<Nursingrecord> getNursingRecordList(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<Nursingrecord>>() {
		}.getType();
		List<Nursingrecord> nursingRecordList = gson.fromJson(json, listType);
		return nursingRecordList;
	}

	/**
	 * 解析医嘱信息
	 * 
	 * @param json
	 * @return
	 */
	public static List<Order> getOrderList(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<Order>>() {
		}.getType();
		List<Order> orderList = gson.fromJson(json, listType);
		return orderList;
	}

	/**
	 * 解析护理记录单字段
	 * 
	 * @param json
	 * @return
	 */
	public static List<NursingrecordFiled> getNursingStructureList(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<NursingrecordFiled>>() {
		}.getType();
		List<NursingrecordFiled> nursingRecordList = gson.fromJson(json,
				listType);
		return nursingRecordList;
	}

	/**
	 * 解析科室
	 * 
	 * @param json
	 * @return
	 */
	public static List<Office> getOfficeList(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<Office>>() {
		}.getType();
		List<Office> officeList = gson.fromJson(json, listType);
		return officeList;
	}

	/**
	 * 解析病患体温单
	 * 
	 * @param json
	 * @return
	 */
	public static List<Temperature> getPatientTemperature(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<Temperature>>() {
		}.getType();
		List<Temperature> temperatureList = gson.fromJson(json, listType);
		return temperatureList;
	}

	/**
	 * 
	 * 解析护理记录单单条记录
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, String> getNursingrecordToMap(String json) {
		return getSingleNursingrecordData(json);
	}

	/**
	 * 
	 * 解析护理记录单出入量数据
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, String> getInandoutToMap(String json) {
		return getSingleNursingrecordData(json);
	}

	private static Map<String, String> getSingleNursingrecordData(String json) {
		json = cutSlash(json);
		json = cutMark(json);
		GsonBuilder gb = new GsonBuilder();
		Gson gson = gb.create();
		Map<String, String> map = gson.fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		return map;
	}

	/**
	 * 解析体温异常的用户
	 * 
	 */
	public static List<UnusualTemper> getUnusualTemper(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<UnusualTemper>>() {
		}.getType();
		List<UnusualTemper> unusualList = gson.fromJson(json, listType);
		return unusualList;
	}

	/**
	 * 解析血氧（血糖、血压）数据
	 * 
	 * @throws JSONException
	 * 
	 */
	public static List<Blood> getBloodData(String json) {

		json = cutSlash(json);
		List<Blood> bloodoxygenList = new ArrayList<Blood>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray data = object.getJSONArray("data");
			String s = data.toString();
			Type listType = new TypeToken<List<Blood>>() {
			}.getType();
			bloodoxygenList = gson.fromJson(s, listType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bloodoxygenList;
	}

	/**
	 * 解析护理记录单数据
	 * 
	 * @throws JSONException
	 * 
	 */
	public static List<Nursingpaper> getNursingpaperData(String json) {
		json = cutSlash(json);
		List<Nursingpaper> bloodoxygenList = new ArrayList<Nursingpaper>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray data = object.getJSONArray("data");
			String s = data.toString();
			Type listType = new TypeToken<List<Nursingpaper>>() {
			}.getType();
			bloodoxygenList = gson.fromJson(s, listType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bloodoxygenList;
	}

	/**
	 * 解析检验单列表数据
	 * 
	 * @throws JSONException
	 * 
	 */
	public static List<Exam> getExamList(String json) {
		json = cutSlash(json);
		Type listType = new TypeToken<List<Exam>>() {
		}.getType();
		List<Exam> examList = gson.fromJson(json, listType);
		return examList;
	}

	/**
	 * 解析检验单单条数据
	 * 
	 * @throws JSONException
	 * 
	 */
	public static List<ExamItem> getExamItemList(String json) {
		json = cutSlash(json);
		List<ExamItem> examItemList = new ArrayList<ExamItem>();
		try {
			JSONArray data = new JSONArray(json);
			for (int i = 0; i < data.length(); i++) {
				ExamItem item = new ExamItem();
				JSONObject o = data.getJSONObject(i);
				item.setName(o.getString("项目名称"));
				item.setResult(o.getString("检验结果"));
				item.setHint(o.getString("提示"));
				examItemList.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return examItemList;
	}

	/**
	 * 解析新开一组数据
	 * 
	 * @throws JSONException
	 * 
	 */
	public static List<NewOrder> getNewOrderData(String json) {

		json = cutSlash(json);
		List<NewOrder> newOrderList = new ArrayList<NewOrder>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray data = object.getJSONArray("data");
			String s = data.toString();
			Type listType = new TypeToken<List<NewOrder>>() {
			}.getType();
			newOrderList = gson.fromJson(s, listType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newOrderList;
	}

	/**
	 * 解析医嘱（静滴）信息
	 * 
	 * @param json
	 * @return
	 */
	public static List<Order> getDropOrderList(String json) {
		json = cutSlash(json);
		List<Order> orderList = new ArrayList<Order>();
		try {
			JSONArray data = new JSONArray(json);
			for (int i = 0; i < data.length(); i++) {
				Order order = new Order();
				JSONObject o = data.getJSONObject(i);
				order.setORDERCONTENT(o.getString("ORDERCONTENT"));
				order.setDOSETYPE(o.getString("DOSETYPE"));
				order.setFREQUENCY(o.getString("FREQUENCY"));
				order.setEXECTIME(o.getString("执行时间"));
				orderList.add(order);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderList;
	}
}
