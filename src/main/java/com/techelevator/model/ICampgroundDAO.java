package com.techelevator.model;

import java.util.List;

import com.techelevator.POJO.Campground;

public interface ICampgroundDAO {
	
	public List<Campground> getAllCampgroundsByParkName(String parkName);
	

}
