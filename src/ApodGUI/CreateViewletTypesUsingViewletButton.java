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
public class CreateViewletTypesUsingViewletButton 
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
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=814)
	@Parameters({"Nodename"})
	@Test(priority=1)
	public void CreateViewletUsingObjectCheckbox(String Nodename, ITestContext context) throws InterruptedException
	{	
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
			
		//Create a Node
		driver.findElement(By.cssSelector("div.object-type")).click();
		driver.findElement(By.name("viewletName")).clear();
		//Thread.sleep(2000);
		driver.findElement(By.name("viewletName")).sendKeys(Nodename);
		
		//Select WGS
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(1000);
		dd.selectByVisibleText(WGSName);
		
		//driver.findElement(By.xpath("//button[@type='button']")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		if(driver.getPageSource().contains(Nodename))
		{
			System.out.println("Node Viewlet is created");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Viewlet is created");
		}
		else
		{
			System.out.println("Node viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Viewlet is not created");
			driver.findElement(By.xpath("Not created")).click();
		}
	}
	
	@TestRail(testCaseId=797)
	@Test(priority=2)
	public void CreateATemporaryViewletUsingSearchCheckbox(ITestContext context) throws InterruptedException
	{
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		
		//Click on Temporary viewlet check box
		driver.findElement(By.id("temp")).click();
		
		//Click on Create Button
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click(); 
		Thread.sleep(1000);
		
		String ViewletName=driver.findElement(By.name("viewletName")).getText();
		System.out.println(ViewletName);
		
		//Select WGS
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(1000);
		dd.selectByVisibleText(WGSName);
		
		//driver.findElement(By.xpath("//button[@type='button']")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Temporary Viewlet is created");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Created temporary viewlet");
		}
		else
		{
			System.out.println("Temporary viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Temporary viewlet is not created");
			driver.findElement(By.xpath("Temporary viewlet Not created")).click();
		}
	}
	
	@TestRail(testCaseId=815)
	@Parameters({"FavoriteViewletName"})
	@Test(priority=3)
	public void CreateFavoriteViewletUsingCheckbox(String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
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
		
		if(driver.getPageSource().contains(FavoriteViewletName))
		{
			System.out.println("Favorite Viewlet is created");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Favorite viewlet created");
		}
		else
		{
			System.out.println("Favorite viewlet is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Favorite viewlet not created");
			driver.findElement(By.xpath("Not created Favorite")).click();
		}
	}
	
	@TestRail(testCaseId=816)
	@Test(priority=4)
	public void OpenAnExistingCheckbox(ITestContext context) throws InterruptedException
	{
		//Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		
		Boolean Checkbox=driver.findElement(By.id("existing")).isEnabled();
		System.out.println(Checkbox);
		
		if(Checkbox == false)
		{
			System.out.println("Open existing viewlet checkbox is disabled state");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Checkbox is disabled");
		}
		else
		{
			System.out.println("Open existing viewlet checkbox is Enabled state");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Checkbox is not disabled");
			driver.findElement(By.id("Open existing check box condition failed")).click();
		}
		Thread.sleep(1000);
		
		//Click on cancel button
		driver.findElement(By.cssSelector(".g-button-red")).click();
	}
	
	@TestRail(testCaseId=798)
	@Parameters({"Dashboardname"})
	@Test(priority=5)
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
		String ViewletTitle=driver.findElement(By.xpath("//div[4]/app-viewlet/div/div[2]/div/div/div")).getText();
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
		
		/*//Get the viewlet title from viewlet
		String Viewletdata=driver.findElement(By.id("main-page")).getText();
		System.out.println("After logout Temporary Viewlet data is: " +Viewletdata);*/
		
		if(driver.getPageSource().contains(ViewletTitle))
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
	
	@TestRail(testCaseId=817)
	@Parameters({"NewViewletName"})
	@Test(priority=6)
	public void EditViewletOption(String NewViewletName, ITestContext context) throws InterruptedException
	{		
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//select Edit viewlet option
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(1000);
		
		//Change the viewlet name
		driver.findElement(By.id("viewletName")).clear();
		driver.findElement(By.id("viewletName")).sendKeys(NewViewletName);
		
		//click on Apply changes
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		//Store the viewlet name into string
		String ViewletName=driver.findElement(By.cssSelector(".title-table")).getText();
		System.out.println("Viewlet Name is:" +ViewletName);
		
		if(ViewletName.equalsIgnoreCase(NewViewletName))
		{
			System.out.println("Viewlet Edit is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Edit viewlet option working");
		}
		else
		{
			System.out.println("Viewlet Edit is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Edit viewlet option not working");
			driver.findElement(By.id("Edit option failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=818)
	@Test(priority=7)
	public void DeleteViewletOption(ITestContext context) throws InterruptedException
	{
		//Store the viewlet name into string
		String BeforeDeleteViewletName=driver.findElement(By.cssSelector(".title-table")).getText();
		System.out.println("Viewlet Name is:" +BeforeDeleteViewletName);
		
		//Click on Dropdown menu
		driver.findElement(By.id("dropdownMenuButton")).click();
		
		//select Edit viewlet option
		driver.findElement(By.linkText("Delete viewlet")).click();
		Thread.sleep(1000);
		
		//Confirmation ok button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
		
		//Store the viewlet name into string
		String AfterDeleteViewletName=driver.findElement(By.cssSelector(".block-title-row")).getText();
		System.out.println("Viewlet page data:" +AfterDeleteViewletName);
		
		if(AfterDeleteViewletName.equalsIgnoreCase(BeforeDeleteViewletName))
		{
			System.out.println("Delete option is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Delete viewlet option not working");
			driver.findElement(By.id("Delete viewlet option failed")).click();
		}
		else
		{
			System.out.println("Delete option is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Delete viewlet option working");
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
