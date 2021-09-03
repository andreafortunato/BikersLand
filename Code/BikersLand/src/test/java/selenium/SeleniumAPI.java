package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumAPI {
	public static void main(String[] args) {
		try {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/com/bikersland/drivers/chromedriver.exe");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		/* Test dello studente Fortunato Andrea */
		testFortunato();
		
		/* Test dello studente De Santis Ludovico */
		testDeSantis();
	}
	
	private static void testFortunato() {
		String username = "galaxy";
		String password = "password";
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost:8080/BikersLand/index.jsp");
		// driver.get("https://www.rapidtables.com/convert/temperature/celsius-to-fahrenheit.html");
		
		driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/button[2]")).click();
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(username);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
		driver.findElement(By.xpath("//*[@id=\"loginForm\"]/input")).click();
		driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/button")).click();
		
		WebElement emailElement = driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[3]/th"));
		
		System.out.println("Running Selenium API test for Fortunato Andrea");
		System.out.println(String.format("Email of user '%s': %s\n", username, emailElement.getText().strip()));
		
		driver.close();
	}
	
	private static void testDeSantis() {
		String username = "ludovix";
		String password = "password";
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost:8080/BikersLand/index.jsp");
		// driver.get("https://www.rapidtables.com/convert/temperature/celsius-to-fahrenheit.html");
		
		driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/button[2]")).click();
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(username);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
		driver.findElement(By.xpath("//*[@id=\"loginForm\"]/input")).click();
		driver.findElement(By.xpath("/html/body/div[3]/div[1]/form/input[2]")).click();
		driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[10]/td/input[1]")).click();
		
		WebElement numberOfParticipantsElement = driver.findElement(By.xpath("/html/body/div[2]/form/table/tbody/tr[5]/td"));
		
		System.out.println("Running Selenium API test for De Santis Ludovico");
		System.out.println(String.format("Number of participants: %s\n", numberOfParticipantsElement.getText().strip()));
		
		driver.close();
	}
}
