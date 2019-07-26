package util;

import constant.Path;
import exception.NaverSearchExtractorException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static constant.Path.WEBDRIVER_COPY_PATH;
import static exception.NaverSearchExtractorExceptionCode.FILE_IO_EX_CODE;
import static exception.NaverSearchExtractorExceptionCode.UNDEFINED_EX_CODE;

public class WebDriverUtils {

    public WebDriver accessChromeWebDriver() throws NaverSearchExtractorException {
        ChromeDriver chromeDriver = null;
        try {
            createChromeDriverInResource();
            chromeDriver = new ChromeDriver();
            return chromeDriver;
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        } finally {
            if (chromeDriver != null)
                chromeDriver.quit();
        }
    }

    private void createChromeDriverInResource() throws NaverSearchExtractorException {
        try {
            URL webDriverUrlInResource = getClass().getResource(Path.WEBDRIVER_PATH_IN_RESOURCE);
            File chromeDriver = new File(WEBDRIVER_COPY_PATH);
            FileUtils.copyURLToFile(webDriverUrlInResource, chromeDriver);
            System.setProperty(Path.CHROME_WEBDRIVER_ID, WEBDRIVER_COPY_PATH);
        } catch (IOException e) {
            throw new NaverSearchExtractorException(e, FILE_IO_EX_CODE);
        }
    }


}
