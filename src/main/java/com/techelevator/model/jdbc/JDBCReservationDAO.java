package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.Date;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.POJO.Reservation;
import com.techelevator.model.IReservationDAO;

public class JDBCReservationDAO implements IReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void createNewReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {

		String sqlCreateNewReservation = "insert into reservation " + "values (DEFAULT, ?, ?, ?, ?, current_date);";
		jdbcTemplate.update(sqlCreateNewReservation, siteId, name, fromDate, toDate);

	}

	@Override
	public Reservation getNewReservation(int siteId) {
		Reservation reservation = new Reservation();

		String sqlRetrieveNewReservation = "SELECT reservation_id, site_id, name, from_date, to_date, create_date from reservation;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlRetrieveNewReservation);
		while (results.next()) {
			reservation = mapRowToReservation(results);
		}
		return reservation;
	}

	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation;
		reservation = new Reservation();

		reservation.setReservationId(results.getInt("reservation_id"));
		reservation.setSiteId(results.getInt("site_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		reservation.setCreateDate(results.getDate("create_date").toLocalDate());
		return reservation;
	}
}
