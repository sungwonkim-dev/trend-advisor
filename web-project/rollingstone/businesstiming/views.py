from django.http import JsonResponse
from django.shortcuts import render, redirect
from django.views.generic import View
from rest_framework.views import APIView
from rest_framework.response import Response
from .forms import SearchForm
import datetime
import pandas as pd
import os
import random
import MySQLdb as mysql

# Create your views here.
def index(request):
    return render(request, 'businesstiming/index.html')        


def keyword(request):
    key_list = []
    context = {}
    if request.method == "POST":
        weekday = request.POST['keyword']
        
        try:
            con = mysql.connect(host = 'camel.cy5ept1oyktw.ap-northeast-2.rds.amazonaws.com',
            user = 'camel',
            passwd = 'mypassword',
            db = 'trend',
            charset='utf8',
            use_unicode=True
            )
            cur = con.cursor()
            cur.execute("SELECT weeks, rank, keyword FROM keywords WHERE weeks = 20180625")
            data_list = cur.fetchall()
            print("query OK")
            for key in data_list:
                
                # 20위까지 리스트에 저장
                if key[1] <= 20:
                    key_list.append(key[2])
            
            context = {
                "weekday": weekday,
                "key": key_list,
            }
            con.close()
            random.shuffle(key_list)
        except mysql.Error:
            print(mysql.Error)
        return render(request, 'businesstiming/keyword.html', context)
    else:
        return render(request, 'businesstiming/keyword.html', context)

def graph(request):
    return render(request, 'businesstiming/graph.html')

class ChartData(APIView):

    def get(self, request, format=None):

        # 데이터 폴더 열어 목록 가져오기
        week = request.GET['week']           # 주
        clickword = request.GET['word'] # 클릭한 아이템
        
        module_dir = os.path.dirname(__file__)
        workDir = os.path.join(module_dir, 'src/naverdata')
        filename_list = []
        for dirpath, dirnames, filenames in os.walk(workDir):
            for filename in filenames:
                filename_list.append(filename)

        # 6주전 주차로 초기화

        cal = datetime.date(2018,6,26)
        cal = cal - datetime.timedelta(days=7*6)
        iso = cal.isocalendar()[1]

        # 이전 검색 순위 찾기
        first_data = []
        for filename in filename_list:
            # 주 구하기 (파일명은 yyyymmdd.csv 이어야 합니다.)
            csv_isodate = datetime.date(int(filename[:4]), int(filename[4:6]), int(filename[6:8])).isocalendar()[1]
            if csv_isodate >= iso and csv_isodate < iso + 6:
                openfile = pd.read_csv(workDir + '/' + filename, encoding="cp949")
                if clickword in openfile["item"].values:
                    key_data = openfile[openfile["item"] == clickword]["rank"].values[0]
                    # print(key_data)
                    if key_data <= 40:
                        first_data.append(key_data)
                    else:
                        first_data.append(200)
                else:
                    first_data.append(200)
        try:
            con = mysql.connect(host = 'camel.cy5ept1oyktw.ap-northeast-2.rds.amazonaws.com',
            user = 'camel',
            passwd = 'mypassword',
            db = 'trend',
            charset='utf8',
            use_unicode=True)

            cur = con.cursor()
            cur.execute("SELECT rank FROM keywords WHERE keyword = '" + clickword + "' AND weeks = '20180625'")
            data_list = cur.fetchall()
            print(data_list)
            for ran in data_list:
                print(ran[0])
                first_data.append(ran[0])
                
            con.close()
        except mysql.Error:
            print(mysql.Error)
        
        # 그래프에 쓸 x축 라벨 만들기
        labels = []
        for i in range(6):
            ju = cal.isocalendar()[1]
            lbstr = str(ju) + "주"
            cal = cal + datetime.timedelta(days=7)
            labels.append(lbstr)
        cal = cal + datetime.timedelta(days=7*3)
        ju = cal.isocalendar()[1]
        lbstr = str(ju) + "주"
        labels.append(lbstr)
        
    
        print(len(labels), len(first_data))

        data = {
            "labels" : labels,
            "first_data" : first_data,
            "word" : clickword,
        }   
        return Response(data)
    

