package module;

import util.ElementsParser;
import util.HttpConnector;
import util.JSONParser;

public class NaverShoppingInsightExtractor {
    private ElementsParser elementsParser;
    private HttpConnector httpConnector;
    private JSONParser jsonParser;

    protected void init() {
        this.elementsParser = new ElementsParser();
        this.httpConnector = new HttpConnector();
        this.jsonParser = new JSONParser();
    }
}
