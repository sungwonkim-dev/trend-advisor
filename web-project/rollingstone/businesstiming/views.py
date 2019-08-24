from django.http import JsonResponse
from django.shortcuts import render, redirect
from django.views.generic import View
from rest_framework.views import APIView
from rest_framework.response import Response
from .forms import SearchForm
import datetime
import pandas as pd
import os


# Create your views here.
def index(request):
    return render(request, 'businesstiming/index.html')        

class AutocompleteData(APIView):
    def get(self, request, format=None):
        module_dir = os.path.dirname(__file__)
        workDir = os.path.join(module_dir, 'src\\ranking')
        df = pd.read_csv(workDir+"/dataset_all.csv",  encoding="cp949", header=None)
        autocompleteList = df[0].values
        print(autocompleteList)
        data = {
            'autocompleteList': autocompleteList
        }
        return Response(data)

def keyword(request):
    if request.method == "POST":
        form = request.POST
        if form:
            keyword_list = []
            now = datetime.datetime(2019, 7, 30)
            ju = now.isocalendar()[1]
            searchWord = request.POST['keyword']
            for key in range(100):
                key = request.POST['keyword']
                keyword_list.append(key)
            context = {
                'searchWord' : searchWord,
                'key' : keyword_list, 
                'ju' : ju%5
            }
            return render(request, 'businesstiming/keyword.html', context)
    else:
        return render(request, 'businesstiming/keyword.html')

def graph(request):
    return render(request, 'businesstiming/graph.html')

def get_data(request):
    data = {
        "first_data":[31,26,32,14,76,34,56,22,33,44],
        "second_data":[88,77,66,33,44,55,11,22,99,48]
    }
    return JsonResponse(data)

class ChartData(APIView):

    def get(self, request, format=None):

        # 데이터 폴더 열어 목록 가져오기
        module_dir = os.path.dirname(__file__)
        workDir = os.path.join(module_dir, 'src/naverdata')
        filename_list = []
        for dirpath, dirnames, filenames in os.walk(workDir):
            for filename in filenames:
                filename_list.append(filename)

        # 6주전 주차로 초기화
        cal = datetime.datetime.now()
        cal = cal - datetime.timedelta(days=7*6)
        iso = cal.isocalendar()[1]
        # 이전 검색 순위 찾기
        word = request.GET["word"]
        first_data = []
        for filename in filename_list:
            # 주 구하기 (파일명은 yyyymmdd.csv 이어야 합니다.)
            csv_isodate = datetime.date(int(filename[:4]), int(filename[4:6]), int(filename[6:8])).isocalendar()[1]
            if csv_isodate >= iso and csv_isodate < iso + 6:
                openfile = pd.read_csv(workDir + '/' + filename, encoding="cp949")
                if word in openfile["item"].values:
                    key_data = openfile[openfile["item"] == word]["rank"].values[0]
                    first_data.append(key_data)
                else:
                    first_data.append(500)

        
        # 그래프에 쓸 x축 라벨 만들기
        labels = []
        for i in range(6):
            ju = cal.isocalendar()[1] % 5
            if ju == 0:
                ju = 4
            lbstr = str(cal.month) + "월" + str(ju) + "주"
            cal = cal + datetime.timedelta(days=7)
            labels.append(lbstr)
        cal = cal + datetime.timedelta(days=7*3)
        ju = cal.isocalendar()[1] % 5
        if ju == 0:
            ju = 4
        lbstr = str(cal.month) + "월" + str(ju) + "주"
        labels.append(lbstr)
        



        data = {
            "labels" : labels,
            "first_data" : first_data,
            "second_data":[88,77,66,33,44,55,11,22,99,48]
        }   
        return Response(data)
    

