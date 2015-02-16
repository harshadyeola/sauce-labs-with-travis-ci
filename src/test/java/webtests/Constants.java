/**
 * @author Juhi
 * @author Sumeet
 */

package webtests;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;

/** Static class for Application constants. */
public class Constants {


	
	public static final String WP_SERVER = "http://rtmediatesting.com/";
	/*public static final String WP_SERVER = "http://sumeetqatester.rtcamp.net";*/

	public static final String AlbumName = "My New Album";


	public static final String USERNAME1 = "juhi";
	public static final String UPASSWORD1 = "rtcamp.com";
	/*public static final String USERNAME1 = "admin";
	public static final String UPASSWORD1 = "Sumeet1@";*/

	/*public static final String Forums = "http://sumeetqatester.rtcamp.net/forums/";*/

	// Other Roles
	  public static final String TestAuthor = "TestAuthor";
	  public static final String TestAuthorPassword = "1234567890";
	  public static final String TestEditor = "TestEditor";
	  public static final String TestEditorPassword = "1234567890";
	  public static final String TestContributor = "TestContributor";
	  public static final String TestContributorPassword = "1234567890";
	  public static final String TestSubscriber = "TestSubscriber";
	  public static final String TestSubscriberPassword = "1234567890";
	  public static final String TestAdmin = "TestAdmin";
	  public static final String TestAdminPassword = "1234567890";

	public static final String PhotoPhantom = ".//src//Resources//test_data//images//test.jpg";
	public static final String Photo2= ".//src//Resources//test_data//images//test2.jpeg";
	public static final String MusicPhantom = "./src/Resources/test_data/music/mpthreetest.mp3";
	public static final String Music2 = "./src/Resources/test_data/music/SukoonMila.mp3";
	public static final String VideoPhantom = "./src/Resources/test_data/videos/testmpfour.mp4";
	public static final String Video2 = "./src/Resources/test_data/videos/testmpfour2.mp4";
	public static final String DocumentPhantom = "./src/Resources/test_data/documents/testdoc.docx";
	public static final String Document= "./src/Resources/test_data/documents/document1.pdf";
public static final String OthersPhantom="./src/Resources/test_data/others/other.zip";	
public static final String PhotoPhantom2="'./src/test/resources/test_data/images/test.jpg','./src/test/resources/test_data/images/test2.jpg'";
/*public static final String WP_SERVER = "http://localhost/wordpress/";
//Admin Login
public static final String USERNAME1 = "juhi";
public static final String USERNAME2 = "test";
public static final String UPASSWORD1 = "rtcamp.com";
public static final String UPASSWORD2 = "rtcamp.com";  

// public static final String UPLOADFILE = "E:\\file_upload.exe";
	// public static final String UPLOADFILE1 = "E:\\file_upload1.exe";
	public static final String UPLOADFILE1 = "./src/Resources/test.jpg";
	
	//public static final String UPLOADFILEFORPRIVACY = "E:\\Test Data\\privacy_file_uploade.exe";
	
	public static final String UPLOADVIDEOFILE = "./src/Resources/Tom And Jerry Kids Intro.mp4";
	public static final String IMAGEATTACHEMENTNAME = "test";
	public static final String VIDEOATTACHEMENTNAME = "Tom And Jerry Kids Intro";
	
	public static final String UPLOADAUDIOFILE = "./src/Resources/Test.mp3";
	public static final String AUDIOATTACHEMENTNAME = "Test";  
		
	public static final String USERNAME3 = "TestAuthor";
	public static final String UPASSWORD3 = "rtcamp.com";
	public static final String USERNAME4 = "TestEditor";
	public static final String UPASSWORD4 = "rtcamp.com";
	public static final String USERNAME5 = "TestContributor";
	public static final String UPASSWORD5 = "rtcamp.com";
	
	*/
  	//	For WP settings
	public static final String IMAGEATTACHEMENTNAME = "test";
	public static final String VIDEOATTACHEMENTNAME = "Tom And Jerry Kids Intro";	
	public static final String AUDIOATTACHEMENTNAME = "Test";  
	public static final String UPLOADAUDIOFILE = "./src/Resources/test_data/music/Test.mp3";
	
	public static final String UPLOADFILE1 = "./src/Resources/test_data/images/test.jpg";
	public static final String UPLOADVIDEOFILE = "./src/Resources/test_data/videos/Tom And Jerry Kids Intro.mp4";
		
  	/*public static final String USERNAME2 = "ron";
  	public static final String UPASSWORD2 = "1234567890";
  	public static final String USERNAME3 = "shan";
  	public static final String UPASSWORD3 = "1234567890";
  	public static final String USERNAME4 = "antawn";
  	public static final String UPASSWORD4 = "1234567890";*/
  	

