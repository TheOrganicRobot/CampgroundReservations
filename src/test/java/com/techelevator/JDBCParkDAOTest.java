package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.POJO.Park;
import com.techelevator.model.jdbc.JDBCParkDAO;

public class JDBCParkDAOTest extends DAOIntegrationTest {

	private JDBCParkDAO dao;
	private JdbcTemplate jdbcTemplate;
	private static final String TEST_PARKNAME = "The Back Nine";
	private static final String TEST_LOCATION = "Nebraska";
	private static final LocalDate TEST_ESTABLISH_DATE = LocalDate.of(2016, 10, 20);
	private static final int TEST_AREA = 500050000;
	private static final int TEST_VISITORS = 1000000;
	private static final String TEST_DESCRIPTION = "A scenic semblance filling the air with God's fart, filling the human mind with a "
			+ "unity of spirit resembling that which only be described as... a Shart.";

	@Before
	public void setup() {
		String sqlMakeFakePark = "INSERT INTO park VALUES(DEFAULT, ?, ?, ?, ?, ?, ?);";
		jdbcTemplate = new JdbcTemplate(getDataSource());
		jdbcTemplate.update(sqlMakeFakePark, TEST_PARKNAME, TEST_LOCATION, TEST_ESTABLISH_DATE, TEST_AREA,
				TEST_VISITORS, TEST_DESCRIPTION);
		dao = new JDBCParkDAO(getDataSource());
	}

	@Test
	public void get_park_by_name_test() {
		Park testPark = dao.getParkByName(TEST_PARKNAME);
		Park park = new Park();
		park.setParkId(4);
		park.setName(TEST_PARKNAME);
		park.setLocation(TEST_LOCATION);
		park.setEstablishDate(TEST_ESTABLISH_DATE);
		park.setArea(TEST_AREA);
		park.setVisitors(TEST_VISITORS);
		park.setDescription(TEST_DESCRIPTION);

		assertNotEquals(null, testPark.getParkId());
		assertParkAreEqual(park, testPark);

	}

	private void assertParkAreEqual(Park expected, Park actual) {
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getLocation(), actual.getLocation());
		assertEquals(expected.getEstablishDate(), actual.getEstablishDate());
		assertEquals(expected.getArea(), actual.getArea());
		assertEquals(expected.getVisitors(), actual.getVisitors());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

}