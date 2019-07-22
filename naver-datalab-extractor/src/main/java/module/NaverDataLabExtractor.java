package module;

import exception.NaverSearchExtractorException;
import org.openqa.selenium.WebDriver;
import utils.ElementsParser;
import utils.HttpConnector;
import utils.JSONParser;
import utils.WebDriverUtils;

public class NaverDataLabExtractor {
    private ElementsParser elementsParser;
    private HttpConnector httpConnector;
    private JSONParser jsonParser;
    private WebDriverUtils webDriverUtils;

    public void init() {
        this.elementsParser = new ElementsParser();
        this.httpConnector = new HttpConnector();
        this.jsonParser = new JSONParser();
        this.webDriverUtils = new WebDriverUtils();
    }

    public void test() throws NaverSearchExtractorException {
        WebDriver webDriver = null;
        try {
            webDriver = webDriverUtils.accessChromeWebDriver();
            httpConnector.setWebDriver(webDriver);
            String html = httpConnector.getHtmlFromUrlUseWebdriver("https://datalab.naver.com/shoppingInsight/sCategory.naver");
            System.out.println(html);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (webDriver != null) webDriver.quit();
        }
    }
}
