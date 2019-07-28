package module;

/**
 *  설계상 필요하지 않아 개발하지 않음. 추후 필요하다면 개발 재개. (sungwonkim-dev, 2019,07,28)
 */
public class NaverDataLabExtractor {
//    private ElementsParser elementsParser;
//    private HttpConnector httpConnector;
//    private JSONParser jsonParser;
//    private ResourceUtils resourceUtils;
//    private Set<Map> rankSet;
//
//    public void init() {
//        this.elementsParser = new ElementsParser();
//        this.httpConnector = new HttpConnector();
//        this.jsonParser = new JSONParser();
//        this.resourceUtils = new ResourceUtils();
//        this.rankSet = new HashSet<>();
//    }
//
//    public void run() throws NaverSearchExtractorException, IOException {
//        try {
//            String html = readHtmlFromTestFileOnTest();
//            List<String> titleListContainRankNum = elementsParser.getElementValuesListBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_TITLE);
//            List<String> rankNumList = elementsParser.getElementValuesListBySelector(html, Selector.N_CATEGORY_KEYWORD_RANK_NUM);
//            insertRankListToSet(titleListContainRankNum, rankNumList);
//            for (Iterator<Map> it = rankSet.iterator(); it.hasNext(); ) {
//                Map map = it.next();
//                System.out.println(map.toString());
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void insertRankListToSet(List<String> titleListContainRankNum, List<String> rankNumList) {
//        if (!titleListContainRankNum.isEmpty() && (titleListContainRankNum.size() == rankNumList.size())) {
//            Map<String, String> rank = new HashMap<>();
//            for (int index = 0; index < titleListContainRankNum.size(); index++) {
//                String rankNum = rankNumList.get(index).trim();
//                String title = titleListContainRankNum.get(index).replace(rankNum, "").trim();
//                rank.put(rankNum, title);
//            }
//            this.rankSet.add(rank);
//        }
//    }
//
//    public void printHtmlByUrlOnTest(String url) throws NaverSearchExtractorException {
//        WebDriver webDriver = null;
//        try {
//            webDriver = resourceUtils.accessChromeWebDriver();
//            httpConnector.setWebDriver(webDriver);
//            String html = httpConnector.getHtmlByUrlUseWebdriver(url);
//            System.out.println(html);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            if (webDriver != null) {
//                webDriver.quit();
//                webDriver.close();
//            }
//        }
//    }
//
//    public String readHtmlFromTestFileOnTest() throws NaverSearchExtractorException, IOException {
//        String testHtmlPath = createTestHtmlFileReturnPath();
//        byte[] encoded = Files.readAllBytes(Paths.get(testHtmlPath));
//        return new String(encoded, "euc-kr");
//    }
//
//    private String createTestHtmlFileReturnPath() throws NaverSearchExtractorException {
//        try {
//            URL webDriverUrlInResource = getClass().getResource(TEST_HTML_PATH_IN_RESOURCE);
//            File homeHtml = new File(TEST_HTML_COPY_PATH);
//            FileUtils.copyURLToFile(webDriverUrlInResource, homeHtml);
//            return TEST_HTML_COPY_PATH;
//        } catch (IOException e) {
//            throw new NaverSearchExtractorException(e, FILE_IO_EX_CODE);
//        }
//    }
}
