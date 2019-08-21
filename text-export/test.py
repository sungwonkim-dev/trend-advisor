from krwordrank.word import summarize_with_keywords
from krwordrank.sentence import summarize_with_sentences
from konlpy.tag import Kkma, Okt
from soynlp.noun import LRNounExtractor_v2, NewsNounExtractor
from soynlp.utils import DoublespaceLineCorpus
import json
import os
import re

text = ["흑당버블티"]
#kkma = Kkma()
corpus_path = './text.txt'
sents = DoublespaceLineCorpus(corpus_path, iter_sent=True)
noun_extractor = LRNounExtractor_v2(verbose=True)
nouns = noun_extractor.train_extract(sents)
print(nouns)
print(list(noun_extractor._compounds_components.items())[:5])


