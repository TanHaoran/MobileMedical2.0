package com.thr.bean;

/**
 * @description 科室实体
 * @date 2015-9-15 11:52:54
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class Office {

	/**
	 * 科室id
	 */
	private String OFFICEHISID;
	/**
	 * 科室名称
	 */
	private String OFFICENAME;
	/**
	 * 是否禁止
	 */
	private String ISDISABLE;
	/**
	 * 医院名称
	 */
	private String HOSTIPALNAME;

	public Office() {
	}

	public String getOFFICEHISID() {
		return OFFICEHISID;
	}

	public void setOFFICEHISID(String oFFICEHISID) {
		OFFICEHISID = oFFICEHISID;
	}

	public String getOFFICENAME() {
		return OFFICENAME;
	}

	public void setOFFICENAME(String oFFICENAME) {
		OFFICENAME = oFFICENAME;
	}

	public String getISDISABLE() {
		return ISDISABLE;
	}

	public void setISDISABLE(String iSDISABLE) {
		ISDISABLE = iSDISABLE;
	}

	public String getHOSTIPALNAME() {
		return HOSTIPALNAME;
	}

	public void setHOSTIPALNAME(String hOSTIPALNAME) {
		HOSTIPALNAME = hOSTIPALNAME;
	}

	@Override
	public String toString() {
		return "OfficeInfo [OFFICEHISID=" + OFFICEHISID + ", OFFICENAME="
				+ OFFICENAME + ", ISDISABLE=" + ISDISABLE + ", HOSTIPALNAME="
				+ HOSTIPALNAME + "]";
	}

}
