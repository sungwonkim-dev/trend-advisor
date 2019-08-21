import json
import re
import os
import text_rank as tr
import take_dict as td
from konlpy.tag import Okt, Kkma
from collections import Counter
from gensim.summarization.summarizer import summarize
# 기사 읽기
workDir = os.path.abspath('./news/')
filename_list = []
contents = []
for dirpath, dirnames, filenames in os.walk(workDir):
	#for dirname in dirnames:
	#	print('\t', dirname)
	for filename in filenames:
		filename_list.append('./news/'+filename)
for filename in filename_list:
	with open(filename, 'r', encoding='utf-8') as json_file:
		text = json.load(json_file)
		content = text["detail"]["CONTENT"]
		content = re.sub('<.+?>', '', content, 0).strip()
		contents.append([text["detail"]["NEWS_ID"], text["detail"]["TITLE"], content])

# print(contents[0][0])
# print(contents[0][1])
# contents = [ [ 기사ID, 기사제목 , 기사내용 ], ...]
file_data = dict()
json_data = dict()
cnt_key = dict()

summarize_news = [] # [id][summarize contents]
keywords_list = []

filepath = './dict/'
keydict = td.noun_dictation(filepath)
j = 0
for content in contents:
        try:
            # 기사내용 요약
            # print("content:",content[2])
            textrank = tr.TextRank(content[2])
            summ = textrank.summarize(1)
            summ = "\n".join(summ)
            kkma = Kkma()

            tsumm = []
            tsumm.append(content[0])
            tsumm.append(summ)
            summarize_news.append(tsumm)
            
            
            # 요약본에서 키워드 추출
            summ_keywords = tr.TextRank(summ)
            keywords = summ_keywords.keywords()
            keywords = keydict.isit_item(keywords)
            for keyword in keywords:
                if not keyword in keywords_list:
                     keywords_list.append(keyword)

            if len(keywords) != 0:
                print("keywords: ",keywords)
                file_data["id"] = content[0]
                file_data["keyword"] = keywords
            for ki in keywords:
                if ki in cnt_key:
                    cnt_key[ki] += 1
                else:
                    cnt_key[ki] = 1
            if j % 10 == 0:
                print(j, "개의 기사 키워드를 찾았습니다")
            j += 1
            if j > 200:
                break
            
        except:
            # 요약, 키워드 추출이 안되는 기사
            j += 1
            print("###ERROR ARTICLE###")
            print("except :",content[0])


keyword_rank = [] #[keywords][count]

for keyword in keywords_list:

    count = 0
    for summ in summarize_news:
        if keyword in summ[1]:
            count += 1
            
    temp_kr = []
    temp_kr.append(keyword)
    temp_kr.append(count)
    keyword_rank.append(temp_kr)


keyword_rank_list = sorted(keyword_rank, key=lambda keyword_rank: keyword_rank[1], reverse=True)

output = [] #[rank][keyword][news_id]
rank = 1

print(keyword_rank_list)

for keyword in keyword_rank_list:
    #print(keyword)
    for summ in summarize_news:
        if keyword[0] in summ[1]:
            temp_output = []
            temp_output.append(rank)
            temp_output.append(keyword[0])
            temp_output.append(summ[0])
            output.append(temp_output)
    
    rank += 1

df = []
temp_df = []

temp_df.append("rank")
temp_df.append("keyword")
temp_df.append("id")

df.append(temp_df)
for temp_o in output:
    df.append(temp_o)

#print(df)
import pandas as pd

dataframe = pd.DataFrame(df)
#print(dataframe)
dataframe.to_csv("./keyword_ranking.csv", header=False, index=False)
