package ApodGUI;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
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
import org.w3c.dom.NodeList;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class QueueProperties 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Q_QueueName;
	static String Dnode;
	static String DestinationManager;
	static String FinalQueuename="";
	static String MsgDepth="";
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME();
		Q_QueueName =Settings.getQ_QueueName();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "LocalQueue", "Managername", "Channelname"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String LocalQueue, String Managername, String Channelname) throws Exception
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
		Thread.sleep(20000);
		
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
		
		//--------- Create Queue viewlet-----------
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
			
		//Create Route viewlet
		driver.findElement(By.cssSelector(".object-type:nth-child(3)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(LocalQueue);
		
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(1000);
		dd.selectByVisibleText(WGSName);
	
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		/*//Restore Default settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(1000);
		
		// ---- Creating Manager Viewlet ----
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
			
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(2)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Managername);
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(WGSName);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		// ---- Creating Channel Viewlet ----
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
			
		//Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(4)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(Channelname);
		
		//Select WGS type
		Select WGSSelection1=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection1.selectByVisibleText(WGSName);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);*/
		
	}
	
	@TestRail(testCaseId = 830)
	@Parameters({"PutMessageOption"})
	@Test(priority=1)
	public void PutMessageInInhibitedQueue(String PutMessageOption, ITestContext context) throws InterruptedException
	{
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//change put message to inhibited
		Select inhibited=new Select(driver.findElement(By.id("putAllowed")));
		inhibited.selectByVisibleText(PutMessageOption);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		
		List<WebElement> lst=driver.findElements(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]/ul/li"));
		
		StringBuilder buffer=new StringBuilder();
		for(WebElement lstvalue : lst)
		{
			List<WebElement> links= lstvalue.findElements(By.tagName("a"));
			//System.out.println("options are: " +lstvalue.getAttribute("innerHTML"));
			for(WebElement weblink : links)
			{
				//System.out.println("options links html:  " +weblink.getAttribute("innerHTML"));
				String options=weblink.getAttribute("innerHTML");
				buffer.append(options);
				buffer.append(",");
				
			}
		}
		
		String Listofoptions=buffer.toString();
		System.out.println("Final options are: " +Listofoptions);
		
		
		//Refresh viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);
		
		if(Listofoptions.contains("Put New Message"))
		{
			System.out.println("Put messages inhibited is not working");
			this.Resetput();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Put messages inhibited is failed");
			driver.findElement(By.id("Put Inhibited failed")).click();
		}
		else
		{
			System.out.println("Put messages inhibited is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Put messages inhibited is working");
		}
		

		this.Resetput();
		
		/*Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		
		//driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		WebElement op=driver.findElement(By.id("dropdown-block")).findElement(By.tagName("div")).findElement(By.tagName("ul"));
		List <WebElement> Lis=op.findElements(By.tagName("li"));
		System.out.println("List of lis are: " +Lis.size());
		//StringBuilder buffer=new StringBuilder();
		for(WebElement anc : Lis)
		{
			if(anc.getText().equalsIgnoreCase("Messages"))
			{
				WebElement sub=anc.findElement(By.tagName("ul"));
				List<WebElement> Sublis=sub.findElements(By.tagName("li"));
				System.out.println("Sub options size is: " +Sublis.size());
				for(WebElement subvalue : Sublis)
				{
					WebElement dt=subvalue.findElement(By.tagName("a"));
					System.out.println("options are: " +subvalue.getAttribute("innerHTML"));
					System.out.println("Attributes are: " +subvalue.getAttribute("title"));
					System.out.println("options are text: " +subvalue.getAttribute("innerTEXT"));
					System.out.println("options are text: " +subvalue.getAttribute("value"));
					System.out.println("options are text: " +subvalue.getText());
				}				
			}
		}*/
		
		
	}
	
	@TestRail(testCaseId = 831)
	@Parameters({"GetMessageOption"})
	@Test(priority=2)
	public void GetMessageFromInhibitedQueue(String GetMessageOption, ITestContext context) throws InterruptedException
	{
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//change put message to inhibited
		Select inhibited=new Select(driver.findElement(By.id("getAllowed")));
		inhibited.selectByVisibleText(GetMessageOption);
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		WebElement op=driver.findElement(By.id("dropdown-block")).findElement(By.tagName("div")).findElement(By.tagName("ul"));
		List <WebElement> Lis=op.findElements(By.tagName("li"));
		System.out.println("List of lis are: " +Lis.size());
		StringBuilder buffer=new StringBuilder();
		for(WebElement anc : Lis)
		{
			String Options=anc.getText();
			if(Options.equalsIgnoreCase(""))
			{
				
			}
			else
			{
				buffer.append(Options);
				buffer.append(",");
			}
		}
		
		String OptionsNames=buffer.toString();
		System.out.println("List of dashboards are: " +OptionsNames);
		
		//Refresh viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);
		
		if(OptionsNames.contains("Browse messages"))
		{
			System.out.println("Get messages inhibited is not working");
			this.Resetget();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Get messages inhibited is failed");
			driver.findElement(By.id("Get Inhibited failed")).click();
		}
		else
		{
			System.out.println("Get messages inhibited is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Get messages inhibited is working");
		}
		this.Resetget();
	}
	
	@TestRail(testCaseId = 832)
	@Parameters({"MessageData", "messagesize"})
	@Test(priority=3)
	public void MaximumQueueDepth(String MessageData, int messagesize, ITestContext context) throws InterruptedException
	{
		int CDepth_Index=5;
		if(!WGSName.contains("MQM"))
		{
			CDepth_Index=6;
		}
		
		//Get current depth of the Queue          
		String value=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ CDepth_Index +"]/div/span")).getText();
		int cd=Integer.parseInt(value);
		System.out.println("Current depth is: " +cd);
		
		int MDepth_Index=6;
		if(!WGSName.contains("MQM"))
		{
			MDepth_Index=7;
		}
		
		//Get maximum depth of queue
		String valuem=driver.findElement(By.xpath(" //div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MDepth_Index +"]/div/span")).getText();
		int MaxD=Integer.parseInt(valuem);
		System.out.println("Initial maximum depth is:" +MaxD);
		
		//Increase the depth 
		int MaxDepth=cd+2;
		System.out.println("Maximum depth value is:" +MaxDepth);
		
		/*//Open properties page
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Click on Extend tab
		driver.findElement(By.linkText("Extended")).click();
		Thread.sleep(3000);*/
		
		MaximumDepth();
		
		driver.findElement(By.id("maxQueueDepth")).clear();
		driver.findElement(By.id("maxQueueDepth")).sendKeys(Integer.toString(MaxDepth));
		Thread.sleep(3000);
		
		//click on ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		Putmessage(MessageData, messagesize);
		
		//refresh the viewlet
		for(int i=0; i<=3; i++)
		{
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(5000);
		}
		
		//get the current depth and store into string
		String FinalDepth=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ CDepth_Index +"]/div/span")).getText();
		int rcd=Integer.parseInt(FinalDepth);
		System.out.println("Final depth is: " +rcd);
		
		if(rcd==MaxDepth)
		{
			System.out.println("Maximum depth is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Maximum queue depth is working");
		}
		else
		{
			System.out.println("Maximum depth is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Maximum depth is failed");
			MaximumDepth();
			driver.findElement(By.id("maxQueueDepth")).sendKeys(valuem);
			Thread.sleep(3000);
			
			//click on ok button
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
			driver.findElement(By.id("maximum depth failed")).click();
		}
		
		MaximumDepth();
		driver.findElement(By.id("maxQueueDepth")).sendKeys(valuem);
		Thread.sleep(3000);
		
		//click on ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);

	}
	
	
	@TestRail(testCaseId = 833)
	@Parameters({"MessageLength", "length"})
	@Test(priority=4)
	public void MessageLength(String MessageLength, int length, ITestContext context) throws InterruptedException
	{
		int CDepth_Index=5;
		if(!WGSName.contains("MQM"))
		{
			CDepth_Index=6;
		}
		
		//Get current depth of the Queue          
		String value=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ CDepth_Index +"]/div/span")).getText();
		int cd=Integer.parseInt(value);
		System.out.println("Current depth is: " +cd);
		
		/*//Open properties page
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Click on Extend tab
		driver.findElement(By.linkText("Extended")).click();
		Thread.sleep(3000);*/
		
		MessageDepth();
		
		//Get the depth		
		MsgDepth=driver.findElement(By.id("maxMsgLength")).getAttribute("value");
		System.out.println("Message Attribute depth is: " +MsgDepth);			
		
		driver.findElement(By.id("maxMsgLength")).clear();
		
		//Edit max message length
		driver.findElement(By.id("maxMsgLength")).sendKeys("100");
		
		//click on ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		
		Putmessage(MessageLength, length);
		
		//refresh the viewlet
		for(int i=0; i<=3; i++)
		{
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(5000);
		}
		
		//get the current depth and store into string
		String FinalDepth=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ CDepth_Index +"]/div/span")).getText();
		int rcd=Integer.parseInt(FinalDepth);
		System.out.println("Final depth is: " +rcd);
		
		if(cd==rcd)
		{
			System.out.println("Maximum message length is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "maximum message length is working");
		}
		else
		{
			System.out.println("Maximum message length is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Maximum message length not working");
			MessageDepth();
			driver.findElement(By.id("maxMsgLength")).clear();
			driver.findElement(By.id("maxMsgLength")).sendKeys(MsgDepth);
			Thread.sleep(3000);
			
			//click on ok button
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
			
			driver.findElement(By.id("Maximum msg length failed")).click();
		}
		
		MessageDepth();
		//Get the depth				
		driver.findElement(By.id("maxMsgLength")).clear();
		driver.findElement(By.id("maxMsgLength")).sendKeys(MsgDepth);
		Thread.sleep(3000);
		
		//click on ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
	}
	
	
	@Test(priority=3)
	public void MQStatisticsForManager(String Managername, ITestContext context) throws InterruptedException
	{		
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("MQ Statistics...")).click();
		Thread.sleep(4000);
		
		//Select last 24hours option
		Select Period=new Select(driver.findElement(By.xpath("//div[2]/div/div/select")));
		Period.selectByVisibleText("Last 24 hours");
		
		//Get the statistics data
		String BeforeStatisctis=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Before statistics data is: " +BeforeStatisctis);
		
		//Close button
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(3000);
		
		//---- Change the properties for getting the statistics ----
		this.EnableStatistics();
		
		//------ Update description ---- 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.linkText("General")).click();
		Thread.sleep(3000);
		
		//Change description
		driver.findElement(By.id("qmngrDescription")).clear();
		driver.findElement(By.id("qmngrDescription")).sendKeys("Statistics");
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(50000);
		
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[2]")).click();
			Thread.sleep(10000);
		}
		
		//---- Select Browse Messages Option --
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("MQ Statistics...")).click();
		Thread.sleep(4000);
		
		//Select last 24hours option
		Select Period1=new Select(driver.findElement(By.xpath("//div[2]/div/div/select")));
		Period1.selectByVisibleText("Last 24 hours");
		
		//Get the statistics data
		String AfterStatisctis=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("After statistics data is: " +AfterStatisctis);
		
		//Close button
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(3000);
		
		//---- Change the properties for stopping the statistics ----
		this.DiableStatistics();
		
		if(AfterStatisctis.equalsIgnoreCase(BeforeStatisctis)) 
		{
			System.out.println("Manager statistics not updated");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Manager statistics not updated");
			driver.findElement(By.id("Manager statistics failed")).click();
		}
		else
		{
			System.out.println("Manager statistics is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Manager statistics updated");
		}
	}
	
	@Test(priority=4)
	public void QueueStatistics(ITestContext context) throws InterruptedException
	{
		int ManagerName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ManagerName_Index=4;
		}
		
		//Get the manager name
		String FinalManager=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ ManagerName_Index +"]/div/span")).getText();
		System.out.println("Manager name is: " +FinalManager);
		
		//------ Apply filter with the node name ------------------
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select node value 
		driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
		Thread.sleep(3000);
		
		try 
		{
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				System.out.println(s);
				if(s.equals(FinalManager))
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
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//------ Click on the queue statistics -------
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("MQ Statistics...")).click();
		Thread.sleep(4000);
		
		//Select last 24hours option
		Select Period=new Select(driver.findElement(By.xpath("//div[2]/div/div/select")));
		Period.selectByVisibleText("Last 24 hours");
		
		//Get the statistics data
		String BeforeStatisctis=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Before statistics data is: " +BeforeStatisctis);
		
		//Close button
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(3000);
		
		//Enable statistics
		this.EnableStatistics();
		
		//----- Put the message into queue ------
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(4000);
				
		//Message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys("Test Statistics");
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(50000);
		
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(10000);
		}
						
		//---- Select Queue Statistics Option --
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("MQ Statistics...")).click();
		Thread.sleep(4000);
		
		//Select last 24hours option
		Select Period1=new Select(driver.findElement(By.xpath("//div[2]/div/div/select")));
		Period1.selectByVisibleText("Last 24 hours");
		
		//Get the statistics data
		String AfterStatisctis=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("After statistics data is: " +AfterStatisctis);
		
		//Close button
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(3000);
				
		//Disable the statistics
		this.DiableStatistics();
		
		if(AfterStatisctis.equalsIgnoreCase(BeforeStatisctis)) 
		{
			System.out.println("Queue statistics not updated");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue statistics not updated");
			driver.findElement(By.id("Queue statistics failed")).click();
		}
		else
		{
			System.out.println("Queue statistics is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue statistics updated");
		}
	}
	
	@Test(priority=5)
	public void MQStatisticsForChannels(ITestContext context) throws InterruptedException
	{
		int ManagerName_Index=3;
		if(!WGSName.contains("MQM"))
		{
			ManagerName_Index=4;
		}
		
		//Get the manager name
		String FinalManager=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ ManagerName_Index +"]/div/span")).getText();
		System.out.println("Manager name is: " +FinalManager);
		
		//------ Apply filter with the node name ------------------
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select node value 
		driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
		Thread.sleep(3000);
		
		try 
		{
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				System.out.println(s);
				if(s.equals(FinalManager))
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
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//------ Click on the channel statistics -------
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("MQ Statistics...")).click();
		Thread.sleep(4000);
		
		//Select last 24hours option
		Select Period=new Select(driver.findElement(By.xpath("//div[2]/div/div/select")));
		Period.selectByVisibleText("Last 24 hours");
		
		//Get the statistics data
		String BeforeStatisctis=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Before statistics data is: " +BeforeStatisctis);
		
		//Close button
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(3000);
		
		//Enable statistics
		//this.EnableStatistics();
		
		//------ Update description ---- 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
				
		//Change description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Statistics");
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(50000);
		
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
			Thread.sleep(10000);
		}
		
		//------ Click on the channel statistics -------
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("MQ Statistics...")).click();
		Thread.sleep(4000);
		
		//Select last 24hours option
		Select Period1=new Select(driver.findElement(By.xpath("//div[2]/div/div/select")));
		Period1.selectByVisibleText("Last 24 hours");
		
		//Get the statistics data
		String AfterStatisctis=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("After statistics data is: " +AfterStatisctis);
		
		//Close button
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(3000);
		
		//Disable the channel properties
		//this.DiableStatistics();
		
		
		if(AfterStatisctis.equalsIgnoreCase(BeforeStatisctis)) 
		{
			System.out.println("Channel statistics not updated");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Channel statistics not updated");
			driver.findElement(By.id("Channel statistics failed")).click();
		}
		else
		{
			System.out.println("Channel statistics is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel statistics updated");
		}
	}
		
	@Test(priority=25)
	public static void Logout() throws Exception
	{		
		//Delete the dashboard 
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
		
		//Logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.close();
	}
	
	public void MaximumDepth() throws InterruptedException
	{
		//Open properties page
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Click on Extend tab
		driver.findElement(By.linkText("Extended")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.id("maxQueueDepth")).clear();			
	}
	
	public void MessageDepth() throws InterruptedException
	{
		//Open properties page
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Click on Extend tab
		driver.findElement(By.linkText("Extended")).click();
		Thread.sleep(3000);			
	}
	
	public void Putmessage(String Message, int size) throws InterruptedException
	{
		//Select the put new message option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions PutMessagesMousehour=new Actions(driver);
		PutMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(4000);
		
		//Select the number of messages
		driver.findElement(By.name("generalNumberOfMsgs")).click();
		driver.findElement(By.name("generalNumberOfMsgs")).clear();
		driver.findElement(By.name("generalNumberOfMsgs")).sendKeys(Integer.toString(size));
		
		//Put a message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(Message);
		Thread.sleep(4000);
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
		Thread.sleep(6000);
		
	}
	
	public void EnableStatistics() throws InterruptedException
	{
		//---- Change the properties for getting the statistics ----
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//select Monitoring tab
		driver.findElement(By.linkText("Monitoring")).click();
		
		//Change the settings
		Select MQI=new Select(driver.findElement(By.id("monitoringSDCMQI")));
		MQI.selectByVisibleText("ON");
		
		Select Queues=new Select(driver.findElement(By.id("monitoringSDCQueues")));
		Queues.selectByVisibleText("ON");
		
		Select Channels=new Select(driver.findElement(By.id("monitoringSDCChannels")));
		Channels.selectByVisibleText("MEDIUM");
		
		driver.findElement(By.id("monitoringSDCDataOutputInterval")).clear();
		driver.findElement(By.id("monitoringSDCDataOutputInterval")).sendKeys("60");
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
	}
	
	public void DiableStatistics() throws InterruptedException
	{
		//---- Change the properties for stopping the statistics ----
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//select Monitoring tab
		driver.findElement(By.linkText("Monitoring")).click();
		
		//Change the settings
		Select MQI1=new Select(driver.findElement(By.id("monitoringSDCMQI")));
		MQI1.selectByVisibleText("OFF");
		
		Select Queues1=new Select(driver.findElement(By.id("monitoringSDCQueues")));
		Queues1.selectByVisibleText("OFF");
		
		Select Channels1=new Select(driver.findElement(By.id("monitoringSDCChannels")));
		Channels1.selectByVisibleText("OFF");
		
		driver.findElement(By.id("monitoringSDCDataOutputInterval")).clear();
		driver.findElement(By.id("monitoringSDCDataOutputInterval")).sendKeys("1800");
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
	}
	
	public void Resetget() throws InterruptedException
	{
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//change put message to inhibited
		Select inhibited=new Select(driver.findElement(By.id("getAllowed")));
		inhibited.selectByVisibleText("Allowed");
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);	
	}
	
	public void Resetput() throws InterruptedException
	{
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//change put message to inhibited
		Select inhibited=new Select(driver.findElement(By.id("putAllowed")));
		inhibited.selectByVisibleText("Allowed");
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);	
	}
	
	public List<WebElement> Getlist()
	{
		WebElement lis=driver.findElement(By.className("wrapper-dropdown")).findElement(By.tagName("ul"));
		List<WebElement> op=lis.findElements(By.tagName("li"));
		return op;
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
