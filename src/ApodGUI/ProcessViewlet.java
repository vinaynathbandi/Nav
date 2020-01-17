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
public class ProcessViewlet
{
	String FinalProcess="";
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String Dnode;
	static String DestinationManager;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		WGSName =Settings.getWGSNAME();
		UploadFilepath =Settings.getUploadFilepath();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager(); 
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
		Thread.sleep(2000);
	}
	
	@Test(priority=1)
	@TestRail(testCaseId = 116)
	@Parameters({"Processname"})
	public static void AddProcessViewlet(String Processname, ITestContext context) throws InterruptedException
	{
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
				
		//Create Process viewlet
		driver.findElement(By.cssSelector(".object-type:nth-child(5)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Processname);
		
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(WGSName);	
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Verification of process viewlet
		if(driver.getPageSource().contains(Processname))
		{
			System.out.println("Process Viewlet is created");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Process viewlet is created successfully");
		}
		else
		{
			System.out.println("Process viewlet is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create process viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		
	}
	
	@Parameters({"NewProcessName", "ProcessDescription", "ApplicationId"})
	@TestRail(testCaseId = 117)
	@Test(priority=2)
	public void CreateProcessFromPlusIcon(String NewProcessName, String ProcessDescription, String ApplicationId, ITestContext context) throws InterruptedException
	{
		try
		{
		//Click on + Icon for creating the process
		driver.findElement(By.xpath("//img[@title='Add Process']")).click();
		
		//Select WGS
		Select WGS=new Select(driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/select")));
		WGS.selectByVisibleText(WGSName);
		
		//Select Node 
		driver.findElement(By.xpath("//div[2]/input")).click();
		try 
		{
			List<WebElement> ProcessNode=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(ProcessNode.size());	
			for (int i=0; i<ProcessNode.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + ProcessNode.get(i).getAttribute("id"));
				String s=ProcessNode.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=ProcessNode.get(i).getAttribute("id");
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
        try 
		{
			List<WebElement> ProcessManager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(ProcessManager.size());	
			for (int i=0; i<ProcessManager.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				System.out.println("Radio button id:" + ProcessManager.get(i).getAttribute("id"));
				String s=ProcessManager.get(i).getText();
				if(s.equals(DestinationManager))
				{
					String id=ProcessManager.get(i).getAttribute("id");
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
		
		//Click on Select path
		driver.findElement(By.xpath("//div[3]/div/div/div/button")).click();
		Thread.sleep(2000);
		
		//Enter the process name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(NewProcessName);
		
		//Enter the description
		driver.findElement(By.id("description")).sendKeys(ProcessDescription);
		
		//Enter the application id
		driver.findElement(By.id("applicationId")).sendKeys(ApplicationId);
		
		//Click on Submit the process
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(10000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Store the process viewlet data into string
		String Processdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification 
		
		if(Processdata.contains(NewProcessName))
		{
			System.out.println("process is created from the Icon option");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Process viewlet is created successfully using add icon");
		}
		else
		{
			System.out.println("process is not created from the Icon option");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create process viewlet using add icon");
			//driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Process creation failed")).click();
		}
		}
		
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while creating the process, check details: "+ e.getMessage());
			System.out.println("Unable to create the Process");
		}
		
	}
	
	@Parameters({"schemaName"})
	@TestRail(testCaseId = 118)
	@Test(priority=17)
	public static void ShowObjectAttributesForProcess(String schemaName,ITestContext context) throws InterruptedException
	{
		/*//Select Show Object Attributes option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);

		String  AttributeName=driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
		//System.out.println(AttributeName);

		//Verification of Object Attribute page
		if(AttributeName.equals("Attributes"))
		{
			System.out.println("Show Object Attribute page is opened");
		}
		else
		{
			System.out.println("Show Object Attribute page is not opened");
			driver.findElement(By.xpath("Attribute page verification failed")).click();
		}
		
		//Close the object Attribute page
		driver.findElement(By.cssSelector(".close-button")).click();*/
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ObjectAttributesVerification(driver, schemaName, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show Object Attribute page is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to show object attribute page, check details: "+ e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@Parameters({"CopyObjectName", "NewProcessName"})
	@TestRail(testCaseId = 119)
	@Test(priority=4, dependsOnMethods= {"CreateProcessFromPlusIcon"})
	public void CopyAsFromCommands(String CopyObjectName, String NewProcessName, ITestContext context) throws InterruptedException
	{
		//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NewProcessName);
    	Thread.sleep(1000);
		
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	
    	//Give the object name
    	driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(CopyObjectName);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	}
    	
    	//Combining the strings 
    	String CopyasProcessName=NewProcessName+CopyObjectName;
    	System.out.println(CopyasProcessName);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Edit the search field data
    	for(int j=0; j<=NewProcessName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasProcessName))
    	{
    		System.out.println("Process is copied");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "CopyAs command is working fine");
    	}
    	else
    	{
    		System.out.println("Process is not copied");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "CopyAs command is nt working properly");
    		driver.findElement(By.xpath("Process failed to copy")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"RenameProcess", "NewProcessName", "CopyObjectName"})
	@TestRail(testCaseId = 120)
	@Test(priority=5, dependsOnMethods= {"CopyAsFromCommands"})
	public void RenameFromCommands(String RenameProcess, String NewProcessName, String CopyObjectName, ITestContext context) throws InterruptedException
	{
		//Combining the strings 
    	String CopyasProcessName=NewProcessName+CopyObjectName;
    	System.out.println(CopyasProcessName);
    	
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasProcessName);
    	Thread.sleep(2000); 
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(RenameProcess);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasProcessName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);	
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	}
    	
    	//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcess);
    	Thread.sleep(1000); 
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println("Viewlet data after Rename: " +ModifiedName);
    	
    	//Edit the new name
		for(int j=0; j<=RenameProcess.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameProcess))
    	{
    		System.out.println("The Process is renamed");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Rename command is working fine");
    	}
    	else
    	{
    		System.out.println("The Process rename is failed");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Process rename command failed");
    		driver.findElement(By.xpath("Rename for Process is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"RenameProcess"})
	@TestRail(testCaseId = 121)
	@Test(priority=6, dependsOnMethods= {"RenameFromCommands"})
	public void DeleteFromCommands(String RenameProcess,ITestContext context) throws InterruptedException
	{
		//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcess);
    	Thread.sleep(1000);   	
    	
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(6000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println("Viewlet data after deleting: " +Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=RenameProcess.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameProcess))
    	{
    		System.out.println("Process is not deleted");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Failed to delete process");
    		driver.findElement(By.xpath("Process delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Process is deleted");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process delete command is working fine");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"ProcessNameFromOptions", "ProcessDescriptionFromOptions", "ApplicationIdFromOptions"})
	@TestRail(testCaseId = 122)
	@Test(priority=7)
	public void CreateProcess(String ProcessNameFromOptions, String ProcessDescriptionFromOptions, String ApplicationIdFromOptions,ITestContext context) throws InterruptedException
	{	
		try
		{
		//Select create process option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Process")).click();
		Thread.sleep(4000);
		
		//Give the process name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(ProcessNameFromOptions);
		
		/*//Select Topic name
		Select Topic=new Select(driver.findElement(By.xpath("//ng-select/div")));
		Topic.selectByIndex(0);
		
		//Topic String
		driver.findElement(By.id("topicString")).sendKeys(TopicString);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/ 
		
		//Enter the description
		driver.findElement(By.id("description")).sendKeys(ProcessDescriptionFromOptions);
		
		//Enter the application id
		driver.findElement(By.id("applicationId")).sendKeys(ApplicationIdFromOptions);
		Thread.sleep(2000);
		
		//Click on Submit the process
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Store the process viewlet data into string
		String Processdata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(Processdata);
		//Verification 
		
		if(Processdata.contains(ProcessNameFromOptions))
		{
			System.out.println("process is created from the options");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process is created from the options");
		}
		else
		{
			System.out.println("process is not created from the options");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Failed to create process");
			//driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Process creation failed")).click();
		}
		}
		
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Got an exception while creating process name, check details: "+ e.getMessage());
			//Click on Close button
			driver.findElement(By.xpath("//div[3]/button")).click();
			
			System.out.println("Unable to create the Process from options");
			
		}
	}
	
	@Test(priority=8)
	@TestRail(testCaseId = 123)
	public void Properties(ITestContext context) throws InterruptedException
	{
		//Select Properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(8000);
		
		//Store the Name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Process name is Disabled");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process name is disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
		}
		else
		{
			System.out.println("The Process name is Enable");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "The Process name is Enable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath("Process name field is Enable")).click();
		}
	}
	
	
	@Test(priority=9)
	@TestRail(testCaseId = 124)
	public static void ProcessEvents(ITestContext context) throws InterruptedException
	{
		//Select Events option
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
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Events page is opened");
		}
		else
		{
			System.out.println("Events page is not opened");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Events page is not working");
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
	@TestRail(testCaseId = 125)
	@Test(priority=10)
	public static void AddToFavoriteViewlet(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int ProcessName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ProcessName_Index=4;
		}
		
		//Store process name into String 
		String ProcessName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ ProcessName_Index +"]/div/span")).getText();
		
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
		String Favdata=driver.findElement(By.xpath("//div[4]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of process added to the favorite viewlet
		if(Favdata.contains(ProcessName))
		{
			System.out.println("Process name is added to the Favorite viewlet");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process name is added successfully to the Favorite viewlet");
		}
		else
		{
			System.out.println("Process name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add process name to the Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Test(priority=11)
	@TestRail(testCaseId = 126)
	public static void CompareProcessNames(ITestContext context) throws InterruptedException
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
	
	@TestRail(testCaseId = 774)
	@Test(priority=12)
	public void CheckDifferencesForProcess(ITestContext context) throws InterruptedException
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
	
	@Parameters({"CopyObjectNameForMultiple"})
	@TestRail(testCaseId = 127)
	@Test(priority=13)
	public void CopyAsFromCommandsForMultipleProcess(String CopyObjectNameForMultiple, ITestContext context) throws InterruptedException
	{		
		//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
		Thread.sleep(2000);
		
		//Get the Existing process name
		String ExistingProcess=driver.findElement(By.xpath("//div[2]/div/input")).getText();
		System.out.println("Existing process name is: " +ExistingProcess);
		
		//Give the object name
    	driver.findElement(By.xpath("//div[2]/div/input")).sendKeys(CopyObjectNameForMultiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(10000);
    	
    	FinalProcess=ExistingProcess+CopyObjectNameForMultiple;
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("No exception occured");
    	}
    	   	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalProcess);
    	Thread.sleep(2000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinalProcess.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(Subviewlet.contains(FinalProcess))
    	{
    		System.out.println("Multiple Process are copied");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Copying multiple process names working fine");
    	}
    	else
    	{
    		System.out.println("Multiple Process are not copied");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to copy multiple process names");
    		driver.findElement(By.xpath("Multiple Process failed to copy")).click();
    	}
    	Thread.sleep(1000);		
	}
	
	@Parameters({"RenameProcessForMultiple"})
	@TestRail(testCaseId = 128)
	@Test(priority=14, dependsOnMethods= {"CopyAsFromCommandsForMultipleProcess"})
	public void RenameFromCommandsForMultipleProcess(String RenameProcessForMultiple, ITestContext context) throws InterruptedException
	{
		//Search
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalProcess);
    	
		//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Rename")).click();
		Thread.sleep(4000);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//div[2]/input")).sendKeys(RenameProcessForMultiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(10000);
    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    	}
    	catch (Exception e)
    	{
    		System.out.println("No exception occured");
    	}
    	
    	for(int j=0; j<=FinalProcess.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcessForMultiple);
    	Thread.sleep(1000);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int j=0; j<RenameProcessForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameProcessForMultiple))
    	{
    		System.out.println("The multiple Process are renamed");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Renaming multiple process names working fine");
    	}
    	else
    	{
    		System.out.println("The Multiple Process rename are failed");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to rename multiple process names");
    		driver.findElement(By.xpath("Rename for Multiple Processes are failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"RenameProcessForMultiple"})
	@TestRail(testCaseId = 129)
	@Test(priority=15, dependsOnMethods= {"RenameFromCommandsForMultipleProcess"})
	public void DeleteFromCommandsForMultipleProcess(String RenameProcessForMultiple, ITestContext context) throws InterruptedException
	{
		//Send the New name into field
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcessForMultiple);
    	Thread.sleep(2000);
    	
    	//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete")).click();
		Thread.sleep(1000);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(8000);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
    	Thread.sleep(4000);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=RenameProcessForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameProcessForMultiple))
    	{
    		System.out.println("Process is not deleted");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete multiple process names using command");
    		driver.findElement(By.xpath("Process delete failed")).click();
    	}
    	else
    	{
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Deleting multiple process names using command is working fine");
    		System.out.println("Process is deleted");
    	}
    	Thread.sleep(1000);
    	
    	/*//Clear the search data
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();*/
    	
	}
	
	
	@Parameters({"MultipleDescription", "AppID"})
	@TestRail(testCaseId = 130)
	@Test(priority=16)
	public void MultipleProperties(String MultipleDescription, String AppID, ITestContext context) throws InterruptedException
	{
		//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(8000);
		
		//Give the description for multiple process
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(MultipleDescription);
		
		//Give the application id for multiple process
		driver.findElement(By.id("applicationId")).clear();
		driver.findElement(By.id("applicationId")).sendKeys(AppID);
		
		//click on OK
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Open the properties for First process
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]")).click();
		Thread.sleep(2000);
		
		//Get the description and application id for First process
		String FirstDescription=driver.findElement(By.id("description")).getAttribute("value");
		String FirstApplicationID=driver.findElement(By.id("applicationId")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Open the properties for First process
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]")).click();
		Thread.sleep(2000);
				
		//Get the description and application id for First process
		String SecondDescription=driver.findElement(By.id("description")).getAttribute("value");
		String SecondApplicationID=driver.findElement(By.id("applicationId")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Verification 
		if(FirstDescription.equals(MultipleDescription) && FirstApplicationID.equals(AppID) && SecondDescription.equals(MultipleDescription) && SecondApplicationID.equals(AppID))
		{
			System.out.println("Properites are Updated for multiple process");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Updating multiple process names is working fine");
		}
		else
		{
			System.out.println("Properites are not Updated for multiple process");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to update multiple process names");
			driver.findElement(By.xpath("Properties updation failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"FavoriteViewletName"})
	@TestRail(testCaseId = 131)
	@Test(priority=17, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleProcess(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		int ProcessName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ProcessName_Index=4;
		}
		
		//Store the process Names into strings
		String ProcessName2=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell["+ ProcessName_Index +"]/div/span")).getText();
		System.out.println(ProcessName2);                
		String ProcessName3=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell["+ ProcessName_Index +"]/div/span")).getText();
		System.out.println(ProcessName3);                
		
		//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(4000);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[4]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verify the multiple processes added to favorite viewlet
		if(Favdata.contains(ProcessName2) && Favdata.contains(ProcessName3))
		{
			System.out.println("Multiple Process names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple process names are added to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Process names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add multiple process names to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);	
		
	}
	
	@Test(priority=20)
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

