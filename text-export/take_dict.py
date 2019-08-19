import pandas as pd

class noun_dictation():
    def __init__(self, file_path):
        try:
            self.pronoun = pd.read_csv(file_path + "proper-noun.csv", header=None, encoding="cp949")
            self.brand = pd.read_csv(file_path + "brand.csv", header=None, encoding="cp949")
            self.product = pd.read_csv(file_path+"product-name.csv", header=None, encoding="cp949")
            print("successfully imported csv file")
        except:
            print("failed read to csv")
            print("파일 경로가 잘못되었거나 proper-noun.csv, brand.csv, product-name.csv 파일이 없습니다.")
    def __str__(self):
        try:
            return "brand name\n"+str(self.brand.head(5))+'\npropername\n'+str(self.pronoun.head(5))+'\nproductname\n'+str(self.product.head(5))
        except:
            return "no data"
    def isit_item(self, keywords):
        # 키워드가 브랜드, 고유명사, 제품명에 속하는지 확인
        real_keywords = []
        for ki in keywords:
            #if ki in self.brand[0].values:
            #    real_keywords.append(ki)
            #    continue
            # 고유명사는 필요없을 것 같아서 지웠습니다.
            #if ki in self.pronoun[0].values:
            #    real_keywords.append(ki)
            #    continue
            if ki in self.product[0].values:
                real_keywords.append(ki)
                continue
        # 키워드는 리스트형태로 반환됩니다.
        return real_keywords
        
    def take_brand_noun(self):
        bd = self.brand[0].values
        return bd
    def take_proper_noun(self):
        pn = self.pronoun[0].values
        return pn
    def take_product_noun(self):
        pn = self.product[0].values
        return pn

if __name__ == "__main__":
    filepath = "./dict/"
    nd = noun_dictation(filepath)
    print(nd)
    print(nd.isit_item(['울랄라세션', '삼성', '구글', '장갑', '반도체']))
