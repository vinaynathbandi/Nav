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
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
public class QueuesInsideBrowseMessages
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

	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "wgs", "Queuemanager", "MessageData", "QueueName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, int wgs, String Queuemanager, String MessageData, String QueueName) throws Exception
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
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new EdgeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else
		{
			/*System.setProperty(sDriver, sDriverpath);
			//driver = new FirefoxDriver();
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", true);
			driver = new FirefoxDriver(options);*/
			
			System.setProperty(sDriver, sDriverpath);       
			
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", true);
			options.addPreference("browser.download.folderList", 2);
			options.addPreference("browser.download.dir", DownloadPath);
			options.addPreference("browser.download.useDownloadDir", true);
			options.addPreference("browser.helperApps.neverAsk.saveToDisk", "text/plain");
			options.addPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer
			options.addPreference("jsonjs.disabled", true);

			driver = new FirefoxDriver(options);
		}
		
		//Enter the URL into browser
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		//Login details
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(10000);
		
		//Adding the Dash board
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		/*driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(wgs);
		
		//Selection of Node
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
			
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(QueueName);
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Check Show Empty Queues check box
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
		
		//put the messages into empty queues for testing
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
			//driver.findElement(By.id("encoding-text-9")).click();
			driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			Thread.sleep(1000);
			try
			{
				driver.findElement(By.id("yes")).click();
				driver.findElement(By.cssSelector(".btn-danger")).click();
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				System.out.println("No Exception");
			}
		}
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(2000);          
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
	}
	
	@Parameters({"MessageData"})
	@TestRail(testCaseId = 89)
	@Test(priority=1)
	public static void PutAMessageUsingPutNewIcon(String MessageData, ITestContext context) throws InterruptedException
	{
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		
		//Find the queue current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span"));
		String depthbefore=Depth.getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Browse Message option
		WebElement Queuecheckbox=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"));
		Queuecheckbox.click();
		driver.findElement(By.linkText("Browse messages")).click();
		Thread.sleep(1000);
		
		//Select put New symbole
		driver.findElement(By.xpath("//img[@title='Put New']")).click();
		
		driver.findElement(By.name("generalNumberOfMsgs")).clear();
		driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("6");
		
		//Message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(8000);
		
		//Store the depth value into string after adding the message into queue
		WebElement Depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span"));
		String depthafter=Depthafter.getText();
		
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-6;
		System.out.println(Final);
		
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages added to Queue successfully");
		}
		else
		{
			System.out.println("The new message was not added into the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Test(priority=2)
	@TestRail(testCaseId = 90)
	public static void DeleteMessageUsingDeleteIcon(ITestContext context) throws Exception
	{
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		//Find the queue current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span"));
		String depthbeforeDelete=Depth.getText();
		int result = Integer.parseInt(depthbeforeDelete);
		System.out.println(result);
				
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Delete Icon
		driver.findElement(By.cssSelector("img[title=\"Delete\"]")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Store the depth value into string after deleting the message from the queue
		WebElement DepthafterDelete=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span"));
		String depthafter=DepthafterDelete.getText();
		
		int result1 = Integer.parseInt(depthafter);
		int Final=result1+1;
		System.out.println(Final);
		
		//Message decrement condition
		if(Final==result)
		{
			System.out.println("The new message was successfully deleted from the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages deleted from Queue successfully");
		}
		else
		{
			System.out.println("The new message was not deleted from the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Test(priority=3)
	@TestRail(testCaseId = 91)
	public static void CopyMessageUsingCopyIcon(ITestContext context) throws InterruptedException
	{
		int manager_Depth=4;
		if(!WGSName.contains("MQM"))
		{
			manager_Depth=5;
		}
		
		//Get the Manager name of first one
		String Managername=driver.findElement(By.xpath("//datatable-body-cell["+ manager_Depth +"]/div/span")).getText();
		
		//Search with that manager
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Managername);
		
		int QueueName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			QueueName_Index=4;
		}
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(SecondQueueName); 
		
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		
		//store the target queue name into string
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Copy Icon
		driver.findElement(By.xpath("//img[@title='Copy message']")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		driver.findElement(By.cssSelector(".ng-star-inserted:nth-child(1) > input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is copied to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages copied successfully");
		}
		else
		{
			System.out.println("Message is not copied to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy messages");
			driver.findElement(By.xpath("Copy message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=4)
	@TestRail(testCaseId = 92)
	public static void MoveMessageUsingMoveIcon(ITestContext context) throws InterruptedException
	{
		int manager_Depth=4;
		if(!WGSName.contains("MQM"))
		{
			manager_Depth=5;
		}
		
		//Get the Manager name of first one
		String Managername=driver.findElement(By.xpath("//datatable-body-cell["+ manager_Depth +"]/div/span")).getText();
		
		int QueueName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			QueueName_Index=4;
		}
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(SecondQueueName);
		
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		
		//Store the target queue name into string
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Move Icon
		driver.findElement(By.xpath("//img[@title='Move message']")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span/input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		for(int i=0; i<=Managername.length(); i++)
		{
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is moved to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages moved successfully");
		}
		else
		{
			System.out.println("Message is not moved to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to move messages");
			driver.findElement(By.xpath("Move message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"MessageData"})
	@TestRail(testCaseId = 93)
	@Test(priority=5)
	public static void EditMessageUsingEditIcon(String MessageData,ITestContext context) throws InterruptedException
	{
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Edit Icon
		driver.findElement(By.xpath("//img[@title='Edit message']")).click();
		Thread.sleep(2000);
		
		//Store the edit message popup page title into string
		String EditMessage=driver.findElement(By.cssSelector(".modal-title")).getText();
		
		//verification of title page
		if(EditMessage.contains("Edit message"))
		{
			System.out.println("Edit message page is Opened");
		}
		else
		{
			System.out.println("Edit message page is not Opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to edit messages");
		}
		
		//Click on Data
		driver.findElement(By.cssSelector(".g-tab:nth-child(7)")).click();
		Thread.sleep(2000);
		
		//Message data
		driver.findElement(By.id("7")).click();
		driver.findElement(By.id("7")).sendKeys(MessageData);
		driver.findElement(By.cssSelector(".button-blue")).click();
		Thread.sleep(2000);
		
		System.out.println("Message is updated successfully");
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Messages updated successfully");
		Thread.sleep(1000);
	}
	
	@Test(priority=6)
	@TestRail(testCaseId = 94)
	public static void LoadMessageFromFileUsingIcon(ITestContext context) throws InterruptedException, AWTException
	{
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Edit Icon
		driver.findElement(By.xpath("//img[@title='Load from file']")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
		
		//Loading a file from the load file option
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
	    Thread.sleep(5000);
	    
	    //verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1==result)
		{
			System.out.println("The file is not uploaded successfully from icon");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to upload messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		else
		{
			System.out.println("The file is uploaded successfully from icon");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully using icon");
		}
		Thread.sleep(1000); 
	    
	    
	}
	
	@Test(priority=7)
	@TestRail(testCaseId = 95)
	public static void ExportSelectedMessageUsingExportIcon(ITestContext context) throws InterruptedException
	{
		try {
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
				
		//Export Icon (MMF Export)
		driver.findElement(By.xpath("//div[10]/button")).click();
		driver.findElement(By.xpath("//button[contains(.,'Export in MMF')]")).click();
		
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(2000);
		Actions a=new Actions(driver);
		a.sendKeys(Keys.ENTER).perform();
		Thread.sleep(4000);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Export Icon (Text Export)
		driver.findElement(By.xpath("//div[10]/button")).click();
		driver.findElement(By.xpath("//button[contains(.,'Export in text')]")).click();
		
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(2000);
		Actions a1=new Actions(driver);
		a1.sendKeys(Keys.ENTER).perform();
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Exporting Messages is working fine");
	
		Thread.sleep(4000);
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to export message, check details: "+ e.getMessage());
		}
	}
	
	// --------------------- From Message Drop down Options -----------------
	
	@Parameters({"MessageData"})
	@TestRail(testCaseId = 96)
	@Test(priority=8)
	public static void EditMessageUsingEditOption(String MessageData, ITestContext context) throws InterruptedException
	{
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		Thread.sleep(1000);
		
		//Edit option                   
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(1)")).click();
		Thread.sleep(2000);
		
		//Store the Edit message popup page title into string
		String EditMessage=driver.findElement(By.cssSelector(".modal-title")).getText();
		
		//Verification
		if(EditMessage.contains("Edit message"))
		{
			System.out.println("Edit message page is Opened");
		}
		else
		{
			System.out.println("Edit message page is not Opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to edit messages");
		}
		
		//Click on Data
		driver.findElement(By.cssSelector(".g-tab:nth-child(7)")).click();
		
		//Message data
		driver.findElement(By.id("7")).click();
		driver.findElement(By.id("7")).sendKeys(MessageData);
		driver.findElement(By.cssSelector(".button-blue")).click();
		Thread.sleep(2000);
		
		System.out.println("Message is updated successfully from sub option");
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Messages updated successfully");
		Thread.sleep(1000);
	}
	
	@Test(priority=9)
	@TestRail(testCaseId = 97)
	public static void DeleteMessageUsingDeleteOption(ITestContext context) throws Exception
	{
		
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		//Find the queue current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span"));
		String depthbeforeDelete=Depth.getText();
		int result = Integer.parseInt(depthbeforeDelete);
		System.out.println(result);
				
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Delete option
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Store the queue depth after deleting the message
		WebElement DepthafterDelete=driver.findElement(By.xpath("//datatable-body-cell["+ Queue_Depth +"]/div/span"));
		String depthafter=DepthafterDelete.getText();
		
		int result1 = Integer.parseInt(depthafter);
		int Final=result1+1;
		System.out.println(Final);
		
		//Message decrement condition
		if(Final==result)
		{
			System.out.println("The new message was successfully deleted from the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages deleted successfully");
		}
		else
		{
			System.out.println("The new message was not deleted from the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Test(priority=10)
	@TestRail(testCaseId = 98)
	public static void CopyMessageUsingCopyOption(ITestContext context) throws InterruptedException
	{
		int manager_Depth=4;
		if(!WGSName.contains("MQM"))
		{
			manager_Depth=5;
		}
		
		//Get the Manager name of first one
		String Managername=driver.findElement(By.xpath("//datatable-body-cell["+ manager_Depth +"]/div/span")).getText();
		
		//Search with that manager
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Managername);
		
		int QueueName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			QueueName_Index=4;
		}
		
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(SecondQueueName);
		
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		
		//Store the Target queue name into string
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Copy option
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(3)")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		driver.findElement(By.cssSelector(".ng-star-inserted:nth-child(1) > input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is copied to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages copied to destination queue successfully");
		
		}
		else
		{
			System.out.println("Message is not copied to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy messages");
			driver.findElement(By.xpath("Copy message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=11)
	@TestRail(testCaseId = 99)
	public static void MoveMessageUsingMoveOption(ITestContext context) throws InterruptedException
	{
		int manager_Depth=4;
		if(!WGSName.contains("MQM"))
		{
			manager_Depth=5;
		}
		
		//Get the Manager name of first one
		String Managername=driver.findElement(By.xpath("//datatable-body-cell["+ manager_Depth +"]/div/span")).getText();
		
		int QueueName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			QueueName_Index=4;
		}
		
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ QueueName_Index +"]/div/span")).getText();
		System.out.println(SecondQueueName);
		
		int Queue_Depth=5;
		if(!WGSName.contains("MQM"))
		{
			Queue_Depth=6;
		}
		
		//Store the target queue name into string after moving the message
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		Thread.sleep(2000);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Move option
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(4)")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".ng-star-inserted:nth-child(1) > input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ Queue_Depth +"]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		for(int i=0; i<=Managername.length(); i++)
		{
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is moved to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages moved to destination queue successfully");
		}
		else
		{
			System.out.println("Message is not moved to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to move messages");
			driver.findElement(By.xpath("Move message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=12)
	public void Logout() throws InterruptedException 
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
		
		//Logout and close the browser
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
