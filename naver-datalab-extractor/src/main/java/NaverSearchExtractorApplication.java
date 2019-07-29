import module.AuctionSalesTableMaker;
import module.NaverShoppingInsightExtractor;

public class NaverSearchExtractorApplication {
    public static void main(String args[]) throws Exception {

        if (args.length != 2 && args.length != 4)
            printUsage();

        String mode = args[0].trim();
        String category = args[1].trim();
        switch (mode) {
            case "proof": {
                AuctionSalesTableMaker auctionSalesTableMaker = new AuctionSalesTableMaker();
                auctionSalesTableMaker.init(category);
                auctionSalesTableMaker.run();
                break;
            }
            case "rank": {
                String startDate = args[2];
                String endDate = args[3];
                NaverShoppingInsightExtractor naverShoppingInsightExtractor = new NaverShoppingInsightExtractor();
                naverShoppingInsightExtractor.init(category, startDate, endDate);
                naverShoppingInsightExtractor.run();
                break;
            }
            default: {
                System.out.println(String.format("%s mode is not yet supported", mode));
                System.exit(0);
                break;
            }
        }
    }

    private static void printUsage() {
        System.out.println("============================================================================================================================");
        System.out.println("      usage : java -jar naver-datalab-extractor-1.0.6-jar-with-dependencies.jar {proof|rank} {category} {start} {end}");
        System.out.println("-----------------------------------------------------------mode-------------------------------------------------------------");
        System.out.println("             proof - 상관 관계 증명 데이터 저장");
        System.out.println("                ex : java -jar naver-datalab-extractor-1.0.4-jar-with-dependencies.jar proof 건강");
        System.out.println();
        System.out.println("              rank - 쇼핑 인사이트 검색어 랭킹 저장");
        System.out.println("                ex : java -jar naver-datalab-extractor-1.0.4-jar-with-dependencies.jar rank 건강 2017/08/01 2019/07/27");
        System.out.println();
        System.out.println("-----------------------------------------------------------category---------------------------------------------------------");
        System.out.println("                   - 네이버 쇼핑 인사이트의 대분류 작성");
        System.out.println("                ex :  패션의류, 패션잡화...");
        System.out.println("-----------------------------------------------------------result-----------------------------------------------------------");
        System.out.println("             proof : csv file");
        System.out.println("                       - rank, item, purchase, review");
        System.out.println("              rank : csv file");
        System.out.println("                       - rank, item");
        System.out.println("-----------------------------------------------------------warning----------------------------------------------------------");
        System.out.println("           warning - 실행 옵션 중 start >= 20170801, end < today 조건을 만족하지 않는다면 실행되지 않습니다");
        System.out.println("                   - 프로그램 실행 중 임의로 브라우져를 조작한다면 프로세스가 진행되지 않습니다.");
        System.out.println("============================================================================================================================");
        System.exit(0);
    }
}
