package com.techelevator;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.techelevator.POJO.Reservation;
import com.techelevator.model.jdbc.JDBCReservationDAO;

public class JDBCReservationDAOTest extends DAOIntegrationTest {

	private JDBCReservationDAO dao;
	private JdbcTemplate jdbcTemplate;
	private static final int TEST_SITE_ID = 270;
	private static final String TEST_NAME = "Barney";
	private static final LocalDate TEST_FROM_DATE = LocalDate.of(2019, 10, 20);
	private static final LocalDate TEST_TO_DATE = LocalDate.of(2019, 10, 30);

	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		dao = new JDBCReservationDAO(getDataSource());
	}

	@Test
	public void create_new_reservation_test() {

		dao.createNewReservation(TEST_SITE_ID, TEST_NAME, TEST_FROM_DATE, TEST_TO_DATE);
		Reservation actual = dao.getNewReservation(TEST_SITE_ID);

		Reservation expected = new Reservation();
		expected.setSiteId(TEST_SITE_ID);
		expected.setName(TEST_NAME);
		expected.setFromDate(TEST_FROM_DATE);
		expected.setToDate(TEST_TO_DATE);

		assertReservationsAreEqual(expected, actual);
	}

	private void assertReservationsAreEqual(Reservation expected, Reservation actual) {
		assertEquals(expected.getSiteId(), actual.getSiteId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getFromDate(), actual.getFromDate());
		assertEquals(expected.getToDate(), actual.getToDate());
	}

}
