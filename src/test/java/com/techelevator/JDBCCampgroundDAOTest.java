package com.techelevator;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.POJO.Campground;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;

public class JDBCCampgroundDAOTest extends DAOIntegrationTest {

	private JDBCCampgroundDAO dao;
	private JdbcTemplate jdbcTemplate;
	private static final String TEST_PARK_NAME = "Arches";

	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(getDataSource());
		dao = new JDBCCampgroundDAO(getDataSource());
	}

	@Test
	public void get_all_campgrounds_by_park_name_test() {
		List<Campground> myList = dao.getAllCampgroundsByParkName(TEST_PARK_NAME);

		assertNotNull(myList);
		assertEquals(3, myList.size());
		assertEquals("Devil's Garden", myList.get(0).getName());
		assertEquals("01", myList.get(0).getOpenFromMm());
		assertEquals("12", myList.get(0).getOpenToMm());
		assertEquals(BigDecimal.valueOf(25.00), myList.get(0).getDailyFee());
	}

}