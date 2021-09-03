package junit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.bikersland.Main;
import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.EventDetailsControllerApp;
import com.bikersland.controller.application.MainControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.model.User;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConvertMethods;

public class JUnit {
	@Test
	public void testEventIdFortunato() throws InternalDBException {
		EventBean eventBean = EventDetailsControllerApp.getEventById(2);
		
		assertEquals((int)2, (int)eventBean.getId());
	}
	
	@Test
	public void testDateFortunato() {
		Main.setLocale(Locale.ITALIAN);
		Date testDate = new Date(Long.valueOf("1629012345678"));
		String testConvertedDate = ConvertMethods.dateToLocalFormat(testDate);
		
		assertEquals("15-08-2021", testConvertedDate);
	}
	
	@Test
	public void testAdventureIsATagFortunato() throws InternalDBException {
		List<String> testTagList = MainControllerApp.getTags();
		
		assertThat(testTagList, hasItem("Adventure"));
	}
	
	@Test
	public void testNullLoggedUserDeSantis() {
		User testLoggedUser = LoginSingleton.getLoginInstance().getUser();
		
		assertNull(testLoggedUser);
	}
}
