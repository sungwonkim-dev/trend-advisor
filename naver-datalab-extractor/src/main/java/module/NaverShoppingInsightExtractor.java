package module;

import constant.Selector;
import constant.Url;
import exception.NaverSearchExtractorException;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import util.DateUtils;
import util.ElementsParser;
import util.HttpConnector;
import util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import static exception.NaverSearchExtractorExceptionCode.INVALIDATION_EX_CODE;
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
    private String chromeVersion;
    private int sleep;

    public void init(String category, String startDate, String endDate, String chromeVersion, int sleep) {
        this.elementsParser = new ElementsParser();
        this.httpConnector = new HttpConnector();
        this.resourceUtils = new ResourceUtils();
        this.dateUtils = new DateUtils();
        this.salesList = new ArrayList<>();
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.chromeVersion = chromeVersion;
        this.sleep = sleep;
    }

    public void run() throws NaverSearchExtractorException {
        try {
            WebDriver chromeWebDriver = resourceUtils.accessChromeWebDriver(chromeVersion);
            this.httpConnector.setWebDriver(chromeWebDriver, this.sleep);
            this.httpConnector.movePageByUrl(Url.N_SHOPPING_INSIGHT_HOME);
            this.httpConnector.setCategory(category);

            List<String[]> dateList = dateUtils.makeAllParameterArrayByStartDate(startDate, endDate);
            String[] lastDate = dateUtils.makeParameterArrayByYesterday();
            for (int index = 0; index + 1 < dateList.size(); index = index + 2) {
                try {
                    String[] startDate = dateList.get(index);
                    String[] endDate = dateList.get(index + 1);
                    String date = String.format("%s%s%s~%s%s%s", startDate[0], startDate[1], startDate[2], endDate[0], endDate[1], endDate[2]);
                    this.httpConnector.searchByStartDateAndEndDate(startDate, endDate, lastDate);
                    extractRankDataInShoppingInsight();
                    validateSalesList(date);
                    this.resourceUtils.makeSalesTableToCsvByList(category, salesList, date);
                    this.salesList.clear();
                } catch (NaverSearchExtractorException ex) {
                    if (ex.getExceptionCode() == INVALIDATION_EX_CODE) {
                        System.out.println(ex.getMessage());
                        break;
                    } else throw new Exception(ex);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
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
            //String html = this.httpConnector.getCurHtmlUseWebdriver();
            String html = getVaildHtml();
            List<String> itemListContainRankNum = elementsParser.getElementValuesListBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_TITLE);
            List<String> rankNumList = elementsParser.getElementValuesListBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_NUM);

            if (itemListContainRankNum != null && rankNumList != null && itemListContainRankNum.size() == rankNumList.size())
                for (int index = 0; index < itemListContainRankNum.size(); index++) {
                    String rankNum = rankNumList.get(index).trim();
                    String title = itemListContainRankNum.get(index).trim();
                    Pair<String, String> sales = new Pair<>(rankNum, title);
                    salesList.add(sales);
                }
            this.httpConnector.moveNextRankPageWithSleep();
        }
    }

    private String getVaildHtml() throws NaverSearchExtractorException, InterruptedException {
        String html = null;
        boolean isInVaildHtml = true;
        while (isInVaildHtml) {
            html = this.httpConnector.getCurHtmlUseWebdriver();
            String firstNumInHtml = elementsParser.getFirstElementValueBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_NUM).trim();
            String firstTitleInHtml = elementsParser.getFirstElementValueBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_TITLE).trim();

            if (isContainValuesInSalesList(firstTitleInHtml, firstNumInHtml)) {
                System.out.println("reload start by rank and title  " + firstNumInHtml + " " + firstTitleInHtml);
                this.httpConnector.reloadRankInShoppingInsight();
            } else {
                isInVaildHtml = false;
            }
        }
        return html;
    }

    private boolean isContainValuesInSalesList(String title, String num) {
        boolean isContains = false;
        for (Pair pair : salesList) {
            String key = pair.getKey().toString();
            String value = pair.getValue().toString();
            if (StringUtils.equals(key, num) && !StringUtils.equals(value, title)) {
                isContains = true;
                break;
            }
        }
        return isContains;
    }

    private void validateSalesList(String date) throws NaverSearchExtractorException {
        if (salesList.size() != 500)
            throw new NaverSearchExtractorException(String.format("salesList size was not 500. date : %s", date), INVALIDATION_EX_CODE);
        for (int index = 0; index < salesList.size(); index++) {
            int key = Integer.parseInt(salesList.get(index).getKey().toString().trim());
            if (key != index + 1)
                throw new NaverSearchExtractorException(String.format("duplicated index. : %d", key), INVALIDATION_EX_CODE);
        }
    }
}
