package com.techelevator;

import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.POJO.Site;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class JDBCSiteDAOTest extends DAOIntegrationTest {

	private JDBCSiteDAO dao;
	private JdbcTemplate jdbcTemplate;
	private static final int TEST_CAMPGROUND_ID = 6;
	private static final LocalDate TEST_FROM_DATE = LocalDate.of(2019, 10, 20);
	private static final LocalDate TEST_TO_DATE = LocalDate.of(2019, 10, 30);

	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		dao = new JDBCSiteDAO(getDataSource());
	}

	@Test
	public void get_available_sites_test() {
		List<Site> myList = dao.getAvailableSites(TEST_CAMPGROUND_ID, TEST_FROM_DATE, TEST_TO_DATE);

		assertNotNull(myList);
		assertEquals(1, myList.size());
		assertEquals(1, myList.get(0).getSiteNumber());
		assertEquals(55, myList.get(0).getMaxOccupancy());
		assertTrue(myList.get(0).isAccessible());
		assertEquals(0, myList.get(0).getMaxRvLength());
		assertFalse(myList.get(0).isUtilities());
	}

}