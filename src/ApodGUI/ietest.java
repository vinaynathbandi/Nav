package ApodGUI;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class ietest {
	static WebDriver driver;

	@Test
	public void test() throws InterruptedException, AWTException {
		// TODO Auto-generated method stub

		System.setProperty("webdriver.chrome.driver", "F:\\Automation\\lib\\Drivers\\chromedriver.exe");
		driver = new ChromeDriver();

		// Enter Url
		driver.get("http://localhost:8080/navigator/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Login page
		driver.findElement(By.id("username")).sendKeys("Admin");
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(6000);

		// Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys("Dashboardname");

		// Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);

		// Create local queue viewlet
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();

		// Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys("QueueName");

		// Select WGS type
		Select WGSSelection = new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText("MQM - 0");

		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);

		/*WebElement ngs = driver.findElement(By.className("datatable-body"))
				.findElement(By.tagName("datatable-scroller"));

		// .findElement(By.tagName("datatable-row-wrapper")).findElement(By.tagName("datatable-body-row"));
		List<WebElement> ngs_ele = ngs.findElements(By.tagName("datatable-row-wrapper"));
		System.out.println("List of wrappers are: " +ngs_ele.size());

		for (WebElement row : ngs_ele) {
			WebElement row_ele = row.findElement(By.tagName("datatable-body-row"));

			System.out.println("no is " + row_ele.getAttribute("innerHTML"));
			List<WebElement> divs = row_ele.findElements(By.tagName("div"));
			System.out.println("no is " + divs.size());

			for (WebElement ss : divs) {
				String classname = ss.getAttribute("class");
				if (classname.contains("datatable-row-center")) {
					System.out.println("Value is: " + ss.getAttribute("InnerText"));
					System.out.println("Text is: " + ss.getText());
				}

			}
		}

		
		 * for(int i=2; i<=16; i++) { String
		 * Depth=driver.findElement(By.xpath("//datatable-row-wrapper["+ i
		 * +"]/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		 * int result = Integer.parseInt(Depth);
		 * System.out.println("Result values are: "+result);
		 * 
		 * }
		 */

		
		

	}

}
