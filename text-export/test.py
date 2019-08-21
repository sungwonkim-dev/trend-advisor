from konlpy.tag import Kkma, Okt

kkma = Kkma()
okt = Okt()

text = "나는배가고파서햄버거와흑당버블티를먹었다"
text2 = "나는 배가고파서 햄버거와 흑당 버블티를 먹었다"
print(okt.nouns(text))
print(okt.nouns(text2))
