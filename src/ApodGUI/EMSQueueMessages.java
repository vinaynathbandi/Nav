package ApodGUI;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
public class EMSQueueMessages 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String UploadFilepath;
	static String UploadLargeFile;
	static String EMS_WGSNAME;
	
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		UploadFilepath =Settings.getUploadFilepath();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		UploadLargeFile =Settings.getUploadLargeFile();
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "MessageData", "QueueName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String MessageData, String QueueName) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		String filepath=System.getProperty("user.dir") + "\\" + DownloadPath;
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver, sDriverpath);
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("download.default_directory", filepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		driver=new ChromeDriver(options);
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
		
		//Create local queue viewlet
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
			
		//Create Queue
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(QueueName);
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(EMS_WGSNAME);
		
		//Click on EMS checkbox
		driver.findElement(By.id("ems")).click();
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
		
		//Check Show Empty Queues check box
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
		
		/*//put the messages into empty queues for testing
		for(int m=1; m<=3; m++)
		{
			//Select the put new message option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ m +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions PutMessagesMousehour=new Actions(driver);
			PutMessagesMousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]"))).perform();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]/ul/li")).click();
			Thread.sleep(1000);
			
			//Select the number of messages
			driver.findElement(By.name("generalNumberOfMsgs")).click();
			driver.findElement(By.name("generalNumberOfMsgs")).clear();
			driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("4");
			
			//Put a message data
			//driver.findElement(By.xpath("//textarea")).click();
			driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			Thread.sleep(2000);
			
			try
			{
				driver.findElement(By.id("yes")).click();
				driver.findElement(By.cssSelector(".btn-danger")).click();
				Thread.sleep(2000);
			}
			catch (Exception e)
			{
				System.out.println("No Exception");
			}
		}*/
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
	}
	
	@Parameters({"MessageData"})
	@TestRail(testCaseId=283)
	@Test(priority=1)
	public static void PutNewMessageIntoQueue(String MessageData, ITestContext context) throws InterruptedException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(1000);
				
		//Message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(8000);
		
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-1;
		System.out.println(Final);
				
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the EMS Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "The new message was successfully added to EMS Queue");
		}
		else
		{
			System.out.println("The new message was not added into the EMS Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to EMS Queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Test(priority=2)
	@TestRail(testCaseId=284)
	public static void LoadFromFileUsingYesbutton(ITestContext context) throws InterruptedException, AWTException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Load from file Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Load From File...")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + UploadFilepath;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(8000);
	    try {
	    	 if (!checkprogress()) {

	 			System.out.println("exit");
	 		}
	    }
	    catch (Exception e)
	    {
	    	Thread.sleep(10000);
	    }
	    
	    	    
	    //store the queue depth after loading file
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1==result)
		{
			System.out.println("The file is not uploaded successfully");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to upload file");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		else
		{
			System.out.println("The file is uploaded successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully");
		}
		Thread.sleep(1000); 
	}
	
	
	@TestRail(testCaseId=788)
	@Test(priority=3)
	public void LoadFromFileUsingConfigurebuttonWithSingleMessage(ITestContext context) throws InterruptedException, AWTException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Load from file Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Load From File...")).click();
		Thread.sleep(1000);
		
		//Click on Configure
		driver.findElement(By.xpath("//button[contains(.,'Configure')]")).click();
		Thread.sleep(4000);
		
		//Select single option
		Select dd=new Select(driver.findElement(By.xpath("//div[3]/div/div/select")));
		dd.selectByVisibleText("Create Single Message");
		Thread.sleep(3000);
		
		//Use once button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(4000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + UploadLargeFile;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(8000);
	    
	    try {
	    	 if (!checkprogress()) {

	 			System.out.println("exit");
	 		}
	    }
	    catch (Exception e)
	    {
	    	Thread.sleep(10000);
	    }
	    	    
	    //store the queue depth after loading file
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		int Final=result1 - 1;
		System.out.println(Final);
				
		//Message increment condition
		if(Final==result)
		{
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully");
			System.out.println("The file is uploaded successfully");
		}
		else
		{
			System.out.println("The file is not uploaded successfully");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "File upload failed");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@TestRail(testCaseId=789)
	@Test(priority=4)
	public void LoadFromFileUsingConfigurebuttonWithMultipleMessages(ITestContext context) throws InterruptedException, AWTException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Load from file Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Load From File...")).click();
		Thread.sleep(1000);
		
		//Click on Configure
		driver.findElement(By.xpath("//button[contains(.,'Configure')]")).click();
		Thread.sleep(4000);
		
		//Select single option
		Select dd=new Select(driver.findElement(By.xpath("//div[3]/div/div/select")));
		dd.selectByVisibleText("Create Multiple Messages");
		Thread.sleep(3000);
		
		//Use once button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(4000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + UploadLargeFile;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(4000);
	    
	    if (!checkprogress()) {

			System.out.println("exit");
		}
	    
	    //store the queue depth after loading file
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1 > result+1)
		{
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully");
			System.out.println("The file is uploaded successfully");
		}
		else
		{
			System.out.println("The file is not uploaded successfully");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "File upload failed");
			driver.findElement(By.xpath("Condition is Failed")).click();
			
		}
		Thread.sleep(1000);	
	}
	
	
	@Test(priority=5)
	@TestRail(testCaseId=285)
	public static void ExportAllMessages(ITestContext context) throws InterruptedException
	{
		try {
		//Export All Messages As MMF
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MMFMousehour=new Actions(driver);
		MMFMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		MMFMousehour.moveToElement(driver.findElement(By.linkText("Export All Messages..."))).perform();
		driver.findElement(By.linkText("As .MMF")).click();
		Thread.sleep(1000);                                       
		
		//Click on Yes button
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(16000);
		
		//Export All Messages as TXT
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions TXTMousehour=new Actions(driver);
		TXTMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		TXTMousehour.moveToElement(driver.findElement(By.linkText("Export All Messages..."))).perform();
		driver.findElement(By.linkText("As .TXT")).click();
		Thread.sleep(16000);
		
		//Click on Yes button
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(6000);
		
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Successfully exported all EMS queue messages");
		}
		catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to export all EMS queue messages, check details: "+ e.getMessage());
		}
		
	}
	
	@Test(priority=6)
	@TestRail(testCaseId=286)
	public static void CopyAllMessagesFromOneQueueToAnotherQueue(ITestContext context) throws InterruptedException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		//Find the queue current depth
		String depthbeforeCopy=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int resultCopy = Integer.parseInt(depthbeforeCopy);
		System.out.println(resultCopy);
		
		//Store depth of the target queue 
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		int FinalResultBefore=resultCopy+TargetCopy;
		System.out.println(FinalResultBefore);
		
		int QueueName_Index=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			QueueName_Index=4;
		}
		
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(SecondQueueName);
		
		//Copy All Messages
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions CopyAllMessagesMousehour=new Actions(driver);
		CopyAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Copy All")).click();
		Thread.sleep(1000);
		
		 try {
	    	 if (!checkprogress()) {

	 			System.out.println("exit");
	 		}
	    }
	    catch (Exception e)
	    {
	    	Thread.sleep(10000);
	    }
		
		//Search with target queue name 
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		driver.findElement(By.cssSelector(".viewlet-cell-checkbox > input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
			
		 try {
	    	 if (!checkprogress()) {

	 			System.out.println("exit");
	 		}
	    }
	    catch (Exception e)
	    {
	    	Thread.sleep(10000);
	    }
		
		//Getting the Second Queue depth after copying the all messages
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		//Verification
		if(FinalResultBefore==FinalResultAfterCopy)
		{
			System.out.println("All messages are copied to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "All messages are copied to destination queue");
		}
		else
		{
			System.out.println("All messages are not copied to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy All messages to destination queue");
			driver.findElement(By.xpath("Copy messages is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=7)
	@TestRail(testCaseId=287)
	public static void MoveAllMessagesFromOneQueueToAnotherQueue(ITestContext context) throws InterruptedException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		
		//Find the queue current depth
		String depthbeforeMove=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int resultCopy = Integer.parseInt(depthbeforeMove);
		System.out.println(resultCopy);
		
		//Store depth of the target queue
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		int FinalResultBeforeMove=resultCopy+TargetCopy;
		System.out.println(FinalResultBeforeMove);
		
		int QueueName_Index=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			QueueName_Index=4;
		}
		//Second Queue name
		String DestinationQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(DestinationQueueName);
		
		//Move All Messages
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MoveAllMessagesMousehour=new Actions(driver);
		MoveAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Move All")).click();
		Thread.sleep(1000);
		 try {
	    	 if (!checkprogress()) {

	 			System.out.println("exit");
	 		}
	    }
	    catch (Exception e)
	    {
	    	Thread.sleep(10000);
	    }	
		//Search with the target queue name
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(DestinationQueueName);
		driver.findElement(By.cssSelector(".viewlet-cell-checkbox > input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		 try {
	    	 if (!checkprogress()) {

	 			System.out.println("exit");
	 		}
	    }
	    catch (Exception e)
	    {
	    	Thread.sleep(10000);
	    }
		//Second Queue depth after moving the all messages
		String FinalDepth=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int FinalResultAfterMove=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterMove);
				
		//Verification
		if(FinalResultBeforeMove==FinalResultAfterMove)
		{
			System.out.println("All messages are moved to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "All messages are moved to destination queue");
		}
		else
		{
			System.out.println("All messages are not moved to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to move All messages to destination queue");
			driver.findElement(By.xpath("Move messages is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=8)
	@TestRail(testCaseId=288)
	public static void DeleteAllMessagesFromQueue(ITestContext context) throws InterruptedException
	{
		// Restore the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(3000);          
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
		
		int QueueName_Index=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			QueueName_Index=4;
		}
		
		//Queue Name before deleting messages
		String Queuename=driver.findElement(By.xpath("//datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(Queuename);
		
		//Select Delete All option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteAllMessagesMousehour=new Actions(driver);
		DeleteAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Delete All")).click();
		
		//Click on Yes button for deleting the queue
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		 try {
	    	 if (!checkprogress()) {

	 			System.out.println("exit");
	 		}
	    }
	    catch (Exception e)
	    {
	    	Thread.sleep(10000);
	    }
		//Queue name after deleting the messages
		String QueuenameAfter=driver.findElement(By.xpath("//datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(QueuenameAfter);
		
		//verification condition
		if(Queuename.equalsIgnoreCase(QueuenameAfter))
		{
			System.out.println("Messages are not deleted from the queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete All messages from destination queue");
			driver.findElement(By.xpath("Deleting messages failed")).click();
		}
		else
		{
			System.out.println("Messages are deleted from the queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "All messages are deleted from destination queue");
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=9)
	@TestRail(testCaseId=289)
	public static void ClearAllMessagesFromQueue(ITestContext context) throws InterruptedException
	{
		// Restore Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
		
		int QueueName_Index=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			QueueName_Index=4;
		}
		
		//Queue Name before deleting messages
		String Queuename=driver.findElement(By.xpath("//datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(Queuename);
		
		//Select Delete All option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteAllMessagesMousehour=new Actions(driver);
		DeleteAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Clear All")).click();
		
		//Click on Yes for clearing the Messages from the queue
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Queue name after deleting the messages
		String QueuenameAfter=driver.findElement(By.xpath("//datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(QueuenameAfter);
		
		//verification condition
		if(Queuename.equalsIgnoreCase(QueuenameAfter))
		{
			System.out.println("Messages are not Cleared from the queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to clear All messages from destination queue");
			driver.findElement(By.xpath("Clear messages failed")).click();
		}
		else
		{
			System.out.println("Messages are Cleared from the queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "All messages are cleared from destination queue");
		}
		Thread.sleep(1000);
		
	}
	
	@TestRail(testCaseId = 553)
	@Test(priority=10)
	public void PutMessageUsingJsonFile(ITestContext context) throws InterruptedException, AWTException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(1000);	
		
		//click on Attache file option
		driver.findElement(By.xpath("//input[3]")).click();
		Thread.sleep(3000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + "Screenshots\\Staticimages\\File.json";
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(8000);
				
		driver.findElement(By.id("save-message")).click();
		Thread.sleep(8000);
		 		
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		//int Final=result1-1;
		//System.out.println(Final);
				
		//Message increment condition
		if(!(result1==result))
		{
			System.out.println("The Json file is added to the EMS Queue like a message");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Successfully uploaded the json file to EMS queue");
		}
		else
		{
			System.out.println("The Json file is not added to the EMS Queue like a message");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to EMS queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 554)
	@Test(priority=11)
	public void PutMessageUsingXMLFile(ITestContext context) throws InterruptedException, AWTException
	{
		
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(4000);
		
		//click on Attache file option
		driver.findElement(By.xpath("//input[3]")).click();
		Thread.sleep(3000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + "Screenshots\\Staticimages\\FileX.xml";
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(8000);
	    		
		driver.findElement(By.id("save-message")).click();
		Thread.sleep(8000);
				
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		//int Final=result1-1;
		//System.out.println(Final);
				
		//Message increment condition
		if(!(result1==result))
		{
			System.out.println("The xml file is added to the EMS Queue like a message");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Successfully uploaded the xml file to EMS queue");
		}
		else
		{
			System.out.println("The xml file is not added to the EMS Queue like a message");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 555)
	@Parameters({"HexMessageData"})
	@Test(priority=12)
	public static void PutHexMessageIntoQueue(String HexMessageData, ITestContext context) throws InterruptedException
	{
		int Queue_Depth=6;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			Queue_Depth=7;
		}
		
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(4000);
		
		//Message data
		//driver.findElement(By.xpath("//textarea")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(HexMessageData);
		Thread.sleep(2000);
		
		//Clickon HEX button
		driver.findElement(By.xpath("//button[contains(.,'Hex')]")).click();
		Thread.sleep(2000);
				
		//Close the popup window 
		driver.findElement(By.id("save-message")).click();
		Thread.sleep(8000);
				
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-1;
		System.out.println(Final);
				
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the EMS Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "The new message was successfully added to EMS Queue");
		}
		else
		{
			System.out.println("The new message was not added into the EMS Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to EMS Queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Test(priority=20)
	public static void Logout() throws Exception
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
		
		//logout and close the browser
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
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		
		System.out.println("result getStatus: " + result.getStatus());
		// System.out.println("dir: " + dir);
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
