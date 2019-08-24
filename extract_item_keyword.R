library(NLP4kec)
library(tm)
library(rJava)
library(jsonlite)
library(data.table)
library(stringr)
library(Rtextrankr)
library(dplyr)


Article_Keywords_Extraction <- function(directory_day = 12){
  
  # 파일경로 설정
  day <- directory_day
  part_paths <- "article/"
  paths <- paste0(part_paths, day, "/")
  json_files <- list.files(paths)
  
  # 기사 변수
  articles <- data.frame()
  
  # 아이템 키워드 읽어오기
  items <- fread("filtering_keywords.csv", header = F, data.table = F, stringsAsFactors = F)$V1
  
  print("Read Article.")
  for(i in 1 : length(json_files)){
    
    # json 형식으로 저장된 기사 내용 추출
    json <- fromJSON(paste0(paths, json_files[i]))
    id <- json$detail$NEWS_ID
    content <- json$detail$CONTENT
    
    # 문자 제거
    pre_content <- gsub("<br/>", "", content)
    pre_content <- gsub("<span class='quot[0-9]'>", "", pre_content)
    pre_content <- gsub("</span>", "", pre_content)
    pre_content <- gsub(" ", "", pre_content)
    
    # 요약문장에서 띄어쓰기를 제거 후 연결
    # possible_error <- tryCatch(
    #   top_2_summarization <- Rtextrankr::summarize(pre_content, 2),
    #   error = function(e){
    #     print(e)
    #   }
    # )
    # 
    # if(inherits(possible_error, "error")) 
    #   next
    # sentence <- gsub(" ", "", paste(top_2_summarization[1], top_2_summarization[2]))
    
    articles <- bind_rows(articles, data.frame(article_id = id, before_parsing_content = pre_content, stringsAsFactors = F))
    
    print(paste0("article preprocessing : ", i, " / ", length(json_files)))
  }
  
  # # 기사 내용을 바탕으로 요약 문장 생성 (1)
  # library(lexRankr)
  # top_2_summarization <- lexRank(content, docId = rep(1, length(pre_content)), n = 2, continuous = T)
  
  # # 기사 내용을 바탕으로 요약 문장 생성 (2)
  # library(LSAfun)
  # top_2_summarization <- genericSummary(content, k = 0)
  
  # 기사 내용을 바탕으로 요약 문장 생성 (3)
  # library(Rtextrankr)
  # top_2_summarization <- Rtextrankr::summarize(articles$article_content[1], 2)
  
  # 형태소 분석 중 파싱을 통해 명사만 추출
  parser <- r_parser_r(articles$before_parsing_content, language = "ko", korDicPath = "exdic.txt")
  
  # 아이템 키워드 사전에 있는 키워드만 필터링
  keywords_list <- parser %>% str_split(pattern = " ")
  
  keywords <- c()

  for(i in 1 : length(json_files)){
    keywords <- c(keywords, list(keywords_list[[i]][which(keywords_list[[i]] %in% items)]))
    
    print(paste0("item keyword : ", i, " / ", length(json_files)))
  }
  
  # 기사 아이디와 키워드 데이터 프레임 생성
  result_part <- data.frame()
  for(i in 1 : length(json_files)){
    result_part <- bind_rows(result_part, 
                             data.frame(article_id = rep(articles$article_id[i], length(keywords[[i]])), 
                                        keyword = keywords[[i]]))
    
    print(paste0("item dataframe : ", i, " / ", length(json_files)))
  }
  
  # 키워드 갯수 생성 및 키워드 별 랭크 생성, count 제거 및 열 위치 변경
  result_part2 <- result_part %>% group_by(keyword) %>% 
    summarise(count = n()) %>% arrange(desc(count)) %>% mutate(rank = rank(desc(count), ties.method = "random"))
  result_part2$count <- NULL
  result_part2 <- result_part2[, c(2, 1)]
  
  # 중복 제거 후 키워드 정보와 병합
  result_part <- result_part %>% distinct()
  result <- merge(result_part2, result_part, by = "keyword", all.y =  T)
  result <- result %>% arrange(rank)
  
  fwrite(result, "article_result.csv", col.names = T, row.names = F)
  print("Complete!")
}


