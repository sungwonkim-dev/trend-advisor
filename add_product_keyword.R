library(dplyr)
library(data.table)
library(stringr)

product <- fread("dataset_all_utf.csv", data.table = F, header = F, stringsAsFactors = F)
add_keyword <- fread("result.csv", data.table = F, header = F, stringsAsFactors = F)
product2 <- fread("product_name_utf.csv", data.table = F, header = F, stringsAsFactors = F)

colnames(product)[1] <- "keyword"
colnames(add_keyword)[1] <- "keyword"
colnames(product2)[1] <- "keyword"

str(product)
str(add_keyword)
str(product2)

# 키워드 컬럼 빼고 모두 제거
product2[, c(2, 3)] <- NULL

# 전체보기가 포함된 키워드 제거
add_keyword <- data.frame(keyword = add_keyword[-which(str_detect(add_keyword$keyword, "전체보기")), ])

# 키워드 병합
all_product <- bind_rows(product, add_keyword, product2)
str(all_product)

# 중복 단어 제거
all_product <- unique(all_product)

# 파일 저장 (column, row 이름 불필요)
fwrite(all_product, "products.csv", col.names = F, row.names = F)


# 파트를 나눠 아이템 키워드 선별
part1 <- fread("products_copy.csv", header = F, data.table = F, stringsAsFactors = F)
part2 <- fread("products_part1_utf.csv", header = F, data.table = F, stringsAsFactors = F)
part3 <- fread("products_part2_utf.csv", header = F, data.table = F, stringsAsFactors = F)

str(part1)
str(part2)
str(part3)

all_products <- bind_rows(part1, part2, part3)
all_products <- all_products %>% filter(V1 != "")
fwrite(all_products, "filtering_keywords.csv", col.names = F, row.names = F)

