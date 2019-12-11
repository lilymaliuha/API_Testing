import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;

public class DriverMaster {

    private static HashMap<Long, WebDriver> map = new HashMap<>();
    private static WebDriver myDriver;

    private DriverMaster() {
    }

    public static WebDriver getDriverInstance() {

        if (myDriver == null) {
            createDriverInstance();
            return myDriver;
        }
        return myDriver;
    }

    public static WebDriver checkIfOldDriverClosedAndCreateNewDriverInstance() {

        if (myDriver == null) {
            createDriverInstance();
        } else {
            stopDriver();
            createDriverInstance();
        }
        return myDriver;
    }

    static void stopDriver() {

        if (!(myDriver == null || myDriver.toString().contains("null"))) {
            myDriver.quit();
            myDriver = null;
        }
    }

    private static void createDriverInstance() {
        myDriver = createMyDriver();
//        myDriver = createRemoteWebDriver();
//        myDriver = createLocalRemoteWebDriver();
    }

    private static WebDriver createMyDriver() {
        WebDriver driver = null;
        String path = new File("").getAbsolutePath().replace("\\target", "") +
                "\\src\\test\\resources\\BrowserDrivers\\";
        BrowserType browser = BrowserType.get("browser");
        switch (browser) {
            case FIREFOX:
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                firefoxProfile.setPreference("browser.download.folderList", 2);
                firefoxProfile.setPreference("browser.download.manager.alertOnEXEOpen", false);
                firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, " +
                        "application/csv, application/ris, text/csv, image/png, application/pdf, text/html, " +
                        "text/plain, application/zip, application/x-zip, application/x-zip-compressed, " +
                        "application/download, application/octet-stream, application/vnd.ms-excel, application/zip, " +
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, " +
                        "application/x-shockwave-flash, video/mp4");
                firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
                firefoxProfile.setPreference("browser.download.manager.focusWhenStarting", false);
                firefoxProfile.setPreference("browser.download.useDownloadDir", true);
                firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
                firefoxProfile.setPreference("browser.download.manager.alertOnEXEOpen", false);
                firefoxProfile.setPreference("browser.download.manager.closeWhenDone", true);
                firefoxProfile.setPreference("browser.download.manager.showAlertOnComplete", false);
                firefoxProfile.setPreference("browser.download.manager.useWindow", false);
                firefoxProfile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
                firefoxProfile.setPreference("pdfjs.disabled", true);

                System.setProperty("webdriver.gecko.driver", path + "geckodriver.exe");

                DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                capabilities.setJavascriptEnabled(true);
                capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
                capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
                driver = new FirefoxDriver(capabilities);

                map.put(Thread.currentThread().getId(), driver);
                break;
            case CHROME:
                System.setProperty("webdriver.chrome.driver", path + "chromedriver.exe");

                HashMap<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("profile.default_content_settings.popups", 1);
                chromePrefs.put("credentials_enable_service", false);
                chromePrefs.put("profile.password_manager_enabled", false);
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", chromePrefs);
                options.addArguments("disable-infobars");
                DesiredCapabilities cap = DesiredCapabilities.chrome();
                cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                cap.setCapability(ChromeOptions.CAPABILITY, options);
                cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

                try {
                    driver = new ChromeDriver(cap);
                } catch (Exception e) {
                    driver = new ChromeDriver(cap);
                }
                map.put(Thread.currentThread().getId(), driver);
                break;
            case IE:
                System.setProperty("webdriver.ie.driver", path + "IEDriverServer.exe");
                capabilities = new org.openqa.selenium.remote.DesiredCapabilities();
                driver = new InternetExplorerDriver(capabilities);
                map.put(Thread.currentThread().getId(), driver);
                break;
            case IOS:
                break;
            default:
                firefoxProfile = new FirefoxProfile();
                capabilities = DesiredCapabilities.firefox();
                capabilities.setJavascriptEnabled(true);
                capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
                driver = new FirefoxDriver(capabilities);
                map.put(Thread.currentThread().getId(), driver);
        }
        return driver;

    }
}
