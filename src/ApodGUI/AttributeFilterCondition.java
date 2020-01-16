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
import org.openqa.selenium.firefox.FirefoxOptions;
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
public class AttributeFilterCondition 
{
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String EMS_WGS_INDEX;
	static String EMS_WGSNAME;
	static String SelectTopicName;
	static String DeleteDurableName;
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
		EMS_WGS_INDEX =Settings.getEMS_WGS_INDEX();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		SelectTopicName = Settings.getSelectTopicName(); 
		DeleteDurableName =Settings.getDeleteDurableName();
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
	}
	
	@Parameters({"sDriver", "sDriverpath"})
	@Test
	public static void Login(String sDriver, String sDriverpath) throws Exception
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
	}
	
	@Parameters({"Dashboardname", "LocalQueue"})
	@Test(priority=1)
	public static void AddDashboard(String Dashboardname, String LocalQueue) throws Exception
	{
		
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByVisibleText(wgs);
		
		//Selection of Node
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
	}
	
	@Parameters({"ConditionName", "AttributeName", "CompareOperation", "ConditionData", "RowValue"})
	@TestRail(testCaseId = 348)
	@Test(priority=2)
	public static void AddAttributeFilterCondition(String ConditionName, String AttributeName, String CompareOperation, String ConditionData, String RowValue, ITestContext context) throws Exception
	{	
		try
		{
		//Edit Viewlet page
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Add attribute filter
		driver.findElement(By.cssSelector("button.btn-white-round")).click();
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter/div/button")).click();
		Thread.sleep(2000);
		
		//Filter name
		driver.findElement(By.cssSelector("div.mafa-filter-name > input")).sendKeys(ConditionName);
		driver.findElement(By.xpath("//div[4]/button")).click();
		Thread.sleep(2000);
		
		//Select the attribute
		driver.findElement(By.xpath("//td[contains(.,'"+ AttributeName +"')]")).click();
		Thread.sleep(2000);
		//driver.findElement(By.xpath("//td[contains(.,'Maximum Depth')]")).click();
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter-add-available-attr/div/div/div[3]/button")).click();
		Thread.sleep(2000);
		
		//Comparison operater name
		Select Compare=new Select(driver.findElement(By.name("isNumber")));
		Compare.selectByVisibleText(CompareOperation);
		
		/*//Filter the data
		driver.findElement(By.xpath("//app-mod-manage-attribute-filter-add-available-attr/div/div/div/div/input")).sendKeys(RowValue);
		driver.findElement(By.xpath("//td/div")).click();
		
		driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
		Thread.sleep(2000);*/
		
		//Enter the Condition value 
		driver.findElement(By.cssSelector("td.mafa-table-row-cell > input")).click();
		driver.findElement(By.cssSelector("td.mafa-table-row-cell > input")).sendKeys(ConditionData);
		Thread.sleep(2000);
		
		//Click on Ok
		driver.findElement(By.xpath("(//button[@type='button'])[10]")).click();
		Thread.sleep(6000);
		
		//Select the added one
		WebElement table=driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"));
		List<WebElement> Rows=table.findElements(By.tagName("tr"));
		System.out.println("No of rows are: " +Rows.size());
		
		for(WebElement td:Rows)
		{
			//System.out.println("inner :" +td.getAttribute("innerHTML"));
			//System.out.println("inner text :" +td.getAttribute("innerText"));
			String value=td.findElement(By.tagName("td")).findElement(By.tagName("input")).getAttribute("value");
			System.out.println("Final value is:" +value);
			
			if(value.equalsIgnoreCase(ConditionName))
			{
				td.click();
			}
		}
		
		//driver.findElement(By.xpath("//td/input")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[7]")).click();
		Thread.sleep(2000);
		
		//Click on Apply
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		String Noofqueues=driver.findElement(By.xpath("//datatable-footer/div/span")).getText();
		String[] Index1=Noofqueues.split(" Visible");
		String[] Index2=Index1[0].split(": ");
		int k=Integer.parseInt(Index2[1]);
		for(int i=1; i<=k; i++)
		{
			String Result=driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
			int integer=Integer.parseInt(Result);
			System.out.println("Values are: " +integer+ " ConditionData is: " +ConditionData);
			if(integer==Integer.parseInt(ConditionData))
			{
				
				System.out.println("Attribute filter is working fine");
			}
		}
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Inactive Check box is Working fine");
		}
		catch (Exception e)
		{
			System.out.println("Exception occured");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Attribute filter condition not working");
			driver.findElement(By.xpath("Attribute filter condition failed")).click();
		}
	}
	
	//Logout
	@Test(priority=3)
	public void Logout() throws InterruptedException 
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
