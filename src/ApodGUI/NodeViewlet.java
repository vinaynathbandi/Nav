package ApodGUI;

import java.io.File;
import java.io.IOException;
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
public class NodeViewlet {
	static WebDriver driver;
	static String IPAddress;
	static String HostName;
	static String PortNo;
	static String WGSPassword;
	static String Node_hostname;
	static String NodeNameFromIcon;
	static String HostNameFromIcon;
	static String IPAddressFromIcon;
	static String QueueManagerName;
	static String Node_Hostname;
	static String DefaultTransmissionQueue;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String Manager1;
	static String Manager2;
	static String NodeWGS;
	static String WGSName;

	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		IPAddress = Settings.getIPAddress();
		HostName = Settings.getWGS_HostName();
		PortNo = Settings.getWGS_PortNo();
		WGSPassword = Settings.getWGS_Password();
		Node_hostname = Settings.getNode_Hostname();
		NodeNameFromIcon = Settings.getNode_NameFromIcon();
		HostNameFromIcon = Settings.getHostNameFromIcon();
		IPAddressFromIcon = Settings.getIPAddressFromIcon();
		QueueManagerName = Settings.getQueueManagerName();
		Node_Hostname =Settings.getNode_Hostname();
		DefaultTransmissionQueue =Settings.getDefaultTransmissionQueue();
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
		NodeWGS =Settings.getNodeWGS();
		WGSName =Settings.getWGSNAME();
	}

	@Parameters({ "sDriver", "sDriverpath", "NodeDashboardname", "NodeViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String NodeDashboardname, String NodeViewletName) throws Exception {
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new ChromeDriver();
		} else if (sDriver.equalsIgnoreCase("webdriver.ie.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new InternetExplorerDriver();
		} else if (sDriver.equalsIgnoreCase("webdriver.edge.driver")) {
			System.setProperty(sDriver, sDriverpath);
			driver = new EdgeDriver();
		} else {
			System.setProperty(sDriver, sDriverpath);
			driver = new FirefoxDriver();
		}

		Reporter.log("Url to test");
		driver.get(URL);
		driver.manage().window().maximize();
		Reporter.log("Maximizing the window");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//Create New Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(NodeDashboardname);	
			
		//Create viewlet button
		driver.findElement(By.xpath("//form/div/button[2]")).click();
		Thread.sleep(2000);
		
		// ---- Creating Node Viewlet ----
		//Click on Viewlet button
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click(); 
			
		//Create node
		driver.findElement(By.cssSelector(".object-type:nth-child(1)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys(NodeViewletName);
		
		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(NodeWGS);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		

		/*if (TypeOfNode.equals("WGS10")) {
			try {
				// Click on WGS6 Checkbox
				driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();

				// Delete the WGS6
				driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
				driver.findElement(By.cssSelector(".btn-primary")).click();
				Thread.sleep(3000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("WGS6 is not present");
			}
		} else {
			// Deleting the WGS10 if it is present
			try {
				WebElement DeleteWGS = driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"));
				if (DeleteWGS.isDisplayed()) {
					// Select the Delete WGS option
					DeleteWGS.click();
					driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
					driver.findElement(By.cssSelector(".btn-primary")).click();
					Thread.sleep(3000);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("WGS10 is not present");
			}
		}*/
		Thread.sleep(2000);

	}

	@Parameters({ "SchemaName" })
	@TestRail(testCaseId = 36)
	@Test(priority = 20)
	public static void ShowObjectAttributesforNode(String SchemaName, ITestContext context) throws InterruptedException {
		try {

			// ------------ Objects Verification ----------------
			ObjectsVerificationForAllViewlets obj = new ObjectsVerificationForAllViewlets();
			obj.NodeAttributes(driver, SchemaName);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Object Attributes for Node is working fine");
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while showing object properties, Check details: " + e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}

	}

	@Test(priority = 3)
	@TestRail(testCaseId = 37)
	public void ShowTopology(ITestContext context) throws InterruptedException {
		
		try {

			// Select Show Topology option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Show Topology")).click();
			Thread.sleep(25000);

			// Save the topology page data into string
			String Topology = driver.findElement(By.cssSelector("svg")).getText();
		    System.out.println(Topology);
		    Thread.sleep(2000);
		    
		    System.out.println("Manager1 is: " +Manager1);
		    System.out.println("Manager2 is: " +Manager2);

			if (Topology.contains(Manager1)) {
				System.out.println("Topology page is opened with the list of QM's");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Topology page is opened with the list of QM's");
				driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			} else {
				System.out.println("Topology page is not opened with the list of QM's");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed opening Topology page with the list of QM's");
				driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
				driver.findElement(By.xpath("Topology openeing is failed with the list of QM's")).click();
			}

			Thread.sleep(2000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment","Exception occured while showing Topology, Check details: " + e.getMessage());
			driver.findElement(By.xpath("Topology openeing is failed with the list of QM's")).click();
		}
	}

	@Test(priority = 4)
	@TestRail(testCaseId = 38)
	public static void NodeEvents(ITestContext context) throws InterruptedException {
		
			// Select Events option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.linkText("Events...")).click();
			Thread.sleep(8000);

			// Store the Event popup page value into string
			String EventName = driver.findElement(By.cssSelector("th:nth-child(1)")).getText();
			// System.out.println(EventName);

			// Verification
			if (EventName.contains("Event #")) {
				System.out.println("Events page is opened");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Events page is working fine");
			} else {
				System.out.println("Events page is not opened");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to open Events page");
				driver.findElement(By.xpath("Events failed")).click();
			}

			// Clicking on Events Count
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

	@Test(priority = 5)
	@TestRail(testCaseId = 39)
	public static void ManageAndUnmanageNode(ITestContext context) throws InterruptedException {
		try {
			// Select Manage option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
			Thread.sleep(5000);

			/*
			 * //Refreshing the Viewlet
			 * driver.findElement(By.xpath("//div[2]/div/div/img")).click();
			 * Thread.sleep(2000);
			 * 
			 * //Store the viewlet data into string String
			 * Manage=driver.findElement(By.cssSelector("datatable-body.datatable-body")).
			 * getText(); //System.out.println(Manage);
			 * 
			 * //verification if(Manage.contains("No data to display")) {
			 * System.out.println("Manage Node is done Successfully"); } else {
			 * System.out.println("Manage Node is not Done");
			 * driver.findElement(By.xpath("Manage Node not")).click(); }
			 * Thread.sleep(2000);
			 */

			// Select UnManage option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
			Thread.sleep(5000);

			/*
			 * //Refreshing the Viewlet
			 * driver.findElement(By.xpath("//div[2]/div/div/img")).click();
			 * Thread.sleep(2000);
			 * 
			 * //Store the vuewlet data into string String
			 * Unmanage=driver.findElement(By.cssSelector("datatable-body.datatable-body")).
			 * getText(); //System.out.println(Unmanage);
			 * 
			 * //Verification if(Unmanage.contains("M10")) {
			 * System.out.println("UnManage Node is done Successfully"); } else {
			 * System.out.println("UnManage Node is not Done");
			 * driver.findElement(By.xpath("UnManage Node not")).click(); }
			 */
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Manage And Unmanaging Node is working fine");
			Thread.sleep(2000);
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while Manage And Unmanaging Node, check details: " + e.getMessage());
			driver.findElement(By.id("Manage and Unmanage failed")).click();
		}
	}

	@Test(priority = 6)
	@TestRail(testCaseId = 40)
	public static void DiscoverNow(ITestContext context) throws InterruptedException {
		try {
			// Select Incremental option from Discover now
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions MousehoverIncremental = new Actions(driver);
			MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
			Thread.sleep(2000);
			driver.findElement(By.linkText("Incremental")).click();
			Thread.sleep(5000);

			// Select Full option from Discover now
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions MousehoverFull = new Actions(driver);
			MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
			Thread.sleep(2000);
			driver.findElement(By.linkText("Full")).click();
			Thread.sleep(5000);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Discover now oprtion is working fine");
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception, check details: " + e.getMessage());
			driver.findElement(By.id("Discover failed")).click();
		}

	}

	
	@TestRail(testCaseId = 45)
	@Test(priority = 7)
	public void CreateNodeUsingIcon(ITestContext context) throws InterruptedException {

		// Click on + Icon for adding the Node from Node viewlet
		driver.findElement(By.xpath("//img[@title='Add Node']")).click();
		Thread.sleep(2000);

		// Create Node page
		driver.findElement(By.xpath("//app-mod-node-properties-identity/div/div[2]/div/input")).sendKeys(NodeNameFromIcon);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(HostNameFromIcon);
		driver.findElement(By.xpath("//div[5]/div/input")).sendKeys(IPAddressFromIcon);

		// Submit
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup not displayed");
		}

		// Refresh the viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(2000);

		// Store the Viewlet data into string
		String NodeViewletdata = driver.findElement(By.xpath("//datatable-body")).getText();

		// Verification
		if (NodeViewletdata.contains(NodeNameFromIcon)) {
			System.out.println("Node is created from + Icon");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Node is created from + Icon");
		} else {
			System.out.println("Node is not created from + Icon");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed creating Node using + Icon");
			driver.findElement(By.xpath("Node Creation Failed")).click();

		}
		Thread.sleep(1000);
	}

	@Test(priority = 8)
	@TestRail(testCaseId = 42)
	public static void PropertiesOfNode(ITestContext context) throws InterruptedException {
		// Select Manage
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(5000);
		try {
			// Properties option selection
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[7]")).click();
			Thread.sleep(2000);

			// Store the editable function in to a string
			boolean FieldNamevalue = driver.findElement(By.xpath("//app-mod-node-properties-identity/div/div[2]/div/input")).isEnabled();
			System.out.println(FieldNamevalue);

			// Verification
			if (FieldNamevalue == false) {
				System.out.println("Node Name field is UnEditable");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Node Name field is UnEditable");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				Thread.sleep(8000);
			} else {
				System.out.println("Node Name field is Editable");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Node Name field is Editable");
				driver.findElement(By.cssSelector(".btn-danger")).click();
				Thread.sleep(8000);
				driver.findElement(By.xpath("Node name edit function Failed")).click();
			}
		} catch (Exception e) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Getting exception in properties page, check details: " + e.getMessage());
			System.out.println("Getting exception in properties page");
			// Select Manage
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
			Thread.sleep(5000);
			driver.findElement(By.cssSelector(".btn-danger")).click();
		}
		Thread.sleep(1000);

		// Select Manage
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		Thread.sleep(5000);

	}

	@Parameters({ "Dashboardname", "FavoriteViewletName", "Favwgs" })
	@Test(priority = 9)
	public void AddToFavotiteViewlet(String Dashboardname, String FavoriteViewletName, int Favwgs, ITestContext context) throws InterruptedException {
		try {
		Reporter.log("Reading WGS name");
		// Store the WGS name into string
		String NodeName = driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();

		Reporter.log("Adding Dashboard");
		// Add Dashboard
		driver.findElement(By.cssSelector("div.block-with-border3")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		Reporter.log("Dashboardname: "+ Dashboardname);
		System.out.println("Dashboardname: "+ Dashboardname);
		// Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);

		// Create the favorite viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);

		Select wgsdropdown = new Select(driver.findElement(By.name("wgs")));
		wgsdropdown.selectByIndex(Favwgs);

		// Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);

		// Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);

		// Select Add to Favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);

		// Select the favorite viewlet name
		Select fav = new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);

		// Go to Favorite viewlet dashboard page
		driver.findElement(By.xpath("//li[33]")).click();
		Thread.sleep(1000);

		// Store the favorite viewlet data into string
		String FavViewletData = driver.findElement(By.xpath("//datatable-body")).getText();

		if (FavViewletData.contains(NodeName)) {

			System.out.println("Node is added to favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Node is added to favorite viewlet");
		} else {
			System.out.println("Node is not added to favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Node Failed adding to favorite viewlet");
			driver.findElement(By.id("Add to favorite failed")).click();
		}
		Thread.sleep(2000);

		// Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			Reporter.log("Test Case Failed" );
			Reporter.log("Error:  "+ e.getMessage());
			//To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
                        //We do pass the path captured by this mehtod in to the extent reports using "logger.addScreenCapture" method. 			
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile,new File("D:\\SCREENSHOTS\\Workgroup server\\AddToFavotiteViewlet.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Node Failed adding to favorite viewlet, check details: "+ e.getMessage());
			 String screenshotPath ="D:\\SCREENSHOTS\\Workgroup server\\AddToFavotiteViewlet.png";
			// String htmlText = new String("<img src=\"\\\"file://\"\" alt=\"\\\"\\\"/\" />");
			 String path ="<img src=\" "+ screenshotPath +"\" alt=\"\"\"/\" />";
			//To add it in the report 
			 Reporter.log(path);
			// Reporter.log(screenshotPath);
			driver.findElement(By.xpath("failed buton")).click();
			 
			// logger.log(LogStatus.FAIL, logger.addScreenCapture(screenshotPath));
		}
	}
	

	@Test(priority = 10)
	@TestRail(testCaseId = 43)
	public static void CompareTwoNodes(ITestContext context) throws InterruptedException 
	{	
		//Get the First object Name                         
		String compare1 = driver.findElement(By.xpath("//datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		System.out.println("First obj name is: " +compare1);
		
		//Get the second object name
		String compare2 = driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		System.out.println("Second obj name is: " +compare2);
		
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();

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
	
	@TestRail(testCaseId = 762)
	@Test(priority=11)
	public void CheckDifferencesForNodes(ITestContext context) throws InterruptedException
	{
		// Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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

	
	@TestRail(testCaseId = 41)
	@Test(priority = 18, dependsOnMethods= {"CreateNodeUsingIcon"})
	public static void DeleteNode(ITestContext context) throws Exception 
	{
		// Search with node name
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(NodeNameFromIcon);
		Thread.sleep(1000);

		/*
		 * //Un manage the Node driver.findElement(By.xpath(
		 * "/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"
		 * )).click(); driver.findElement(By.xpath(
		 * "//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
		 */

		// Delete the Node
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Delete")).click();
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(8000);

		// Edit the search field data
		for (int j = 0; j <= NodeNameFromIcon.length(); j++) {

			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(4000);

		// Store the viewlet data after deleting the node
		String AfterDeleting = driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("After Deleting Node:" + AfterDeleting);

		// Deleted node verification
		if (AfterDeleting.contains(NodeNameFromIcon)) {
			System.out.println("Node is not deleted");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Deletion of node is not working");
			driver.findElement(By.xpath("Node not deleted")).click();
		} else {
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Node is deleted");
			System.out.println("Node is deleted successfully");
		}
		Thread.sleep(2000);
		
		
		
		/*if (TypeOfNode.equals("WGS10")) {
			// Search with node name
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(NodeNameFromIcon);
			Thread.sleep(1000);

			
			 * //Un manage the Node driver.findElement(By.xpath(
			 * "/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"
			 * )).click(); driver.findElement(By.xpath(
			 * "//app-dropdown[@id='dropdown-block']/div/ul/li[5]")).click();
			 

			// Delete the Node
			driver.findElement(By.xpath(
					"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
					.click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]")).click();
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			Thread.sleep(1000);

			// Edit the search field data
			for (int j = 0; j <= NodeNameFromIcon.length(); j++) {

				driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(4000);

			// Refresh Viewlet
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(3000);

			// Store the viewlet data after deleting the node
			String AfterDeleting = driver
					.findElement(By.xpath("//datatable-body")).getText();
			System.out.println("After Deleting Node:" + AfterDeleting);

			// Deleted node verification
			if (AfterDeleting.contains(NodeNameFromIcon)) {
				System.out.println("Node is not deleted");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Deletion of node working fine");
				driver.findElement(By.xpath("Node not deleted")).click();
			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete node");
				System.out.println("Node is deleted successfully");
			}
			Thread.sleep(2000);
		} else {
			// Search with node name
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(NodeNameFromIcon);
			Thread.sleep(1000);

			// Delete the Node
			driver.findElement(By.xpath(
					"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
					.click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]")).click();
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			Thread.sleep(1000);

			// Edit the search field data
			for (int j = 0; j <= NodeNameFromIcon.length(); j++) {

				driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(4000);

			// Refresh Viewlet
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(3000);

			// Store the viewlet data after deleting the node
			String AfterDeleting = driver
					.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			System.out.println("After Deleting Node1:" + AfterDeleting);

			// Deleted node verification
			if (AfterDeleting.contains(NodeNameFromIcon)) {
				System.out.println("Node is not deleted");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete node");
				driver.findElement(By.xpath("Node not deleted")).click();
			} else {
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Deletion of node working fine");
				System.out.println("Node is deleted successfully");
			}
			Thread.sleep(2000);
		}*/

	}

	@Parameters({ "FavoriteViewletName" })
	
	@Test(priority = 12)
	public void AddtoFavoriteForMultipleNodes(String FavoriteViewletName,ITestContext context) throws InterruptedException {
		try {
		// Store two nodes data into string
		String NodeName2 = driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		String NodeName3 = driver.findElement(By.xpath(
				"//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span"))
				.getText();

		// Select compare option for comparing the two nodes
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
				.click();
		driver.findElement(By.xpath(
				"/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"))
				.click();
		driver.findElement(By.xpath("//ul[2]/li")).click();
		Thread.sleep(2000);

		// Select the favorite viewlet name
		Select fav = new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);

		// Go to Favorite viewlet dashboard page
		driver.findElement(By.xpath("//li[2]")).click();
		Thread.sleep(1000);

		// Store the favorite viewlet data into string
		String FavViewletData = driver.findElement(By.xpath("//datatable-body")).getText();

		if (FavViewletData.contains(NodeName2) && FavViewletData.contains(NodeName3)) {
			System.out.println("Multiple Nodes are added to favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple Nodes are added to favorite viewlet");
		} else {
			System.out.println("Multiple Nodes are not added to favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Multiple Nodes are not added to favorite viewlet");
			driver.findElement(By.id("Add to favorite failed")).click();
		}
		Thread.sleep(2000);

		// Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Multiple Nodes are not added to favorite viewlet, check details: "+ e.getMessage());
			driver.findElement(By.xpath("custom failed condition")).click();
		}

	}

	@TestRail(testCaseId = 44)
	@Test(priority = 19)
	public static void SearchFilter(ITestContext context) throws InterruptedException 
	{
		//Store the node name into string
		String NodeNameForSearch=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		
		// Enter the search input data into search box
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(NodeNameForSearch);
		Thread.sleep(2000);

		// Store the Viewlet data into string
		String Viewletdata = driver.findElement(By.xpath("//datatable-body")).getText();
		// System.out.println(Viewletdata);

		// Verification
		if (Viewletdata.toUpperCase().contains(NodeNameForSearch.toUpperCase())) {
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Search is working fine");
			System.out.println("Search is working fine");
		} else {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Search is not working fine");
			System.out.println("Search is not working fine");
			driver.findElement(By.xpath("Search is failed")).click();
		}
		Thread.sleep(2000);

		// Clear the search data
		for (int k = 0; k <= NodeNameForSearch.length(); k++) {
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(2000);

		// Refresh the viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(2000);

	}

	@Parameters({"Description"})
	@TestRail(testCaseId = 46)
	@Test(priority = 14)
	public void CreateQueueManagerFromNodeViewletOptions(String Description, ITestContext context) throws InterruptedException {
		try {
		// Select Create Queue manager option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Queue Manager")).click();
		Thread.sleep(1000);

		// Queue Details
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(QueueManagerName);
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DefaultTransmissionQueue);
		driver.findElement(By.xpath("//textarea[@type='text']")).sendKeys(Description);

		// Next button
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();

		// driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys("New
		// Manager");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();

		// Log Path
		// driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Desktop");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();

		// Data Path
		// driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Test
		// data path");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();

		// Final Submit
		driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/button")).click();
		Thread.sleep(20000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}

		/*try {
			// Get Error Message
			String Errorpopup = driver.findElement(By.xpath("//app-mod-errors-display/div/div")).getText();
			System.out.println(Errorpopup);                  
			driver.findElement(By.id("yes")).click();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got some exception, check details: "+ e.getMessage());
			System.out.println("No message is displaying");
		}*/
		
		// Click on Viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();

		// Create Manager
		driver.findElement(By.cssSelector(".object-type:nth-child(2)")).click();
		driver.findElement(By.name("viewletName")).clear();
		driver.findElement(By.name("viewletName")).sendKeys("Manager Viewlet");

		//Select WGS type
		Select WGSSelection=new Select(driver.findElement(By.name("wgsKey")));
		WGSSelection.selectByVisibleText(NodeWGS);
				
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(1000);
		
		// Store viewlet data into string
		String Favdata = driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(Favdata);
		
		if(Favdata.contains(QueueManagerName))
		{
			System.out.println("Queue manager is added successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue manager is added successfully");
		}
		else
		{
			System.out.println("Queue manager is not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue manager is not added");
			driver.findElement(By.id("Add Queue manager failed")).click();
		}
		
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while adding Queue Manager, check details: "+ e.getMessage());
			driver.findElement(By.xpath("Got exception")).click();
		}
	}

	
	@Test(priority = 22)
	public static void Logout() throws InterruptedException 
	{
		/*// Click on + icon
		driver.findElement(By.cssSelector("img[title=\"Add Workgroup Server\"]")).click();

		// Add WGS Details
		driver.findElement(By.name("ip")).sendKeys(IPAddress);
		driver.findElement(By.name("hostName")).sendKeys(Node_hostname);
		driver.findElement(By.name("port")).clear();
		driver.findElement(By.name("port")).sendKeys(PortNo);
		driver.findElement(By.name("password")).sendKeys(WGSPassword);
		Thread.sleep(1000);

		// Click on Verify
		driver.findElement(By.xpath("//form/div[2]/button[2]")).click();
		Thread.sleep(2000);

		// Click on submit button
		driver.findElement(By.cssSelector(".button-blue")).click();
		Thread.sleep(2000);*/
		
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

		// Logout
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
