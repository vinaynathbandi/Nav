package ApodGUI;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import testrail.Settings;


public class ObjectsVerificationForAllViewlets 
{
	
	public void WGSAttributes(WebDriver driver, String schemaName, String WGSName) throws InterruptedException
	{
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
		List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div")); 
		String links[]=new String[Attributescolumn.size()];
   	    int k=1;
   	    StringBuilder ListOfAttributes = new StringBuilder();
   	    
   	    for (WebElement tdElement : Attributescolumn)
   	    {
   	    	//System.out.println(tdElement.getText());
	        links[k]=tdElement.getText();
	        String verify= tdElement.getText();
	        
	        if(!verify.isEmpty())
	        {
	        	String None= "None";
	        	
	        	if(!verify.contains(None))
	        	{
	        		ListOfAttributes.append(links[k]);
		    		ListOfAttributes.append(',');
	        	}
	        }
   	    }
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println(ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		int wgsindex=3;
		if(!WGSName.contains("MQM"))
		{
			wgsindex=4;
		}
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell["+ wgsindex +"]/div/span")).getText();
		Thread.sleep(2000);                       
		
		//------------- Create user Schema With the stored attributes ------- 
		//Create User Schema           
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
		driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
		//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
		Thread.sleep(1000);
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(2000); 
		
		/*//------ Apply filter with the node name ------------------
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select node value
		driver.findElement(By.xpath("//ng-select/div")).click();
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
				if(s.equals(name))
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
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Viewlet data is: " +finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Object Attributes")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";
   	    	 if(!verify.contains(None))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println("Attributes popup page values: " +Values);
		
		//Edit the search field data
	    for(int j=0; j<=name.length(); j++)
	    {
	    	
	    driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
	    }
	    Thread.sleep(4000);
		
		// ----- Compare both attribute values ------------
		if(Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
				
		//Refresh the Viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);
	}
	
	public void NodeAttributes(WebDriver driver, String SchemaName) throws InterruptedException
	{
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		//------- Get the Attributes names and Store into array -----------
		List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div")); 
		String links[]=new String[Attributescolumn.size()];
   	    int k=1;
   	    StringBuilder ListOfAttributes = new StringBuilder();
   	    
   	    for (WebElement tdElement : Attributescolumn)
   	    {
   	    	//System.out.println(tdElement.getText());
	        links[k]=tdElement.getText();
	        String verify= tdElement.getText();
	        
	        if(!verify.isEmpty())
	        {
	        	String None= "None";
	        	
	        	if(!verify.contains(None))
	        	{
	        		ListOfAttributes.append(links[k]);
		    		ListOfAttributes.append(',');
	        	}
	        }
   	    }
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println(ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		Thread.sleep(2000);                      
		
		//------------- Create user Schema With the stored attributes ------- 
		//Create User Schema           
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(SchemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
		driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
		//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
		Thread.sleep(1000);
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(2000); 
		
		//------ Apply filter with the node name ------------------
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select node value
		driver.findElement(By.xpath("//ng-select/div")).click();
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
				if(s.equals(name))
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
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		// --------------- Store the Attribute values into array ----------------
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";
   	    	 if(!verify.contains(None))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println(Values);
		
		// ----- Compare both attribute values ------------
		if(Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//Edit the search field data
	    for(int j=0; j<=name.length(); j++)
	    {
	    	
	    driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
	    }
	    Thread.sleep(4000);
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);
	}
	
	
		public void ManagerAttributes(WebDriver driver, String SchemaName, String Attributes, String WGSName) throws InterruptedException
		{	
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			//------- Get the Attributes names and Store into array -----------
			List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div")); 
			String links[]=new String[Attributescolumn.size()];
	   	    int k=1;
	   	    StringBuilder ListOfAttributes = new StringBuilder();
	   	    
	   	    for (WebElement tdElement : Attributescolumn)
	   	    {
	   	    	//System.out.println(tdElement.getText());
		        links[k]=tdElement.getText();
		        String verify= tdElement.getText();
		        
		        if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!verify.contains(None))
		        	{
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
	   	    }
	   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			int ManagerName_Index=3;
			if(!WGSName.contains("MQM"))
			{
				ManagerName_Index=4;
			}
			//Store the name into string
			String name=driver.findElement(By.xpath("//datatable-body-cell["+ ManagerName_Index +"]/div/span")).getText();
			Thread.sleep(2000);
			
			//------------- Create user Schema With the stored attributes ------- 
			//Create User Schema
			driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(SchemaName);
			Thread.sleep(2000);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
			driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
			driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
			//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
			Thread.sleep(1000);
			}
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(2000); 
			
			//------ Apply filter with the node name ------------------
			driver.findElement(By.id("dropdownMenuButton")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select node value 
			driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
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
					if(s.equals(name))
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
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
			System.out.println(finaldata);
			System.out.println("----------------------------------");
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			// --------------- Store the Attribute values into array ----------------
			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
			String ObjectAttributes[]=new String[AttributesData.size()];
			int i=1;
			StringBuilder buffer = new StringBuilder();
			for (WebElement FinalData : AttributesData)
			{
				//System.out.println(tdElement.getText());
				ObjectAttributes[i]=FinalData.getText();
	   	        String verify= FinalData.getText();
	   	        
	   	        if(!verify.isEmpty()) 
	   	        {
	   	    	 String None= "None";
	   	    	 if(!verify.contains(None))
	   	    	 {
	   	         buffer.append(ObjectAttributes[i]);
	    	     buffer.append('\n');
	    	     }
	   	    	 }
	   	     }
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			System.out.println(Values);
			
			// ----- Compare both attribute values ------------
			if(Values.contains(finaldata))
			{
				System.out.println("Attributes are Verified");
			}
			else
			{
				System.out.println("Attributes are not Verified");
				driver.findElement(By.xpath("Attributes verification failed")).click();
			}
			
			//clear search data
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			Thread.sleep(3000);
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(4000);
		}
		
		public void QueuesAttributesVerification(WebDriver driver, String schemaName, String WGSName) throws InterruptedException
		{
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
			Thread.sleep(1000);
			
			//------- Get the Attributes names and Store into array -----------
			List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div")); 
			String links[]=new String[Attributescolumn.size()];
	   	    int k=1;
	   	    StringBuilder ListOfAttributes = new StringBuilder();
	   	    
	   	    for (WebElement tdElement : Attributescolumn)
	   	    {
	   	    	//System.out.println(tdElement.getText());
		        links[k]=tdElement.getText();
		        String verify= tdElement.getText();
		        
		        if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!verify.contains(None))
		        	{
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
	   	    }
	   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			int ManagerName=4;
			if(!WGSName.contains("MQM"))
			{
				ManagerName=5;
			}
			//Store the name into string
			String name=driver.findElement(By.xpath("//datatable-body-cell["+ ManagerName +"]/div/span")).getText();
			Thread.sleep(2000);
			
			//Store the Queue name into string 
			String Queuename=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			Thread.sleep(2000);
			
			//------------- Create user Schema With the stored attributes ------- 
			//Create User Schema
			driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(schemaName);
			Thread.sleep(2000);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
			driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
			driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
			//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
			Thread.sleep(1000);
			}
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(2000); 
			
			//------ Apply filter with the node name ------------------
			driver.findElement(By.id("dropdownMenuButton")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select node value 
			driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
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
					if(s.equals(name))
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
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Queuename);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
			System.out.println(finaldata);
			System.out.println("----------------------------------");
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
			Thread.sleep(1000);
			
			// --------------- Store the Attribute values into array ----------------
			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
			String ObjectAttributes[]=new String[AttributesData.size()];
			int i=1;
			StringBuilder buffer = new StringBuilder();
			for (WebElement FinalData : AttributesData)
			{
				//System.out.println(tdElement.getText());
				ObjectAttributes[i]=FinalData.getText();
	   	        String verify= FinalData.getText();
	   	        
	   	        if(!verify.isEmpty()) 
	   	        {
	   	    	 String None= "None";
	   	    	 if(!verify.contains(None))
	   	    	 {
	   	         buffer.append(ObjectAttributes[i]);
	    	     buffer.append('\n');
	    	     }
	   	    	 }
	   	     }
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			System.out.println(Values);
			
			// ----- Compare both attribute values ------------
			if(Values.contains(finaldata))
			{
				System.out.println("Attributes are Verified");
			}
			else
			{
				System.out.println("Attributes are not Verified");
				driver.findElement(By.xpath("Attributes verification failed")).click();
			}
			
			//clear search data
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			Thread.sleep(3000);
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(4000);
		}
		
		public void ChannelAttributes(WebDriver driver, String schemaName, String Attributes, String WGSName) throws InterruptedException
		{
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			//------- Get the Attributes names and Store into array -----------
			List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div")); 
			String links[]=new String[Attributescolumn.size()];
	   	    int k=1;
	   	    StringBuilder ListOfAttributes = new StringBuilder();
	   	    
	   	    for (WebElement tdElement : Attributescolumn)
	   	    {
	   	    	//System.out.println(tdElement.getText());
		        links[k]=tdElement.getText();
		        String verify= tdElement.getText();
		        
		        if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!verify.contains(None))
		        	{
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
	   	    }
	   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			int ChannelMgrName=4;
			if(!WGSName.contains("MQM"))
			{
				ChannelMgrName=5;
			}
			//Store the name into string
			String name=driver.findElement(By.xpath("//datatable-body-cell["+ ChannelMgrName +"]/div/span")).getText();
			Thread.sleep(2000);
			
			//Store the Queue name into string 
			String Channelname=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
			Thread.sleep(2000);
			
			//------------- Create user Schema With the stored attributes ------- 
			//Create User Schema
			driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(schemaName);
			Thread.sleep(2000);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
		    if(FinalListOfAttributes.contains("Transmission Queue") || FinalListOfAttributes.contains("Non-persistent Message Speed"))
		    {
		    	System.out.println("Extra object");
		    }
		    else
		    {
			driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
			driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
			//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
			Thread.sleep(1000);
		    }
			}
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(2000); 
			
			//------ Apply filter with the node name ------------------
			driver.findElement(By.id("dropdownMenuButton")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select node value 
			driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
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
					if(s.equals(name))
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
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Channelname);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
			System.out.println(finaldata);
			System.out.println("----------------------------------");
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			// --------------- Store the Attribute values into array ----------------
			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
			String ObjectAttributes[]=new String[AttributesData.size()];
			int i=1;
			StringBuilder buffer = new StringBuilder();
			for (WebElement FinalData : AttributesData)
			{
				//System.out.println(tdElement.getText());
				ObjectAttributes[i]=FinalData.getText();
	   	        String verify= FinalData.getText();
	   	        
	   	        if(!verify.isEmpty()) 
	   	        {
	   	    	 String None= "None";
	   	    	 if(!verify.contains(None))
	   	    	 {
	   	         buffer.append(ObjectAttributes[i]);
	    	     buffer.append('\n');
	    	     }
	   	    	 }
	   	     }
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			System.out.println(Values);
			
			// ----- Compare both attribute values ------------
			if(Values.contains(finaldata))
			{
				System.out.println("Attributes are Verified");
			}
			else
			{
				System.out.println("Attributes are not Verified");
				driver.findElement(By.xpath("Attributes verification failed")).click();
			}
			
			//clear search data
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			Thread.sleep(3000);
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(4000);
		}
		
		public void ObjectAttributesVerification(WebDriver driver, String schemaName, String WGSName) throws InterruptedException
		{
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div"));
			   
			String links[]=new String[Attributescolumn.size()];
	   	    int k=1;
	   	    StringBuilder ListOfAttributes = new StringBuilder();
	   	    
	   	    for (WebElement tdElement : Attributescolumn)
	   	    {
	   	    	//System.out.println(tdElement.getText());
		        links[k]=tdElement.getText();
		        String verify= tdElement.getText();
		        
		        if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!verify.contains(None))
		        	{
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
	   	    }
	   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			int MgrName=4;
			if(!WGSName.contains("MQM"))
			{
				MgrName=5;
			}
			
			//Store the Manager name into string 
			String managername=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MgrName +"]/div/span")).getText();
			System.out.println(managername);
			
			int AttributeName=3;
			if(!WGSName.contains("MQM"))
			{
				AttributeName=4;
			}
			
			//Store the name into string
			String name=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ AttributeName +"]/div/span")).getText();
			Thread.sleep(2000);
			
			//Create User Schema
			driver.findElement(By.xpath("//div[3]/app-viewlet/div/div[2]/div/div[2]/div[3]/img")).click();
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(schemaName);
			Thread.sleep(2000);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				
			driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
			driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
			//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
			Thread.sleep(1000);
			}
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(3000);
			
			/*//Edit viewlet for applying to required conditions
			driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select manager 
			driver.findElement(By.name("queueMngr")).sendKeys(managername);
			Thread.sleep(2000);
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);*/
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			System.out.println(finaldata);
			System.out.println("----------------------------------");
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
			String ObjectAttributes[]=new String[AttributesData.size()];
			int i=1;
			StringBuilder buffer = new StringBuilder();
			for (WebElement FinalData : AttributesData)
			{
				//System.out.println(tdElement.getText());
				ObjectAttributes[i]=FinalData.getText();
	   	        String verify= FinalData.getText();
	   	        
	   	        if(!verify.isEmpty()) 
	   	        {
	   	    	 String None= "None";
	   	    	 if(!verify.contains(None))
	   	    	 {
	   	         buffer.append(ObjectAttributes[i]);
	    	     buffer.append('\n');
	    	     }
	   	    	 }
	   	     }
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			System.out.println(Values);
					
			if(finaldata.contains(Values) || Values.contains(finaldata))
			{
				System.out.println("Attributes are Verified");
			}
			else
			{
				System.out.println("Attributes are not Verified");
				driver.findElement(By.xpath("Attributes verification failed")).click();
			}
			
			//clear search data
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			Thread.sleep(3000);
			
			/*//Edit viewlet for applying to required conditions
			driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select manager 
			driver.findElement(By.name("queueMngr")).click();
			driver.findElement(By.name("queueMngr")).clear();
			driver.findElement(By.xpath("//app-modal-title/div")).click();
			Thread.sleep(4000);
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(2000);*/
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
			Thread.sleep(4000);		
		}
		
