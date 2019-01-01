package com.techelevator.POJO;

import java.math.BigDecimal;

public class Campground {
	
	private int campgroundId;
	private int parkId;
	private String name;
	private String openFromMm;
	private String openToMm;
	private BigDecimal dailyFee;
	
	
	
	public int getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenFromMm() {
		return openFromMm;
	}
	public void setOpenFromMm(String openFromMm) {
		this.openFromMm = openFromMm;
	}
	public String getOpenToMm() {
		return openToMm;
	}
	public void setOpenToMm(String openToMm) {
		this.openToMm = openToMm;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	

}
