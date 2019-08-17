import json
import re
import os
import text_rank as tr
from konlpy.tag import Okt, Kkma
from collections import Counter

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
for content in contents:
        try:
            # 기사내용 요약
            textrank = tr.TextRank(content[2])
            summ = textrank.summarize(3)
            summ = "\n".join(summ)
            # 요약본에서 키워드 추출
            summ_keywords = tr.TextRank(summ)
            keywords = summ_keywords.keywords()
            print("#", content[0])
            # print(summ)
            print(keywords)
            print()
        except:
            print("###ERROR###")
            print(content[0])



