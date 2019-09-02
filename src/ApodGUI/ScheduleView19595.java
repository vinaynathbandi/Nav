package ApodGUI;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;

@Listeners(TestClass.class)
public class ScheduleView19595 
{
static WebDriver driver;
	
	@Parameters({"sDriver", "sDriverpath", "URL", "uname", "password", "Dashboardname", "wgs"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String URL, String uname, String password, String Dashboardname, String wgs) throws Exception
	{
		Settings.read();
		String urlstr=Settings.getSettingURL();
		URL= urlstr+URL;
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver, sDriverpath);
		driver=new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new EdgeDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new FirefoxDriver();
		}
		
		//Enter Url
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(10000);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(wgs);
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		
		
	}
	
	@TestRail(testCaseId = 17)
	@Test(priority=1)
	public static void ExportAllMessages(ITestContext context) throws InterruptedException
	{
		try {
		//Export All Messages As MMF
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MMFMousehour=new Actions(driver);
		MMFMousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]"))).perform();
	
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]/ul/li")).click();
		Thread.sleep(1000);   
		
		//Click on Schedule
		 driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		
				
		//Click on Date picker
		driver.findElement(By.xpath("//app-datetime-input/div/div/div/button")).click();
		
		
		//Select Date
		//driver.findElement(By.xpath("//form[@id='scheduleForm']/app-datetime-input/div/div/ngb-datepicker/div[2]/div/ngb-datepicker-month-view/div[5]/div[3]/div")).click();
		driver.findElement(By.xpath("//form[@id='scheduleForm']/app-datetime-input/div/div/div/button")).click();
		
		//Click on OK
		driver.findElement(By.xpath("//app-mod-scheduler/div/div[2]/div/div/div/button")).click();
		
		//Click on OK 
		 driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		 Thread.sleep(25000);
		  
		 try
		 {
		//Handle Error message
		 driver.findElement(By.xpath("//app-mod-errors-display/div/button")).click();
		 Thread.sleep(8000);  
		 }catch (Exception e) {
			// TODO: handle exception
		}
		 
		 
		 //Store the object name before clicking
		String ObjectName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(ObjectName);
		Thread.sleep(4000);
		
		 //Click Schedular ICon
		driver.findElement(By.xpath("//datatable-body-cell[3]/div/i")).click();
		Thread.sleep(1000);       
		
		//Store the Object name
		String NodeData=driver.findElement(By.xpath("//div/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).getText();
		
		//Verification of node is added or not
		if(ObjectName.equalsIgnoreCase(NodeData))
		{
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Schedular list popup success");
			System.out.println("Schedular list popup success");
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Schedular not working");
			System.out.println("Schedular not working");
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
			driver.findElement(By.xpath("Node creation failed")).click();
		}
		
		Thread.sleep(1000);
		}catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			context.setAttribute("Status", 4);
			context.setAttribute("Comment", "Error occured");
			
			driver.close();
		}
		
	}
	
	@Test(priority=2)
	public void Logout() throws Exception
	{
		try
		{
		//Close the opened Dashboard
		driver.findElement(By.cssSelector(".active .g-tab-btn-close-block")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		}
		catch (Exception e)
		{
			System.out.println("Dashboards are not present");
		}
		Thread.sleep(1000);
		
		//Logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.close();
	}
	
	
}
