package receiptManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MyReceipt {
	String vendor;
	String address;
	String phone;
	String date; 
	String time;
	String total;
	String tax;
	ArrayList <HashMap<String, String>> rcgItem;
	String xmlExportPath;
	
	public String getVendor() {
		return vendor;
	}
	public String getAddress() {
		return address;
	}
	public String getPhone() {
		return phone;
	}
	public String getDate() {
		return date;
	}
	public String getTime() {
		return time;
	}
	public String getTotal() {
		return total;
	}
	public String getTax() {
		return tax;
	}
	public ArrayList<HashMap<String, String>> getRcgItem() {
		return rcgItem;
	}
	public String getXmlExportPath() {
		return xmlExportPath;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public void setRcgItem(ArrayList<HashMap<String, String>> rcgItem) {
		this.rcgItem = rcgItem;
	}
	public void setXmlExportPath(String xmlExportPath) {
		this.xmlExportPath = xmlExportPath;
	}
	
	@Override
	public String toString() {
		return "MyReceipt [vendor=" + vendor + ", address=" + address + ", phone=" + phone + ", date=" + date
				+ ", time=" + time + ", total=" + total + ", tax=" + tax + ", rcgItem=" + rcgItem + ", xmlExportPath="
				+ xmlExportPath + "]";
	}


}
