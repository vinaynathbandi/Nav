package ApodGUI;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EMSObjects 
{
	public void ObjectAttributesVerification(WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
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
		
		/*//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);*/
		
		int AttributeName=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			AttributeName=4;
		}
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell["+ AttributeName +"]/div/span")).getText();
		Thread.sleep(2000);
		
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
		String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
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
		for(int j=0; j<=name.length(); j++)
    	{
    	
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(2000);
		
		//Refresh the Viewlet
    	driver.findElement(By.xpath("//input[@type='text']")).click();
		Thread.sleep(4000);		
	}
	
	
	public void EMSQueueObjectAttributesVerification(WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
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
		
		/*//Store the Manager name into string 
		String managername=driver.findElement(By.xpath("//div[3]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
		System.out.println(managername);*/
		
		int AttributeName=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			AttributeName=4;
		}
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell["+ AttributeName +"]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		//Add the Required attributes which are located in the Object attribute page
		for (String FinalListOfAttributes : ListOfAttributesPresent)
		{
			//System.out.println("Attribute is: " +FinalListOfAttributes);
			String FinalString="";
			if(FinalListOfAttributes.contains("'From' Queue name"))
			{
				FinalString=FinalListOfAttributes.replace("'", "\"");
				
			}
			else if(FinalListOfAttributes.contains("'To' Queue name"))
			{
				FinalString=FinalListOfAttributes.replace("'", "\"");
			}
			else
			{
				FinalString=FinalListOfAttributes;
			}
			//System.out.println("FinalString is: " +FinalString); 
			driver.findElement(By.xpath("//td[contains(.,'"+ FinalString +"')]")).click();
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
		String finaldata=driver.findElement(By.cssSelector("datatable-body.datatable-body")).getText();
		System.out.println(finaldata);
		System.out.println("----------------------------------");
		
		//Show Object Attribute option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]")).click();
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
		for(int j=0; j<=name.length(); j++)
    	{
    	
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(2000);
		
		//Refresh the Viewlet
    	driver.findElement(By.xpath("//input[@type='text']")).click();
		Thread.sleep(4000);		
	}
	
	public void ChannelObjectAttributesVerification(WebDriver driver, String schemaName, String EMS_WGSNAME) throws InterruptedException
	{	
		int AttributeName=3;
		if(!EMS_WGSNAME.contains("MQM"))
		{
			AttributeName=4;
		}
		
		//Store the name into string
		String name=driver.findElement(By.xpath("//datatable-body-cell["+ AttributeName +"]/div/span")).getText();
		Thread.sleep(2000);
		
		//Create User Schema
		driver.findElement(By.xpath("//img[@title='Manage viewlet schemas']")).click();
		driver.findElement(By.xpath("//app-mod-manage-schemas/div/div/div[2]/button")).click();
		
		//Give schema name
		driver.findElement(By.name("name")).sendKeys(schemaName);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//app-mod-edit-schema/div/div/div[2]/div[2]/button")).click();
		driver.findElement(By.cssSelector("td[title=\"Active\"]")).click();
		driver.findElement(By.xpath("//div[4]/div/button")).click();
		driver.findElement(By.xpath("//div[4]/div/button")).click();
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
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);*/
		
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
		for(int j=0; j<=name.length(); j++)
    	{
    	
			driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(2000);
		
		//Refresh the Viewlet
    	driver.findElement(By.xpath("//input[@type='text']")).click();
		Thread.sleep(4000);		
	}

}
