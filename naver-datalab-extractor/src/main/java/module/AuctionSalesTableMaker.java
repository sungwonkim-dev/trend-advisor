package module;

import constant.Selector;
import constant.Url;
import exception.NaverSearchExtractorException;
import javafx.util.Pair;
import org.openqa.selenium.WebDriver;
import util.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constant.Path.WORKING_DIRECTORY;
import static exception.NaverSearchExtractorExceptionCode.UNDEFINED_EX_CODE;
import static java.lang.Thread.sleep;

public class AuctionSalesTableMaker {
    private ElementsParser elementsParser;
    private HttpConnector httpConnector;
    private JSONParser jsonParser;
    private WebDriverUtils webDriverUtils;
    private List<Pair> salesList;
    private List<Pair> salesCountList;
    private DateUtils dateUtils;
    private String category;

    public void init(String category) {
        this.elementsParser = new ElementsParser();
        this.jsonParser = new JSONParser();
        this.httpConnector = new HttpConnector();
        this.salesList = new ArrayList<>();
        this.salesCountList = new ArrayList<>();
        this.dateUtils = new DateUtils();
        this.webDriverUtils = new WebDriverUtils();
        this.category = category;
    }

    public void run() throws NaverSearchExtractorException, IOException {
        try {
            makeSalesListInNaverShoppingInsight(category);
            searchSalesCountInAuctionBySalesList();
            makeCsvFileByResult(category);
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    private void makeSalesListInNaverShoppingInsight(String category) throws NaverSearchExtractorException, InterruptedException {
        try {
            WebDriver chromeWebDriver = webDriverUtils.accessChromeWebDriver();
            this.httpConnector.setWebDriver(chromeWebDriver);
            this.httpConnector.movePageByUrl(Url.N_SHOPPING_INSIGHT_HOME);

            String[] dateParameter = dateUtils.makeParameterArrayByYesterday();
            this.httpConnector.searchByParameter(category, dateParameter);

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
            httpConnector.moveNextRankPage();
        }
    }

    private void searchSalesCountInAuctionBySalesList() throws Exception {
        final int MAX_SEARCH_PAGE = 5;
        for (Pair sales : salesList) {
            int purchaseNum = 0;
            int review = 0;
            String item = sales.getValue().toString();
            for (int page = 1; page <= MAX_SEARCH_PAGE; page++) {
                String searchUrl = null;
                try {
                    searchUrl = String.format(Url.A_STRING_FORMAT_SEARCH_BY_KEYAWORD, item, page);
                    String html = this.httpConnector.getHtmlFromUrl(searchUrl);
                    purchaseNum = purchaseNum + countPurchaseNum(html);
                    review = review + countReview(html);
                    sleep(8000);
                } catch (Exception ex) {
                    System.out.println(String.format("fail in searchSalesCountInAuctionBySalesList. url : %s, msg : %s", searchUrl, ex.getMessage()));
                    continue;
                }
            }
            Pair<String, String> counts = new Pair<>(String.valueOf(purchaseNum), String.valueOf(review));
            salesCountList.add(counts);
        }
    }

    private int countPurchaseNum(String html) throws NaverSearchExtractorException {
        List<String> purchaseNumList = elementsParser.getElementValuesListBySelector(html, Selector.A_PURCHASE_NUM);
        int total = 0;
        for (String record : purchaseNumList) {
            record = record.replace("구매", "").replace(",", "").trim();
            total = total + Integer.parseInt(record);
        }
        return total;
    }

    private int countReview(String html) throws NaverSearchExtractorException {
        List<String> reviewList = elementsParser.getElementValuesListBySelector(html, Selector.A_REVIEW_NUM);
        int total = 0;
        for (String record : reviewList) {
            record = record.replace("후기", "").replace(",", "").trim();
            total = total + Integer.parseInt(record);
        }
        return total;
    }

    private void makeCsvFileByResult(String category) throws IOException {
        File csv = new File(String.format("%s/%s_%s.csv", WORKING_DIRECTORY, category, dateUtils.getYesterdayString()));
        if (csv.exists())
            csv.delete();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csv));

        bufferedWriter.write("rank,item,purchase,review");
        bufferedWriter.newLine();
        for (int index = 0; index < salesCountList.size(); index++) {
            String rank = salesList.get(index).getKey().toString().trim();
            String item = salesList.get(index).getValue().toString().trim();
            String purchase = salesCountList.get(index).getKey().toString().trim();
            String review = salesCountList.get(index).getValue().toString().trim();

            bufferedWriter.write(String.format("%s,%s,%s,%s", rank, item, purchase, review));
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
