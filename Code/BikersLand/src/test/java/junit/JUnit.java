package junit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.bikersland.Main;
import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.EventDetailsControllerApp;
import com.bikersland.controller.application.MainControllerApp;
import com.bikersland.db.UserDAO;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.InvalidLoginException;
import com.bikersland.exception.user.UserNotFoundException;
import com.bikersland.model.User;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConvertMethods;

public class JUnit {
	
	/* Test #1 Fortunato Andrea */
	@Test
	public void testEventIdFortunato() throws InternalDBException {
		EventDetailsControllerApp eventDetailsControllerApp = new EventDetailsControllerApp();
		EventBean eventBean = eventDetailsControllerApp.getEventById(2);
		
		assertEquals((int)2, (int)eventBean.getId());
	}
	
	/* Test #2 Fortunato Andrea */
	@Test
	public void testDateFortunato() {
		Main.setLocale(Locale.ITALIAN);
		Date testDate = new Date(Long.valueOf("1629012345678"));
		String testConvertedDate = ConvertMethods.dateToLocalFormat(testDate);
		
		assertEquals("15-08-2021", testConvertedDate);
	}
	
	/* Test #3 Fortunato Andrea */
	@Test
	public void testAdventureIsATagFortunato() throws InternalDBException {
		MainControllerApp mainControllerApp = new MainControllerApp();
		List<String> testTagList = mainControllerApp.getTags();
		
		assertThat(testTagList, hasItem("Adventure"));
	}
	
	/* Test #1 De Santis Ludovico */
	@Test
	public void testNullLoggedUserDeSantis() {
		User testLoggedUser = LoginSingleton.getLoginInstance().getUser();
		
		assertNull(testLoggedUser);
	}
	
	/* Test #2 De Santis Ludovico */
	@Test
	public void testExistingUserDeSantis() throws SQLException, ImageConversionException {
		
		int result;
		try {
			User user = UserDAO.getUserByUsername("ludovix");
			result = 1;
		} catch (UserNotFoundException e) {
			
			result = 0;
		}
		
		assertEquals(1, result);
	}
	
	/* Test #3 De Santis Ludovico */
	@Test
	public void testUserNotRegisteredDeSantis() throws SQLException {
		
		int result;
		
		try {
			UserDAO.askLogin("Notexistinguser", "pass");
			result = 1;
		} catch (InvalidLoginException e) {
			result = 0;
		}
		
		assertEquals(0, result);
	}
}
