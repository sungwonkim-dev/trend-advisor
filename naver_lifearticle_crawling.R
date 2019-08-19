library(rvest)
library(dplyr)
library(stringr)
library(lubridate)
library(data.table)

rm(list = ls()); gc(reset = T)

# 상단의 메뉴 리스트 url 100 ~ 105
# 100 : 정치, 101 : 경제, 102 : 사회, 103 : 생활/문화, 104 : 세계, 105 : IT/과학
# https://news.naver.com/main/main.nhn?mode=LSD&mid=shm&sid1=
menu_num <- 103
class_name <- "생활/문화"
main_url <- "https://news.naver.com"


# 수집할 데이터 column
# 1. company
# 2. date
# 3. class
# 4. subclass
# 5. article title
# 6. article content


repeat{
  
  # 서브카테고리 url
  main_html <- read_html(paste0("https://news.naver.com/main/main.nhn?mode=LSD&mid=shm&sid1=", menu_num))
  subclass_url <- main_html %>% html_nodes("#snb li a") %>% html_attr("href")
  subclass_name <- main_html %>% html_nodes("#snb li") %>% html_text() %>% trimws()
  
  # 속보제거
  subclass_len <- length(subclass_url)
  subclass_url <- subclass_url[1 : subclass_len - 1]
  subclass_name <- subclass_name[1 : subclass_len - 1]

  # 서브카테고리 이름 사이에 빈칸이 있다면 언더바로 변경
  subclass_name <- gsub(" ", "_", subclass_name)
  
  # 각 세부 카테고리 접근
  for(i in 1 : subclass_len){
    
    # 날짜 주소 덧붙이기. 17, 18년도 데이터 모으기
    year <- "2017"
    month <- "08"
    day <- "00"
    date <- paste0(year, month, day)
    data <- data.frame()
    
    while(date != "20190101"){
      day <- as.character(as.numeric(day) + 1)
      
      if(day == "32"){
        day <- "01"
        month <- as.character(as.numeric(month) + 1)
      } else if(nchar(day) == 1){ day <- paste0("0", day) }
      
      if(month == "13"){
        month <- "01"
        year <- as.character(as.numeric(year) + 1)
      } else if(nchar(month) == 1){ month <- paste0("0", month) }
      
      date <- paste0(year, month, day)
      
      # url에 날짜 붙이기
      subclass_url_part <- paste0(main_url, subclass_url[i], "&date=", date)
      subclass_html_part <- read_html(subclass_url_part)
      
      # 없는 날짜 비교
      day_check <- subclass_html_part %>% html_node(".viewday") %>% html_text() %>% as.Date(format = "%m월%d일") %>% day()
      if(day_check != as.numeric(day))
        next
      
      # url에 페이지 번호 붙이기
      page_nums <- subclass_html_part %>% html_nodes(".paging") %>% html_text() %>% 
        trimws() %>% str_split(pattern = "\n", simplify = T) %>% as.vector()
    
      # 페이지 접근
      for(j in page_nums){
        subclass_html <- read_html(paste0(subclass_url_part, "&page=", j))
        
        companys <- subclass_html %>% html_nodes("#main_content .writing") %>% html_text()
        dates <- (subclass_html %>% html_nodes("#main_content .date") %>% html_text() %>% 
          str_split(pattern = " ", simplify = T))[, 1] %>% as.Date(format = "%Y.%m.%d.")
        
        article_urls <- subclass_html %>% 
          html_nodes("#main_content .type06_headline dt:first_child a") %>% html_attr("href")
        article_urls <- c(article_urls, subclass_html %>% 
                           html_nodes("#main_content .type06 dt:first_child a") %>% html_attr("href"))
        
        titles <<- c()
        contents <<- c()
        for(k in 1 : length(article_urls)){
          tryCatch({
            titles <- c(titles, read_html(article_urls[k]) %>% html_node(".article_info #articleTitle") %>% html_text())
            contents <- c(contents, (read_html(article_urls[k]) %>% html_node("#articleBodyContents") %>% html_text() %>% 
                                       str_split(pattern = "\\}", simplify = T))[, 2] %>% trimws())
            },
            error = function(e){
              print(e)
              titles <<- c(titles, "")
              contents <<- c(contents, "")
            },
            finally = NULL
          )
        }
        titles <- titles[!is.na(titles)]
        
        data <- rbind(data, data.frame(company = companys, 
                                       date = dates, 
                                       class = rep(class_name, length(titles)), 
                                       subclass = rep(subclass_name[i], length(titles)), 
                                       title = titles, 
                                       content = contents))
        print(paste0("date : ", date, " / page : ", j))
      }
    }
  }
}

