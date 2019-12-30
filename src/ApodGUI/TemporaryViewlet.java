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
public class TemporaryViewlet 
{

	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String Dnode;
	static String Manager1;
	
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
		Manager1 =Settings.getManager1();
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
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	public void CreateTemporaryViewletUsingDirectOption(String Dashboardname, ITestContext context) throws Exception
	{
		Settings.read();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[2]")).click();
		
		//select temporary viewlet checkbox
		driver.findElement(By.id("temp")).click();
		
		//click on Ok button
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
		Thread.sleep(3000);
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(WGSName);
		
		//click on Save changes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//get the viewlet title from viewlet
		String ViewletTitle=driver.findElement(By.cssSelector(".title-table")).getText();
		System.out.println("Temporary Viewlet title is: " +ViewletTitle);
		
		//Logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		
		//Login again
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//Go to that dashboard
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase(Dashboardname))
			{
				fi.click();
				break;
			}
		}
		Thread.sleep(4000);
		
		//Get the viewlet title from viewlet
		String Viewletdata=driver.findElement(By.id("main-page")).getText();
		System.out.println("After logout Temporary Viewlet data is: " +Viewletdata);
		
		if(Viewletdata.contains(ViewletTitle))
		{
			System.out.println("Temporary viewlet functionality failed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Temporary viewlet creation failed");
			driver.findElement(By.id("Temporary failed")).click();
		}
		else
		{
			System.out.println("Temporary viewlet functionality Working");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Temporary viewlet created using Checkbox option");
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	public void CreateTemporaryViewletUsingTemporaryViewletCheckbox(String Dashboardname, ITestContext context) throws Exception
	{
		Settings.read();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[2]")).click();
		
		//select temporary viewlet checkbox
		//driver.findElement(By.id("temp")).click();
		
		//click on Ok button
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
		Thread.sleep(3000);
		
		//Select Temp checkbox
		driver.findElement(By.name("isTemporary")).click();
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(WGSName);
		
		//click on Save changes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//get the viewlet title from viewlet
		String ViewletTitle=driver.findElement(By.cssSelector(".title-table")).getText();
		System.out.println("Temporary Viewlet title is: " +ViewletTitle);
		
		//Logout
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		
		//Login again
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//Go to that dashboard
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase(Dashboardname))
			{
				fi.click();
				break;
			}
		}
		Thread.sleep(4000);
		
		//Get the viewlet title from viewlet
		String Viewletdata=driver.findElement(By.id("main-page")).getText();
		System.out.println("After logout Temporary Viewlet data is: " +Viewletdata);
		
		if(Viewletdata.contains(ViewletTitle))
		{
			System.out.println("Temporary viewlet functionality failed from Checkbox");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Temporary viewlet creation failed");
			driver.findElement(By.id("Temporary failed")).click();
		}
		else
		{
			System.out.println("Temporary viewlet functionality Working using edit viewlet option");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Temporary viewlet is created using editviewlet page option");
		}
		
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
