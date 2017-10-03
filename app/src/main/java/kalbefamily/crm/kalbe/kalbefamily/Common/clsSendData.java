package kalbefamily.crm.kalbe.kalbefamily.Common;

import java.util.HashMap;

public class clsSendData {
	private dataJson dtdataJson;
	private HashMap<String, byte[]> FileUpload;
	private HashMap<String, byte[]> FileUploadProfile;
	public dataJson getDtdataJson() {
		return dtdataJson;
	}
	public void setDtdataJson(dataJson dtdataJson) {
		this.dtdataJson = dtdataJson;
	}
	public HashMap<String, byte[]> getFileUpload() {
		return FileUpload;
	}
	public void setFileUpload(HashMap<String, byte[]> fileUpload) {
		FileUpload = fileUpload;
	}
	public HashMap<String, byte[]> getFileUploadProfile() {
		return FileUploadProfile;
	}

	public void setFileUploadProfile(HashMap<String, byte[]> fileUploadProfile) {
		FileUploadProfile = fileUploadProfile;
	}
}
