package util;

import constant.Path;
import exception.NaverSearchExtractorException;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static constant.Path.WEBDRIVER_COPY_PATH;
import static constant.Path.WORKING_DIRECTORY;
import static exception.NaverSearchExtractorExceptionCode.*;

public class ResourceUtils {

    public WebDriver accessChromeWebDriver(String chromeVersion) throws NaverSearchExtractorException {
        ChromeDriver chromeDriver = null;
        try {
            createChromeDriver(chromeVersion);
            chromeDriver = new ChromeDriver();
            return chromeDriver;
        } catch (Exception ex) {
            if (chromeDriver != null)
                chromeDriver.quit();
            throw new NaverSearchExtractorException(ex, UNDEFINED_EX_CODE);
        }
    }

    private void createChromeDriver(String chromeVersion) throws NaverSearchExtractorException {
        try {
            String path = makeWebDriverPathByChromeVersion(chromeVersion);
            URL webDriverUrlInResource = getClass().getResource(path);
            File chromeDriver = new File(WEBDRIVER_COPY_PATH);
            FileUtils.copyURLToFile(webDriverUrlInResource, chromeDriver);
            System.setProperty(Path.CHROME_WEBDRIVER_ID, WEBDRIVER_COPY_PATH);
        } catch (IOException e) {
            throw new NaverSearchExtractorException(e, FILE_IO_EX_CODE);
        }
    }

    public void makeSalesTableToCsvByItemInfoList(String category, List<String> itemInfoList, String date) throws IOException {
        File csv = new File(String.format("%s/%s_%s.csv", WORKING_DIRECTORY, category, date));
        if (csv.exists())
            csv.delete();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csv));

        bufferedWriter.write("rank,keyword,title,link,seller,purchase,review");
        bufferedWriter.newLine();
        for (String line : itemInfoList) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public void makeSalesTableToCsvByList(String category, List<Pair> salesList, String date) throws IOException {
        File csv = new File(String.format("%s/%s_%s.csv", WORKING_DIRECTORY, category, date));
        if (csv.exists())
            csv.delete();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csv));

        bufferedWriter.write("rank,item");
        bufferedWriter.newLine();
        for (int index = 0; index < salesList.size(); index++) {
            String rank = salesList.get(index).getKey().toString().trim();
            String item = salesList.get(index).getValue().toString().replace(rank, "").trim();

            bufferedWriter.write(String.format("%s,%s", rank, item));
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();

        System.out.println(String.format("saved file path : %s", csv.getPath()));
    }

    private String makeWebDriverPathByChromeVersion(String chromeVersion) throws NaverSearchExtractorException {
        if (chromeVersion == null || chromeVersion.length() < 1)
            throw new NaverSearchExtractorException("chrome vesion parameter is empty.", INVALID_VALUE_EX_CODE);
        String path = null;
        if (StringUtils.equals(chromeVersion, "74"))
            path = Path.WEBDRIVER_74_PATH_IN_RESOURCE;
        else if (StringUtils.equals(chromeVersion, "75"))
            path = Path.WEBDRIVER_75_PATH_IN_RESOURCE;
        return path;
    }
}
