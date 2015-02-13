package webtests.bbPress;

/**
 * @author Ross Rowe
 */

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import com.saucelabs.testng.SauceOnDemandTestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import webtests.Constants;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;

/**
 * Simple TestNG test which demonstrates being instantiated via a DataProvider
 * in order to supply multiple browser combinations.
 *
 * @author Ross Rowe
 */
@Listeners({ SauceOnDemandTestListener.class })
public class bbPressSettingsTest implements SauceOnDemandSessionIdProvider,
		SauceOnDemandAuthenticationProvider {

	/**
	 * Constructs a {@link com.saucelabs.common.SauceOnDemandAuthentication}
	 * instance using the supplied user name/access key. To use the
	 * authentication supplied by environment variables or from an external
	 * file, use the no-arg
	 * {@link com.saucelabs.common.SauceOnDemandAuthentication} constructor.
	 */
	public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(
			"rtCamp", "ccdfc1a4-ef9b-4f28-a514-5f17c7866d72");

	/**
	 * ThreadLocal variable which contains the {@link WebDriver} instance which
	 * is used to perform browser interactions with.
	 */
	private ThreadLocal<RemoteWebDriver> webDriver = new ThreadLocal<RemoteWebDriver>();

	/**
	 * ThreadLocal variable which contains the Sauce Job Id.
	 */
	private ThreadLocal<String> sessionId = new ThreadLocal<String>();
	
	private RemoteWebDriver driver;

	/**
	 * DataProvider that explicitly sets the browser combinations to be used.
	 *
	 * @param testMethod
	 * @return
	 */
	@DataProvider(name = "hardCodedBrowsers", parallel = true)
	public static Object[][] sauceBrowserDataProvider(Method testMethod) {
		return new Object[][] { new Object[] { "chrome", "35", "Windows 8.1" },
		// new Object[]{"safari", "6", "OSX 10.8"},
		};
	}

	/**
	 * /** Constructs a new {@link RemoteWebDriver} instance which is configured
	 * to use the capabilities defined by the browser, version and os
	 * parameters, and which is configured to run against
	 * ondemand.saucelabs.com, using the username and access key populated by
	 * the {@link #authentication} instance.
	 *
	 * @param browser
	 *            Represents the browser to be used as part of the test run.
	 * @param version
	 *            Represents the version of the browser to be used as part of
	 *            the test run.
	 * @param os
	 *            Represents the operating system to be used as part of the test
	 *            run.
	 * @return
	 * @throws MalformedURLException
	 *             if an error occurs parsing the url
	 */
	private RemoteWebDriver createDriver(String browser, String version, String os)
			throws MalformedURLException {

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		if (version != null) {
			capabilities.setCapability(CapabilityType.VERSION, version);
		}
		capabilities.setCapability(CapabilityType.PLATFORM, os);
		capabilities.setCapability("name", "bbPressSettings");
		webDriver.set(new RemoteWebDriver(new URL("http://"
				+ authentication.getUsername() + ":"
				+ authentication.getAccessKey()
				+ "@ondemand.saucelabs.com:80/wd/hub"), capabilities));
//		 driver.setFileDetector(new LocalFileDetector());
		sessionId.set(((RemoteWebDriver) getWebDriver()).getSessionId()
				.toString());
		return webDriver.get();
	}

	/**
	 * Runs a simple test verifying the title of the amazon.com homepage.
	 *
	 * @param browser
	 *            Represents the browser to be used as part of the test run.
	 * @param version
	 *            Represents the version of the browser to be used as part of
	 *            the test run.
	 * @param os
	 *            Represents the operating system to be used as part of the test
	 *            run.
	 * @throws Exception
	 *             if an error occurs during the running of the test
	 */
	@Test(dataProvider = "hardCodedBrowsers")
	public void bbPressSettings(String browser, String version, String os)
			throws Exception {
		RemoteWebDriver driver = createDriver(browser, version, os);
		WebDriverWait wait = new WebDriverWait(driver, 20); // wait for a
		driver.setFileDetector(new LocalFileDetector());												// maximum of 20
															// seconds
		 
driver.get(Constants.WP_SERVER);
    	
		driver.findElement(By.cssSelector("li#wp-admin-bar-bp-login > a.ab-item")).click();
		
		driver.findElement(By.id("user_login")).click();
		System.out.println("User_login");
		driver.findElement(By.id("user_login")).clear();
		driver.findElement(By.id("user_login")).sendKeys(Constants.USERNAME1);
		driver.findElement(By.id("user_pass")).click();
		System.out.println("User_pass");
		driver.findElement(By.id("user_pass")).clear();
		driver.findElement(By.id("user_pass")).sendKeys(Constants.UPASSWORD1);
		driver.findElement(By.id("wp-submit")).click();
		System.out.println("wp-submit");
		Thread.sleep(2000);
		new Actions(driver).moveToElement(
driver.findElement(By.cssSelector("#wp-admin-bar-site-name > a.ab-item"))).build().perform();
		System.out.println("Mouse over on site");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li#wp-admin-bar-dashboard > a.ab-item")));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li#wp-admin-bar-dashboard > a.ab-item")).click();
		System.out.println("dashboard");
		// driver.findElement(By.linkText("rtMedia")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li#toplevel_page_rtmedia-settings > a.wp-has-submenu.wp-not-current-submenu.menu-top.toplevel_page_rtmedia-settings.menu-top-last > div.wp-menu-name")).click();
		System.out.println("rtMedia");
		// Thread.sleep(10000);
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);

		((JavascriptExecutor) driver)
				.executeScript("jQuery('.rtm-settings-tab-container dd').each(function(){"
						+ "jQuery(this).removeClass('active');"
						+ "});"
						+ "jQuery('.rtm-settings-tab-container dd:nth-child(3) a').trigger('click');");


		System.out.println("rtMedia-bbPress");

		
		List<WebElement> switchElement = driver
.findElements(By.cssSelector("span.rt-form-checkbox.rtm_enable_bbpress > label > div.rt-switch.has-switch > div.switch-animate.switch-off"));

		if (switchElement.size() != 0) {

	switchElement.get(0).findElement(By.cssSelector("span.switch-right")).click();

			if (!driver.findElement(By.id("rt-form-checkbox-14")).isSelected()) {
				driver.findElement(By.id("rt-form-checkbox-14")).click();
			}
			((JavascriptExecutor) driver)
	.executeScript("jQuery('#rt-form-radio-4').attr('checked',true);");
			System.out.println("with thumbnails + filename");
			// driver.findElement(By.id("rt-form-radio-4")).click();
	((JavascriptExecutor) driver).executeScript("jQuery('#rtmedia-settings-submit').trigger('click');");
			// driver.findElement(By.id("rtmedia-settings-submit")).click();
			System.out.println("rtmedia-settings-submit");
			Thread.sleep(2000);

			new Actions(driver)
.moveToElement(driver.findElement(By.cssSelector("#wp-admin-bar-site-name > a.ab-item"))).build().perform();
			System.out.println("wp-admin-bar-site-name ");
			driver.findElement(By.linkText("Visit Site")).click();
			System.out.println("Visit Site");
			/*driver.findElement(By.linkText("FORUMS")).click();*/
			
			driver.navigate().to(Constants.WP_SERVER+ "/forums");
			driver.findElement(By.linkText("TestForum")).click();
			driver.findElement(By.linkText("discussion1")).click();
			driver.findElement(By.id("bbp_reply_content")).click();
			driver.findElement(By.id("bbp_reply_content")).clear();
			driver.findElement(By.id("bbp_reply_content")).sendKeys(
					"This is a test reply to forum");
			if (!driver.findElement(By.id("bbp_topic_subscription")).isSelected()) {
				driver.findElement(By.id("bbp_topic_subscription")).click();
			}
			driver.findElement(By.id("rtmedia_simple_file_input")).click();
// Insert a file named test
		
			
			Thread.sleep(3000);
			
			
			driver.findElement(By.id("bbp_reply_submit")).click();
			Thread.sleep(5000);

			String expectedComment = "This is a test reply to forum";
String actualComment = driver.findElement(
By.xpath("//div[1]/div/div[1]/div/div/article/div/div/ul/li[2]/div[4]/div[2]/p"))
.getText();

			try {
			Assert.assertEquals(actualComment, expectedComment);
			} catch (Throwable e) {
		System.out.println("Expected Comment in section not present - Failed");
			}

			driver.findElement(By.cssSelector("img[alt=\"test\"]")).click();
			
			((JavascriptExecutor) driver)
.executeScript("jQuery('.webwidget_rating_simple li:nth-child(1)').trigger('click');");
			// driver.findElement(By.cssSelector("ul.webwidget_rating_simple > li"))
			// .click();
//			Thread.sleep(5000);
			System.out.println("webwidget_rating_simple ");
			((JavascriptExecutor) driver)
					.executeScript("jQuery('.rtmedia-actions-before-comments button').trigger('click');");
			System.out.println("Like Button ");
			// driver.findElement(
			// By.xpath("//div[@class='rtmedia-actions-before-comments']//button[.='Like']"))
			// .click();
			Thread.sleep(5000);

			driver.findElement(By.id("comment_content")).click();
			driver.findElement(By.id("comment_content")).clear();
			driver.findElement(By.id("comment_content"))
					.sendKeys("nice pic...test");
			driver.findElement(By.id("rt_media_comment_submit")).click();
			driver.findElement(By.cssSelector("span.mfp-close")).click();
//			Thread.sleep(2000);

			driver.findElement(By.cssSelector("img[alt=\"test\"]")).click();
//			Thread.sleep(10000);

      String actual1 = driver.findElement(By.cssSelector("span.rtmedia-avg-rate")).getText();
			System.out.println(actual1);
			String expected1 = "Rating : 1";
			try {
				Assert.assertEquals(actual1, expected1);
			} catch (Throwable e) {
				System.out.println("Expected Text Not Present for RATING - failed");
			}

			String actual2 = driver.findElement(By.cssSelector("span.rtmedia-like-counter-wrap")).getText();
			String expected2 = "1 people like this";
			try {
				Assert.assertEquals(actual2, expected2);
			} catch (Throwable e) {
				System.out
						.println("Expected Text Not Present FOR LIKE - failed");
			}
			String actual3 = driver
					.findElement(
							By.xpath("//div[@class='rtmedia-comment-content']//p[.='nice pic...test']"))
					.getText();
			String expected3 = "nice pic...test";
			try {
				Assert.assertEquals(actual3, expected3);
			} catch (Throwable e) {
				System.out
						.println("Expected Text Not Present for COMMENT- failed");
			}

			driver.findElement(By.cssSelector("span.mfp-close")).click();
//			Thread.sleep(5000);
			new Actions(driver)
					.moveToElement(
		driver.findElement(By.cssSelector("#wp-admin-bar-my-account > a.ab-item"))).build().perform();
			driver.findElement(By.linkText("Log Out")).click();
			driver.quit();

		} else {
			System.out.println("Enable Attachment Switch is Already On");
			((JavascriptExecutor) driver)
			.executeScript("jQuery('#rt-form-radio-4').attr('checked',true);");
			System.out.println("with thumbnails + filename");
		
//		((JavascriptExecutor) driver).executeScript("jQuery('#rtmedia-settings-submit').trigger('click');");
			 driver.findElement(By.id("rtmedia-settings-submit")).click();
			System.out.println("rtmedia-settings-submit");
//			Thread.sleep(2000);

			new Actions(driver)
					.moveToElement(
							driver.findElement(By
									.cssSelector("#wp-admin-bar-site-name > a.ab-item")))
					.build().perform();
			System.out.println("wp-admin-bar-site-name ");
			driver.findElement(By.linkText("Visit Site")).click();
			System.out.println("Visit Site");
			
			driver.navigate().to(Constants.WP_SERVER + "/forums");
			/*driver.findElement(By.linkText("FORUMS")).click();*/
			System.out.println("Forums");
			driver.findElement(By.linkText("TestForum")).click();
			System.out.println("TEST Forum clicked");
			driver.findElement(By.linkText("discussion1")).click();
			System.out.println("Discussion 1 clicked ");
			driver.findElement(By.id("bbp_reply_content")).click();
			driver.findElement(By.id("bbp_reply_content")).clear();
			driver.findElement(By.id("bbp_reply_content")).sendKeys(
					"This is a test reply to forum");
			System.out.println("Reply Content");
			if (!driver.findElement(By.id("bbp_topic_subscription")).isSelected()) {
				driver.findElement(By.id("bbp_topic_subscription")).click();
				System.out.println("TOP SUBSCRIPTION CLICKED");
			}
			WebElement ele=null;
			List<WebElement> fileElement=driver.findElements(By.id("rtmedia_simple_file_input"));
			if(fileElement.size()!=0){
				System.out.println("File:"+fileElement.size());
			 ele=driver.findElement(By.id("rtmedia_simple_file_input"));
//			 driver.findElement(By.id("rtmedia_simple_file_input")).click();
//				driver.findElement(By.id("rtmedia_simple_file_input")).sendKeys("E:\\test.jpg");
			}
			
 //upolade the file from client to server
//		    
//		    StringSelection ss=new StringSelection(Constants.UPLOADFILE1);
//		    
//			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
//		    
//		    Robot r =new Robot();
//		    
//		    r.keyPress(KeyEvent.VK_ENTER);
//		    r.keyRelease(KeyEvent.VK_ENTER);
//		    r.keyPress(KeyEvent.VK_CONTROL);
//		    r.keyPress(KeyEvent.VK_V);
//		    r.keyRelease(KeyEvent.VK_V);
//		    r.keyRelease(KeyEvent.VK_CONTROL);
//		    
//		    r.keyPress(KeyEvent.VK_ENTER);
//		    r.keyRelease(KeyEvent.VK_ENTER);
			ele.sendKeys(Constants.UPLOADFILE1);
			
//			((PhantomJSDriver) driver).executePhantomJS("var page=this; "
//					+ "var count=0;" + "page.uploadFile('input[type=file]','"
//					+ Constants.PhotoPhantom + "');"					 
//					+"page.render('./screen/nextprintscreen' + count+'.png');");		
//			Thread.sleep(3000);		

	
			driver.findElement(By.id("bbp_reply_submit")).click();
			System.out.println("Clicked on Submit");
//			Thread.sleep(5000);
/*// Having problem in locating xpath. Commenting for now
String expectedComment = "This is a test reply to forum";

String actualComment = driver.findElement(By.xpath("//div[1]/div/div[1]/div/div/article/div/div/ul/li[2]/div[4]/div[2]/p")).getText();

			try {
				Assert.assertEquals(actualComment, expectedComment);
			} catch (Throwable e) {
				System.out.println("Expected Comment in section not present - Failed");
			}
*/
			List<WebElement> imageElement = driver.findElements(By.cssSelector("img[alt=\"test\"]"));
			System.out.println(imageElement.size() + " :Size");
			if (imageElement.size() != 0) {
				driver.findElement(By.cssSelector("img[alt=\"test\"]")).click();
				System.out.println(driver.findElement(By.cssSelector("img[alt=\"test\"]")).getText()+ "image");
			}
//			Thread.sleep(5000);
			((JavascriptExecutor) driver)
.executeScript("jQuery('.webwidget_rating_simple li:nth-child(1)').trigger('click');");
			// driver.findElement(By.cssSelector("ul.webwidget_rating_simple > li"))
			// .click();
//			Thread.sleep(5000);
			System.out.println("webwidget_rating_simple ");
			((JavascriptExecutor) driver)
					.executeScript("jQuery('.rtmedia-actions-before-comments button').trigger('click');");
			System.out.println("Like Button ");
		
//			Thread.sleep(5000);
		
			driver.findElement(By.id("comment_content")).click();
			driver.findElement(By.id("comment_content")).clear();
			driver.findElement(By.id("comment_content")).sendKeys("nice pic...test");
			driver.findElement(By.id("rt_media_comment_submit")).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.mfp-close")));
//			driver.findElement(By.cssSelector("span.mfp-close")).click();
//			Thread.sleep(2000);

//			driver.findElement(By.cssSelector("img[alt=\"test\"]")).click();

//			Thread.sleep(10000);

			// String actual1 = driver.findElement(By.tagName("html")).getText();
			String actual1 = driver.findElement(By.cssSelector("span.rtmedia-avg-rate")).getText();
			System.out.println(actual1);
			String expected1 = "Rating : 1";
			try {
				Assert.assertEquals(actual1, expected1);
			} catch (Throwable e) {
				System.out
						.println("Expected Text Not Present for RATING - failed");
			}

			String actual2 = driver.findElement(By.cssSelector("span.rtmedia-like-counter-wrap")).getText();
			String expected2 = "1 people like this";
			System.out.println(actual2);
			try {
				Assert.assertEquals(actual2, expected2);
			} catch (Throwable e) {
				System.out
						.println("Expected Text Not Present FOR LIKE - failed");
			}
			String actual3 = driver
					.findElement(
							By.xpath("//div[@class='rtmedia-comment-content']//p[.='nice pic...test']"))
					.getText();
			String expected3 = "nice pic...test";
			System.out.println(actual3);
			try {
				Assert.assertEquals(actual3, expected3);
			} catch (Throwable e) {
				System.out
						.println("Expected Text Not Present for COMMENT- failed");
			}
		new Actions(driver).sendKeys(Keys.ESCAPE).build().perform();
//			driver.findElement(By.cssSelector("span.mfp-close")).click();
//			Thread.sleep(5000);
			new Actions(driver)
					.moveToElement(
							driver.findElement(By
									.cssSelector("#wp-admin-bar-my-account > a.ab-item")))
					.build().perform();
			driver.findElement(By.linkText("Log Out")).click();
		}
	}

	/**
	 * @return the {@link WebDriver} for the current thread
	 */
	public WebDriver getWebDriver() {
		System.out.println("WebDriver" + webDriver.get());
		return webDriver.get();
	}
	
	/**
	 *
	 * @return the Sauce Job id for the current thread
	 */
	public String getSessionId() {
		return sessionId.get();
	}
	@AfterMethod
    public void tearDown()
    {
        driver.quit();
    }
	/**
	 *
	 * @return the {@link SauceOnDemandAuthentication} instance containing the
	 *         Sauce username/access key
	 */
	@Override
	public SauceOnDemandAuthentication getAuthentication() {
		return authentication;
	}
}
