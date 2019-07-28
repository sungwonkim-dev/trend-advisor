package module;

import constant.Selector;
import constant.Url;
import exception.NaverSearchExtractorException;
import javafx.util.Pair;
import org.openqa.selenium.WebDriver;
import util.DateUtils;
import util.ElementsParser;
import util.HttpConnector;
import util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import static exception.NaverSearchExtractorExceptionCode.UNDEFINED_EX_CODE;
import static java.lang.Thread.sleep;

public class NaverShoppingInsightExtractor {
    private ElementsParser elementsParser;
    private HttpConnector httpConnector;
    private ResourceUtils resourceUtils;
    private DateUtils dateUtils;
    private List<Pair> salesList;
    private String category;
    private String startDate;
    private String endDate;

    public void init(String category, String startDate, String endDate) {
        this.elementsParser = new ElementsParser();
        this.httpConnector = new HttpConnector();
        this.resourceUtils = new ResourceUtils();
        this.dateUtils = new DateUtils();
        this.salesList = new ArrayList<>();
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void run() throws NaverSearchExtractorException {
        try {
            WebDriver chromeWebDriver = resourceUtils.accessChromeWebDriver();
            this.httpConnector.setWebDriver(chromeWebDriver);
            this.httpConnector.movePageByUrl(Url.N_SHOPPING_INSIGHT_HOME);
            this.httpConnector.setCategory(category);

            List<String[]> dateList = dateUtils.makeAllParameterArrayByStartDate(startDate, endDate);

            for (int index = 0; index + 1 < dateList.size(); index++) {
                String[] startDate = dateList.get(index);
                String[] endDate = dateList.get(index + 1);
                String date = String.format("%s%s%s", startDate[0], startDate[1], startDate[2]);
                this.httpConnector.searchByStartDateAndEndDate(startDate, endDate);
                extractRankDataInShoppingInsight();
                this.resourceUtils.makeSalesTableToCsvByList(category, salesList, date);
                this.salesList.clear();
                sleep(10000);
            }
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        } finally {
            if (this.httpConnector.getWebDriver() != null)
                this.httpConnector.getWebDriver().quit();
        }
    }

    private void extractRankDataInShoppingInsight() throws NaverSearchExtractorException, InterruptedException {
        for (int page = 1; page <= 25; page++) {
            String html = this.httpConnector.getCurHtmlUseWebdriver();
            List<String> itemListContainRankNum = elementsParser.getElementValuesListBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_TITLE);
            List<String> rankNumList = elementsParser.getElementValuesListBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_NUM);

            if (itemListContainRankNum != null && rankNumList != null && itemListContainRankNum.size() == rankNumList.size())
                for (int index = 0; index < itemListContainRankNum.size(); index++) {
                    String rankNum = rankNumList.get(index).trim();
                    String title = itemListContainRankNum.get(index).replace(rankNum, "").trim();
                    Pair<String, String> sales = new Pair<>(rankNum, title);
                    salesList.add(sales);
                }
            this.httpConnector.moveNextRankPageWithSleep();
        }
    }
}
