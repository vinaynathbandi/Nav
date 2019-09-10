package ApodGUI;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ChannelViewlet 
{
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		WGSName =Settings.getWGSNAME();
		UploadFilepath =Settings.getUploadFilepath();
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "wgs", "ChannelName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, int wgs, String ChannelName) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
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
		
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		/*driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(wgs);*/
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		
		
		// ---- Creating Channel Viewlet ----
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
			
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(4)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(ChannelName);
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
	
	}
	
	@Parameters({"schemaName", "Attributes"})
	@TestRail(testCaseId = 100)
	@Test(priority=18)
	public static void ShowObjectAttributes(String schemaName, String Attributes, ITestContext context) throws InterruptedException
	{		
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ChannelAttributes(driver, schemaName, Attributes, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show object attributes working fine");
		
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while showing object attributes, check details: "+  e.getMessage());
		}
	}
	
	@Test(priority=2)
	@TestRail(testCaseId = 101)
	public static void ShowChannelStatus(ITestContext context) throws InterruptedException
	{
		//Select channel status option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Channel Status")).click();
		Thread.sleep(1000);
		
		int Channel_Status=6;
		if(!WGSName.contains("MQM"))
		{
			Channel_Status=7;
		}
		
		//Channel status
		String ChannelStatus=driver.findElement(By.xpath("//datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		System.out.println(ChannelStatus);
		
		//Popup page channel status
		String PopupStatus=driver.findElement(By.cssSelector(".dark-row > td:nth-child(4)")).getText();
		System.out.println(PopupStatus);
		
		//Verification of channel status
		if(ChannelStatus.equals(PopupStatus))
		{
			System.out.println("Channel status is verified");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel status is verified");
		}
		else
		{
			System.out.println("Channel status is not verified");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to verify channel status");
			//Close Channel status popup page
			driver.findElement(By.cssSelector(".close-button")).click();
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("Channel status failed")).click();
		}
		
		//Close Channel status popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=3)
	@TestRail(testCaseId = 102)
	public static void StartChannelFromCommands(ITestContext context) throws InterruptedException
	{
		//Select the Start option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li")).click();
		Thread.sleep(5000);
		
		//Click on yes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		int Channel_Status=6;
		if(!WGSName.contains("MQM"))
		{
			Channel_Status=7;
		}
		//Store the channel status into string
		String Status=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		
		//Verification
		if(Status.equalsIgnoreCase("Running") || Status.equalsIgnoreCase("Starting"))
		{
			System.out.println("Channel is started");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel started and running");
		}
		else
		{
			System.out.println("Channel is not started");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Channel failed to start");
			driver.findElement(By.xpath("Channle start failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Test(priority=4)
	@TestRail(testCaseId = 103)
	public void StopChannelFromCommands(ITestContext context) throws InterruptedException
	{
		//Select the Stop option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[2]")).click();
		Thread.sleep(5000);
		
		//Click on yes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		int Channel_Status=6;
		if(!WGSName.contains("MQM"))
		{
			Channel_Status=7;
		}
		//Store the channel status into string
		String Status=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		
		//Verification
		if(Status.equalsIgnoreCase("Stopping") || Status.equalsIgnoreCase("Stopped"))
		{
			System.out.println("Channel is stopped");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel stopped ");
		}
		else
		{
			System.out.println("Channel is not statoped");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to stop channel");
			driver.findElement(By.xpath("Channel stop failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Test(priority=5)
	@TestRail(testCaseId = 104)
	public void PingChannelFromCommands(ITestContext context) throws InterruptedException
	{
		//Select the Ping channel option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[3]")).click();
		Thread.sleep(2000);
		
		//click on Ping option
		driver.findElement(By.xpath("//div[4]/button")).click();
		
		try
		{
			if(driver.findElement(By.cssSelector(".confirm-btn")).isDisplayed())
			driver.findElement(By.cssSelector(".confirm-btn")).click();
			if(driver.findElement(By.cssSelector(".btn-primary")).isDisplayed())
			driver.findElement(By.cssSelector(".btn-primary")).click();
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Ping channel from command working fine");
		}
		catch (Exception e)
		{
			System.out.println("Ping channel exception occured");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while pinging channel using command");
			if(driver.findElement(By.cssSelector(".btn-primary")).isDisplayed())
			driver.findElement(By.cssSelector(".btn-primary")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=6)
	@TestRail(testCaseId = 105)
	public void ResolveChannelFromCommands(ITestContext context) throws InterruptedException
	{
		try {
		//Select the Resolve channel option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[4]")).click();
		Thread.sleep(2000);
		
		//Click on close button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Resolve channel from command is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while resolving channel using command, check details: "+ e.getMessage());
		}
		
	}
	
	@Test(priority=7)
	@TestRail(testCaseId = 106)
	public void ResetChannelFromCommands(ITestContext context) throws InterruptedException
	{
		try {
		//Select the Resolve channel option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Click on close button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
		Alert al=driver.switchTo().alert();
		System.out.println(al.getText());
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Channel reset command is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while resetting channel using command, check details: "+ e.getMessage());
		}
		
	}
	
	@Test(priority=8)
	@TestRail(testCaseId = 107)
	public static void Properties(ITestContext context) throws InterruptedException
	{
		//Select channel properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(2000);
		
		//Store the editable function in to a string
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification of name field is editable or not
		if(FieldNamevalue == false)
		{
			System.out.println("Channel Name field is UnEditable");
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Channel Name field is UnEditable");
		}
		else
		{
			System.out.println("Channel Name field is Editable");
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Channel Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			driver.findElement(By.xpath("Channel name edit function Failed")).click();
			
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=9)
	@TestRail(testCaseId = 108)
	public static void Events(ITestContext context) throws InterruptedException
	{
		//Select channel Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Events...")).click();
		Thread.sleep(1000);
		
		//Events Popup page
		String Eventdetails=driver.findElement(By.xpath("//th")).getText();
		//System.out.println(Eventdetails);
				
		//Verification condition
		if(Eventdetails.equalsIgnoreCase("Event #"))
		{
			System.out.println("Events page is opened");
			context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Events option is working properly");
		}
		else
		{
			System.out.println("Events page is not opened");
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Events option is not working properly");
			driver.findElement(By.xpath("Events failed")).click();
		}
				
		//Clicking on Events Count
		try 
		{
			if(driver.findElement(By.xpath("//td")).isDisplayed())
			{
				String Eventcount=driver.findElement(By.xpath("//td")).getText();
				System.out.println(Eventcount);
				driver.findElement(By.xpath("//td")).click();
				
				//Click on daignostic tab
				driver.findElement(By.xpath("//app-mod-event-details/div/div/div/button[2]")).click();
				
				//get the vents count and store the into string
				String DignosticCount=driver.findElement(By.xpath("//div/input")).getAttribute("value");
				System.out.println("Daignostic events count:" +DignosticCount);
				
				if(Eventcount.equalsIgnoreCase(DignosticCount))
				{
					System.out.println("Events count is matched");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "Event Count is Matched and working fine");
					//Close the Event details page
					driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
				}
				else
				{
					System.out.println("Events count is not matched");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Got exception while opening events page, check details: ");
					driver.findElement(By.xpath("//app-mod-event-details/div/div[2]/button")).click();
					driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
					driver.findElement(By.id("Events count failed")).click();
				}
				
				
			}
		}
		catch (Exception e)
		{
			System.out.println("Events are not present");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Events not found");
		}
				
		//Close the events popup page
		driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
		
	}
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId = 109)
	@Test(priority=10)
	public static void AddToFavorites(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//CLick on Viewlet and choose favorite viewlet crate check box
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		//WGS selection
		Select wgsdropdown=new Select(driver.findElement(By.name("wgs")));
		wgsdropdown.selectByVisibleText(WGSName);
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		int ChannelName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ChannelName_Index=4;
		}
		//Store String value
		String channelName=driver.findElement(By.xpath("//datatable-body-cell["+ ChannelName_Index +"]/div/span")).getText();
		
		
		//Select Add to Favorites option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		//Get the data from favorite viewlet
		String Favoritedata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verify the channel added to Favorite viewlet
		if(Favoritedata.contains(channelName))
		{
			System.out.println("Channel is added to the favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel added to favorite viewlet");
		}
		else
		{
			System.out.println("Channel is added to the favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add Channel to favorite viewlet");
			driver.findElement(By.xpath("Channel condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=11)
	@TestRail(testCaseId = 110)
	public static void ComapareChannels(ITestContext context) throws InterruptedException
	{
		//Select Two channels
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(1000);
		
		//Store the popup page value into string
		String CompareChannels=driver.findElement(By.cssSelector("span.name")).getText();
		System.out.println(CompareChannels);
		
		//Verification condition
		if(CompareChannels.equalsIgnoreCase("Compare"))
		{
			System.out.println("Channels Compare page is opened");
			
		}
		else
		{
			System.out.println("Channels Compare page is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Faile to open comparision page");
			driver.findElement(By.xpath("Comparision failed")).click();
		}
		Thread.sleep(1000);
				
		//Differences only
		driver.findElement(By.cssSelector("div.differences > label.switch > span.slider.round")).click();
				
		try
		{
		String difference1=driver.findElement(By.xpath("//tr[2]/td[2]")).getText();
		System.out.println("First value" +difference1);
		String difference2=driver.findElement(By.xpath("//tr[2]/td[3]")).getText();
		System.out.println("Second value" +difference2);
		
		if(!(difference1.isEmpty() && difference2.isEmpty()))
		{
		
		if(difference1.equalsIgnoreCase(difference2))
		{
			System.out.println("Popup showing the same values Differences");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Bridge comparision is working fine");
    		driver.findElement(By.xpath("Differences")).click();
		}
		else
		{
			System.out.println("Popup showing the Different values");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Showing the different values");
			
		}
		}
		else
		{
			System.out.println("Empty records");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Showing the different values");
		}
		}
			
		catch (Exception e) {
	     // TODO Auto-generated catch block
	        System.out.println("No differences between Processes");
	        context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while comparing channels, check details: "+ e.getMessage());
	       } 
		 
		//Closing the compare popup page
		driver.findElement(By.cssSelector(".close-button")).click();
	    Thread.sleep(1000);		
	}
	
	@Test(priority=12)
	@TestRail(testCaseId = 111)
	public static void ShowChannelStatusForMultiple(ITestContext context) throws InterruptedException
	{
		int Channel_Status=6;
		if(!WGSName.contains("MQM"))
		{
			Channel_Status=7;
		}
		
		//Store the Channel status into string              
		String ChannelStatus1=driver.findElement(By.xpath("//datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		System.out.println("Channel1 Status from viewlet: " +ChannelStatus1);
		
		//Store second channel status into string
		String ChannelStatus2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		System.out.println("Channel2 Status from viewlet: " +ChannelStatus2);              
		
		//Select Two channels and choose show channel status
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Channels Status")).click();
		Thread.sleep(4000);
		
		//Show multiple channels status page
		String FirstchannelStatus=driver.findElement(By.xpath("//tr[2]/td[4]")).getText();
		System.out.println("Channel1 Status from popup: " +FirstchannelStatus);
		
		//Show second channel status
		String SecondChannelstatus=driver.findElement(By.xpath("//tr[4]/td[4]")).getText();
		System.out.println("Channel2 Status from popup: " +SecondChannelstatus);
		
		//Verification
		if(FirstchannelStatus.equalsIgnoreCase(ChannelStatus1) && SecondChannelstatus.equalsIgnoreCase(ChannelStatus2)) 
		{
			System.out.println("Multiple channels staus is verified");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel status verified successfully");
		}
		else
		{
			System.out.println("Multiple channels staus is not verified");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Faile to verify channel status");
			driver.findElement(By.xpath("Multi channels verification failed")).click();
		}
		
		//Close the popup window
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);	
		
	}
	
	@Test(priority=13)
	@TestRail(testCaseId=112)
	public static void StartMultipleChannelsFromCommands(ITestContext context) throws InterruptedException
	{
		//Start multiple channels
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		
		Actions MousehoverstartCommands=new Actions(driver);
		MousehoverstartCommands.moveToElement(driver.findElement(By.cssSelector(".vertical-nav > .ng-star-inserted:nth-child(3)"))).perform();
		driver.findElement(By.cssSelector(".sub-menu > .ng-star-inserted:nth-child(1)")).click();
		Thread.sleep(1000);
		
		//Close the popup
		driver.findElement(By.xpath("//div[3]/button")).click();
		
		int Channel_Status=6;
		if(!WGSName.contains("MQM"))
		{
			Channel_Status=7;
		}
		
		//Store the channels status into strings
		String Status1=driver.findElement(By.xpath("//datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		String Status2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		
		if(Status1.equalsIgnoreCase("") && Status2.equalsIgnoreCase(""))
		{
			System.out.println("Multiple channels are Started");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel started successfully");
		}
		else
		{
			System.out.println("Multiple channels are not Started");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to start multiple channel");
			driver.findElement(By.xpath("Multi channels Starting failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=14)
	@TestRail(testCaseId=113)
	public void StopMultipleChannelsFromCommands(ITestContext context) throws InterruptedException
	{
		//Stop multiple Channels
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		
		Actions MousehoverStopCommands=new Actions(driver);
		MousehoverStopCommands.moveToElement(driver.findElement(By.cssSelector(".vertical-nav > .ng-star-inserted:nth-child(3)"))).perform();
		driver.findElement(By.cssSelector(".sub-menu > .ng-star-inserted:nth-child(2)")).click();
		Thread.sleep(1000);
				
		//Close the popup
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(1000);
		
		int Channel_Status=6;
		if(!WGSName.contains("MQM"))
		{
			Channel_Status=7;
		}
		//Store the channels status into strings
		String Status1=driver.findElement(By.xpath("//datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		String Status2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Channel_Status +"]/div/span")).getText();
		
		if(Status1.equalsIgnoreCase("") && Status2.equalsIgnoreCase(""))
		{
			System.out.println("Multiple channels are Stopped");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel stopped successfully");
		}
		else
		{
			System.out.println("Multiple channels are not Stopped");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to stop multiple channel");
			driver.findElement(By.xpath("Multi channels Stopping failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"ChannelDescription", "ChannelConnectionName"})
	@TestRail(testCaseId=114)
	@Test(priority=15)
	public void MultipleProperties(String ChannelDescription, String ChannelConnectionName, ITestContext context) throws InterruptedException
	{
		//Select Two channels and choose properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Enter the Description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(ChannelDescription);
		
		//Enter the Connection name
		driver.findElement(By.id("connectionName")).clear();
		driver.findElement(By.id("connectionName")).sendKeys(ChannelConnectionName);
		
		//Close the Popup
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(5000);
		
		//Open the properties of first channel
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(2000);
		
		String FirstChannelDescription=driver.findElement(By.id("description")).getAttribute("value");
		String FirstChannelConnection=driver.findElement(By.id("connectionName")).getAttribute("value");
		
		//Close the Popup
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(5000);
		
		//Open the properties of second channel
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(2000);
		
		String SecondChannelDescription=driver.findElement(By.id("description")).getAttribute("value");
		String SecondChannelConnection=driver.findElement(By.id("connectionName")).getAttribute("value");
		
		//Close the Popup
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(5000);
		
		//Verification
		if(FirstChannelDescription.equals(ChannelDescription) && FirstChannelConnection.equals(ChannelConnectionName) && SecondChannelDescription.equals(ChannelDescription) && SecondChannelConnection.equals(ChannelConnectionName))
        {
        	System.out.println("Multiple channel properties are verified");
        	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel properties verified successfully");
        }
        else
        {
        	System.out.println("Multiple channel properties are not verified");
        	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to verify multiple channel properties");
        	driver.findElement(By.xpath("Multiple properties failed")).click();
        }
        Thread.sleep(2000);
		
	}
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId=115)
	@Test(priority=16)
	public static void AddToFavoriteForMultipleChannels(String FavoriteViewletName,ITestContext context) throws InterruptedException
	{
		int ChannelName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ChannelName_Index=4;
		}
		
		//Storage of channel names
		String channelname1=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ ChannelName_Index +"]/div/span")).getText();
		String channelname2=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ ChannelName_Index +"]/div/span")).getText();
		
		//Select Addto favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);
		
		//Get the data from favorite viewlet
		String Favoritedata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verify the channel is added to Favorite viewlet
		if(Favoritedata.contains(channelname1) && Favoritedata.contains(channelname2))
		{
			System.out.println("Multiple channels are added into the Favorite Viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel added to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple channels are not added into the Favorite Viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add multiple channel to favorite viewlet");
			driver.findElement(By.xpath("favorite viewlet condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=27)
	public static void Logout() throws InterruptedException
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
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		//System.out.println("dir: " + dir);
		if (!result.getMethod().getMethodName().contains("Logout")) {
			if (ITestResult.FAILURE == result.getStatus()) {
				this.capturescreen(driver, result.getMethod().getMethodName(), "FAILURE");
				Reporter.setCurrentTestResult(result);

				Reporter.log("<br/>Failed to execute method: " + result.getMethod().getMethodName() + "<br/>");
				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsFailure/"
						+ result.getMethod().getMethodName() + ".png";

			} else {
				this.capturescreen(driver, result.getMethod().getMethodName(), "SUCCESS");
				Reporter.setCurrentTestResult(result);

				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsSuccess/"
						+ result.getMethod().getMethodName() + ".png";

			}

			String path = "<img src=\" " + screenshotPath + "\" alt=\"\"\"/\" />";
			// To add it in the report
			Reporter.log("<br/>");
			Reporter.log(path);
			
			try {
				//Update attachment to testrail server
				int testCaseID=0;
				//int status=(int) result.getTestContext().getAttribute("Status");
				//String comment=(String) result.getTestContext().getAttribute("Comment");
				  if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
					{
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();
					
					
					
					TestRailAPI api=new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());
					
					}
				}catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}
		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {
			
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (status.equals("FAILURE")) {
				FileUtils.copyFile(scrFile,
						new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				Reporter.log(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png");
			} else if (status.equals("SUCCESS")) {
				FileUtils.copyFile(scrFile,
						new File(Screenshotpath + "./ScreenshotsSuccess/" + screenShotName + ".png"));

			}

			System.out.println("Printing screen shot taken for className " + screenShotName);

		} catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
		}

	}
}