	// Method to login
	public static void login(WebDriver driver, String userName, String password)
			throws Exception {
		Thread.sleep(2000);
		driver.findElement(By.id("user_login")).click();
		driver.findElement(By.id("user_login")).clear();
		driver.findElement(By.id("user_login")).sendKeys(userName);
		driver.findElement(By.id("user_pass")).click();
		driver.findElement(By.id("user_pass")).clear();
		driver.findElement(By.id("user_pass")).sendKeys(password);
		driver.findElement(By.id("wp-submit")).click();
	}

//	// Method to login by phantomjs
//	public static void login(PhantomJSDriver wd, String userName,
//			String password) throws Exception {
//		Thread.sleep(2000);
//		wd.findElement(By.id("user_login")).click();
//		wd.findElement(By.id("user_login")).clear();
//		wd.findElement(By.id("user_login")).sendKeys(userName);
//		wd.findElement(By.id("user_pass")).click();
//		wd.findElement(By.id("user_pass")).clear();
//		wd.findElement(By.id("user_pass")).sendKeys(password);
//		wd.findElement(By.id("wp-submit")).click();
//	}

	// Method to Logout
	public static void logout(RemoteWebDriver wd) throws Exception {
		new Actions(wd).moveToElement(wd.findElement(By.cssSelector("#wp-admin-bar-my-account > a.ab-item"))).build().perform();
		Thread.sleep(2000);
		wd.findElement(By.linkText("Log Out")).click();
	}

//	// Method to Logout by phantomjs
//	public static void logout(PhantomJSDriver wd) throws Exception {
//		new Actions(wd).moveToElement(wd.findElement(By.cssSelector("#wp-admin-bar-my-account > a.ab-item"))).build().perform();
//		Thread.sleep(2000);
//		wd.findElement(By.linkText("Log Out")).click();
//	}
//	// Method to open rtMedia Settings by phantomjs
//		public static void openrtMediaSettings(PhantomJSDriver wd) throws Exception {
//			Thread.sleep(3000);
//			// Do a mouse over on the primary menu on top LHS
//			new Actions(wd).moveToElement(wd.findElement(By.cssSelector("#wp-admin-bar-site-name > a.ab-item"))).build().perform();
//			Thread.sleep(2000);
//			// Click on Dashboard
//			wd.findElement(By.linkText("Dashboard")).click();
//			((PhantomJSDriver) wd).executePhantomJS("var page=this; "+ "page.render('./screen/Dashboard.png'); ");
//			System.out.println("Dashboard Clicked");
//			Thread.sleep(2000);
//			// Click on rtMedia
//			// Do a mouse over on the primary menu on top LHS
//			new Actions(wd)
//		.moveToElement(wd.findElement(By.linkText("rtMedia"))).build().perform();
//			Thread.sleep(2000);
//			 wd.findElement(By.linkText("Settings")).click();
////			((PhantomJSDriver) wd)
////					.executePhantomJS("var page=this; "
////							+ "page.injectJs('./js/jquery-2.1.0.min.js', function() {"
////							+ "  waitFor(function(){ ,"
////							+ "function(){ return page.evaluate(function(){"
////							+ "return $('#adminmenu').is(':visible');"
////							+ "});"
////							+ "}page.evaluate(function() {"
////							+ "   jQuery('#adminmenu li').each(function(){"
////							+ "	jQuery(this).removeClass('wp-menu-open');"
////							+ "});"
////							+ "jQuery('#adminmenu li:nth-child(7) a div:nth-child(3)').click();"
////							+ " });" + "});" + "phantom.exit();}); ");
//			// ((JavascriptExecutor) wd)
//			// .executeScript("jQuery('#adminmenu li').each(function(){"
//			// + "jQuery(this).removeClass('wp-menu-open');"
//			// + "});"
//			// +
//			// "jQuery('#adminmenu li:nth-child(7) a div:nth-child(3)').click();");
//			((PhantomJSDriver) wd).executePhantomJS("var page=this; "+ "page.render('./screen/Privacy/PrivacySettingsTestForPrivate.png'); ");
//			System.out.println("rtMedia Clicked");
//			Thread.sleep(2000);
//		}
	// Method to open rtMedia Settings by Firefox driver
	public static void openrtMediaSettings(WebDriver wd) throws Exception {
		Thread.sleep(3000);
		// Do a mouse over on the primary menu on top LHS
		new Actions(wd).moveToElement(wd.findElement(By.cssSelector("#wp-admin-bar-site-name > a.ab-item"))).build().perform();
		Thread.sleep(2000);
		// Click on Dashboard
		wd.findElement(By.linkText("Dashboard")).click();

		System.out.println("Dashboard Clickedss");
		// Click on rtMedia
		wd.findElement(By.linkText("rtMedia")).click();
		System.out.println("rtMedia Clicked");

	}

		 public static String getTestResource(Object obj, String resourceName)
		            throws Exception {
		        URL url = obj.getClass().getResource("/" + resourceName);
		        File testImage = new File(url.getFile());
		        String testImagePath = testImage.toPath().toString();
		        System.out.println(testImage.toPath().toString());
		        testImagePath = testImagePath.replace("\\", "//");
		        return testImagePath;
		    }
	
}
