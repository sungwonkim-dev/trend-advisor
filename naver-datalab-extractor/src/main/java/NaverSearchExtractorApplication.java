import exception.NaverSearchExtractorException;
import module.AuctionSalesTableMaker;
import module.NaverDataLabExtractor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class NaverSearchExtractorApplication {
    public static void main(String args[]) throws NaverSearchExtractorException, IOException {
        if (args.length != 2)
            printUsage();

        String mode = args[0];
        String category = args[1].trim();

        if (StringUtils.equals(mode, "proof")) {
            AuctionSalesTableMaker auctionSalesTableMaker = new AuctionSalesTableMaker();
            auctionSalesTableMaker.init(category);
            auctionSalesTableMaker.run();
        } else {
            System.out.println(String.format("%s mode is not yet supported", mode));
        }
    }

    private static void printUsage() {
        System.out.println("====================================================================================================================");
        System.out.println("    usage : java -jar naver-datalab-extractor-0.0.1-dev-jar-with-dependencies.jar {proof|rank|portal} {category}");
        System.out.println("    ex : java -jar naver-datalab-extractor-0.0.1-dev-jar-with-dependencies.jar proof 식품");
        System.out.println("    --------------------------mode--------------------------");
        System.out.println("    proof - 상관 관계 증명 데이터 저장");
        System.out.println("    rank  - 쇼핑 인사이트 검색어 랭킹 저장 (개발 예정)");
        System.out.println("    portal - 포털 검색어 랭킹 저장 (개발 예정)");
        System.out.println();
        System.out.println("    --------------------------category--------------------------");
        System.out.println("    네이버 쇼핑 인사이트의 대분류 작성");
        System.out.println("    ex :  패션의류, 패션잡화...");
        System.out.println();
        System.out.println("    --------------------------result--------------------------");
        System.out.println("    proof  : csv file");
        System.out.println("        - rank, item, purchase, review");
        System.out.println("    rank   : undefined");
        System.out.println("    portal : undefined");
        System.out.println("====================================================================================================================");
        System.exit(0);
    }
}
