package com.hibernate.demo;

public class Record {
	private int RecordId;
	private int AccountId;
	private String date;
	private String time;
	private int money;
	private int status;		//-2=ת�˳��� -1=ȡ��  1=��� 2=ת������
	
	public int getRecordId() {
		return RecordId;
	}
	
	public void setRecordId(int recordId) {
		this.RecordId = recordId;
	}
	
	public int getAccountId() {
		return AccountId;
	}
	
	public void setAccountId(int AccountId) {
		this.AccountId = AccountId;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int s) {
		this.status = s;
	}
}
