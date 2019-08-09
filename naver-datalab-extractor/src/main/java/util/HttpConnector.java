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
    private int sleep = 1000;
    private Random random = new Random();

    public void setWebDriver(WebDriver webDriver, int sleep) {
        this.sleep = sleep;
        this.webDriver = webDriver;
    }

    public WebDriver getWebDriver() {
        return this.webDriver;
    }

    public void movePageByUrl(String url) throws NaverSearchExtractorException {
        try {
            webDriver.get(url);
            sleep(sleep);
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    public String getCurHtmlUseWebdriver() throws NaverSearchExtractorException {
        try {
            sleep(sleep);
            return webDriver.getPageSource();
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    public String getHtmlByUrlUseWebdriver(String url) throws NaverSearchExtractorException {
        try {
            webDriver.get(url);
            sleep(sleep);
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

    public void searchByStartDate(String[] date) throws InterruptedException {

        String year = String.format(N_STRING_FORMAT_START_YEAR, date[0]);
        String month = String.format(N_STRING_FORMAT_START_MONTH, date[1]);
        String day = String.format(N_STRING_FORMAT_START_DAY, date[2]);

        clickElementByXPathWithSleep(N_START_YEAR);
        clickElementByXPathWithSleep(year);

        clickElementByXPathWithSleep(N_START_MONTH);
        clickElementByXPathWithSleep(month);

        clickElementByXPathWithSleep(N_START_DAY);
        clickElementByXPathWithSleep(day);
        clickElementByXPathWithSleep(N_SEARCH);

    }

    public void searchByStartDateAndEndDate(String[] startDate, String[] endDate, String[] lastDate) throws InterruptedException {

        String sYear = String.format(N_STRING_FORMAT_START_YEAR, startDate[0]);
        String sMonth = String.format(N_STRING_FORMAT_START_MONTH, startDate[1]);
        String sDay = String.format(N_STRING_FORMAT_START_DAY, startDate[2]);

        String eYear = String.format(N_STRING_FORMAT_END_YEAR, endDate[0]);
        String eMonth = String.format(N_STRING_FORMAT_END_MONTH, endDate[1]);
        String eDay = String.format(N_STRING_FORMAT_END_DAY, endDate[2]);

        String initYear = String.format(N_STRING_FORMAT_END_YEAR, lastDate[0]);
        String initMonth = String.format(N_STRING_FORMAT_END_MONTH, lastDate[1]);
        String initDay = String.format(N_STRING_FORMAT_END_DAY, lastDate[2]);

        clickElementByXPathWithSleep(N_END_YEAR);
        clickElementByXPathWithSleep(initYear);

        clickElementByXPathWithSleep(N_END_MONTH);
        clickElementByXPathWithSleep(initMonth);

        clickElementByXPathWithSleep(N_END_DAY);
        clickElementByXPathWithSleep(initDay);

        clickElementByXPathWithSleep(N_START_YEAR);
        clickElementByXPathWithSleep(sYear);

        clickElementByXPathWithSleep(N_START_MONTH);
        clickElementByXPathWithSleep(sMonth);

        clickElementByXPathWithSleep(N_START_DAY);
        clickElementByXPathWithSleep(sDay);

        clickElementByXPathWithSleep(N_END_YEAR);
        clickElementByXPathWithSleep(eYear);

        clickElementByXPathWithSleep(N_END_MONTH);
        clickElementByXPathWithSleep(eMonth);

        clickElementByXPathWithSleep(N_END_DAY);
        clickElementByXPathWithSleep(eDay);

        clickElementByXPathWithSleep(N_SEARCH);

        Thread.sleep(sleep);
    }


    public void moveNextRankPageWithSleep() throws InterruptedException {
        sleep(random.nextInt(sleep) + 2000);
        webDriver.findElement(new By.ByXPath(XPath.N_NEXT_PAGE)).click();
    }

    public void setCategory(String category) throws InterruptedException {
        category = String.format(N_STRING_FORMAT_CATEGORY, category);
        clickElementByXPathWithSleep(N_CATEGORY);
        clickElementByXPathWithSleep(category);
    }

    private void clickElementByXPathWithSleep(String xPath) throws InterruptedException {
        sleep(random.nextInt(sleep) + 1000);
        By byXPath = new By.ByXPath(xPath);
        webDriver.findElement(byXPath).click();
    }

    private void clickElementBySelectorWithSleep(String selector) throws InterruptedException {
        sleep(random.nextInt(sleep) + 1000);
        By bySelector = By.cssSelector(selector);
        webDriver.findElement(bySelector).click();
    }

    public void reloadRankInShoppingInsight() throws InterruptedException {
        sleep(random.nextInt(sleep) + 2000);
        webDriver.findElement(new By.ByXPath(XPath.N_NEXT_PAGE)).click();
        sleep(random.nextInt(sleep) + 2000);
        webDriver.findElement(new By.ByXPath(XPath.N_PREV_PAGE)).click();
    }
}
