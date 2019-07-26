package util;

import constant.XPath;
import exception.NaverSearchExtractorException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.lang.model.util.Elements;

import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

import static constant.XPath.*;
import static exception.NaverSearchExtractorExceptionCode.UNDEFINED_EX_CODE;
import static java.lang.Thread.*;

public class HttpConnector {
    private WebDriver webDriver;
    private Random random = new Random();

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriver getWebDriver() {
        return this.webDriver;
    }

    public void movePageByUrl(String url) throws NaverSearchExtractorException {
        try {
            sleep(3000);
            webDriver.get(url);
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    public String getCurHtmlUseWebdriver() throws NaverSearchExtractorException {
        try {
            sleep(3000);
            return webDriver.getPageSource();
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    public String getHtmlByUrlUseWebdriver(String url) throws NaverSearchExtractorException {
        try {
            webDriver.get(url);
            sleep(3000);
            return webDriver.getPageSource();
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    public String getHtmlFromUrl(String url) throws Exception {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .execute();
            return response.body();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public String getHtmlFromUrlByPost(String url) throws Exception {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .execute();
            return response.body();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void searchByParameter(String category, String[] parameters) throws InterruptedException {
        category = String.format(N_STRING_FORMAT_CATEGORY, category);

        String year = String.format(N_STRING_FORMAT_START_YEAR, parameters[0]);
        String month = String.format(N_STRING_FORMAT_START_MONTH, parameters[1]);
        String day = String.format(N_STRING_FORMAT_START_DAY, parameters[2]);

        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(N_CATEGORY)).click();
        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(category)).click();

        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(N_START_YEAR)).click();
        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(year)).click();

        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(N_START_MONTH)).click();
        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(month)).click();

        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(N_START_DAY)).click();
        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(day)).click();
        sleep(random.nextInt(1000) + 1000);
        webDriver.findElement(new By.ByXPath(N_SEARCH)).click();
    }

    public void moveNextRankPage() throws InterruptedException {
        sleep(random.nextInt(5000) + 2000);
        webDriver.findElement(new By.ByXPath(XPath.N_NEXT_PAGE)).click();
    }
}
