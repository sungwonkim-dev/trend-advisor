library(data.table)
library(dplyr)
library(tidyr)
library(ggplot2)

rm(list = ls()); gc(reset = T)

options(scipen = 10)

File_Preprocessing <- function(data){
  complete_data <- data.frame() # 전처리된 데이터를 담을 data frame
  
  char_col_idx <- 8 : length(colnames(data)) # char형으로 변환할 column 인덱스
  data[, char_col_idx] <- apply(data[, char_col_idx], 2, as.character) # type을 char형으로 통일시킨 후에 작업
  
  repeat{
    idx <- grep("http", data[, 4]) # 올바른 데이터 인덱스 추출
    
    colnames(data)[4 : 7] <- c("link", "seller", "purchase", "review") # column name 변경
    
    complete_data <- bind_rows(complete_data, data[idx, 1 : 7]) # 최종 데이터 프레임에 합치기
    
    data <- data[-idx, ] # 올바르게 전처리된 데이터를 빼고 완전히 처리되지 않은 데이터만을 다시 덮어씌우기
    
    if(nrow(data) == 0) # 모두 전처리 했다면 나가기
      break
    
    data <- data %>% unite(title, c(colnames(data)[3], colnames(data)[4]), sep = " ") # 처리작업
  }
  
  return (complete_data)
}

data_0729 <- fread("health_20190729.csv", data.table = F, header = T, stringsAsFactors = F) # 전체 데이터
data_0730 <- fread("health_20190730.csv", data.table = F, header = T, stringsAsFactors = F) # 전체 데이터
data_0731 <- fread("health_20190731.csv", data.table = F, header = T, stringsAsFactors = F) # 전체 데이터

data_0729 <- File_Preprocessing(data_0729)
data_0729$date <- as.Date("2019-07-29")
data_0730 <- File_Preprocessing(data_0730)
data_0730$date <- as.Date("2019-07-30")
data_0731 <- File_Preprocessing(data_0731)
data_0731$date <- as.Date("2019-07-31")

threedays_data <- bind_rows(data_0729, data_0730, data_0731)
fwrite(threedays_data, "health_data.csv")

head(threedays_data)

# 랭크에 따라 판매량과 리뷰수를 계산

# (1) char -> numeric으로 형변환 하기
threedays_data[, c(6, 7)] <- apply(threedays_data[, c(6, 7)], 2, as.numeric)

# (2) 결측치 NA는 0으로 채우기
threedays_data[, 6] <- ifelse(is.na(threedays_data[, 6]), 0, threedays_data[, 6])
threedays_data[, 7] <- ifelse(is.na(threedays_data[, 7]), 0, threedays_data[, 7])

# (3) 판매량과 리뷰수의 상관계수 보기
cor(threedays_data$purchase, threedays_data$review) # 0.5764839


# 랭크 변화량과 날짜 별 총 거래량의 관계 보기
pur_rev_data <- threedays_data %>% group_by(date, keyword, rank) %>% summarise(count = n(),
                                                                               total_pur = sum(purchase),
                                                                               mean_pur = round(mean(purchase), 1),
                                                                               total_rev = sum(review),
                                                                               mean_rev = round(mean(review), 1))

# (1) 데이터를 원하는 형태로 만들기 위한 첫 번째 작업 : 원하는 데이터만 잘라서 가져오기
test <- pur_rev_data %>% 
  ungroup() %>% 
  mutate(date = paste("rank", date, sep= "_")) %>% 
  spread(date, rank, fill = 0) %>% select(1, 7 : 9)
test1 <- pur_rev_data %>% 
  ungroup() %>% 
  mutate(date = paste("total_pur", date, sep= "_")) %>% 
  spread(date, total_pur, fill = 0) %>% select(1, 7 : 9)


# (2) 원하는 모양으로 변환
rank_pur_data <- bind_rows(test, test1)
rank_pur_data[is.na(rank_pur_data)] <- 0 # NA는 0으로 채우기
colnames(rank_pur_data) <- gsub("-", "_", colnames(rank_pur_data)) # column name 변환 (관리하기 편하게)

rank_pur_data <- rank_pur_data %>% 
  gather(key = date, value = rank, -keyword) %>% 
  group_by(keyword, date) %>% 
  summarise(rank = sum(rank)) %>% 
  spread(date, rank) %>% mutate(rank_diff_2930 = rank_2019_07_29 - rank_2019_07_30,
                                rank_diff_3031 = rank_2019_07_30 - rank_2019_07_31,
                                total_pur_diff_2930 = total_pur_2019_07_30 - total_pur_2019_07_29,
                                total_pur_diff_3031 = total_pur_2019_07_31 - total_pur_2019_07_30,
                                total_num = total_pur_diff_3031 - total_pur_diff_2930)

rank_pur_data %>% as.data.frame() %>% head()

# (3) threshold를 정하기 위한 이상치 분포보기
# total_purchase 분포
box1 <- boxplot(rank_pur_data$total_pur_diff_2930)
min(box1$out[box1$out > 0]) # 22366
median(box1$out[box1$out > 0]) # 112150
max(box1$out[box1$out < 0]) # -15942
median(box1$out[box$out < 0]) # -98287

box2 <- boxplot(rank_pur_data$total_pur_diff_3031)
median(box2$out[box2$out > 0]) # 46400
min(box2$out[box2$out > 0]) # 8048
max(box2$out[box2$out < 0]) # -8853
median(box2$out[box2$out < 0]) # -22300

# rank 분포
rank_box1 <- boxplot(rank_pur_data$rank_diff_2930)
min(rank_box1$out[rank_box1$out > 0]) # 65
max(rank_box1$out[rank_box1$out < 0]) # -60

rank_box2 <- boxplot(rank_pur_data$rank_diff_3031)
min(rank_box2$out[rank_box2$out > 0]) # 22
max(rank_box2$out[rank_box2$out < 0]) # -21

# (4) 정해진 threshold를 기준으로 필터링
filter_rank_pur_data <- rank_pur_data %>% filter(total_pur_diff_2930 > -98287,
                                                 total_pur_diff_2930 < 112150, 
                                                 total_pur_diff_3031 < 46400,
                                                 total_pur_diff_3031 > -22300)

# (5) 그래프 그려서 분포 및 관계 확인하기
cor.test(rank_pur_data$total_num, rank_pur_data$rank_diff_2930)

coefs <- coef(lm(total_num ~ rank_diff_2930, data = rank_pur_data))
summary(lm(total_num ~ rank_diff_2930, data = rank_pur_data))
ggplot(rank_pur_data, aes(x = rank_diff_2930, y = total_num)) +
  geom_point() +
  geom_abline(intercept = coefs[1], slope = coefs[2], color = "red", size = 2) +
  theme_light() +
  labs(x = "Rank Differentiation 29 ~ 30", y = "Total Purchase Differentiation 29 ~ 30")

coefs <- coef(lm(total_pur_diff_3031 ~ rank_diff_3031, data = filter_rank_pur_data))
ggplot(filter_rank_pur_data, aes(x = rank_diff_3031, y = total_pur_diff_3031)) +
  geom_point() +
  geom_abline(intercept = coefs[1], slope = coefs[2], color = "red", size = 2) +
  theme_light() +
  labs(x = "Rank Differentiation 30 ~ 31", y = "Total Purchase Differentiation 30 ~ 31")

