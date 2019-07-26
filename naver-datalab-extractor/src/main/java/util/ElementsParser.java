package util;

import exception.NaverSearchExtractorException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static exception.NaverSearchExtractorExceptionCode.*;

public class ElementsParser {

    private final int CSSQUERY_INDEX = 0;
    private final int ATTR_KEY_INDEX = 1;
    private final int POSITION_INDEX = 2;

    public String getFirstElementValueBySelector(String html, String selector) throws NaverSearchExtractorException {
        Document document = convertHtmlToDocument(html);
        String[] convertedSelectorArray = convertSelctorToArray(selector);
        Elements elements = selectElementsByCssQuery(document, convertedSelectorArray[CSSQUERY_INDEX]);
        Element element = getFirstElement(elements);
        return parseAttrByAttrKey(element, convertedSelectorArray[ATTR_KEY_INDEX]);
    }

    public String getElementValueBySelector(String html, String selector) throws NaverSearchExtractorException {
        Document document = convertHtmlToDocument(html);
        String[] convertedSelectorArray = convertSelctorToArray(selector);
        Elements elements = selectElementsByCssQuery(document, convertedSelectorArray[CSSQUERY_INDEX]);
        Element element = getElementByIndex(elements, convertedSelectorArray[POSITION_INDEX]);
        return parseAttrByAttrKey(element, convertedSelectorArray[ATTR_KEY_INDEX]);
    }

    public List<String> getElementValuesListBySelector(String html, String selector) throws NaverSearchExtractorException {
        Document document = convertHtmlToDocument(html);
        String[] convertedSelectorArray = convertSelctorToArray(selector);
        Elements elements = selectElementsByCssQuery(document, convertedSelectorArray[CSSQUERY_INDEX]);
        List<String> attrList = parseAttrList(elements, convertedSelectorArray[ATTR_KEY_INDEX]);
        return attrList;
    }

    private List<String> parseAttrList(Elements elements, String attrKey) throws NaverSearchExtractorException {
        List<String> attrList = new ArrayList<String>();

        for (Element element : elements) {
            String attr = parseAttrByAttrKey(element, attrKey);
            if (StringUtils.isNotBlank(attr))
                attrList.add(attr);
        }
        return attrList;
    }

    private Element getElementByIndex(Elements elements, String index) throws NaverSearchExtractorException {
        int position = 0;
        try {
            position = Integer.parseInt(index);
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(String.format("%s can't convert to Integer.", index), INVALID_VALUE_EX_CODE);
        }
        if (elements.size() <= position)
            throw new NaverSearchExtractorException(String.format("elements size : %d , position : %d. position must over than elements size ", elements.size(), position), INVALID_VALUE_EX_CODE);
        return elements.get(position);
    }

    private Element getFirstElement(Elements elements) {
        return elements.first();
    }

    private Elements selectElementsByCssQuery(Document document, String cssQuery) throws NaverSearchExtractorException {
        Elements elements = document.select(cssQuery);
        if (elements == null || elements.size() == 0)
            throw new NaverSearchExtractorException(String.format("%s is invaild cssQuery", cssQuery), INVALID_VALUE_EX_CODE);
        return elements;
    }

    private String parseAttrByAttrKey(Element element, String attrKey) throws NaverSearchExtractorException {
        String attr = null;
        if (StringUtils.equals(attrKey, "text"))
            attr = element.text();
        else if (StringUtils.equals(attrKey, "id"))
            attr = element.id();
        else attr = element.attr(attrKey);

        if (StringUtils.isBlank(attr))
            throw new NaverSearchExtractorException("selector is blank. please check input selector.", STRING_BLANK_EX_CODE);

        return attr;
    }

    private void checkVaildSelector(String selector) throws NaverSearchExtractorException {
        if (StringUtils.isBlank(selector))
            throw new NaverSearchExtractorException("selector is blank. please check input selector.", STRING_BLANK_EX_CODE);
        String SELECTOR_SPLITTER = "//";
        if (StringUtils.containsNone(selector, SELECTOR_SPLITTER))
            throw new NaverSearchExtractorException(String.format("%s is invaild selector", selector), INVALID_VALUE_EX_CODE);
    }

    private String[] convertSelctorToArray(String selector) throws NaverSearchExtractorException {
        checkVaildSelector(selector);
        return selector.split("//");
    }

    private Document convertHtmlToDocument(String html) throws NaverSearchExtractorException {
        if (StringUtils.isBlank(html))
            throw new NaverSearchExtractorException("target html is blank. please check input URL.", STRING_EMPTY_EX_CODE);

        Document document = null;
        try {
            document = Jsoup.parse(html);
            if (document == null || !document.hasText())
                throw new NaverSearchExtractorException("Wrong html. please check input html.", DOCUMENT_PARSE_EX_CODE);
            return document;
        } catch (Exception ex) {
            throw new NaverSearchExtractorException(ex, DOCUMENT_PARSE_EX_CODE);
        }
    }
}
