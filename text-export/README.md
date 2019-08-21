## 설치 패키지

konlpy
sklearn
numpy
pandas

## sentiment analysis.py

감성분석기

## knusl

감성분석 사전에서 단어 분류

## text_rank.py

텍스트 요약 및 키워드 추출

텍스트 요약시 발생되는 문제

1. 문장의 길이가 너무 짧음
2. stopword에 모든 단어가 걸러짐
3. 주어지는 문장이 1개임

위에 사항이 해당되면 결과가 제대로 나오지않거나 에러가 발생  
그래서 문제가 발생되는 기사는 예외처리를 시켰음

코드 참고 사이트  
<https://excelsior-cjh.tistory.com/93>

## export_keywords.py

텍스트 요약 및 키워드 가져오기

### news 디렉토리

뉴스 json 파일 모음집

### data 디렉토리

SentiWord_info.json 단어 사전


## 예정 사항

키워드 추출시 개체명 인식을 이용해 키워드 추출의 정확도를 올릴 필요가 있음

추출한 키워드 카운트가 필요
