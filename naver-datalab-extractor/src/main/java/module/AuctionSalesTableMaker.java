package module;

import constant.Selector;
import constant.Url;
import exception.NaverSearchExtractorException;
import javafx.util.Pair;
import org.openqa.selenium.WebDriver;
import util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constant.Selector.*;
import static exception.NaverSearchExtractorExceptionCode.UNDEFINED_EX_CODE;
import static java.lang.Thread.sleep;

public class AuctionSalesTableMaker {
    private ElementsParser elementsParser;
    private HttpConnector httpConnector;
    private ResourceUtils resourceUtils;
    private List<Pair> salesList;
    private List<String> salesInfoList;
    private DateUtils dateUtils;
    private String category;

    public void init(String category) {
        this.elementsParser = new ElementsParser();
        this.httpConnector = new HttpConnector();
        this.salesList = new ArrayList<>();
        this.salesInfoList = new ArrayList<>();
        this.dateUtils = new DateUtils();
        this.resourceUtils = new ResourceUtils();
        this.category = category;
    }

    public void run() throws NaverSearchExtractorException, IOException {
        try {
            makeSalesListInNaverShoppingInsight(category);
            searchSalesCountInAuctionBySalesList();
            makeSalesTableToCsv();
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    private void makeSalesListInNaverShoppingInsight(String category) throws NaverSearchExtractorException, InterruptedException {
        try {
            WebDriver chromeWebDriver = resourceUtils.accessChromeWebDriver();
            this.httpConnector.setWebDriver(chromeWebDriver);
            this.httpConnector.movePageByUrl(Url.N_SHOPPING_INSIGHT_HOME);

            String[] startDate = dateUtils.makeParameterArrayByYesterday();
            this.httpConnector.setCategory(category);
            this.httpConnector.searchByStartDate(startDate);

            extractRankDataInShoppingInsight();
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        } finally {
            if (this.httpConnector.getWebDriver() != null)
                this.httpConnector.getWebDriver().quit();
        }
    }

    private void extractRankDataInShoppingInsight() throws NaverSearchExtractorException, InterruptedException {
        for (int page = 1; page <= 5; page++) {
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

    private void searchSalesCountInAuctionBySalesList() throws Exception {
        final int MAX_SEARCH_PAGE = 5;
        for (Pair sales : salesList) {
            String item = sales.getValue().toString();
            for (int page = 1; page <= MAX_SEARCH_PAGE; page++) {
                String searchUrl = null;
                try {
                    searchUrl = String.format(Url.A_STRING_FORMAT_SEARCH_BY_KEYAWORD, item, page);
                    String html = this.httpConnector.getHtmlFromUrl(searchUrl);
                    List<String> itemList = elementsParser.getElementStringListBySelector(html, DIV_ITEM_INFO);
                    extractItemListInfo(itemList, sales);
                    sleep(8000);
                } catch (Exception ex) {
                    System.out.println(String.format("fail in searchSalesCountInAuctionBySalesList. url : %s, msg : %s", searchUrl, ex.getMessage()));
                    continue;
                }
            }
        }
    }

    private void makeSalesTableToCsv() throws IOException {
        resourceUtils.makeSalesTableToCsvByItemInfoList(category, salesInfoList, dateUtils.getYesterdayString());
    }

    private void extractItemListInfo(List<String> itemList, Pair sales) {
        for (String item : itemList) {
            try {
                String rank = sales.getKey().toString();
                String keyword = sales.getValue().toString();
                String title = elementsParser.getElementValueBySelector(item, A_ITEM_TITLE).trim();
                String link = elementsParser.getElementValueBySelector(item, A_ITEM_LINK).trim();
                String seller = elementsParser.getElementValueBySelector(item, SPAN_SELLER).trim();
                String purchase = elementsParser.getElementValueBySelector(item, A_PURCHASE_NUM).replace("구매", "").replace(",", "").trim();
                String review = elementsParser.getElementValueBySelector(item, A_REVIEW_NUM).replace("후기", "").replace(",", "").trim();

                String line = String.format("%s,%s,%s,%s,%s,%s,%s", rank, keyword, title, link, seller, purchase, review);
                salesInfoList.add(line);
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
    }
}

