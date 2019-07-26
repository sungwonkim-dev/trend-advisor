package module;

import exception.NaverSearchExtractorException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import util.ElementsParser;
import util.HttpConnector;
import util.JSONParser;
import util.WebDriverUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static constant.Path.TEST_HTML_COPY_PATH;
import static constant.Path.TEST_HTML_PATH_IN_RESOURCE;
import static exception.NaverSearchExtractorExceptionCode.FILE_IO_EX_CODE;

public class NaverDataLabExtractor {
    private ElementsParser elementsParser;
    private HttpConnector httpConnector;
    private JSONParser jsonParser;
    private WebDriverUtils webDriverUtils;
    private Set<Map> rankSet;

    public void init() {
        this.elementsParser = new ElementsParser();
        this.httpConnector = new HttpConnector();
        this.jsonParser = new JSONParser();
        this.webDriverUtils = new WebDriverUtils();
        this.rankSet = new HashSet<>();
    }

    public void run() throws NaverSearchExtractorException, IOException {
        try {
            String html = readHtmlFromTestFileOnTest();
//            List<String> titleListContainRankNum = elementsParser.getElementValuesListBySelector(html, Selector.CATEGORY_KEYWORD_RANK_TITLE);
//            List<String> rankNumList = elementsParser.getElementValuesListBySelector(html, Selector.CATEGORY_KEYWORD_RANK_NUM);
//            insertRankListToSet(titleListContainRankNum, rankNumList);
//            for (Iterator<Map> it = rankSet.iterator(); it.hasNext(); ) {
//                Map map = it.next();
//                System.out.println(map.toString());
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertRankListToSet(List<String> titleListContainRankNum, List<String> rankNumList) {
        if (!titleListContainRankNum.isEmpty() && (titleListContainRankNum.size() == rankNumList.size())) {
            Map<String, String> rank = new HashMap<>();
            for (int index = 0; index < titleListContainRankNum.size(); index++) {
                String rankNum = rankNumList.get(index).trim();
                String title = titleListContainRankNum.get(index).replace(rankNum, "").trim();
                rank.put(rankNum, title);
            }
            this.rankSet.add(rank);
        }
    }

    public void printHtmlByUrlOnTest(String url) throws NaverSearchExtractorException {
        WebDriver webDriver = null;
        try {
            webDriver = webDriverUtils.accessChromeWebDriver();
            httpConnector.setWebDriver(webDriver);
            String html = httpConnector.getHtmlByUrlUseWebdriver(url);
            System.out.println(html);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (webDriver != null) {
                webDriver.quit();
                webDriver.close();
            }
        }
    }

    public String readHtmlFromTestFileOnTest() throws NaverSearchExtractorException, IOException {
        String testHtmlPath = createTestHtmlFileReturnPath();
        byte[] encoded = Files.readAllBytes(Paths.get(testHtmlPath));
        return new String(encoded, "euc-kr");
    }

    private String createTestHtmlFileReturnPath() throws NaverSearchExtractorException {
        try {
            URL webDriverUrlInResource = getClass().getResource(TEST_HTML_PATH_IN_RESOURCE);
            File homeHtml = new File(TEST_HTML_COPY_PATH);
            FileUtils.copyURLToFile(webDriverUrlInResource, homeHtml);
            return TEST_HTML_COPY_PATH;
        } catch (IOException e) {
            throw new NaverSearchExtractorException(e, FILE_IO_EX_CODE);
        }
    }
}
