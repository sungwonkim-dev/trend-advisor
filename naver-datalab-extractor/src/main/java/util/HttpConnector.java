package util;

import exception.NaverSearchExtractorException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.openqa.selenium.WebDriver;

import static exception.NaverSearchExtractorExceptionCode.UNDEFINED_EX_CODE;

public class HttpConnector {
    private WebDriver webDriver;

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getHtmlFromUrlUseWebdriver(String url) throws NaverSearchExtractorException {
        try {
            webDriver.get(url);
            Thread.sleep(5000);
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
}
