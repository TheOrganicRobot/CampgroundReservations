package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.POJO.Campground;
import com.techelevator.model.ICampgroundDAO;

public class JDBCCampgroundDAO implements ICampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgroundsByParkName(String parkName) {
		List<Campground> campgrounds = new ArrayList<>();
		String sqlGetAllCampgrounds = "select c.campground_id, c.park_id, c.name, c.open_from_mm, c.open_to_mm, c.daily_fee "
				+ "from park p " + "inner join campground c " + "on p.park_id = c.park_id " + "where p.name = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgrounds, parkName);
		while (results.next()) {
			campgrounds.add(mapRowToCampground(results));
		}
		return campgrounds;
	}

	private Campground mapRowToCampground(SqlRowSet results) {

		Campground camp;
		camp = new Campground();
		camp.setCampgroundId(results.getInt("campground_id"));
		camp.setParkId(results.getInt("park_id"));
		camp.setName(results.getString("name"));
		camp.setOpenFromMm(results.getString("open_from_mm"));
		camp.setOpenToMm(results.getString("open_to_mm"));
		camp.setDailyFee(results.getBigDecimal("daily_fee"));
		return camp;
	}

}
