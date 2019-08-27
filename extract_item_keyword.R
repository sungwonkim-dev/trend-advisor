library(NLP4kec)
library(tm)
library(rJava)
library(jsonlite)
library(data.table)
library(stringr)
library(dplyr)
library(tidyr)

rm(list = ls()); gc(reset = T)

Article_Keywords_Extraction <- function(input_month, input_week){
  
  # 파일경로 설정
  month <- input_month
  if(length(month) == 1)
    month <- paste0("0", month)
  week <- input_week
  part_paths <- "article/"
  paths <- paste0(part_paths, "2018/", month, "/", week, "/")
  json_files <- list.files(paths)
  
  # 기사 변수
  articles <- data.frame()
  
  # 아이템 키워드 읽어오기
  items <- fread("filtering_keywords.csv", header = F, data.table = F, stringsAsFactors = F)$V1
  
  print("Read Article.")
  for(i in 1 : length(json_files)){
    
    # json 형식으로 저장된 기사 내용 추출
    read_error <- tryCatch(
      json <- jsonlite::fromJSON(paste0(paths, json_files[i])),
      error = function(e){
        NULL
        print(e)
      },
      finally = NULL
    )
    
    if(is.null(read_error))
      next
    
    # id <- json$detail["NEWS_ID"]
    id <- json$detail$NEWS_ID
    # content <- json$detail["CONTENT"]
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
    
    #sentimentclass
    #한개의 기사 내의 P의 비율이 0.58보다 높으면 P판정
    # sentiment <- json$detail["TMS_SENTIMENT_CLASS"]
    sentiment <- json$detail$TMS_SENTIMENT_CLASS
    
    if(sentiment=="NEUTRAL"){sentimentNP <- 0}
    else if(sentiment==""){sentimentNP <- NA}
    else{
      t <- str_split(sentiment,"<br/>")
      t <- as.data.frame(t)
      t <- separate(t, colnames(t[1]), c("A", "B"), sep = " ")
      
      n <- nrow(t %>% dplyr::filter(A=="NEGATIVE"))
      p <- nrow(t %>% dplyr::filter(A=="POSITIVE"))
      
      if(p / (n + p) <= 0.48){ sentimentNP <- -1 }
      else if(p / (n + p) <= 0.60){ sentimentNP <- 0 }
      else{ sentimentNP <- 1 }
    }
    
    articles <- bind_rows(articles, data.frame(before_parsing_content = pre_content, 
                                               NP = sentimentNP, stringsAsFactors = F))
    
    if(i %% 1000 == 0)
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
  for(i in 1 : dim(articles)[1]){
    keywords <- c(keywords, list(keywords_list[[i]][which(keywords_list[[i]] %in% items)]))
    
    if(i %% 1000 == 0)
      print(paste0("item keyword : ", i, " / ", dim(articles)[1]))
  }
  
  # 기사 아이디와 키워드 데이터 프레임 생성
  result_part <- data.frame()
  for(i in 1 : dim(articles)[1]){
    result_part <- bind_rows(result_part, 
                             data.frame(keyword = keywords[[i]], 
                                        NP = rep(articles$NP[i], length(keywords[[i]]))))
    
    if(i %% 1000 == 0)
      print(paste0("item dataframe : ", i, " / ", dim(articles)[1]))
  }
  
  result_part <- result_part %>% filter(!is.na(NP))
  
  # 키워드 갯수 생성 및 키워드 별 랭크 생성, count 제거 및 열 위치 변경
  result_part2 <- result_part %>% 
    group_by(keyword) %>% 
    summarise(count = n(), npscore = sum(NP)) %>% 
    arrange(desc(count)) %>% filter(npscore > 0) %>% mutate(rank = rank(desc(count), ties.method = "random"))
  
  fwrite(result_part2, paste0("article_result_", month, week, ".csv"), col.names = T, row.names = F)
  
  print("Complete!")
}

for(i in 1 : 4){
  Article_Keywords_Extraction(6, i)
}
