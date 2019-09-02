package ApodGUI;
import java.io.File;
import java.io.IOException;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;


public class WorkgroupServer_demo
{
	static WebDriver driver;
	
	@Test
	@Parameters({"sDriver", "sDriverpath", "URL", "uname", "password", "PortNo", "ScreenshotPath"})
	public void Login(String sDriver, String sDriverpath, String URL, String uname, String password, String PortNo, String ScreenshotPath) throws Exception
	{
		try
		{
			Settings.read();
			String urlstr=Settings.getSettingURL();
			URL= urlstr+URL;
		//Select the required browser for running the script
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new ChromeDriver();
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
			System.setProperty(sDriver, sDriverpath);
			FirefoxOptions options=new FirefoxOptions();
			options.setCapability("marionette", false);
			driver=new FirefoxDriver(options);
		}
		
		//Enter the URL into browser and Maximize the browser 
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Login
		Reporter.log("Enter user name");
		driver.findElement(By.id("username")).sendKeys(uname);
		Reporter.log("Enter Password");
		driver.findElement(By.id("password")).sendKeys(password);
		Reporter.log("Click on Login button");
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(2000);
		
		//Delete the WGS10 if present
		this.DeleteWorkgroup(PortNo, ScreenshotPath);
		Thread.sleep(1000);
		}
		catch (Exception e)
		{
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"Loginpage.png"));
		}
	}
	
	
	@Test(priority=1)
	@Parameters({"IPAddress", "HostName", "PortNo", "WGSPassword", "VerificationData", "ScreenshotPath"})
	public void AddWorkgroupFromPlusIcon(String IPAddress, String HostName, String PortNo, String WGSPassword, String VerificationData, String ScreenshotPath) throws Exception
	{
			
		try
		{
		//Click on + icon
		Reporter.log("Click on Add + Icon present in the Workgroup server viewlet");
		driver.findElement(By.cssSelector("img[title=\"Add Workgroup Server\"]")).click();
		
		//Add WGS Details
		Reporter.log("</br>Enter IP address");
		driver.findElement(By.name("ip")).sendKeys(IPAddress);
		Reporter.log("</br>Enter Host name");
		driver.findElement(By.name("hostName")).sendKeys(HostName);
		Reporter.log("</br>Enter port no");
		driver.findElement(By.name("port")).clear();
		driver.findElement(By.name("port")).sendKeys(PortNo);
		Reporter.log("</br>Enter password");
		driver.findElement(By.name("password")).sendKeys(WGSPassword);
		Thread.sleep(1000);
		
		//Click on Verify
		Reporter.log("</br>Click on Verify");
		driver.findElement(By.xpath("//form/div[2]/button[2]")).click();
		Thread.sleep(2000);
		
		//Click on submit button
		Reporter.log("</br>Click on Submit button");
		driver.findElement(By.cssSelector(".button-blue")).click();
		Thread.sleep(2000);
		
		//Store the Viewlet data into string
		Reporter.log("</br> Store the viewlet data");
		String ViewletData=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
		//System.out.println(ViewletData);
		
		//Verification Condition
		Reporter.log("</br>Verification of Viewlet data");
		if(ViewletData.contains(VerificationData))
		{
			System.out.println("Workgroup server is added successfylly");
			Reporter.log("</br>Workgroup server is added successfylly");
		}
		else
		{
			System.out.println("Workgroup Server is not added");
			Reporter.log("</br>Failed to add Workgroup server");
			driver.findElement(By.xpath("Condition failed")).click();
		}
		}
		catch (Exception e)
		{
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"AddWorkgroupserver.png"));
			System.out.println("Exception Occured when adding the work group server");
			Reporter.log("</br>Exception Occured when adding the work group server");
			//Close the popup page
			driver.findElement(By.cssSelector(".g-button-red")).click();
			driver.findElement(By.id("Add failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"WGSPassword", "ChangedHostName", "ScreenshotPath"})
	@Test(priority=2)
	public static void EditWorkgroup(String WGSPassword, String ChangedHostName, String ScreenshotPath) throws Exception
	{
		try
		{
		//Select the Edit WGS option
		Reporter.log("Editing workgroup server, Click on Workgroup server Checkbox");
		WebElement EditWGS=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"));
		EditWGS.click();
		Reporter.log("</br>Click on Edit Work group server link");
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
		Thread.sleep(2000);
		
		//Change Local host Name
		Reporter.log("</br>Updating Localhost Name");
		driver.findElement(By.name("hostName")).clear();
		driver.findElement(By.name("hostName")).sendKeys(ChangedHostName);
		Thread.sleep(2000);
		Reporter.log("</br>Update password");
		driver.findElement(By.name("password")).sendKeys(WGSPassword);
		
		//Click on Verify
		Reporter.log("</br>Click on Verify button");
		driver.findElement(By.xpath("//form/div[2]/button[2]")).click();
		Thread.sleep(2000);
		
		//Click on Submit button
		Reporter.log("Click on submit button");
		driver.findElement(By.cssSelector(".button-blue")).click();
		Thread.sleep(3000);
		
		//Store the Host name into string
		Reporter.log("</br>Store host name into string verify");
		String HostName=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		//System.out.println(HostName);
		
		//Verification condition
		Reporter.log("</br>Verifying updated workgroup server");
		if(HostName.equalsIgnoreCase(ChangedHostName))
		{
			System.out.println("Updating Workgroup server is working fine");
			Reporter.log("</br>Workgroup server updated successfully");
		}
		else
		{
			System.out.println("Updating Workgroup server is not working fine");
			Reporter.log("</br>Failed to update workgroup server");
			driver.findElement(By.xpath("condition failed")).click();
			
		}
		}
		catch (Exception e)
		{
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"EditWorkgroupserver.png"));
			System.out.println("Exception Occured when Editing the work group server");
			Reporter.log("</br>Exception Occured when Editing the work group server");
			//Close the popup page
			driver.findElement(By.cssSelector(".g-button-red")).click();
			
			driver.findElement(By.id("Failed condition")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"PortNo", "ScreenshotPath"})
	@Test(priority=4)
	public void DeleteWorkgroup(String PortNo, String ScreenshotPath) throws Exception
	{
		try {
		Reporter.log("Checking of Second WGS checkbox");
		WebElement DeleteWGS=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input"));
		if(DeleteWGS.isDisplayed())
		{
			//Select the Delete WGS option
			Reporter.log("</br>Click on Second WGS checkbox");
			DeleteWGS.click();
			Reporter.log("</br>Click on delete WGS link");
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Reporter.log("</br>Click on Conformation OK button");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(3000);
			
			//Store the Viewlet data into string
			Reporter.log("</br>Store the WGS viewlet data into string");
			String WGSServerData=driver.findElement(By.xpath("//datatable-body")).getText();
			//System.out.println(WGSServerData);
			
			//Verification of delete work group server
			Reporter.log("</br>Verification of Delete condition");
			if(WGSServerData.contains(PortNo))
			{
				System.out.println("WGS is not deleted");
				driver.findElement(By.xpath("Deleting failed")).click();
			}
			else
			{
				System.out.println("WGS is deleted successfully");
			}
			Thread.sleep(1000);
		}
		}
		catch (Exception e) 
		{
            System.out.println("Second WGS is not present");
            
            //Take screen shot
            File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"EditWorkgroupserver.png"));
			
			//Fail the method
			driver.findElement(By.id("Failed")).click();
        }
	}
	
	@Test(priority=3)
	public static void ConnectIcon() throws InterruptedException
	{
		//Select the Edit WGS option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
		                            
		//Change password
		driver.findElement(By.name("port")).clear();
		driver.findElement(By.name("port")).sendKeys("4011");
		driver.findElement(By.cssSelector(".button-blue")).click();
		Thread.sleep(3000);
		
		//Store the Connection status into string
		String ConnectionFail=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(ConnectionFail);
		
		//Verification condition
		if(ConnectionFail.equalsIgnoreCase("Not Connected"))
		{
			System.out.println("Work Group Server connection failed is successfully done");
		}
		else
		{
			System.out.println("Work Group Server connection failed");
		}
		
		//Click on Connection Icon
		driver.findElement(By.cssSelector("img.settings-image")).click();
		Thread.sleep(3000);
		
		//Connection Verification
		String ConnectionPass=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		if(ConnectionPass.equalsIgnoreCase("Connected"))
		{
			System.out.println("Work Group Server connected");
		}
		else
		{
			System.out.println("Work Group Server not connected");
		}
		Thread.sleep(1000);
		
		/*//Click on Edit 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
		
		//Change password
		driver.findElement(By.name("port")).clear();
		driver.findElement(By.name("port")).sendKeys("4010");
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".button-blue")).click();
		Thread.sleep(3000);*/
	}
	
	@Parameters({"NodeName", "HostName", "IPAddress", "PortNumber", "ScreenshotPath"})
	@Test(priority=4)
	public static void AddNode(String NodeName, String HostName, String IPAddress, String PortNumber, String ScreenshotPath) throws InterruptedException, IOException
	{
		try
		{
		//Click on checbox and Select the create node option
		Reporter.log("Click on WGS checkbox to select node option");
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehourNode=new Actions(driver);
		Reporter.log("</br>Click on Node option");
		MousehourNode.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]/ul/li")).click();
		
		//Create Node page
		//driver.findElement(By.xpath("(//input[@type='text'])[4]")).click();
		Reporter.log("</br>Enter Node name");
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NodeName);
		Reporter.log("</br>Enter Host name");
		driver.findElement(By.xpath("(//input[@type='text'])[4]")).sendKeys(HostName);
		Reporter.log("</br>Enter IP address");
		driver.findElement(By.xpath("(//input[@type='text'])[5]")).sendKeys(IPAddress);
		
		//port number
		Reporter.log("</br>Enter Port number");
		driver.findElement(By.xpath("(//input[@type='text'])[6]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[6]1121121")).sendKeys(PortNumber);
		
		
		//Submit
		Reporter.log("</br>Click on submit button");
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Refresh the Node viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);
		
		//Store the viewlet data into string
		String NodeData=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of node is added or not
		if(NodeData.contains(NodeName))
		{
			System.out.println("Node is created successfully");
		}
		else
		{
			System.out.println("Node is not created");
			driver.findElement(By.xpath("Node creation failed")).click();
		}
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"AddNode.png"));
			Reporter.log("</br>Exception occured when adding Node from Workspace page</br>");
			System.out.println("Exception occured when adding Node from Workspace page");
			
			//Close the popup page
			driver.findElement(By.cssSelector(".btn-danger")).click();
			
			String screenshotPath =ScreenshotPath+"\\AddNode.png";
			// String htmlText = new String("<img src=\"\\\"file://\"\" alt=\"\\\"\\\"/\" />");
			 String path ="<img src=\" "+ screenshotPath +"\" alt=\"\"\"/\" />";
			//To add it in the report 
			 Reporter.log(path);
			
			//Fail condition
			driver.findElement(By.id("Node failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"ConnectionInstanceName", "RemoteQueueManagerName", "ConnectionName", "CommandQueueName", "ChannelName", "ScreenshotPath"})
	@Test(priority=5)
	public void AddRemoteQueueManager(String ConnectionInstanceName, String RemoteQueueManagerName, String ConnectionName, String CommandQueueName, String ChannelName, String ScreenshotPath) throws InterruptedException, IOException
	{
		try
		{
		//Select Remote queue manager option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehour=new Actions(driver);
		Mousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]/ul/li[2]/a")).click();
		Thread.sleep(1000);
		
		//Click on Add button
		driver.findElement(By.xpath("//div[2]/div/div/button")).click();
		
		//Queue manager connection instance
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys(ConnectionInstanceName);
		
		//Queue manager name
		driver.findElement(By.name("qmgrName")).clear();
		driver.findElement(By.name("qmgrName")).sendKeys(RemoteQueueManagerName);
		
		//Goto communication Tab
		Reporter.log("</br>Click on Communication tab");
		driver.findElement(By.linkText("Communication")).click();
		
		//Connection name
		Reporter.log("</br>Enter Connection name");
		driver.findElement(By.name("connName")).clear();
		driver.findElement(By.name("connName")).sendKeys(ConnectionName);
		
		//command Queue Name
		Reporter.log("</br>Select queue name from dropdown");
		Select queue=new Select(driver.findElement(By.name("references")));
		queue.selectByVisibleText(CommandQueueName);
		
		//Channel Name
		Reporter.log("</br>Enter Channel name");
		driver.findElement(By.name("channelName")).clear();
		driver.findElement(By.name("channelName")).sendKeys(ChannelName);
		
		//click on OK button
		Reporter.log("</br>Click on OK button");
		driver.findElement(By.xpath("//app-mod-remote-queue-manager-options/div/div[2]/div/div/div/button")).click();
		Thread.sleep(2000);
		
		//Store the Queue managers into string
		Reporter.log("</br>Store the Queue managets into string");
		String Queuemanagers=driver.findElement(By.xpath("//app-mod-remote-queue-manager-connections/div/div/div/div")).getText();
		
		//Verification
		Reporter.log("</br>Verification of Remote Queue manager");
		if(Queuemanagers.contains(RemoteQueueManagerName))
		{
			System.out.println("Remote Queue Manager is added successfully");
		}
		else
		{
			System.out.println("Remote Queue Manager is not added");
			driver.findElement(By.xpath("Queue manager add option is failed")).click();
		}
		Thread.sleep(1000);
			
		//Close the remote queue managers connection popup page
		Reporter.log("Click on OK button in remote queue manager connection popup page");
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"AddRemoteQueueManager.png"));
			
			System.out.println("Exception occured when Adding remote Queue manager");
			
			//close the popups
			boolean popup2=driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).isDisplayed();
			if(popup2)
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
			else			
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
			
		}
		Thread.sleep(1000);
					
	}
	
	@Parameters({"NewConnectionName", "ScreenshotPath"})
	@Test(priority=6)
	public void ModifyRemoteQueueManager(String NewConnectionName, String ScreenshotPath) throws InterruptedException, IOException
	{
		try
		{
		//Select Remote queue manager option
		Reporter.log("Select WGS checkbox");
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehour=new Actions(driver);
		Mousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]"))).perform();
		Reporter.log("Click on option Modify remote queue manager");
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]/ul/li[2]/a")).click();
		Thread.sleep(1000);
		
		//Click on Modify button
		Reporter.log("Click on Modify button");
		driver.findElement(By.xpath("//div[2]/div/div[2]/button")).click();
		
		//Goto communication Tab
		Reporter.log("Click on Communication Tab");
		driver.findElement(By.linkText("Communication")).click();
		
		//Connection name
		Reporter.log("Update the Connection name");
		driver.findElement(By.name("connName")).clear();
		driver.findElement(By.name("connName2121212121")).sendKeys(NewConnectionName);
		
		//click on OK button
		Reporter.log("Click on OK button");
		driver.findElement(By.xpath("//app-mod-remote-queue-manager-options/div/div[2]/div/div/div/button")).click();
		Thread.sleep(2000);
		
		//Store the connection ip into string after modifying the name
		Reporter.log("Store the Connection name into string");
		String ChangedConnectionip=driver.findElement(By.xpath("//div[2]/div/table/tbody/tr/td[2]")).getText();
		
		Reporter.log("Verification of Modify button");
		if(ChangedConnectionip.equalsIgnoreCase(NewConnectionName))
		{
			System.out.println("Remote Queue modification is done successfully");
		}
		else
		{
			System.out.println("Remote Queue modification is failed");
			driver.findElement(By.xpath("Modification failed")).click();
		}
		
		//Close the remote queue managers connection popup page
		Reporter.log("Click on OK button in Remote Queue managers Connection page");
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"EditRemoteQueueManager.png"));
			
			System.out.println("Exception occured when modifying remote Queue manager");
			
			//close the popups
			boolean popup2=driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).isDisplayed();
			if(popup2)
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
			else			
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"DeleteManagerName", "ScreenshotPath"})
	@Test(priority=7)
	public void DeleteRemoteQueueManager(String DeleteManagerName, String ScreenshotPath) throws InterruptedException, IOException
	{
		try
		{
		//Select Remote queue manager option
		Reporter.log("Calling method to delete remoe queue manager, Click on WGS checkbox");
		Reporter.log("</br>Click on option Modify remote queue manager");
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehour=new Actions(driver);
		Reporter.log("</br>Click on option Delete remote queue manager");
		Mousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]/ul/li[2]/a")).click();
		Thread.sleep(1000);
		
		//Select the required Queue manager
		Reporter.log("</br>Select the Required Remote queue manager");
		driver.findElement(By.xpath("//td[contains(.,'"+ DeleteManagerName +"')]")).click();
		
		//Click on Delete
		Reporter.log("</br>Click on Delete button");
		driver.findElement(By.xpath("//div[3]/button1212121")).click();
		Reporter.log("</br>Click on OK button");
		driver.findElement(By.xpath("//app-mod-confirmation/div/div[2]/div/div/div/button")).click();
		
		//Store the Queue managers into string
		Reporter.log("</br>Store the RemoteQueue managers names into string");
		String Queuemanagers=driver.findElement(By.xpath("//app-mod-remote-queue-manager-connections/div/div/div/div")).getText();
		System.out.println(Queuemanagers);
		
		//Verification condition
		Reporter.log("</br>Verificaton Delete button");
		if(Queuemanagers.contains(DeleteManagerName))
		{
			System.out.println("Remote Queue Manager is not deleted");
			driver.findElement(By.xpath("Remote Queue Manager delete Failed")).click();
		}
		else
		{
			System.out.println("Remote Queue Manager is deleted");
		}
		Thread.sleep(2000);
		//Close the remote queue managers connection popup page
		Reporter.log("</br>Click on OK button in Remote Queue managers Connection page");
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		}
		catch (Exception e)
		{
			 //Reporter.log("Test case failed");
			 
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"DeleteRemoteQueueManager.png"));
			Reporter.log("</br>Exception occured when Deleting remote Queue manager</br>");
			System.out.println("Exception occured when Deleting remote Queue manager");
			
			//close the popups
			//driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			
			String screenshotPath =ScreenshotPath+"\\DeleteRemoteQueueManager.png";
			// String htmlText = new String("<img src=\"\\\"file://\"\" alt=\"\\\"\\\"/\" />");
			 String path ="<img src=\" "+ screenshotPath +"\" alt=\"\"\"/\" />";
			//To add it in the report 
			 Reporter.log(path);
			// Reporter.log("Stacktrace log: "+ e.getMessage());
			 
			
			//Fail condition
			driver.findElement(By.id("Failed Condition")).click();
		}
		Thread.sleep(1000);	         
	}
	
	@Parameters({"AgentInstanceName", "ServerName", "ServerURL", "ScreenshotPath"})
	@Test(priority=8)
	public void AddRemoteEMSManager(String AgentInstanceName, String ServerName, String ServerURL, String ScreenshotPath) throws InterruptedException, IOException
	{
		try
		{
		//Select Remote EMS manager option
		Reporter.log("Select WGS checkbox");
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehour=new Actions(driver);
		Reporter.log("Click on Remote EMS Queue manager");
		Mousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[3]/ul/li[3]")).click();
		Thread.sleep(1000);
		
		//Click on Add button
		Reporter.log("Click on OK button");
		driver.findElement(By.xpath("//div[2]/div/div/button")).click();
		
		//EMS Agent Instance Name
		Reporter.log("Enter Agent instaance name");
		driver.findElement(By.id("agentInstanceName")).clear();
		driver.findElement(By.id("agentInstanceName")).sendKeys(AgentInstanceName);
		
		//EMS Server name
		Reporter.log("Enter Server  instaance name");
		driver.findElement(By.id("serverName")).clear();
		driver.findElement(By.id("serverName")).sendKeys(ServerName);
		
		//Server URL
		Reporter.log("Enter server URL");
		driver.findElement(By.id("serverURL")).clear();
		driver.findElement(By.id("serverURL")).sendKeys(ServerURL);
		
		//click on OK button
		Reporter.log("Click on OK button");
		driver.findElement(By.xpath("//app-mod-remote-ems-manager-options/div/div[2]/div/div/div/button")).click();
		Thread.sleep(2000);
		
		//Store the EMS servers data into strin
		Reporter.log("Store the Remote EMS valus into string");
		String RemoteEMSserver=driver.findElement(By.xpath("//app-mod-remote-ems-manager-connections/div/div/div/div")).getText();
		
		//verification of Remote ems server
		Reporter.log("Verification of Remote EMS server");
		if(RemoteEMSserver.contains(AgentInstanceName) && RemoteEMSserver.contains(ServerName))
		{
			System.out.println("Remote EMS server is added");
		}
		else
		{
			System.out.println("Remote EMS server is not added");
			driver.findElement(By.id("Add EMS failed")).click();
		}
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"AddRemoteEMSManager.png"));
			
			//close the popups
			boolean popup2=driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).isDisplayed();
			if(popup2)
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
			else			
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
			
			//Failed Condition
			driver.findElement(By.id("Add Remote EMS failed")).click();
			
		}
		Thread.sleep(1000);
	}
	
	
	@Parameters({"UpdatedServerURL", "ScreenshotPath"})
	@Test(priority=9)
	public void ModifyRemoteEMSServer(String UpdatedServerURL, String ScreenshotPath) throws InterruptedException, IOException
	{
		try
		{
		//Click on Modify button
		Reporter.log("Click on Modify button");
		driver.findElement(By.xpath("//div[2]/div/div[2]/button")).click();
		
		//Server URL
		Reporter.log("Update Server URL");
		driver.findElement(By.id("serverURL")).clear();
		driver.findElement(By.id("serverURL")).sendKeys(UpdatedServerURL);
		
		//click on OK button
		Reporter.log("Click on OK button");
		driver.findElement(By.xpath("//app-mod-remote-ems-manager-options/div/div[2]/div/div/div/button")).click();
		Thread.sleep(2000);
		
		//Store the Server URL value into string
		Reporter.log("Store the UPdate URL into string");
		String URL=driver.findElement(By.xpath("//div[2]/div/table/tbody/tr/td[2]")).getText();
		
		Reporter.log("Verification of Remote EMS server");
		if(URL.equalsIgnoreCase(UpdatedServerURL))
		{
			System.out.println("Remote EMS server is modified");
		}
		else
		{
			System.out.println("Remote EMS server is not modified");
			driver.findElement(By.id("Modify failed")).click();
		}
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"EditRemoteEMSManager.png"));
			
			//close the popups
			boolean popup2=driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).isDisplayed();
			if(popup2)
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
			else			
			{
				driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			}
			
			//Failed Condition
			driver.findElement(By.id("Modify Remote EMS failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Parameters({"ServerName", "ScreenshotPath"})
	@Test(priority=10)
	public void DeleteRemoteEMSServer(String ServerName, String ScreenshotPath) throws InterruptedException, IOException
	{
		try
		{
		//Click on Server name
		Reporter.log("Select the required server name");
		driver.findElement(By.xpath("//td[contains(.,'"+ ServerName +"')]")).click();
		
		Thread.sleep(2000);
		
		//Click on Delete button
		Reporter.log("Click on Delete button");
		driver.findElement(By.xpath("//div[3]/button")).click();
		
		//Click on Confirmation yes button
		Reporter.log("Click on Confitamation Yes button");
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(2000);
		
		//Store the EMS servers data into string
		Reporter.log("Store the EMS servers into String");
		String RemoteEMSserver=driver.findElement(By.xpath("//app-mod-remote-ems-manager-connections/div/div/div/div")).getText();
		
		Reporter.log("Verification of Delete Remote EMS server");
		//verification of Remote ems server
		if(RemoteEMSserver.contains(ServerName))
		{
			System.out.println("Delete Remote EMS server is failed");
			driver.findElement(By.id("Delete failed")).click();
		}
		else
		{
			System.out.println("Remote EMS server is deleted");
		}
		Thread.sleep(1000);
		
		//Close the window
		Reporter.log("Click on OK button in Remote EMS queue manager popup page");
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(2000);
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"DeleteRemoteEMSManager.png"));
			
			//close the popups
			//driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
			
			//Failed Condition
			driver.findElement(By.id("Delete Remote EMS failed")).click();
		}
		
	}
	
	
	@Parameters({"Dashboardname", "FavoriteViewletName", "Favwgs"})
	@Test(priority=11)
	public void AddToFavoriteviewlet(String Dashboardname, String FavoriteViewletName, int Favwgs) throws InterruptedException
	{
		//Store the WGS name into string
		String WGSName=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		
		//Add Dashboard
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		
		//Create the favorite viewlet
		driver.findElement(By.cssSelector("button.g-button-blue.button-add")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		Select wgsdropdown=new Select(driver.findElement(By.name("wgs")));
		wgsdropdown.selectByIndex(Favwgs);
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(2000);
		
		//Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);
		
	    //Select Add to Favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul[2]/li")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		fav.selectByVisibleText(FavoriteViewletName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(1000);
		
		//Go to Favorite viewlet dashboard page
		driver.findElement(By.xpath("//li[2]")).click();
		Thread.sleep(1000);
		
		//Store the favorite viewlet data into string
		String FavViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
		
		if(FavViewletData.contains(WGSName))
		{
			System.out.println("WGS is added to favorite viewlet");
		}
		else
		{
			System.out.println("WGS is not added to favorite viewlet");
			driver.findElement(By.id("Add to favorite failed")).click();
		}
		Thread.sleep(2000);
		
		
	}
	
	@Parameters({"WGSSearchInputData", "IPAddress", "HostName", "PortNo", "WGSPassword", "VerificationData", "ScreenshotPath"})
	@Test(priority=12)
	public  void SearchFilter(String WGSSearchInputData, String IPAddress, String HostName, String PortNo, String WGSPassword, String VerificationData, String ScreenshotPath) throws Exception
	{
		try
		{
		//Back to Workspace page
		driver.findElement(By.xpath("//li/div/div")).click();
		Thread.sleep(1000);
				
		//Add New WGS10
		this.AddWorkgroupFromPlusIcon(IPAddress,  HostName,  PortNo,  WGSPassword,  VerificationData, ScreenshotPath);
		Thread.sleep(2000);
		
		//Enter the search data into search field
		Reporter.log("Enter Search data into search field");
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(WGSSearchInputData);
		Thread.sleep(2000);
				
		//Store the Viewlet data into String
		Reporter.log("Store the viewlet data into string");
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println(Viewletdata);
		
		//Split the Number of rows present in the viewlet
		String[] multiwords=Viewletdata.split("4010", 1);
		//System.out.println("The Rows are:" +Arrays.toString(multiwords)); 
		Thread.sleep(2000);
		
		//Check the each row contains the searched data or not
		Reporter.log("Verification of Search results");
	    for (String a : multiwords)
	    {            
	    if(a.toUpperCase().contains(WGSSearchInputData.toUpperCase()))
	    {
	       System.out.println("Search is working fine");
	    }
	    else
	    {
	       System.out.println("Search is not working fine");
	       driver.findElement(By.xpath("Search is failed")).click();
	    }
	        
	   }
	    Thread.sleep(2000);
	    //Clear the Search data
	    Reporter.log("Clearing the search field data");
	    driver.findElement(By.xpath("//input[@type='text']")).clear();
	    Thread.sleep(2000);
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"search.png"));
			
			driver.findElement(By.id("Failing the condition")).click(); 
		}
	}
	
	@Parameters({"ScreenshotPath"})
	@Test(priority=13)
	public void Logout(String ScreenshotPath) throws Exception
	{
		/*try
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
		Thread.sleep(1000);*/
		
		try
		{
		//Logout
		Reporter.log("Click on logout button");
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		driver.close();
		}
		catch (Exception e)
		{
			//Take screenshot on failure
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,new File(ScreenshotPath+"Logout.png"));
			
			driver.findElement(By.id("Failing the condition")).click(); 
		}
		
	}

}
