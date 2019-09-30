package ApodGUI;


import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class ChannelAuthRecordsViewlet 
{
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String DWGS;
	static String Dnode;
	static String DestinationManager;
	static String DestinationQueue;
	static String DestinationTopicName;
	static String Manager1;
	static String Manager2;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		WGSName =Settings.getWGSNAME();
		UploadFilepath =Settings.getUploadFilepath();
		DWGS =Settings.getDWGS();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
		DestinationQueue =Settings.getDestinationQueue();
		DestinationTopicName =Settings.getDestinationTopicName();
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "wgs"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, int wgs) throws Exception
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
		Thread.sleep(2000);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
	}
	
	 @Parameters({"Viewletname"})
	@Test(priority=1)
	public static void AddChannelAuthrecordsViewlet(String Viewletname) throws InterruptedException
	{
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
					
		//Create Bridge viewlet
		driver.findElement(By.cssSelector(".object-type:nth-child(13)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Viewletname);
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(WGSName);
	
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		if(driver.getPageSource().contains(Viewletname))
		{
			System.out.println("Channel Auth Viewlet is created");
		}
		else
		{
			System.out.println("Channel auth viewlet is not created");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(2000);
			
    }
	 
	 @TestRail(testCaseId=532)
	 @Parameters({"ChannelAuthNameFromOptions", "UserList"})
	 @Test(priority=2)
		public void CreateChannelAuthRecordFromOptions(String ChannelAuthNameFromOptions, String UserList, ITestContext context) throws InterruptedException
		{	
			try
			{
			//Select create process option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Create ChAuthRec")).click();
			Thread.sleep(4000);
			
			//Give the process name
			driver.findElement(By.id("name")).clear();
			driver.findElement(By.id("name")).sendKeys(ChannelAuthNameFromOptions);
			
			//Go to Block tab
			driver.findElement(By.linkText("Block")).click();
			Thread.sleep(2000);
			
			//Give the User list name
			driver.findElement(By.id("userIdList")).sendKeys(UserList);		
			
			//Click on Submit the ChannelAuth
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(10000);
			
			try
			{
				driver.findElement(By.id("yes")).click();
				driver.findElement(By.cssSelector(".btn-primary")).click();
				try
				{
					driver.findElement(By.id("yes")).click();
					driver.findElement(By.cssSelector(".btn-danger")).click();
				}
				catch (Exception e)
				{
					System.out.println("OK button is not working");
				}
			}
			catch (Exception e)
			{
				System.out.println("Error popup is not present");
			}
			
			//Store the process viewlet data into string
			String ChannelAuthdata=driver.findElement(By.xpath("//datatable-body")).getText();
			//System.out.println(ChannelAuthdata);
			 
			//Verification
			if(ChannelAuthdata.contains(ChannelAuthNameFromOptions))
			{
				System.out.println("Channel auth record is created from the options");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is created");
			}
			else
			{
				System.out.println("Channel auth record is not created from the options");
				//driver.findElement(By.cssSelector(".btn-danger")).click();
				driver.findElement(By.xpath("Channel auth record creation failed")).click();
			}
			}
			
			catch (Exception e)
			{
				//Click on Close button
				System.out.println("Unable to create the Channel auth record from options");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record is added");
				driver.findElement(By.xpath("Channel auth record creation failed")).click();
				
			}
		}
	 
	 
	    @Parameters({"ChannelAuthNameFromOptions"})
	    @TestRail(testCaseId=533)
		@Test(priority=3)
		public void DeleteChannelAuthRecordFromCommands(String ChannelAuthNameFromOptions, ITestContext context) throws InterruptedException
		{
	    	//Search with added channelAuth record
	    	driver.findElement(By.xpath("//input[@type='text']")).clear();
	    	driver.findElement(By.xpath("//input[@type='text']")).sendKeys(ChannelAuthNameFromOptions);
	    	
			//Select Delete From commands
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
	    	Actions Mousehoverdelete=new Actions(driver);
	    	Mousehoverdelete.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
	    	driver.findElement(By.linkText("Delete")).click();
			
	    	//Click on Yes
	    	driver.findElement(By.cssSelector(".btn-primary")).click();
	    	Thread.sleep(8000);
	    	
	    	//Search with the new name
			for(int j=0; j<=ChannelAuthNameFromOptions.length(); j++)
	    	{
				driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
	    	}
	    	Thread.sleep(4000);
	    	
	    	//Store the viewlet data into string
	    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
	    	//System.out.println(Subviewlet);
	    	
	    	//Verification of Subscription delete
	    	if(Subviewlet.contains(ChannelAuthNameFromOptions))
	    	{
	    		System.out.println("Channel Auth record is not deleted");
	    		context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record is not deleted");
	    		driver.findElement(By.xpath("Channel auth record delete failed")).click();
	    	}
	    	else
	    	{
	    		System.out.println("Channel Auth record is deleted");
	    		context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is deleted");
	    	}
	    	Thread.sleep(1000);
		}
	    
	    @TestRail(testCaseId=534)
	    @Test(priority=4)
		public void ChannelAuthProperties(ITestContext context) throws InterruptedException
		{
			//Select Properties option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(8000);
			
			//Store the Name field status into boolean
			boolean NameField=driver.findElement(By.id("name")).isEnabled();
			System.out.println(NameField);
			
			//Verification Condition
			if(NameField == false)
			{
				System.out.println("The Channel Auth name is Disabled");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record properties working fine");
				driver.findElement(By.cssSelector(".btn-primary")).click();
				
			}
			else
			{
				System.out.println("The Channel Auth name is Enable");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record properties not working");
				driver.findElement(By.cssSelector(".btn-primary")).click();
				driver.findElement(By.xpath("Channel auth name field is Enable")).click();
			}
			
			Thread.sleep(4000);
		}
		
	    @TestRail(testCaseId=535)
		@Test(priority=5)
		public static void ChannelAuthEvents(ITestContext context) throws InterruptedException
		{
	    	//Events option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Events...")).click();
			Thread.sleep(1000);
			
			//Events Popup page
			String Eventdetails=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
			//System.out.println(Eventdetails);
							
			//Verification condition
			if(Eventdetails.equalsIgnoreCase("Event #"))
			{
				System.out.println("Events page is opened");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "The event option is working fine");
			}
			else
			{
				
				System.out.println("Events page is not opened");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "The event option is not working properly");
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
		
	    @TestRail(testCaseId=536)
		@Parameters({"FavoriteViewletName"})
		@Test(priority=6)
		public static void AddToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
		{
			int ChannelAuth_Index=3;
			if(!WGSName.contains("MQM"))
			{
				ChannelAuth_Index=4;
			}
			
			//Store process name into String 
			String ChannelAuthName=driver.findElement(By.xpath("//datatable-body-cell["+ ChannelAuth_Index +"]/div/span")).getText();
			
			//Create favorite viewlet
			driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
			driver.findElement(By.id("fav")).click();
			driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
			
			//Viewlet Name
			driver.findElement(By.name("viewlet-name")).click();
			driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
			
			Select wgsdropdown=new Select(driver.findElement(By.name("wgs")));
			wgsdropdown.selectByVisibleText(WGSName);
			
			//Submit
			driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
			Thread.sleep(2000);
			
			//Select Add to Favorites option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Add to favorites...")).click();
			Thread.sleep(1000);
			
			//Select the favorite viewlet name
			Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
			fav.selectByVisibleText(FavoriteViewletName);
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
			Thread.sleep(1000);
			
			//Storing the Favorite Viewlet data
			String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			
			//Verification of process added to the favorite viewlet
			if(Favdata.contains(ChannelAuthName))
			{
				System.out.println("Channel Auth record name is added to the Favorite viewlet");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is added to favorite viewlet");
			}
			else
			{
				System.out.println("Channel Auth record name is not added to the Favorite viewlet");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Channel auth record is not added to favorite viewlet");
				driver.findElement(By.xpath("Favorite condition failed")).click();
			}
			Thread.sleep(2000);
		}
		
	    @TestRail(testCaseId=537)
		@Parameters({"ChannelAuthNameFromIcon", "UserList"})
		@Test(priority=7)
		public void CreateChannelAuthRecordFromPlusIcon(String ChannelAuthNameFromIcon, String UserList, ITestContext context) throws InterruptedException
		{
			//Click on + icon present in the viewlet
			driver.findElement(By.xpath("//img[@title='Add ChAuthRec']")).click();
			
			//Select WGS
			Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
			WGS.selectByVisibleText(WGSName);
			
			//Select Node 
			driver.findElement(By.xpath("//div[2]/input")).click();
			try 
			{
				List<WebElement> ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
				System.out.println(ChannelauthNode.size());	
				for (int i=0; i<ChannelauthNode.size();i++)
				{
					//System.out.println("Radio button text:" + Topic.get(i).getText());
					System.out.println("Radio button id:" + ChannelauthNode.get(i).getAttribute("id"));
					String s=ChannelauthNode.get(i).getText();
					if(s.equals(Dnode))
					{
						String id=ChannelauthNode.get(i).getAttribute("id");
						driver.findElement(By.id(id)).click();
						break;
					}
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			Thread.sleep(2000);
			
			//Select Manager
			driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
			//driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
			//Select Manager
	       // driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
	        try 
			{
				List<WebElement> ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
				System.out.println(ChannelAuthManager.size());	
				for (int i=0; i<ChannelAuthManager.size();i++)
				{
					//System.out.println("Radio button text:" + Topic.get(i).getText());
					System.out.println("Radio button id:" + ChannelAuthManager.get(i).getAttribute("id"));
					String s=ChannelAuthManager.get(i).getText();
					if(s.equals(Manager1))
					{
						String id=ChannelAuthManager.get(i).getAttribute("id");
						driver.findElement(By.id(id)).click();
						break;
					}
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
	        Thread.sleep(1000);
			
			//Click on Select path button
			driver.findElement(By.xpath("//div[3]/div/div/div/button")).click();
			Thread.sleep(1000);
			
			//Give the name of the Channel auth name
			driver.findElement(By.id("name")).clear();
			driver.findElement(By.id("name")).sendKeys(ChannelAuthNameFromIcon);
			
			//Go to Block tab
			driver.findElement(By.linkText("Block")).click();
			Thread.sleep(2000);
			
			//Give the User list name
			driver.findElement(By.id("userIdList")).sendKeys(UserList);		
			
			//Click on Submit the ChannelAuth
			driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
			Thread.sleep(6000);
			
			try
			{
				driver.findElement(By.id("yes")).click();
				driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
				try
				{
					driver.findElement(By.id("yes")).click();
					driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
				}
				catch (Exception e)
				{
					System.out.println("OK button is not working");
				}
			}
			catch (Exception e)
			{
				System.out.println("Error popup is not present");
			}
			Thread.sleep(1000);
			
			//Edit the search field data
			driver.findElement(By.xpath("//input[@type='text']")).clear();
	    	driver.findElement(By.xpath("//input[@type='text']")).sendKeys(ChannelAuthNameFromIcon);
	    	
			
			//Store the Topic viewlet data into string
			String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
			//System.out.println(Viewletdata);
			
			//Edit the search field data
	    	for(int j=0; j<=ChannelAuthNameFromIcon.length(); j++)
	    	{
	    	
	    	driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
	    	}
	    	Thread.sleep(4000);
			
			//Verification condition
			if(Viewletdata.contains(ChannelAuthNameFromIcon))
			{
				System.out.println("Channel auth record is created successfully from ICon");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth record is added created using add icon");
			}
			else
			{
				System.out.println("Channel auth record  is not created");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed to create Channel auth record using add icon");
				driver.findElement(By.xpath("Channel auth record Failed From Icon")).click();
			}
			Thread.sleep(1000);		
		}
		
	    @TestRail(testCaseId=538)
		@Parameters({"RenameProcessForMultiple"})
		@Test(priority=8)
		public void DeleteMultipleChannelAuthRecordsFromCommands(String RenameProcessForMultiple, ITestContext context) throws InterruptedException
		{
			/*//Send the New name into field
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
	    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcessForMultiple);
	    	Thread.sleep(2000);*/
	    	
	    	//Select the multiple processes and choose Add to favorite viewlet option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions Mousehoverdelete=new Actions(driver);
	    	Mousehoverdelete.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
	    	driver.findElement(By.linkText("Delete")).click();
			Thread.sleep(1000);
			
	    	//Click on Yes
	    	driver.findElement(By.cssSelector(".btn-primary")).click();
	    	Thread.sleep(8000);
	    	
	    	//Refresh the viewlet
	    	driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
	    	Thread.sleep(4000);
	    	
	    	//Store the viewlet data into string
	    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
	    	//System.out.println(Subviewlet);
	    	
	    	//Verification of Subscription delete
	    	if(Subviewlet.contains(RenameProcessForMultiple))
	    	{
	    		System.out.println("Multiple channel auth records are not deleted");
	    		context.setAttribute("Status",5);
				context.setAttribute("Comment", "multiple Channel auth records are not deleted");
	    		driver.findElement(By.xpath("Channel auth records delete failed")).click();
	    	}
	    	else
	    	{
	    		System.out.println("Multiple channel auth records are deleted");
	    		context.setAttribute("Status",1);
				context.setAttribute("Comment", "Channel auth records are deleted");
	    	}
	    	Thread.sleep(1000);
	    	
	    	/*//Clear the search data
	    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();*/
	    	
		}
		
	    @TestRail(testCaseId=539)
		@Parameters({"MultipleDescription"})
		@Test(priority=9)
		public void MultipleChannelAuthRecordsProperties(String MultipleDescription, ITestContext context) throws InterruptedException
		{
			//Select the multiple processes and choose Add to favorite viewlet option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(4000);
			
			//Give the description for multiple channel auth records
			driver.findElement(By.id("chAuthDescription")).clear();
			driver.findElement(By.id("chAuthDescription")).sendKeys(MultipleDescription);
						
			//click on OK
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Open the properties for First process
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(4000);
			
			//Get the description and application id for First process
			String FirstDescription=driver.findElement(By.id("chAuthDescription")).getAttribute("value");
						
			//close the properties page
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Open the properties for First process
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Properties...")).click();
			Thread.sleep(4000);
					
			//Get the description and application id for First process
			String SecondDescription=driver.findElement(By.id("chAuthDescription")).getAttribute("value");
						
			//close the properties page
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Verification 
			if(FirstDescription.equals(MultipleDescription) && SecondDescription.equals(MultipleDescription))
			{
				System.out.println("Properites are Updated for multiple Channel auth records");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "multiple Channel auth records are updated");
			}
			else
			{
				System.out.println("Properites are not Updated for multiple channel auth records");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "multiple Channel auth records are not updated");
				driver.findElement(By.xpath("Properties updation failed")).click();
			}
			Thread.sleep(1000);
		}
		
	    @TestRail(testCaseId=540)
		@Parameters({"FavoriteViewletName"})
		@Test(priority=10)
		public static void AddToFavoriteForMultipleChannelAuthRecords(String FavoriteViewletName, ITestContext context) throws InterruptedException
		{
			int ChannelAuthName_Index=3;
			if(!WGSName.contains("MQM"))
			{
				ChannelAuthName_Index=4;
			}
			
			//Store the process Names into strings
			String ChannelAuthRecordName2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ ChannelAuthName_Index +"]/div/span")).getText();
			System.out.println(ChannelAuthRecordName2);                             
			String ChannelAuthRecordName3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ ChannelAuthName_Index +"]/div/span")).getText();
			System.out.println(ChannelAuthRecordName3);                
			
			//Select the multiple processes and choose Add to favorite viewlet option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Add to favorites...")).click();
			Thread.sleep(2000);
			
			//Select the favorite viewlet name
			Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
			fav.selectByVisibleText(FavoriteViewletName);
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
			Thread.sleep(6000);
			
			//Storing the Favorite Viewlet data
			String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			
			//Verify the multiple processes added to favorite viewlet
			if(Favdata.contains(ChannelAuthRecordName2) && Favdata.contains(ChannelAuthRecordName3))
			{
				System.out.println("Multiple Channel auth names are added to the Favorite viewlet");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "Multiple Channel auth records are added to favorite viewlet");
			}
			else
			{
				System.out.println("Multiple Channel auth names are not added to the Favorite viewlet");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Multiple Channel auth records are not added to favorite viewlet");
				driver.findElement(By.xpath("Favorite condition failed")).click();
			}
			Thread.sleep(1000);	
			
		}
	 
	 @Test(priority=16)
	 public void Logout() throws InterruptedException 
		{
			//Logout
			try
			{
				driver.findElement(By.cssSelector(".active .g-tab-btn-close-block")).click();
				//driver.findElement(By.cssSelector(".fa-times")).click();
				driver.findElement(By.cssSelector(".btn-primary")).click();
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				System.out.println("Dashboards are not present");
			}
			Thread.sleep(1000);		
			
			//Logout option
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
