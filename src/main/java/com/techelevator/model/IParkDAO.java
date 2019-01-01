package com.techelevator.model;

import com.techelevator.POJO.Park;

public interface IParkDAO {

	public Park getParkByName(String parkName);
	
}
