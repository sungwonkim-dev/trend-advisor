package utils;

import exception.NaverSearchExtractorException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static exception.NaverSearchExtractorExceptionCode.FILE_IO_EX_CODE;

public class WebDriverUtils {

    public WebDriver accessChromeWebDriver() throws NaverSearchExtractorException {
        WebDriver webDriver = null;
        try {
            createChromeDriverInResource();
            return new ChromeDriver(makeChromeOptions());
        } catch (Exception ex) {
            if (webDriver != null) webDriver.close();
        }
        return webDriver;
    }

    private ChromeOptions makeChromeOptions() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("pageLoadStrategy", "normal");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(desiredCapabilities);
        return chromeOptions;
    }

    private void createChromeDriverInResource() throws NaverSearchExtractorException {
        final String ABS_DEST_WEBDIRVER_PATH = "/webdriver/chromedriver.exe";
        final String WORKING_DIRECTORY = System.getProperty("user.dir");
        final String COPY_PATH = String.format("%s/%s", WORKING_DIRECTORY, ABS_DEST_WEBDIRVER_PATH);
        final String CHROME_WEBDRIVER_ID = "webdriver.chrome.driver";
        final String WEBDIREVER_PATH_IN_RESOURCE = "/webdriver/chromedriver.exe";
        try {
            URL webDriverUrlInResource = getClass().getResource(WEBDIREVER_PATH_IN_RESOURCE);
            File chromeDriver = new File(COPY_PATH);
            FileUtils.copyURLToFile(webDriverUrlInResource, chromeDriver);
            System.setProperty(CHROME_WEBDRIVER_ID, COPY_PATH);
        } catch (IOException e) {
            throw new NaverSearchExtractorException(e, FILE_IO_EX_CODE);
        }
    }


}
