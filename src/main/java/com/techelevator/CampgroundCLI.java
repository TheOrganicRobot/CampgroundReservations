package com.techelevator;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.text.WordUtils;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import com.techelevator.POJO.Campground;
import com.techelevator.POJO.Park;
import com.techelevator.POJO.Reservation;
import com.techelevator.POJO.Site;
import com.techelevator.model.ICampgroundDAO;
import com.techelevator.model.IParkDAO;
import com.techelevator.model.IReservationDAO;
import com.techelevator.model.ISiteDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;

import java.text.DateFormatSymbols;

@SuppressWarnings("deprecation")
public class CampgroundCLI {

	private static final String MAIN_MENU_OPTION_ACADIA = "Acadia";
	private static final String MAIN_MENU_OPTION_ARCHES = "Arches";
	private static final String MAIN_MENU_OPTION_CUYAHOGA = "Cuyahoga Valley";
	private static final String MAIN_MENU_OPTION_QUIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_ACADIA, MAIN_MENU_OPTION_ARCHES,
			MAIN_MENU_OPTION_CUYAHOGA, MAIN_MENU_OPTION_QUIT };

	private static final String MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String MENU_OPTION_RETURN_TO_PREVIOUS = "Return to previous screen";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { MENU_OPTION_VIEW_CAMPGROUNDS,
			MENU_OPTION_SEARCH_FOR_RESERVATION, MENU_OPTION_RETURN_TO_PREVIOUS };

	private static final String MENU_OPTION_SEARCH_AVAILABLE_RESERVATIONS = "Search for Available Reservation";
	private static final String[] RESERVATION_MENU_OPTIONS = new String[] { MENU_OPTION_SEARCH_AVAILABLE_RESERVATIONS,
			MENU_OPTION_RETURN_TO_PREVIOUS };

	private Menu menu;
	private ICampgroundDAO campgroundDAO;
	private IParkDAO parkDAO;
	private IReservationDAO reservationDAO;
	private ISiteDAO siteDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
		parkDAO = new JDBCParkDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
	}

	public void run() {

		displayApplicationBanner();
		System.out.println("Welcome to the National Park Reservation Registry\n");
		parksMenu();
	}

	private void parksMenu() {

		while (true) {
			printHeading("Please select a park to begin.");

			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_ACADIA) || choice.equals(MAIN_MENU_OPTION_ARCHES)
					|| choice.equals(MAIN_MENU_OPTION_CUYAHOGA)) {
				System.out.println();
				Park park = parkDAO.getParkByName(choice);
				handlePark(park);
				campgroundsMenu(choice, park);

			} else if (choice.equals(MAIN_MENU_OPTION_QUIT)) {
				System.exit(0);
			}
		}
	}

	private void campgroundsMenu(String parkChoice, Park park) {
		while (true) {
			printHeading(parkChoice + " National Park Campgrounds");
			String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
			if (choice.equals(MENU_OPTION_VIEW_CAMPGROUNDS)) {
				System.out.println();
				List<Campground> campgrounds = campgroundDAO.getAllCampgroundsByParkName(parkChoice);
				handleCampgrounds(park, campgrounds);

			} else if (choice.equals(MENU_OPTION_SEARCH_FOR_RESERVATION)) {
				System.out.println();
				List<Campground> campgrounds = campgroundDAO.getAllCampgroundsByParkName(parkChoice);
				handleCampgrounds(park, campgrounds);
				reservationsMenu(campgrounds, parkChoice, park);

			} else if (choice.equals(MENU_OPTION_RETURN_TO_PREVIOUS)) {
				System.out.println();
				parksMenu();
			}
		}
	}

	private void reservationsMenu(List<Campground> campgrounds, String parkChoice, Park park) {

		while (true) {
			printHeading("Select a Command");
			String choice = (String) menu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS);
			if (choice.equals(MENU_OPTION_SEARCH_AVAILABLE_RESERVATIONS)) {
				System.out.println();
				reservationSubMenu(campgrounds, parkChoice, park);
			} else if (choice.equals(MENU_OPTION_RETURN_TO_PREVIOUS)) {
				System.out.println();
				campgroundsMenu(parkChoice, park);
			}
		}
	}

	// -------------Beginning of the menu for user to make reservation
	private void reservationSubMenu(List<Campground> campgrounds, String parkChoice, Park park) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		Scanner scanner = new Scanner(System.in);
		LocalDate fromDate;
		LocalDate toDate;
		LocalDate now = LocalDate.now();
		String today = now.format(formatter);
		int campgroundId = 0;
		int siteNumber = 0;
		int siteId = 0;
		int counter = 0;
		// ----------------------------Which campground would user like-------------
		while (true) {
			System.out.print("\nWhich campground would you like to make a reservation in? (enter 0 to cancel)? ");
			String response1 = scanner.nextLine();
			try {
				campgroundId = Integer.parseInt(response1);
			} catch (NumberFormatException e) {
				System.out.println("\n----Please enter a numeric value----");
			}
			if (campgroundId == 0) {
				System.out.println("\n\nYour reservation has been cancelled.\n");
				campgroundsMenu(parkChoice, park);
				break;
			} else if (campgroundId > campgrounds.size() || campgroundId < 1) {
				System.out.println("\nPlease enter a valid selection");
			} else {
				campgroundId = campgrounds.get(campgroundId - 1).getCampgroundId();
				break;
			}
		}
		// ----------------------------Which arrival date would the user
		// like-------------
		while (true) {
			System.out.print("\nWhat is the arrival date? (MM/DD/YYYY) ");
			String response2 = scanner.nextLine();
			try {
				fromDate = LocalDate.parse(response2, formatter);
				if (fromDate.compareTo(now) < 0) {
					System.out.println("\n\nSorry, you cannot enter a date prior to " + today + ".");
					continue;
				}
				break;
			} catch (DateTimeParseException e) {
				System.out.println("\n\n----Date not entered in proper format----");
			}
		}
		// ----------------------------Which departure date would user like-------------
		while (true) {
			System.out.print("\nWhat is the departure date? (MM/DD/YYYY) ");
			String response3 = scanner.nextLine();

			try {
				String userFirstDate = formatter.format(fromDate);
				toDate = LocalDate.parse(response3, formatter);
				if (fromDate.compareTo(toDate) >= 0) {
					System.out.println("\n\nSorry, you must book for at least one day after " + userFirstDate + ".");
					continue;
				}
				break;
			} catch (DateTimeParseException e) {
				System.out.println("\n\n----Date not entered in proper format----");
			}
		}
		// ----------------------------Call for the seasonal date checker-------------
		if (campgroundId == 2 || campgroundId == 3 || campgroundId == 7) {
			dateChecker(campgroundId, fromDate, toDate, campgrounds, parkChoice, park);
		}

		List<Site> sites = siteDAO.getAvailableSites(campgroundId, fromDate, toDate);

		// --------------Informs the user that there are no reservations at the selected

		if (sites.size() == 0) {
			System.out.println("\n\nSorry, there are no availabilites during the time selected.");
			System.out.print("Would you like to enter an alternate date range? (Y or N) ");
			String choice = scanner.nextLine();
			if (choice.equalsIgnoreCase("Y")) {
				handleCampgrounds(park, campgrounds);
				reservationSubMenu(campgrounds, parkChoice, park);
			} else if (choice.equalsIgnoreCase("N")) {
				handleCampgrounds(park, campgrounds);
				reservationsMenu(campgrounds, parkChoice, park);
			}

		} else {
			handleSite(sites);
		}
		// ----------------------------Which reservation site would the user would
		// like-------------
		while (true) {
			System.out.print("\nWhich site should be reserved(enter 0 to cancel)? ");
			String response4 = scanner.nextLine();

			try {
				siteNumber = Integer.parseInt(response4);
			} catch (NumberFormatException e) {
				System.out.println("\n\n----Please enter a numeric value----");
			}
			if (siteNumber == 0) {
				System.out.println("\n\nYour reservation has been cancelled.\n");
				campgroundsMenu(parkChoice, park);
				break;
			} else {
				for (Site site : sites) {
					if (site.getSiteNumber() == siteNumber) {
						siteId = site.getSiteId();
						counter++;
						break;
					}
				}
				if (counter == 0) {
					System.out.println("\nPlease enter a valid site number");
					continue;
				}
			}
			break;
		}
		// ----------------------------Which name will the reservation be made
		// in-------------
		System.out.print("\nWhich name should the reserveration be made under? ");
		String name = scanner.nextLine();
		reservationDAO.createNewReservation(siteId, name, fromDate, toDate);
		Reservation newReservation = reservationDAO.getNewReservation(siteId);
		handleReservation(newReservation);

		scanner.close();
	}

	private void dateChecker(int campgroundId, LocalDate fromDate, LocalDate toDate, List<Campground> campgrounds,
			String parkChoice, Park park) {
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
		String convertFromDate = monthFormat.format(fromDate);
		String convertToDate = monthFormat.format(toDate);
		int start = Integer.parseInt(convertFromDate);
		int end = Integer.parseInt(convertToDate);
		Scanner scanner = new Scanner(System.in);

		if (start < 5 || campgroundId == 2 && end >= 10 || campgroundId == 3 && end >= 11
				|| campgroundId == 7 && end >= 12) {
			System.out.println("\nSorry, this campground is closed during this time of year.");
			System.out.print("Would you like to enter an alternate date range? (Y or N) ");
			String choice = scanner.nextLine();
			if (choice.equalsIgnoreCase("Y")) {
				handleCampgrounds(park, campgrounds);
				reservationSubMenu(campgrounds, parkChoice, park);
			} else if (choice.equalsIgnoreCase("N")) {
				handleCampgrounds(park, campgrounds);
				reservationsMenu(campgrounds, parkChoice, park);
			}
		}
		scanner.close();
	}

	private void handlePark(Park park) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		System.out.println();
		System.out.println("**********PARK INFORMATION**********");
		System.out.printf("%1$14s%2$11s", park.getName(), " National Park\n");
		System.out.println("====================================\n");
		System.out.println("Location:    " + "\t\t" + park.getLocation());
		System.out.println("Established:    " + "\t" + park.getEstablishDate().format(format));
		System.out.println(
				"Area:    " + "\t\t" + NumberFormat.getNumberInstance(Locale.US).format(park.getArea()) + " sq km");
		System.out.println(
				"Annual Visitors:    " + "\t" + NumberFormat.getNumberInstance(Locale.US).format(park.getVisitors()));
		System.out.println();
		System.out.println();
		System.out.println(WordUtils.wrap(park.getDescription(), 36) + "\n");
		System.out.println("====================================\n");

	}

	private void handleSite(List<Site> sites) {

		String format = "%1$-10s%2$-16s%3$-13s%4$-12s%5$-10s" + "$" + "%6$-,1.2f";
		System.out.println();
		System.out.println();
		printHeading("Search Results Matching your Criteria");
		System.out.println();
		System.out.println();
		System.out.println("Site No.  Max Occupants   Accessible   RV Length   Utility   Cost");
		System.out.println("--------- --------------- ------------ ----------- --------- ------");
		for (Site site : sites) {
			String length = null;
			if (site.getMaxRvLength() == 0) {
				length = "N/A";
			} else {
				length = Integer.toString(site.getMaxRvLength());
			}
			System.out.format(format, site.getSiteNumber(), site.getMaxOccupancy(),
					BooleanUtils.toStringYesNo(site.isAccessible()), length,
					BooleanUtils.toStringYesNo(site.isUtilities()), site.getTripCost());
			System.out.println();
		}
		System.out.println("--------- --------------- ------------ ----------- --------- ------");
		System.out.println();
		System.out.println();
	}

	private void handleCampgrounds(Park park, List<Campground> campgrounds) {

		String format = "%1$-3s%2$-32s%3$-10s%4$-11s" + "$" + "%5$-7.2f";
		System.out.println();
		System.out.println();
		printHeading(park.getName() + " National Park Campgrounds");
		System.out.println();
		System.out.println();
		System.out.println("   NAME		\t\t   OPEN      CLOSE      DAILY FEE");
		System.out.println("---------------------------------- --------- ---------- ----------");
		int counter = 1;
		for (Campground campground : campgrounds) {
			int from = Integer.parseInt(campground.getOpenFromMm());
			int to = Integer.parseInt(campground.getOpenToMm());
			System.out.format(format, "#" + counter, campground.getName(),
					new DateFormatSymbols().getMonths()[from - 1], new DateFormatSymbols().getMonths()[to - 1],
					campground.getDailyFee());
			System.out.println();
			counter++;
		}
		System.out.println("---------------------------------- --------- ---------- ----------");
		System.out.println();
		System.out.println();
	}

	private void handleReservation(Reservation newReservation) {
		System.out.println("\nThe reservation for " + newReservation.getName()
				+ " has been made and the confimation ID is: " + newReservation.getReservationId());
		System.out.println();
		parksMenu();
	}

	private void displayApplicationBanner() {
		System.out.println(
				" _   _       _   _                   _   _____           _      _____                                _   _               _____            _     _              ");
		System.out.println(
				"| \\ | |     | | (_)                 | | |  __ \\         | |    |  __ \\                              | | (_)             |  __ \\          (_)   | |             ");
		System.out.println(
				"|  \\| | __ _| |_ _  ___  _ __   __ _| | | |__) |_ _ _ __| | __ | |__) |___  ___  ___ _ ____   ____ _| |_ _  ___  _ __   | |__) |___  __ _ _ ___| |_ _ __ _   _ ");
		System.out.println(
				"| . ` |/ _` | __| |/ _ \\| '_ \\ / _` | | |  ___/ _` | '__| |/ / |  _  // _ \\/ __|/ _ \\ '__\\ \\ / / _` | __| |/ _ \\| '_ \\  |  _  // _ \\/ _` | / __| __| '__| | | |");
		System.out.println(
				"| |\\  | (_| | |_| | (_) | | | | (_| | | | |  | (_| | |  |   <  | | \\ \\  __/\\__ \\  __/ |   \\ V / (_| | |_| | (_) | | | | | | \\ \\  __/ (_| | \\__ \\ |_| |  | |_| |");
		System.out.println(
				"|_| \\_|\\__,_|\\__|_|\\___/|_| |_|\\__,_|_| |_|   \\__,_|_|  |_|\\_\\ |_|  \\_\\___||___/\\___|_|    \\_/ \\__,_|\\__|_|\\___/|_| |_| |_|  \\_\\___|\\__, |_|___/\\__|_|   \\__, |");
		System.out.println(
				"                                                                                                                                     __/ |                __/ |");
		System.out.println(
				"                                                                                                                                    |___/                |___/ ");

	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}

	}
}
