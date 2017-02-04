package com.thr.bean;

/**
 * @description 检验报告内容实体
 * @date 2015年10月19日 上午9:56:45
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class ExamItem {

	/**
	 * 检验名称
	 */
	private String name;
	/**
	 * 检验结果
	 */
	private String result;
	/**
	 * 检验提示（偏高、偏低、正常）
	 */
	private String hint;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

}
