package webtests;

/**
 * @author Ross Rowe
 */

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import com.saucelabs.testng.SauceOnDemandTestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import webtests.Constants;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
public class SampleSauceTest implements SauceOnDemandSessionIdProvider,
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
	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

	/**
	 * ThreadLocal variable which contains the Sauce Job Id.
	 */
	private ThreadLocal<String> sessionId = new ThreadLocal<String>();
	
	private WebDriver driver;

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
	private WebDriver createDriver(String browser, String version, String os)
			throws MalformedURLException {

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		if (version != null) {
			capabilities.setCapability(CapabilityType.VERSION, version);
		}
		capabilities.setCapability(CapabilityType.PLATFORM, os);
		capabilities.setCapability("name", "Setup test");
		webDriver.set(new RemoteWebDriver(new URL("http://"
				+ authentication.getUsername() + ":"
				+ authentication.getAccessKey()
				+ "@ondemand.saucelabs.com:80/wd/hub"), capabilities));
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
	public void webDriver(String browser, String version, String os)
			throws Exception {
		 driver = createDriver(browser, version, os);
		WebDriverWait wait = new WebDriverWait(driver, 20); // wait for a
															// maximum of 20
															// seconds
		// Login to Wordpress with Administrator account
		driver.get(Constants.WP_SERVER);
		driver.findElement(By.linkText("Log in")).click();
		Constants.login(driver, Constants.USERNAME1, Constants.UPASSWORD1);
		Thread.sleep(2000);
		// Do a mouse over on the primary menu on top LHS
		new Actions(driver)
				.moveToElement(
						driver.findElement(By
								.cssSelector("#wp-admin-bar-site-name > a.ab-item")))
				.build().perform();

		// Thread.sleep(2000);
		// wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Dashboard")));
		// Click on Dashboard
		driver.findElement(By.linkText("Dashboard")).click();

		Thread.sleep(2000);
		// Do a mouse over on the primary menu on top LHS
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("Posts")))
				.build().perform();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Add New")).click();
		System.out.println("Reached Posts");

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("title")));

		// Enter the Post title for WordPress admin
		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Test Post");

		// Click on publish button
		driver.findElement(By.id("publish")).click();

		String message = driver.findElement(By.cssSelector("div#message > p"))
				.getText();
		System.out.println(message + ": message");
		if ("Post published. View post".equals(message))
			System.out.println("Post Created");
		else
			System.out.println("Post Not created");

		// Add New Post for anonymous user

		driver.findElement(By.linkText("Add New")).click();
		/* driver.findElement(By.id("add-new-h2")).click(); */
		// Enter the title
		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Test Post For Anonymous");

		// Click on publish button
		driver.findElement(By.id("publish")).click();

		message = driver.findElement(By.cssSelector("div#message > p"))
				.getText();
		System.out.println(message + ": message");
		if ("Post published. View post".equals(message))
			System.out.println("Anonymous Post Created");
		else
			System.out.println("Anonymous Post Not created");

		// Add New for Author user
		/* driver.findElement(By.id("add-new-h2")).click(); */
		driver.findElement(By.linkText("Add New")).click();
		// Enter the title
		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Test Post For Author");

		// Click on publish button
		driver.findElement(By.id("publish")).click();

		message = driver.findElement(By.cssSelector("div#message > p"))
				.getText();
		System.out.println(message + ": message");
		if ("Post published. View post".equals(message))
			System.out.println("Author Post Created");
		else
			System.out.println("Author Post Not created");

		// Add New for Contributor user
		/* driver.findElement(By.id("add-new-h2")).click(); */
		driver.findElement(By.linkText("Add New")).click();
		// Enter the title
		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title"))
				.sendKeys("Test Post For Contributor");

		// Click on publish button
		driver.findElement(By.id("publish")).click();

		message = driver.findElement(By.cssSelector("div#message > p"))
				.getText();
		System.out.println(message + ": message");
		if ("Post published. View post".equals(message))
			System.out.println("Contributor Post Created");
		else
			System.out.println("Contributor Post Not created");

		// Add New for Editor user
		/* driver.findElement(By.id("add-new-h2")).click(); */
		driver.findElement(By.linkText("Add New")).click();
		// Enter the title
		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Test Post For Editor");

		// Click on publish button
		driver.findElement(By.id("publish")).click();

		message = driver.findElement(By.cssSelector("div#message > p"))
				.getText();
		System.out.println(message + ": message");
		if ("Post published. View post".equals(message))
			System.out.println("Editor Post Created");
		else
			System.out.println("Editor Post Not created");

		// Add New for Subscriber user
		/* driver.findElement(By.id("add-new-h2")).click(); */
		driver.findElement(By.linkText("Add New")).click();
		// Enter the title
		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Test Post For Subscriber");

		// Click on publish button
		driver.findElement(By.id("publish")).click();

		message = driver.findElement(By.cssSelector("div#message > p"))
				.getText();
		System.out.println(message + ": message");
		if ("Post published. View post".equals(message))
			System.out.println("Subscriber Post Created");
		else
			System.out.println("Subscriber Post Not created");

		// Creating a Group Named test
		// Make sure Enable Media in Group is turned on
		// Open rtMedia Settings
		// Constants.openrtMediaSettings(driver);
		//
		// Thread.sleep(5000);
		// Click on rtMedia
		driver.findElement(By.linkText("rtMedia")).click();
		System.out.println("rtMedia Clicked");

		// Click on rtMedia settings BuddyPress Tab

		driver.findElement(By.id("tab-rtmedia-bp")).click();

		System.out.println("BuddyPress Tab Opened");

		// Check if the switch is on or off, if its off then switch on and
		// proceed
		List<WebElement> switchElement = driver
				.findElements(By
						.cssSelector("span.rt-form-checkbox> label[for=\"rt-form-checkbox-16\"] > div.rt-switch.has-switch > div.switch-animate.switch-off"));

		if (switchElement.size() != 0) {

			switchElement.get(0)
					.findElement(By.cssSelector("span.switch-right")).click();
			System.out.println("'Enable Media in Group' is switched  on");
		} else
			System.out.println("'Enable Media in Group' is already on");
		// Save rtMedia Settings

		driver.findElement(By.id("rtmedia-settings-submit")).click();
		System.out.println("rtMedia Settings Saved");
		Thread.sleep(2000);

		// Make sure user groups is enabled from BuddyPress Settings

		driver.findElement(
				By.xpath("//li[@id='menu-settings']//div[.='Settings']"))
				.click();
		Thread.sleep(1000);
		driver.findElement(
				By.xpath("//li[@id='menu-settings']//a[.='BuddyPress']"))
				.click();
		Thread.sleep(1000);
		// click on User Groups checkbox if not selected
		if (!driver.findElement(By.id("bp_components[groups]")).isSelected()) {
			driver.findElement(By.id("bp_components[groups]")).click();
		}

		driver.findElement(By.id("bp-admin-component-submit")).click();
		Thread.sleep(3000);
		/* try{ */driver.findElement(By.linkText("Groups")).click();
		driver.findElement(By.cssSelector("a.add-new-h2")).click();
		driver.findElement(By.id("group-name")).click();
		driver.findElement(By.id("group-name")).clear();
		driver.findElement(By.id("group-name")).sendKeys("test");
		driver.findElement(By.id("group-desc")).click();
		driver.findElement(By.id("group-desc")).clear();
		driver.findElement(By.id("group-desc")).sendKeys("test");
		driver.findElement(By.id("group-creation-create")).click();
		driver.findElement(By.id("group-creation-next")).click();
		driver.findElement(By.id("group-creation-next")).click();
		driver.findElement(By.id("group-creation-next")).click();
		driver.findElement(By.id("group-creation-finish")).click();

		System.out.println("A group named test is created");

		// CREATE USERS via code : TestEditor Test Subscriber , Test Author ,
		// TestContributor etc

		// Test Admin
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("New"))).build()
				.perform();
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("User"))).build()
				.perform();
		driver.findElement(By.linkText("User")).click();
		driver.findElement(By.id("user_login")).click();
		driver.findElement(By.id("user_login")).clear();
		driver.findElement(By.id("user_login")).sendKeys("TestAdmin");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(
				"sumeet.sarna+testadmin@rtcamp.com");
		driver.findElement(By.id("first_name")).click();
		driver.findElement(By.id("first_name")).clear();
		driver.findElement(By.id("first_name")).sendKeys("TestAdmin");

		driver.findElement(By.id("pass1")).click();
		driver.findElement(By.id("pass1")).clear();
		driver.findElement(By.id("pass1")).sendKeys("1234567890");
		driver.findElement(By.id("pass2")).click();
		driver.findElement(By.id("pass2")).clear();
		driver.findElement(By.id("pass2")).sendKeys("1234567890");
		Select dropdown5 = new Select(driver.findElement(By.id("role")));
		dropdown5.selectByValue("administrator");
		driver.findElement(By.id("createusersub")).click();

		System.out.println("Test Admin user Created");

		// TestEditor
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("New"))).build()
				.perform();
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("User"))).build()
				.perform();
		driver.findElement(By.linkText("User")).click();
		driver.findElement(By.id("user_login")).click();
		driver.findElement(By.id("user_login")).clear();
		driver.findElement(By.id("user_login")).sendKeys("TestEditor");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(
				"sumeet.sarna+testeditor@rtcamp.com");
		driver.findElement(By.id("first_name")).click();
		driver.findElement(By.id("first_name")).clear();
		driver.findElement(By.id("first_name")).sendKeys("TestEditor");
		driver.findElement(By.id("last_name")).click();
		driver.findElement(By.id("last_name")).clear();
		driver.findElement(By.id("last_name")).sendKeys("TestEditor");
		driver.findElement(By.id("pass1")).click();
		driver.findElement(By.id("pass1")).clear();
		driver.findElement(By.id("pass1")).sendKeys("1234567890");
		driver.findElement(By.id("pass2")).click();
		driver.findElement(By.id("pass2")).clear();
		driver.findElement(By.id("pass2")).sendKeys("1234567890");
		/*
		 * if (!driver.findElement(By.xpath(
		 * "//table[@class='form-table']/tbody/tr[9]/td/select//option[8]"
		 * )).isSelected()) { driver.findElement(By.xpath(
		 * "//table[@class='form-table']/tbody/tr[9]/td/select//option[8]"
		 * )).click(); }
		 * driver.findElement(By.xpath("//table[@class='form-table']/tbody/tr[8]/td"
		 * )).click();
		 */

		Select dropdown = new Select(driver.findElement(By.id("role")));
		dropdown.selectByValue("editor");
		driver.findElement(By.id("createusersub")).click();
		System.out.println("Test Editor Created");
		// Test Author
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("New"))).build()
				.perform();
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("User"))).build()
				.perform();
		driver.findElement(By.linkText("User")).click();
		driver.findElement(By.id("user_login")).click();
		driver.findElement(By.id("user_login")).clear();
		driver.findElement(By.id("user_login")).sendKeys("TestAuthor");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(
				"sumeet.sarna+testauthor@rtcamp.com");
		driver.findElement(By.id("first_name")).click();
		driver.findElement(By.id("first_name")).clear();
		driver.findElement(By.id("first_name")).sendKeys("TestAuthor");

		driver.findElement(By.id("pass1")).click();
		driver.findElement(By.id("pass1")).clear();
		driver.findElement(By.id("pass1")).sendKeys("1234567890");
		driver.findElement(By.id("pass2")).click();
		driver.findElement(By.id("pass2")).clear();
		driver.findElement(By.id("pass2")).sendKeys("1234567890");
		Select dropdown2 = new Select(driver.findElement(By.id("role")));
		dropdown2.selectByValue("author");
		driver.findElement(By.id("createusersub")).click();
		System.out.println("Test Author Created");

		// Test Subscriber
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("New"))).build()
				.perform();
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("User"))).build()
				.perform();
		driver.findElement(By.linkText("User")).click();
		driver.findElement(By.id("user_login")).click();
		driver.findElement(By.id("user_login")).clear();
		driver.findElement(By.id("user_login")).sendKeys("TestSubscriber");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(
				"sumeet.sarna+testsubscriber@rtcamp.com");
		driver.findElement(By.id("first_name")).click();
		driver.findElement(By.id("first_name")).clear();
		driver.findElement(By.id("first_name")).sendKeys("TestSubscriber");

		driver.findElement(By.id("pass1")).click();
		driver.findElement(By.id("pass1")).clear();
		driver.findElement(By.id("pass1")).sendKeys("1234567890");
		driver.findElement(By.id("pass2")).click();
		driver.findElement(By.id("pass2")).clear();
		driver.findElement(By.id("pass2")).sendKeys("1234567890");
		Select dropdown3 = new Select(driver.findElement(By.id("role")));
		dropdown3.selectByValue("subscriber");
		driver.findElement(By.id("createusersub")).click();
		System.out.println("Test Subscriber Created");
		// TestContributor

		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("New"))).build()
				.perform();
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("User"))).build()
				.perform();
		driver.findElement(By.linkText("User")).click();
		driver.findElement(By.id("user_login")).click();
		driver.findElement(By.id("user_login")).clear();
		driver.findElement(By.id("user_login")).sendKeys("TestContributor");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(
				"sumeet.sarna+testcontributor@rtcamp.com");
		driver.findElement(By.id("first_name")).click();
		driver.findElement(By.id("first_name")).clear();
		driver.findElement(By.id("first_name")).sendKeys("TestContributor");

		driver.findElement(By.id("pass1")).click();
		driver.findElement(By.id("pass1")).clear();
		driver.findElement(By.id("pass1")).sendKeys("1234567890");
		driver.findElement(By.id("pass2")).click();
		driver.findElement(By.id("pass2")).clear();
		driver.findElement(By.id("pass2")).sendKeys("1234567890");
		Select dropdown4 = new Select(driver.findElement(By.id("role")));
		dropdown4.selectByValue("contributor");
		driver.findElement(By.id("createusersub")).click();

		System.out.println("Test Contributor Created"); /*
														 * }catch(Throwable
														 * T){System
														 * .out.println(
														 * "Already created");}
														 */

		Thread.sleep(6000);
		// Create a TESTFORUM and a discussion named : discussion1 for
		// bbPressSettingsTest.java
		// Click on Forums

		driver.navigate().to(Constants.WP_SERVER + "/wp-admin");
		System.out.println("Reached dashboard");

		Thread.sleep(4000);
		new Actions(driver)
				.moveToElement(driver.findElement(By.linkText("Forums")))
				.build().perform();
		Thread.sleep(2000);

		driver.findElement(By.linkText("New Forum")).click();
		System.out.println("New Forum Clicked");
		Thread.sleep(4000);

		/*
		 * new
		 * Actions(driver).moveToElement(driver.findElement(By.linkText("Forums"
		 * ))).build ().perform();
		 * driver.findElement(By.linkText("Forums")).click();
		 * Thread.sleep(2000);
		 * driver.findElement(By.linkText("New Forum")).click();
		 */
		/*
		 * new
		 * Actions(driver).moveToElement(driver.findElement(By.linkText("New"
		 * ))).build ().perform(); Thread.sleep(1000); new
		 * Actions(driver).moveToElement(driver.findElement
		 * (By.linkText("Forum"))).build().perform(); Thread.sleep(1000);
		 * driver.findElement(By.linkText("Forum")).click();
		 */
		Thread.sleep(4000);
		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("TestForum");

		if (!driver.findElement(By.id("title")).getAttribute("value")
				.equals("TestForum")) {
			System.out.println("verifyElementValue failed");
		}