		public void ObjectAttributesVerificationForNameList(WebDriver driver, String schemaName, String WGSName) throws InterruptedException
		{
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div"));
			   
			String links[]=new String[Attributescolumn.size()];
	   	    int k=1;
	   	    StringBuilder ListOfAttributes = new StringBuilder();
	   	    
	   	    for (WebElement tdElement : Attributescolumn)
	   	    {
	   	    	//System.out.println(tdElement.getText());
		        links[k]=tdElement.getText();
		        String verify= tdElement.getText();
		        
		        if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!verify.contains(None))
		        	{
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
	   	    }
	   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			int MgrName=4;
			if(!WGSName.contains("MQM"))
			{
				MgrName=5;
			}
			//Store the Manager name into string 
			String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MgrName +"]/div/span")).getText();
			System.out.println(managername);
			
			
			int AttributeName=3;
			if(!WGSName.contains("MQM"))
			{
				AttributeName=4;
			}
			//Store the name into string
			String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ AttributeName +"]/div/span")).getText();
			Thread.sleep(2000);
			
			//Create User Schema
			driver.findElement(By.xpath("//div[1]/app-viewlet/div/div[2]/div/div[2]/div[3]/img")).click();
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(schemaName);
			Thread.sleep(2000);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				
			driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
			driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
			//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
			Thread.sleep(1000);
			}
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(3000);
			
			/*//Edit viewlet for applying to required conditions
			driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select manager 
			driver.findElement(By.name("queueMngr")).sendKeys(managername);
			Thread.sleep(2000);
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);*/
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			System.out.println(finaldata);
			System.out.println("----------------------------------");
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
			String ObjectAttributes[]=new String[AttributesData.size()];
			int i=1;
			StringBuilder buffer = new StringBuilder();
			for (WebElement FinalData : AttributesData)
			{
				//System.out.println(tdElement.getText());
				ObjectAttributes[i]=FinalData.getText();
	   	        String verify= FinalData.getText();
	   	        
	   	        if(!verify.isEmpty()) 
	   	        {
	   	    	 String None= "None";
	   	    	 if(!verify.contains(None))
	   	    	 {
	   	         buffer.append(ObjectAttributes[i]);
	    	     buffer.append('\n');
	    	     }
	   	    	 }
	   	     }
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			System.out.println(Values);
					
			if(Values.contains(finaldata))
			{
				System.out.println("Attributes are Verified");
			}
			else
			{
				System.out.println("Attributes are not Verified");
				driver.findElement(By.xpath("Attributes verification failed")).click();
			}
			
			//clear search data
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			Thread.sleep(3000);
			
			/*//Edit viewlet for applying to required conditions
			driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select manager 
			driver.findElement(By.name("queueMngr")).click();
			driver.findElement(By.name("queueMngr")).clear();
			driver.findElement(By.xpath("//app-modal-title/div")).click();
			Thread.sleep(4000);
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(2000);*/
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(4000);		
		}
		
		public void ObjectAttributesVerificationForService(WebDriver driver, String schemaName, String WGSName) throws InterruptedException
		{
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div"));
			   
			String links[]=new String[Attributescolumn.size()];
	   	    int k=1;
	   	    StringBuilder ListOfAttributes = new StringBuilder();
	   	    
	   	    for (WebElement tdElement : Attributescolumn)
	   	    {
	   	    	//System.out.println(tdElement.getText());
		        links[k]=tdElement.getText();
		        String verify= tdElement.getText();
		        
		        if(!verify.isEmpty())
		        {
		        	String None= "None";
		        	
		        	if(!verify.contains(None))
		        	{
		        		ListOfAttributes.append(links[k]);
			    		ListOfAttributes.append(',');
		        	}
		        }
	   	    }
	   	    
	   	    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			//System.out.println(ListOfAttributesPresent);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			int MgrName=4;
			if(!WGSName.contains("MQM"))
			{
				MgrName=5;
			}
			//Store the Manager name into string 
			String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MgrName +"]/div/span")).getText();
			System.out.println(managername);
			
			
			int AttributeName=3;
			if(!WGSName.contains("MQM"))
			{
				AttributeName=4;
			}
			//Store the name into string
			String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ AttributeName +"]/div/span")).getText();
			Thread.sleep(2000);
			
			//Create User Schema
			driver.findElement(By.xpath("//div[1]/app-viewlet/div/div[2]/div/div[2]/div[3]/img")).click();
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
			
			//Give schema name
			driver.findElement(By.name("name")).sendKeys(schemaName);
			Thread.sleep(2000);
			
			//Add the Required attributes which are located in the Object attribute page
			for (String FinalListOfAttributes : ListOfAttributesPresent)
			{
				
			driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
			driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
			//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
			Thread.sleep(1000);
			}
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
			Thread.sleep(3000);
			
			/*//Edit viewlet for applying to required conditions
			driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select manager 
			driver.findElement(By.name("queueMngr")).sendKeys(managername);
			Thread.sleep(2000);
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);*/
			
			//Search the viewlet data using name
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
			Thread.sleep(2000);
			
			//Strore the data into particular string
			String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
			System.out.println(finaldata);
			System.out.println("----------------------------------");
			
			//Show Object Attribute option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
			Thread.sleep(1000);
			
			List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
			String ObjectAttributes[]=new String[AttributesData.size()];
			int i=1;
			StringBuilder buffer = new StringBuilder();
			for (WebElement FinalData : AttributesData)
			{
				//System.out.println(tdElement.getText());
				ObjectAttributes[i]=FinalData.getText();
	   	        String verify= FinalData.getText();
	   	        
	   	        if(!verify.isEmpty()) 
	   	        {
	   	    	 String None= "None";
	   	    	 if(!verify.contains(None))
	   	    	 {
	   	         buffer.append(ObjectAttributes[i]);
	    	     buffer.append('\n');
	    	     }
	   	    	 }
	   	     }
			//System.out.println(buffer);
			driver.findElement(By.cssSelector(".close-button")).click();
			
			String Values=buffer.toString();
			System.out.println(Values);
					
			if(finaldata.contains(Values))
			{
				System.out.println("Attributes are Verified");
			}
			else
			{
				System.out.println("Attributes are not Verified");
				driver.findElement(By.xpath("Attributes verification failed")).click();
			}
			
			//clear search data
			driver.findElement(By.xpath("//input[@type='text']")).clear();
			Thread.sleep(3000);
			
			/*//Edit viewlet for applying to required conditions
			driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
			driver.findElement(By.linkText("Edit viewlet")).click();
			
			//Select manager 
			driver.findElement(By.name("queueMngr")).click();
			driver.findElement(By.name("queueMngr")).clear();
			driver.findElement(By.xpath("//app-modal-title/div")).click();
			Thread.sleep(4000);
			
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(2000);*/
			
			//Refresh the Viewlet
			driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
			Thread.sleep(4000);		
		}
		
		
	public void TransportObjectAttributesVerification(WebDriver driver, String schemaName) throws InterruptedException
	{
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div"));
		   
		String links[]=new String[Attributescolumn.size()];
   	    int k=1;
   	    StringBuilder ListOfAttributes = new StringBuilder();
   	    
   	    for (WebElement tdElement : Attributescolumn)
   	    {
   	    	//System.out.println(tdElement.getText());
	        links[k]=tdElement.getText();
	        String verify= tdElement.getText();
	        
	        if(!verify.isEmpty())
	        {
	        	String None= "None";
	        	
	        	if(!verify.contains(None))
	        	{
	        		ListOfAttributes.append(links[k]);
		    		ListOfAttributes.append(',');
	        	}
	        }
   	    }
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println(ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(managername);
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell[3]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//div[3]/img")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
		driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
		Thread.sleep(1000);
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
	
		try 
		{
			//driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-select")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				System.out.println(s);
				if(s.equals(DestinationManager))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";
   	    	 if(!verify.contains(None))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println(Values);
				
		if(Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).clear();
		driver.findElement(By.xpath("//app-modal-title/div")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);		
	}
	
	
	public void SubscriptionObjectAttributesVerification(WebDriver driver, String schemaName, String AddSubscriptionNameFromIcon, String WGSName) throws InterruptedException
	{		
		String Managername="Manager Name";
		String Node="Node Name";
		
		int MgrName=4;
		if(!WGSName.contains("MQM"))
		{
			MgrName=5;
		}
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MgrName +"]/div/span")).getText();
		System.out.println(managername);
		
		int AttributeName=3;
		if(!WGSName.contains("MQM"))
		{
			AttributeName=4;
		}
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ AttributeName +"]/div/span")).getText();
		System.out.println(name);
		Thread.sleep(4000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button")).click();
		driver.findElement(By.cssSelector("td[title=\""+ Managername +"\"]")).click();
		driver.findElement(By.xpath("//div[2]/div[2]/button[3]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("td[title=\""+ Node +"\"]")).click();
		driver.findElement(By.xpath("//div[2]/div[2]/button[3]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		try 
		{
			//driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-select")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				System.out.println(s);
				if(s.equals(managername))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		/*//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);*/
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";
   	    	 if(!verify.contains(None))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println(Values);
				
		if(finaldata.contains(Values))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).clear();
		driver.findElement(By.xpath("//app-modal-title/div")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);		
	}
	
	public void AuthinfoObjectAttributesVerification(WebDriver driver, String schemaName, String WGSName) throws InterruptedException
	{		
		String Managername="Manager Name";
		String Node="Node Name";
		
		int MgrName=4;
		if(!WGSName.contains("MQM"))
		{
			MgrName=5;
		}
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MgrName +"]/div/span")).getText();
		System.out.println(managername);
		
		int AttributeName=3;
		if(!WGSName.contains("MQM"))
		{
			AttributeName=4;
		}
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ AttributeName +"]/div/span")).getText();
		System.out.println(name);
		Thread.sleep(4000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button")).click();
		driver.findElement(By.cssSelector("td[title=\""+ Managername +"\"]")).click();
		driver.findElement(By.xpath("//div[2]/div[2]/button[3]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("td[title=\""+ Node +"\"]")).click();
		driver.findElement(By.xpath("//div[2]/div[2]/button[3]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		try 
		{
			//driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-select")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				System.out.println(s);
				if(s.equals(managername))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";
   	    	 if(!verify.contains(None))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println(Values);
				
		if(finaldata.contains(Values))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).clear();
		driver.findElement(By.xpath("//app-modal-title/div")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);		
	}
	
	public void ClusterQMObjectAttributesVerification(WebDriver driver, String schemaName, String WGSName) throws InterruptedException
	{		
		String Managername="Manager Name";
		String Node="Node Name";
		
		int MgrName=4;
		if(!WGSName.contains("MQM"))
		{
			MgrName=5;
		}
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MgrName +"]/div/span")).getText();
		System.out.println(managername);
		
		int AttributeName=3;
		if(!WGSName.contains("MQM"))
		{
			AttributeName=4;
		}
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ AttributeName +"]/div/span")).getText();
		System.out.println(name);
		Thread.sleep(4000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button")).click();
		driver.findElement(By.cssSelector("td[title=\""+ Managername +"\"]")).click();
		driver.findElement(By.xpath("//div[2]/div[2]/button[3]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("td[title=\""+ Node +"\"]")).click();
		driver.findElement(By.xpath("//div[2]/div[2]/button[3]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		try 
		{
			//driver.findElement(By.id("destinationQMName")).click();
			List<WebElement> Manager=driver.findElement(By.className("ng-select")).findElements(By.className("ng-option"));
			System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				System.out.println(s);
				if(s.equals(managername))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";
   	    	 if(!verify.contains(None))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println(Values);
				
		if(finaldata.contains(Values))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).clear();
		driver.findElement(By.xpath("//app-modal-title/div")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("//img[@title='Refresh viewlet']")).click();
		Thread.sleep(4000);		
	}
	
	public void ObjectAttributesVerificationForEMS(WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		List<WebElement> Attributescolumn = driver.findElements(By.xpath("//tbody/tr/td/div"));
		   
		String links[]=new String[Attributescolumn.size()];
   	    int k=1;
   	    StringBuilder ListOfAttributes = new StringBuilder();
   	    
   	    for (WebElement tdElement : Attributescolumn)
   	    {
   	    	//System.out.println(tdElement.getText());
	        links[k]=tdElement.getText();
	        String verify= tdElement.getText();
	        
	        if(!verify.isEmpty())
	        {
	        	String None= "None";
	        	
	        	if(!verify.contains(None))
	        	{
	        		ListOfAttributes.append(links[k]);
		    		ListOfAttributes.append(',');
	        	}
	        }
   	    }
   	    
   	    String AttributeValues=ListOfAttributes.toString();
		String[] ListOfAttributesPresent = AttributeValues.split(",");
		//System.out.println(ListOfAttributesPresent);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		int MgrName=4;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			MgrName=5;
		}
		
		//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ MgrName +"]/div/span")).getText();
		System.out.println(managername);
		
		int AttributeName=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			AttributeName=4;
		}
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell["+ AttributeName +"]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//div[3]/app-viewlet/div/div[2]/div/div[2]/div[3]/img")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			
		driver.findElement(By.cssSelector("td[title=\""+ FinalListOfAttributes +"\"]")).click();
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button[2]")).click();
		//driver.findElement(By.cssSelector(".add-buttons > .g-btn-blue-style:nth-child(2)")).click();
		Thread.sleep(1000);
		}
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).sendKeys(managername);
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
		//Search the viewlet data using name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(name);
		Thread.sleep(2000);
		
		//Strore the data into particular string
		String finaldata=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li")).click();
		Thread.sleep(1000);
		
		List<WebElement> AttributesData = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		String ObjectAttributes[]=new String[AttributesData.size()];
		int i=1;
		StringBuilder buffer = new StringBuilder();
		for (WebElement FinalData : AttributesData)
		{
			//System.out.println(tdElement.getText());
			ObjectAttributes[i]=FinalData.getText();
   	        String verify= FinalData.getText();
   	        
   	        if(!verify.isEmpty()) 
   	        {
   	    	 String None= "None";
   	    	 if(!verify.contains(None))
   	    	 {
   	         buffer.append(ObjectAttributes[i]);
    	     buffer.append('\n');
    	     }
   	    	 }
   	     }
		//System.out.println(buffer);
		driver.findElement(By.cssSelector(".close-button")).click();
		
		String Values=buffer.toString();
		System.out.println(Values);
				
		if(finaldata.contains(Values) || Values.contains(finaldata))
		{
			System.out.println("Attributes are Verified");
		}
		else
		{
			System.out.println("Attributes are not Verified");
			driver.findElement(By.xpath("Attributes verification failed")).click();
		}
		
		//clear search data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		Thread.sleep(3000);
		
		/*//Edit viewlet for applying to required conditions
		driver.findElement(By.xpath("(//div[@id='dropdownMenuButton'])[3]")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		
		//Select manager 
		driver.findElement(By.name("queueMngr")).click();
		driver.findElement(By.name("queueMngr")).clear();
		driver.findElement(By.xpath("//app-modal-title/div")).click();
		Thread.sleep(4000);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/
		
		//Refresh the Viewlet
		driver.findElement(By.xpath("(//img[@title='Refresh viewlet'])[3]")).click();
		Thread.sleep(4000);		
	}
		
 }




