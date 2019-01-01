package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.POJO.Site;
import com.techelevator.model.ISiteDAO;

public class JDBCSiteDAO implements ISiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> getAvailableSites(int campgroundId, LocalDate from_date, LocalDate to_date) {
	
		List<Site> sites = new ArrayList<>();
		String sqlAvailableSite = "Select distinct site.site_id, site.campground_id, site.site_number, site.max_occupancy, site.accessible, site.max_rv_length, "
				+ "site.utilities,  ((? ::date - ? ::date) * c.daily_fee)::decimal as daily_fee from site "
				+ "join campground as c on site.campground_id = c.campground_id " + "where site.campground_id = ? "
				+ "and site_id not in " + "(SELECT distinct s.site_id " + "FROM site as s " + "JOIN campground as c "
				+ "ON s.campground_id=c.campground_id " + "JOIN reservation as r " + "ON s.site_id = r.site_id "
				+ "WHERE (r.from_date between ? and ?) " + "OR (r.to_date between ? and ?) " + ") " 
				+ "ORDER BY site.site_id "
				+ "LIMIT 5;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAvailableSite, to_date, from_date, campgroundId, from_date,
				to_date, from_date, to_date);
		while (results.next()) {
			sites.add(mapRowToSites(results));
		}
		return sites;
	}

	private Site mapRowToSites(SqlRowSet results) {

		Site site;
		site = new Site();
		site.setSiteId(results.getInt("site_id"));
		site.setCampgroundId(results.getInt("campground_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccessible(results.getBoolean("accessible"));
		site.setMaxRvLength(results.getInt("max_rv_length"));
		site.setUtilities(results.getBoolean("utilities"));
		site.setTripCost(results.getBigDecimal("daily_fee"));
		return site;
	}

}
