

 /*
  * Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *
  */
  
  
  package org.mot.common.tools;

import java.util.Enumeration;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumGoogleReader {

	Logger logger = LoggerFactory.getLogger(getClass());
	public Hashtable<String, String> tokens = new Hashtable<String, String>(); 
    WebDriver driver = new HtmlUnitDriver();
    
	/**
	 * Default constructor - initializing the lookup table
	 */
	public SeleniumGoogleReader() {
		init();
	}
	
	
	private void init() {   
		
		/* 
		 * Use the Selenium IDE  in Firefox to select each field from the google page. 
		 * Through the IDE just copy and paste the target / xpath definition in here...
		 */

        tokens.put("Range", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[1]/td[2]");
		tokens.put("52week", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[2]/td[2]");
		tokens.put("Open", 		"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[3]/td[2]");
		tokens.put("Vol/Avg", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[4]/td[2]");
		tokens.put("Mkt Cap", 	"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[5]/td[2]");
		tokens.put("P/E", 		"//div[@id='market-data-div']/div[2]/div/table/tbody/tr[6]/td[2]");
		
		tokens.put("Div/yield", "//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[1]/td[2]");
		tokens.put("EPS", 		"//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[2]/td[2]");
		tokens.put("Shares", 	"//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[3]/td[2]");
		tokens.put("Beta", 		"//div[@id='market-data-div']/div[2]/div/table[2]/tbody/tr[4]/td[2]");
		
	}
	
	
	private String convertValueToPath(String value) {
		logger.trace("Looking up value " + value);
		return tokens.get(value);
	}
	
	/**
	 * @return Entire page source - for debugging purposes
	 */
	public String getPageSource() {
		return driver.getPageSource();
	}
	
	/**
	 * @return a list of all possible lookup values
	 */
	public Enumeration<String> getListOfValues() {
		return tokens.keys();
	}
	
	public void disconnect() {
		driver.close();
	}
	
	
	/**
	 * @param symbol - symbol to look up
	 * @param value - which value to return
	 * @return String value
	 */
	public String getFieldValue(String symbol, String value) {
	
        WebElement resultsDiv = null; 
        String retValue = null;
        
    	try {
	        driver.get("http://www.google.com/finance");
	
	        // Find the text input element by its name
	        WebElement element = driver.findElement(By.name("q"));
	
	        // Enter something to search for
	        element.sendKeys(symbol);
	        
	        WebElement sendButton = driver.findElement(By.className("gbqfb"));
	        sendButton.click();

    		logger.debug("Looking up " + value + " for symbol " + symbol);
    		
    		// Give it 200 milliseconds to load the page
    		Thread.sleep(200);
    		
    		//System.out.println(driver.getPageSource());
    		
    		String path = convertValueToPath(value);
    		logger.debug("Looking for path  " + path);
    		
    		resultsDiv = driver.findElement(By.xpath(path));
    		
    		retValue = resultsDiv.getText();
    		logger.debug("Returned value: " + retValue);
    		                
    	} catch (NoSuchElementException e) {
    		logger.warn("Can not find value " + value + " for symbol " + symbol);
    	} catch (IllegalStateException e) {
    		logger.warn("There is no internet connection available - will exit!");
       	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return retValue;
		
	}
	
	
	   public static void main(String[] args) {

		   SeleniumGoogleReader sgr = new SeleniumGoogleReader();
		   
		   String symbol = "GS";
		   
		   System.out.println("Profit to Earnings Ratio: " + sgr.getFieldValue(symbol, "P/E"));
		   //System.exit(0);
		   
		   System.out.println("Market Cap: " + sgr.getFieldValue(symbol, "Mkt Cap"));
		   System.out.println("Range: " + sgr.getFieldValue(symbol, "Range"));
		   System.out.println("Shares: " + sgr.getFieldValue(symbol, "Shares"));
		   System.out.println("52 Week : " + sgr.getFieldValue(symbol, "52week"));
		   System.out.println("Opening Price: " + sgr.getFieldValue(symbol, "Open"));
		   System.out.println("Div / Yield: " + sgr.getFieldValue(symbol, "Div/yield"));
		   
	    }
	
}
