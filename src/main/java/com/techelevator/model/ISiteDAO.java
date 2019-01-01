package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.POJO.Site;

public interface ISiteDAO {
	
	public List<Site> getAvailableSites(int campgroundId, LocalDate from_date, LocalDate to_date);

}
