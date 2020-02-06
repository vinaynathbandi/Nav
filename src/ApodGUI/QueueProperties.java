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
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "LocalQueue"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String LocalQueue) throws Exception
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
		
		//Restore Default settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(1000);
		
	}
	
	@Parameters({"PutMessageOption"})
	@Test(priority=1)
	public void PutMessageInInhibitedQueue(String PutMessageOption) throws InterruptedException
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
		/*Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();*/
		
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
					
					System.out.println("options are: " +subvalue.getAttribute("innerHTML"));
					System.out.println("Attributes are: " +subvalue.getAttribute("title"));
				}
				
			}
		}
		
		this.Resetput();
	}
	
	
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