//		driver.findElement(By.xpath("//html")).click();
		driver.findElement(By.id("publish")).click();

		/*
		 * driver.findElement(By.id("title")).click();
		 * driver.findElement(By.id("title")).clear();
		 * driver.findElement(By.id("title")).sendKeys("TestForum");
		 * driver.findElement(By.id("content")).click();
		 * driver.findElement(By.id("content")).clear();
		 * driver.findElement(By.id("content")).sendKeys("TestForum");
		 * Thread.sleep(4000); driver.findElement(By.id("publish")).click();
		 */

		System.out.println("TestForum published");
		Thread.sleep(4000);
		/*
		 * new
		 * Actions(driver).moveToElement(driver.findElement(By.linkText("View forum"
		 * ) )).build().perform();
		 * driver.findElement(By.linkText("View forum")).click();
		 */
		new Actions(driver)
				.moveToElement(
						driver.findElement(By
								.cssSelector("#view-post-btn > a.button.button-small")))
				.build().perform();
		Thread.sleep(1000);
		driver.findElement(
				By.cssSelector("#view-post-btn > a.button.button-small"))
				.click();
		Thread.sleep(4000);
		driver.findElement(By.id("bbp_topic_title")).click();
		driver.findElement(By.id("bbp_topic_title")).clear();
		driver.findElement(By.id("bbp_topic_title")).sendKeys("discussion1");
		driver.findElement(By.id("bbp_topic_content")).click();
		driver.findElement(By.id("bbp_topic_content")).clear();
		driver.findElement(By.id("bbp_topic_content")).sendKeys("discussion1");
		driver.findElement(By.id("bbp_topic_submit")).click();
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
