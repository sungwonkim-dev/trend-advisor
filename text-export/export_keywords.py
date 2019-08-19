import json
import re
import os
import text_rank as tr
import take_dict as td
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
file_data = dict()
json_data = dict()
cnt_key = dict()

filepath = './dict/'
keydict = td.noun_dictation(filepath)
j = 0
for content in contents:
        try:
            # 기사내용 요
            textrank = tr.TextRank(content[2])
            summ = textrank.summarize(3)
            summ = "\n".join(summ)
            # 요약본에서 키워드 추출
            summ_keywords = tr.TextRank(summ)
            keywords = summ_keywords.keywords()
            keywords = keydict.isit_item(keywords)

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
            # print("#", content[0])
            # print(keywords)
            # print()
        except:
            # 요약, 키워드 추출이 안되는 기사
            j += 1
            print("###ERROR ARTICLE###")
            print(content[0])

# 숫자 카운트 
import operator
cnt_key = sorted(cnt_key.items(), key=operator.itemgetter(1))
json.dumps(cnt_key, ensure_ascii=False, indent="\t")
with open('word_count.json', 'w', encoding='utf-8') as count_file:
    json.dump(cnt_key, count_file, ensure_ascii=False, indent='\t')
