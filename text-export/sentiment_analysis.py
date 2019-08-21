import json
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import knusl as kl
from matplotlib import font_manager, rc
from konlpy.tag import Kkma
from konlpy.tag import Okt
from sklearn.feature_extraction.text import TfidfVectorizer
from collections import defaultdict
import nltk
import pickle
import os

with open('./text.txt', 'r') as tx:
    corpus = [tx.read()]
    text = tx.read()

# 한국어 형태소 분리
kor = Kkma()
okt = Okt()


# tf idf
vectorizer = TfidfVectorizer()
sp_matrix = vectorizer.fit_transform(corpus)

# tf-idf dictation으로 저장
word2id = defaultdict(lambda:0)
for idx, feature in enumerate(vectorizer.get_feature_names()):
    word2id[feature] = idx
    # print(idx, feature)

# 감성사전에서 감성분석
dictation = kl.KnuSL
sentiment_word_list = []
frequently = []
for i, sent in enumerate(corpus):
    frequently.extend([(token, sp_matrix[i, word2id[token]]) for token in sent.split() ])
frequently_sort = sorted(frequently)
for word in frequently:
    wordname = word[0].strip(" ")
    positive = dictation.data_list(wordname)
    if(positive[1] != 'None'):
        sentiment_word_list.append([wordname, int(positive[1])])
for seq in frequently:
    isit = okt.nouns(seq[0])
    for s_seq in isit:
        
        print(isit)

print(sentiment_word_list)


# 기사의 긍정적, 부정적 경향
pos_count = 0
neg_count = 0

for pos_or_neg in sentiment_word_list:
    if(pos_or_neg[1] > 0):
        pos_count = pos_count + 1
    elif(pos_or_neg[1]):
        neg_count = neg_count + 1

if pos_count > neg_count:
    print("긍정적 기사")
elif neg_count > pos_count:
    print("부정적 기사")
else:
    print("중립") 
