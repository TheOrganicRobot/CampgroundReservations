package com.techelevator.model.jdbc;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.POJO.Park;
import com.techelevator.model.IParkDAO;

public class JDBCParkDAO implements IParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Park getParkByName(String parkName) {
		Park park = null;
		String sqlGetParkByName = "select park_id, name, location, establish_date, area, visitors, description "
				+ "from park " + "where name = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkByName, parkName);
		if (results.next()) {
			park = mapRowToPark(results);
		}
		return park;
	}

	private Park mapRowToPark(SqlRowSet results) {

		Park park;
		park = new Park();
		park.setParkId(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		return park;
	}

}
