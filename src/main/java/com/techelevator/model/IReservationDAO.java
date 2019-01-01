package com.techelevator.model;


import java.time.LocalDate;
import java.util.Date;

import com.techelevator.POJO.Reservation;

public interface IReservationDAO {

	public void createNewReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate);
	public Reservation getNewReservation(int siteId);
	
}
