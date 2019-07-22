library(rvest)
library(htmltidy)
library(stringr)

# 먼저 언론사 데이터 크롤링
# article id를 통해 이동
rm(list = ls()); gc(reset = T)

main_url <- "https://news.naver.com/main/read.nhn?mode=LPOD&mid=sec&oid=032&aid="
head_current_aid <- "000"
tail_current_aid <- 2914451
company <- "경향신문"
data <- data.frame()

repeat{
  tryCatch(
    current_html <- read_html(paste0(main_url, head_current_aid, tail_current_aid)),
    error = function(e){ 
      print(e) 
      tail_current_aid <- tail_current_aid - 1 
      },
    finally = NULL
  )
  
  if(!is.na(current_html %>% html_node("#main_content .error"))){
    tail_current_aid <- tail_current_aid - 1
    next
  }
  
  if(is.na(current_html %>% html_node(".content_area")) & 
           is.na(current_html %>% html_node(".end_ct"))){
    title <- current_html %>% html_nodes("#main_content #articleTitle") %>% html_text() %>% trimws()
    date <- current_html %>% html_node("#main_content .t11") %>% html_text()
    content <- str_split(current_html %>% 
                           html_node("#main_content #articleBodyContents") %>% 
                           html_text(), "\\}", simplify = T)[2] %>% trimws()
  } else if(is.na(current_html %>% html_node(".end_ct"))){
    title <- current_html %>% html_nodes(".content_area .title") %>% html_text() %>% trimws()
    date <- str_split(current_html %>% 
                        html_node(".content_area .info span:first-child") %>% 
                        html_text(), " ", simplify = T)[2]
    content <- current_html %>% 
      html_node(".content_area #newsEndContents") %>% 
      html_text() %>% trimws()
  } else{
    title <- current_html %>% html_nodes(".end_ct .end_tit") %>% html_text() %>% trimws()
    date <- current_html %>% 
      html_node(".article_info .author em") %>% 
      html_text()
    content <- current_html %>% 
      html_node(".end_ct #articeBody") %>% 
      html_text() %>% trimws()
  }
  
  data <- rbind(data, data.frame(company, title, date, content))
  
  print(paste(tail_current_aid, date, sep = " / "))
  tail_current_aid <- tail_current_aid - 1
}
write.csv(data, "data.csv")
# 2898161