package ApodGUI;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
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

import com.gargoylesoftware.htmlunit.javascript.host.Set;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class TopicViewlet 
{
    String FinaleCopyAsName="";
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
	static String Manager1Queuename;
	static String Manager2Queuename;
	
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
		Manager1Queuename =Settings.getManager1Queuename();
		Manager2Queuename =Settings.getManager2Queuename();
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname) throws Exception
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
		driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(6000);
	}
	
	@Parameters({"Topicname"})
	@TestRail(testCaseId=132)
	@Test(priority=1)
	public static void AddTopicViewlet(String Topicname, ITestContext context) throws InterruptedException
	{
		try {
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
				
		//Create Topic
		driver.findElement(By.cssSelector(".object-type:nth-child(6)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Topicname);
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(WGSName);
	
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
			
		//Create subscription
		driver.findElement(By.cssSelector(".object-type:nth-child(12)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys("Test Subscription");
		
		//Work group server selection
		Select dd1=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd1.selectByVisibleText(WGSName);
		
	    //Click on Save changes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
				
		if(driver.getPageSource().contains(Topicname))
		{
			System.out.println("Topic Viewlet is created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Topic viewlet is created successfully");
		}
		else
		{
			System.out.println("Topic viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to create topic viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Got an exception while creating topic viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
	}
	
	@Parameters({"schemaName"})
	@TestRail(testCaseId=133)
	@Test(priority=17)
	public static void ShowObjectAttributesForTopic(String schemaName,ITestContext context) throws InterruptedException
	{
	
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.TopicObjectAttributesVerification(driver, schemaName, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attribute for Topic viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Got an exception while show object attributes option is selected for topic viewlet, check details: "+ e.getMessage());
		}
	}
	
	@Parameters({"TopicNameFromOptions", "Description", "TopicUniquestring"})
	@TestRail(testCaseId=134)
	@Test(priority=3)
	public void CreateTopic(String TopicNameFromOptions, String Description, String TopicUniquestring, ITestContext context) throws InterruptedException
	{
		//Search with the manager name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DestinationManager);
		
		//Select Create new Topic option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Topic")).click();
		Thread.sleep(2000);
		
		//Give the name of the topic
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(TopicNameFromOptions);
		
		//Add Data into description field
		driver.findElement(By.id("description")).sendKeys(Description);
		
		//Add Topic string
		driver.findElement(By.id("topicString")).sendKeys(TopicUniquestring);
		
		//Click on submit button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
				
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
		
		for(int j=0; j<=DestinationManager.length(); j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Search with the added Topic name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromOptions);
    	Thread.sleep(1000);
		
		//Store the Topic viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Edit the search field data
    	for(int j=0; j<=TopicNameFromOptions.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
		
    	//Verification condition
		if(Viewletdata.contains(TopicNameFromOptions))
		{
			System.out.println("Topic is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topic created successfully");
		}
		else
		{
			System.out.println("Topic is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create topic");
			driver.findElement(By.xpath("Topic vielwr Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"CopyObjectName", "TopicNameFromOptions", "TopicUniquestringForCopyAs"})
	@TestRail(testCaseId=135)
	@Test(priority=4, dependsOnMethods= {"CreateTopic"})
	public void CopyAsFromCommands(String CopyObjectName, String TopicNameFromOptions, String TopicUniquestringForCopyAs, ITestContext context) throws InterruptedException
	{
		//Search with the added Topic name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromOptions);
    	Thread.sleep(1000);
		
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	
    	//Give the object name
    	driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(CopyObjectName);
    	
    	//Unique string
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(TopicUniquestringForCopyAs);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	    	
    	//Edit the search field data
    	for(int j=0; j<=TopicNameFromOptions.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	        	
    	//Combining the strings 
    	String CopyasTopicName=TopicNameFromOptions+CopyObjectName;
    	System.out.println(CopyasTopicName);
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasTopicName);
    	Thread.sleep(2000);
    	
    	/*//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(2000);
    	}*/
    	Thread.sleep(6000);
    	    	
    	//Store the viewlet data into string  
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println("Viewlet data after copy as: " +Subviewlet);
    	
    	for(int j=0; j<=CopyasTopicName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasTopicName))
    	{
    		System.out.println("Topic is copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Copying topic option is working fine");
		
    	}
    	else
    	{
    		System.out.println("Topic is not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy topic");
		
    		driver.findElement(By.xpath("Topic failed to copy")).click();
    	}
    	Thread.sleep(1000);	   	
	}
	
	@Parameters({"TopicNameFromOptions", "CopyObjectName"})
	@TestRail(testCaseId=136)
	@Test(priority=5, dependsOnMethods= {"CopyAsFromCommands"})
	public void DeleteFromCommands(String TopicNameFromOptions, String CopyObjectName, ITestContext context) throws InterruptedException
	{
		String CopyasTopicName=TopicNameFromOptions+CopyObjectName;
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasTopicName);
    	Thread.sleep(1000);
		    	
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasTopicName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(CopyasTopicName))
    	{
    		System.out.println("Topic is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete topic");
    		driver.findElement(By.xpath("Topic delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Topic is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Deletion of topic option is working fine");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"TopicNameFromOptions", "MessageData", "PropertyName", "PropertyValue", "AddSubscriptionName", "TopicUniquestring"})
	@TestRail(testCaseId=137)
	@Test(priority=6, dependsOnMethods= {"CreateTopic"})
	public void PublishFromCommands(String TopicNameFromOptions, String MessageData, String PropertyName, String PropertyValue, String AddSubscriptionName, String TopicUniquestring, ITestContext context) throws InterruptedException
	{	
		this.Addsubscription(AddSubscriptionName, TopicNameFromOptions, TopicUniquestring);
		
		//Show Empty queues
    	driver.findElement(By.xpath("//i[3]")).click();
    	driver.findElement(By.xpath("//div[2]/input")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//div[3]/button")).click();
    	Thread.sleep(8000);
    	
    	//Search the queue
    	driver.findElement(By.xpath("//input[@type='text']")).clear();
    	driver.findElement(By.xpath("//input[@type='text']")).sendKeys(DestinationQueue);
    	
    	int Queue_Depth=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Queue_Depth=6;
    	}
    	//get the Current depth of the queue
    	String Queuedepth=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
    	int result = Integer.parseInt(Queuedepth);
		System.out.println("Initial depth of the queue: " +result);
		
		//Search with topic name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromOptions);
		Thread.sleep(4000);
    	
		//Select publish From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Publish")).click();
		
    	//Send the New name into field
    	driver.findElement(By.id("messageData")).sendKeys(MessageData);
    	Thread.sleep(2000);
    	
    	//driver.findElement(By.id("propertyName")).sendKeys(PropertyName);
    	//driver.findElement(By.id("propertyValue")).sendKeys(PropertyValue);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    	}
    	catch (Exception e)
    	{
    		
    		System.out.println("Error not present while publishing topic");
       	}
    	
    	Thread.sleep(4000);
    	
    	for(int j=0; j<=TopicNameFromOptions.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Open the browse messages page and close it
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Browse messages")).click();
    	Thread.sleep(4000);
    	
    	//Close the popup page
    	driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
    	Thread.sleep(4000);
    	System.out.println("Pop up is closed");
    	
    	//Refresh the queue viewlet
    	for(int i=0; i<=4; i++)
    	{
    		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
    		Thread.sleep(4000);
    	}
    	
    	//System.out.println("get queue depth");
    	
    	int Queue_Depth0=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Queue_Depth0=6;
    	}
    	//get the Current depth of the queue
    	String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth0 +"]/div/span")).getText();
    	//String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth0 +"]/div/span")).getText();
    	int result1 = Integer.parseInt(Queuedepth1);
		System.out.println("Depth of the after publishing: " +result1);
    	
		//Restore the settings
    	driver.findElement(By.xpath("//i[3]")).click();
    	driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
    	Thread.sleep(4000);
    	driver.findElement(By.xpath("//div[3]/button")).click();
    	Thread.sleep(6000);
    	
    	if(result!=result1)
    	{
    		System.out.println("Publish the messgae into queue is done");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Publishing the mnessages is working fine");
    	}
    	else
    	{
    		System.out.println("Publish the messgae into queue is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to publish messages");
    		driver.findElement(By.xpath("Publish failed")).click();
    	}
	}
	
	@Test(priority=7)
	@TestRail(testCaseId=138)
	public void TopicProperties(ITestContext context) throws InterruptedException
	{
		//Select Properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(6000);
		
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Topic name field is Disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Topic name field is Disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
		}
		else
		{
			System.out.println("The Topic name field is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "The Topic name field is Enabled on properties option");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("Name field is disabled")).click();
			
		}		
	}
	//@Parameters({"Eventsdata"})
	@Test(priority=8)
	@TestRail(testCaseId=139)
	public static void TopicEvents(ITestContext context) throws InterruptedException
	{
		//Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId=140)
	@Test(priority=9)
	public static void AddToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int TopicName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			TopicName_Index=4;
		}
		
		//Store Topic name into string
		String TopicName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ TopicName_Index +"]/div/span")).getText();
		
		//Create the favorite viewlet
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
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(4000);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[5]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		                                            
		//Verification of topic added to favorite viewlet 
		if(Favdata.contains(TopicName))
		{
			System.out.println("Topic name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topic is added to Favorite viewlet");
		}
		else
		{
			System.out.println("Topic name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Topic name to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	private void topiccreation(String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, ITestContext context, String manager) throws InterruptedException
	{
		//Click on + icon present in the viewlet
		driver.findElement(By.xpath("//img[@title='Add Topic']")).click();
		
		//Select WGS
		Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
		WGS.selectByVisibleText(WGSName);
		
		//Select Node 
		driver.findElement(By.xpath("//ng-select/div")).click();
		Thread.sleep(2000);
		try 
		{
			List<WebElement> TopicNode=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(TopicNode.size());	
			for (int i=0; i<TopicNode.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + TopicNode.get(i).getAttribute("id"));
				String s=TopicNode.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=TopicNode.get(i).getAttribute("id");
					Thread.sleep(3000);
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
			List<WebElement> TopicManager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(TopicManager.size());	
			for (int i=0; i<TopicManager.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + TopicManager.get(i).getAttribute("id"));
				String s=TopicManager.get(i).getText();
				if(s.equals(manager))
				{
					String id=TopicManager.get(i).getAttribute("id");
					Thread.sleep(2000);
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
		
		//Give the name of the topic
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(TopicNameFromIcon);
		
		//Add Data into description field
		driver.findElement(By.id("description")).sendKeys(DescriptionFromIcon);
		
		//Add Topic string
		driver.findElement(By.id("topicString")).sendKeys(TopicUniquestringFromICon);
		
		//Click on submit button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(10000);
				
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
		
		//Edit the search field data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromIcon);
    	
		
		//Store the Topic viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		//System.out.println(Viewletdata);
		
		//Edit the search field data
    	for(int j=0; j<=TopicNameFromIcon.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
		
		//Verification condition
		if(Viewletdata.contains(TopicNameFromIcon))
		{
			System.out.println("Topic is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topics are added created using add icon");
		}
		else
		{
			System.out.println("Topic is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create topic using add icon");
			driver.findElement(By.xpath("Topic vielwr Failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"TopicNameFromIcon", "DescriptionFromIcon", "TopicUniquestringFromICon"})
	@TestRail(testCaseId=147)
	@Test(priority=10)
	public void CreateTopicFromPlusIcon(String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, ITestContext context) throws InterruptedException
	{		
		
		
		topiccreation(TopicNameFromIcon, DescriptionFromIcon, TopicUniquestringFromICon, context, Manager1);
		/*//Click on + icon present in the viewlet
		driver.findElement(By.xpath("//img[@title='Add Topic']")).click();
		
		//Select WGS
		Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
		WGS.selectByVisibleText(WGSName);
		
		//Select Node 
		driver.findElement(By.xpath("//ng-select/div")).click();
		Thread.sleep(2000);
		try 
		{
			List<WebElement> TopicNode=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(TopicNode.size());	
			for (int i=0; i<TopicNode.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + TopicNode.get(i).getAttribute("id"));
				String s=TopicNode.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=TopicNode.get(i).getAttribute("id");
					Thread.sleep(3000);
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
			List<WebElement> TopicManager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(TopicManager.size());	
			for (int i=0; i<TopicManager.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + TopicManager.get(i).getAttribute("id"));
				String s=TopicManager.get(i).getText();
				if(s.equals(Manager1))
				{
					String id=TopicManager.get(i).getAttribute("id");
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
		
		//Give the name of the topic
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(TopicNameFromIcon);
		
		//Add Data into description field
		driver.findElement(By.id("description")).sendKeys(DescriptionFromIcon);
		
		//Add Topic string
		driver.findElement(By.id("topicString")).sendKeys(TopicUniquestringFromICon);
		
		//Click on submit button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(10000);
				
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
		
		//Edit the search field data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromIcon);
    	
		
		//Store the Topic viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		//System.out.println(Viewletdata);
		
		//Edit the search field data
    	for(int j=0; j<=TopicNameFromIcon.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
		
		//Verification condition
		if(Viewletdata.contains(TopicNameFromIcon))
		{
			System.out.println("Topic is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topics are added created using add icon");
		}
		else
		{
			System.out.println("Topic is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create topic using add icon");
			driver.findElement(By.xpath("Topic vielwr Failed")).click();
		}
		Thread.sleep(1000);	*/	
	}
	
	@Test(priority=11)
	@TestRail(testCaseId=141)
	public static void CompareTopics(ITestContext context) throws InterruptedException
	{
		int Name_Index=3;
		if(!WGSName.contains("MQM"))
		{
			Name_Index=4;
		}
		
		//Get the First object Name
		String compare1 = driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ Name_Index +"]/div/span")).getText();
		//System.out.println("First obj name is: " +compare1);
		
		//Get the second object name
		String compare2 = driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Name_Index +"]/div/span")).getText();
		//System.out.println("Second obj name is: " +compare2);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();

		// System.out.println("Cpmare to: " + compare1 + "::"+ compare2);
		String comparenameslist = compare1 + "::" + compare2;
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(2000);
		System.out.println("Before names are: " +comparenameslist);


		// Reading comparing
		String aftercompare1 = driver.findElement(By.xpath("//th[2]")).getText();
		String aftercompare2 = driver.findElement(By.xpath("//th[3]")).getText();
		String verifycomparenamelist = aftercompare1 + "::" + aftercompare2;
		System.out.println("After names are: " +verifycomparenamelist);

		if (verifycomparenamelist.compareTo(comparenameslist) == 0) {
			System.out.println("Compare page is opened with selected object names");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Compare option is working fine");
		} else {
			System.out.println("Compare page is not opened with selected objetcs");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to compare option");
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Comparision failed")).click();
		}
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId = 775)
	@Test(priority=12)
	public void CheckDifferencesForTopics(ITestContext context) throws InterruptedException
	{
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Compare")).click();
		Thread.sleep(2000);
		
		// Check differences only option while compare
		driver.findElement(By.cssSelector("div.differences > label.switch > span.slider.round")).click();
		Thread.sleep(4000);
		try {

			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr"));
			System.out.println("AttributesData count: " + AttributesData.size());

			boolean verifydiff = false;
			String difference1=driver.findElement(By.xpath("//tr[4]/td[2]")).getText();
			System.out.println("First value" +difference1);
			String difference2=driver.findElement(By.xpath("//tr[4]/td[3]")).getText();
			System.out.println("Second value" +difference2);

			if(!(difference1.isEmpty() && difference2.isEmpty()))
			{
			for (int i = 0; i < AttributesData.size(); i++) 
			{
				String cls = AttributesData.get(i).getAttribute("style");
				System.out.println("classname: "+ cls);
				if (!cls.contains("display: none")) 
					{
					System.out.println("index: " + i);
					String secondvalue;
					String firstvalue;
					if (i == 0)
					{
						firstvalue = driver.findElement(By.xpath("//td[2]")).getText();
						System.out.println("First value" + firstvalue);
						secondvalue = driver.findElement(By.xpath("//td[3]")).getText();
						System.out.println("Second value" + secondvalue);
						
						if (!firstvalue.equalsIgnoreCase(secondvalue)) 
						{
							verifydiff = true;
						}
					} 
					else
					{
						int j = i + 1;
						System.out.println("index changed: " + j);
						firstvalue = driver.findElement(By.xpath("//tr[" + j + "]/td[2]")).getText();
						System.out.println("First value" + firstvalue);
						secondvalue = driver.findElement(By.xpath("//tr[" + j + "]/td[3]")).getText();
						System.out.println("Second value" + secondvalue);
						if (!firstvalue.equalsIgnoreCase(secondvalue)) 
						{
							verifydiff = true;
						}
					}
				}

				}
			
			}
			else
			{
			verifydiff=true;
			}

			//System.out.println("");
			if (!verifydiff) {
				System.out.println("Popup showing the same values Differences");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Differences is not working");
				driver.findElement(By.xpath("Differences")).click();
			} else {
				System.out.println("Popup showing the Different values");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Showing the different values");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Popup showing the same values Differences");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while differentiate object values, check details: " + e.getMessage());
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Differences")).click();
		}
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"TopicNameFromIcon", "DescriptionFromIcon", "TopicUniquestringFromICon", "CopyObjectNameForMultiple", "TopicUniquestringForMultipleCopy"})
	@TestRail(testCaseId=142)
	@Test(priority=13, dependsOnMethods= {"CreateTopicFromPlusIcon"})
	public void CopyAsFromCommandsForMultipleTopics(String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, String CopyObjectNameForMultiple, String TopicUniquestringForMultipleCopy, ITestContext context) throws InterruptedException
	{
		topiccreation(TopicNameFromIcon, DescriptionFromIcon, TopicUniquestringFromICon, context, Manager2);
		
		//Search with the topic name from icon
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromIcon);
		
		//Select the multiple Topics and choose Copy as for multiple topics
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		Thread.sleep(2000);
    	driver.findElement(By.linkText("Copy As...")).click();
		Thread.sleep(3000);
		
		//Get Existing string name
		String Objectname=driver.findElement(By.xpath("//div[2]/div/input")).getText();
		System.out.println("Existing object name is :" +Objectname);
		
		//Give the object name
    	driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(CopyObjectNameForMultiple);
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(TopicUniquestringForMultipleCopy);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Combine the string
    	 FinaleCopyAsName=Objectname+CopyObjectNameForMultiple;
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinaleCopyAsName);
    	Thread.sleep(2000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinaleCopyAsName.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(Subviewlet.contains(FinaleCopyAsName))
    	{
    		System.out.println("Multiple Topics are copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "CopyAs command is working fine for multiple topics");
    	}
    	else
    	{
    		System.out.println("Multiple Topics are not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to Copy multiple topics using CopyAs command");
    		driver.findElement(By.xpath("Multiple Topics failed to copy")).click();
    	}
    	Thread.sleep(1000);		
	}
	
	@Parameters({"CopyObjectNameForMultiple"})
	@TestRail(testCaseId=143)
	@Test(priority=14, dependsOnMethods= {"CopyAsFromCommandsForMultipleTopics"})
	public void DeleteFromCommandsForMultipleTopics(String CopyObjectNameForMultiple, ITestContext context) throws InterruptedException
	{
		//Send the New name into field
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinaleCopyAsName);
    	Thread.sleep(2000);
    	
    	//Select the multiple topics and choose Delete option for multiple topics
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete")).click();
		Thread.sleep(1000);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	    	
    	//Refresh the view let
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinaleCopyAsName.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(FinaleCopyAsName))
    	{
    		System.out.println("Topics are not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete multiple topics");
    		driver.findElement(By.xpath("Topics delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Topics are deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Deleting multiple topics is working fine");
    	}
    	Thread.sleep(1000);
    	
    	
	}
	
	@Parameters({"TopicNameFromIcon", "DescriptionFromIcon", "TopicUniquestringFromICon", "AddSubscriptionNameforMultiple", "MessageDataForMultiple", "PropertyNameForMultiple", "PropertyValueForMultiple"})
	@TestRail(testCaseId=144)
	@Test(priority=15)
	public void PublishFromCommandsForMultipleTopics(String TopicNameFromIcon, String DescriptionFromIcon, String TopicUniquestringFromICon, String AddSubscriptionNameforMultiple, String MessageDataForMultiple, String PropertyNameForMultiple, String PropertyValueForMultiple, ITestContext context) throws InterruptedException
	{
		AddsubscriptionForMultiple(AddSubscriptionNameforMultiple, TopicNameFromIcon, Manager1, Manager1Queuename);
		AddsubscriptionForMultiple(AddSubscriptionNameforMultiple, TopicNameFromIcon, Manager2, Manager2Queuename);
		
		//Show Empty queues
    	driver.findElement(By.xpath("//i[3]")).click();
    	driver.findElement(By.xpath("//div[2]/input")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//div[3]/button")).click();
    	Thread.sleep(1000);
    	
    	//Search the queue
    	searchqueue1();
    	
    	int Queue_Depth1=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Queue_Depth1=6;
    	}
    	
    	//get the Current depth of the queue
    	String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth1 +"]/div/span")).getText();
    	int BeforeQueue1result1 = Integer.parseInt(Queuedepth1);
		System.out.println(BeforeQueue1result1);
		
		//Clear search data
		Clearqueue1();
		
		//Search queue2
		searchqueue2();
		int Queue_Depth2=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Queue_Depth2=6;
    	}
    	
    	//get the Current depth of the queue
    	String Queuedepth2=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth2 +"]/div/span")).getText();
    	int BeforeQueue2result2 = Integer.parseInt(Queuedepth2);
		System.out.println(BeforeQueue2result2);
		
		//Search with the Topics
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromIcon);
		
		//Select the multiple topics and choose publish option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Publish")).click();
		Thread.sleep(1000);
				
		//Send the New name into field
		 driver.findElement(By.id("messageData")).sendKeys(MessageDataForMultiple);
		    	
		 driver.findElement(By.id("propertyName")).sendKeys(PropertyNameForMultiple);
		 driver.findElement(By.id("propertyValue")).sendKeys(PropertyValueForMultiple);
		 driver.findElement(By.cssSelector(".btn-primary")).click();
		 Thread.sleep(6000);
    	
		 try
	    {
	    	driver.findElement(By.id("yes")).click();
	    	driver.findElement(By.cssSelector(".btn-danger")).click();
	    }
	    catch (Exception e)
	    {
	    	System.out.println("Error not present while publishing topic");
	    }
    	Thread.sleep(4000);
    	
    	//Remove the serach
    	for(int k=0; k<=TopicNameFromIcon.length(); k++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//search with queue1
    	searchqueue1();
    	
    	//Open the browse messages page and close it for queue1
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Browse messages")).click();
    	Thread.sleep(4000);
    	
    	//Close the popup page
    	driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
    	Thread.sleep(4000);
    	
    	
    	//Refresh the queue viewlet
    	for(int i=0; i<=4; i++)
    	{
    		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
    		Thread.sleep(4000);
    	}
    	    	
    	int Queue_Depth3=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Queue_Depth3=6;
    	}
    	//get the Current depth of the queue
    	String Queuedepthforqueue1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth3 +"]/div/span")).getText();
    	//String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth0 +"]/div/span")).getText();
    	int Afterresult1 = Integer.parseInt(Queuedepthforqueue1);
		//System.out.println("Depth of the after publishing: " +Afterresult1);
		
		//Clear search1
		Clearqueue1();
		
		//search with queue1
    	searchqueue2();
    	
    	//Open the browse messages page and close it for queue1
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	driver.findElement(By.linkText("Browse messages")).click();
    	Thread.sleep(4000);
    	
    	//Close the popup page
    	driver.findElement(By.xpath("//app-console-tabs/div/div/ul/li/div/div[2]/i")).click();
    	Thread.sleep(4000);
    	    	
    	//Refresh the queue viewlet
    	for(int i=0; i<=4; i++)
    	{
    		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
    		Thread.sleep(4000);
    	}
    	
    	
    	int Queue_Depth4=5;
    	if(!WGSName.contains("MQM"))
    	{
    		Queue_Depth4=6;
    	}
    	//get the Current depth of the queue
    	String Queuedepthforqueue2=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth4 +"]/div/span")).getText();
    	//String Queuedepth1=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth0 +"]/div/span")).getText();
    	int Afterresult2 = Integer.parseInt(Queuedepthforqueue2);
		//System.out.println("Depth of the after publishing: " +Afterresult2);
		
		//Clear search1
		Clearqueue2();
    	
		//Show Empty queues
    	driver.findElement(By.xpath("//i[3]")).click();
    	driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
    	Thread.sleep(1000);
    	driver.findElement(By.xpath("//div[3]/button")).click();
    	Thread.sleep(1000);
    	
    	System.out.println("Before queue1 is: " +BeforeQueue1result1);
    	System.out.println("Before queue2 is: " +BeforeQueue2result2);
    	System.out.println("After queue1 is: " +Afterresult1);
    	System.out.println("After queue2 is: " +Afterresult2);
    	
    	if(BeforeQueue1result1!=Afterresult1 && BeforeQueue2result2 != Afterresult2)
    	{
    		System.out.println("Publish the messgae into queue is done for multipublish");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Publishing the topic messages to queue is working fine");
    	}
    	else
    	{
    		System.out.println("Publish the messgae into queue is failed for multuipublish");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to publish multiple messages int queue");
    		driver.findElement(By.xpath("Multi Publish failed")).click();
    	}
	}
	
	private void searchqueue1() throws InterruptedException
	{
		//Search the queue
    	driver.findElement(By.xpath("//input[@type='text']")).clear();
    	driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Manager1Queuename);
    	Thread.sleep(2000);
	}
	
	private void searchqueue2() throws InterruptedException
	{
		//Search the queue
    	driver.findElement(By.xpath("//input[@type='text']")).clear();
    	driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Manager2Queuename);
    	Thread.sleep(2000);
	}
	
	private void Clearqueue1()
	{
		for(int i=0; i<=Manager1Queuename.length(); i++)
		{
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
		}
	}
	
	private void Clearqueue2()
	{
		for(int i=0; i<=Manager2Queuename.length(); i++)
		{
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
		}
	}
	
	
	@Parameters({"MultipleDescription", "Communicationinfo"})
	@TestRail(testCaseId=145)
	@Test(priority=16)
	public void MultipleProperties(String MultipleDescription, String Communicationinfo, ITestContext context) throws InterruptedException
	{
		//Select the multiple topics and choose properties option for multiple topics
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(6000);         
		
		//Give the description for multiple topics
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(MultipleDescription);
		
		/*//Give the application id for multiple topics
		driver.findElement(By.id("communicationInfo")).clear();
		driver.findElement(By.id("communicationInfo")).sendKeys(Communicationinfo);*/
		
		//click on OK
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Open the properties for First topic
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(5000);
		
		//Get the description and communication info for First topic
		String FirstDescription=driver.findElement(By.id("description")).getAttribute("value");
		//String FirstCommunicationinfo=driver.findElement(By.id("communicationInfo")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(5000);
		
		//Open the properties for second topic
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(5000);
				
		//Get the description and communication info for second topic
		String SecondDescription=driver.findElement(By.id("description")).getAttribute("value");
		//String SecondCommunicationinfo=driver.findElement(By.id("communicationInfo")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(5000);
		
		//Verification 
		//if(FirstDescription.equals(MultipleDescription) && FirstCommunicationinfo.equals(Communicationinfo) && SecondDescription.equals(MultipleDescription) && SecondCommunicationinfo.equals(Communicationinfo))
		if(FirstDescription.equals(MultipleDescription) && SecondDescription.equals(MultipleDescription))
		{
			System.out.println("Properites are Updated for multiple topics");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Properties are updated successfully for multiple topics");
		}
		else
		{
			System.out.println("Properites are not Updated for multiple topics");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to update properties for multiple topics");
			driver.findElement(By.xpath("Properties updation failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId=146)
	@Test(priority=17, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleTopics(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int TopicName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			TopicName_Index=4;
		}
		
		//Store the Topic Names into string
		String TopicName2=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ TopicName_Index +"]/div/span")).getText();
		String TopicName3=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ TopicName_Index +"]/div/span")).getText();
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[5]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of topics added to favorite viewlet
		if(Favdata.contains(TopicName2) && Favdata.contains(TopicName3))
		{
			System.out.println("Topic names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Topics are added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("Topic names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add topics to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
				
	}
	

	@Test(priority=20)
	public static void Logout() throws InterruptedException
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
	
	private static boolean checkprogress() throws InterruptedException {
		try {
			WebElement progressBar = driver.findElement(By.cssSelector(".progress-bar"));
			while (progressBar.isDisplayed()) {
				System.out.println("Progress bar loading....");
				Thread.sleep(1000);
			}
		} catch (StaleElementReferenceException e) {
			// TODO: handle exception
			return false;
		}
		return false;
	}
	
	
	
	//Create Subscription Viewlet and Add Subscription
	
	@Parameters({"AddSubscriptionName", "TopicNameFromOptions", "TopicUniquestring"})
	public void Addsubscription(String AddSubscriptionName, String TopicNameFromOptions, String TopicUniquestring) throws InterruptedException
	{
		//Search with subscription name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TopicNameFromOptions);
		
		int Manager_Index=4;
		if(!WGSName.contains("MQM"))
		{
			Manager_Index=5;
		}
		//Get the manager name 
		String ManagerName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ Manager_Index +"]/div/span")).getText();

		for(int j=0; j<=TopicNameFromOptions.length(); j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
				
		//Search with the manager name
		driver.findElement(By.xpath("(//input[@type='text'])[4]")).sendKeys(ManagerName);
		Thread.sleep(1000);
		
		//click on check box and choose create subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[4]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Subscription")).click();
		Thread.sleep(1000);
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionName);
		
		//Select the Topic name from the list
		/*driver.findElement(By.cssSelector(".ng-input > input")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();*/
		
		try 
		{
			driver.findElement(By.id("topicName")).click();
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(TopicNameFromOptions))
				{
					String id=Topic.get(i).getAttribute("id");
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
		
		//Give the Topic String
		//driver.findElement(By.id("topicString")).sendKeys(TopicUniquestring);
		Thread.sleep(3000);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		Thread.sleep(4000);
		
		//Select WGS name
		Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
		DestinationWGS.selectByVisibleText(DWGS);
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			driver.findElement(By.id("destinationNodeName")).click();
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
			//	System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
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
		
		//Select Manager value
		try 
		{
			driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
			//	System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(s.equals(DestinationManager))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(3000);
		
		//Select Queue name
		try 
		{
			driver.findElement(By.id("destinationQName")).click();
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				//System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				if(s.equals(DestinationQueue))
				{
					String id=QueueName.get(i).getAttribute("id");
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
		
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error popup is not displayed");
    	}
		
	}
	
	
	@Parameters({"AddSubscriptionNameforMultiple"})
	public void AddsubscriptionForMultiple(String AddSubscriptionNameforMultiple, String TopicNameFromIcon, String manager, String Queue) throws InterruptedException
	{	
		//Search with manager name
		driver.findElement(By.xpath("(//input[@type='text'])[4]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[4]")).sendKeys(manager);
		
		//click on check box and choose create subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[4]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Subscription")).click();
		Thread.sleep(1000);
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionNameforMultiple);
		
		/*//Select the Topic name from the list
		driver.findElement(By.cssSelector(".ng-input > input")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();*/
		
		try 
		{
			driver.findElement(By.id("topicName")).click();
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(TopicNameFromIcon))
				{
					String id=Topic.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(3000);
		//Give the Topic String
		//driver.findElement(By.id("topicString")).sendKeys(TopicStringData);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		Thread.sleep(2000);
		
		//Select WGS name
		Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
		DestinationWGS.selectByVisibleText(DWGS);
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			driver.findElement(By.id("destinationNodeName")).click();
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
				System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
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
		
		//Select Manager value
		try 
		{
			driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(s.equals(manager))
				{
					String id=Manager.get(i).getAttribute("id");
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
		
		//Select Queue name
		try 
		{
			driver.findElement(By.id("destinationQName")).click();
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				if(s.equals(Queue))
				{
					String id=QueueName.get(i).getAttribute("id");
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
		
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error popup is not displayed");
    	}
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
