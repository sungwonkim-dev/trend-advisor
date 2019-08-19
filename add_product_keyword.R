library(dplyr)
library(data.table)
library(stringr)

product <- fread("product_name_utf.csv", data.table = F, header = F, stringsAsFactors = F)
add_keyword <- fread("result.csv", data.table = F, header = F, stringsAsFactors = F)

colnames(product)[1] <- "keyword"
colnames(add_keyword)[1] <- "keyword"

# 키워드 컬럼 빼고 모두 제거
product[, c(2, 3)] <- NULL

# 전체보기가 포함된 키워드 제거
add_keyword <- data.frame(keyword = add_keyword[-which(str_detect(add_keyword$keyword, "전체보기")), ])

# 키워드 병합
all_product <- bind_rows(product, add_keyword)

# 중복 단어 제거
all_product <- unique(all_product)

# 파일 저장 (column, row 이름 불필요)
fwrite(all_product, "product_names.csv", col.names = F, row.names = F)
