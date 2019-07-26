package module;

import constant.Selector;
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
            List<String> titleListContainRankNum = elementsParser.getElementValuesListBySelector(html, Selector.CATEGORY_KEYWORD_RANK_TITLE);
            List<String> rankNumList = elementsParser.getElementValuesListBySelector(html, Selector.CATEGORY_KEYWORD_RANK_NUM);
            insertRankListToSet(titleListContainRankNum, rankNumList);
            for (Iterator<Map> it = rankSet.iterator(); it.hasNext(); ) {
                Map map = it.next();
                System.out.println(map.toString());
            }
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
            String html = httpConnector.getHtmlFromUrlUseWebdriver(url);
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
        final String ABS_DEST_TEST_HTML_PATH = "/test/home.txt";
        final String WORKING_DIRECTORY = System.getProperty("user.dir");
        final String COPY_PATH = String.format("%s/%s", WORKING_DIRECTORY, ABS_DEST_TEST_HTML_PATH);
        final String TEST_HTML_PATH_IN_RESOURCE = "/test/home.txt";
        try {
            URL webDriverUrlInResource = getClass().getResource(TEST_HTML_PATH_IN_RESOURCE);
            File homeHtml = new File(COPY_PATH);
            FileUtils.copyURLToFile(webDriverUrlInResource, homeHtml);
            return COPY_PATH;
        } catch (IOException e) {
            throw new NaverSearchExtractorException(e, FILE_IO_EX_CODE);
        }
    }
}
