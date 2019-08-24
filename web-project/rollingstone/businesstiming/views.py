from django.http import JsonResponse
from django.shortcuts import render, redirect
from django.views.generic import View
from rest_framework.views import APIView
from rest_framework.response import Response
from .forms import SearchForm
import datetime
import json
import os

class GraphView(View):
    def get(self, request, *args, **kwargs):
        return render(request, 'businesstiming/graph.html', {})

# Create your views here.
def index(request):
    return render(request, 'businesstiming/index.html')        

def keyword(request):
    if request.method == "POST":
        form = request.POST
        if form:
            keyword_list = []
            now = datetime.datetime(2019, 7, 30)
            ju = now.isocalendar()[1]
            for key in range(100):
                key = request.POST['keyword']
                keyword_list.append(key)
            context = {
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
    authentication_classes = []
    permission_classes = []
    def get(self, request, format=None):
        module_dir = os.path.dirname(__file__)
        workDir = os.path.join(module_dir, 'naverdata')
        filename_list = []
        print(module_dir, workDir)
        for dirpath, dirnames, filenames in os.walk(workDir):
            for filename in filenames:
                filename_list.append(filename)
        print(filename_list)
        cal = datetime.datetime.now()
        labels = []
        cal = cal - datetime.timedelta(days=7*6)
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
        # labels = ["January", "February", "March", "April", "May", label, "July"]
        data = {
            "labels" : labels,
            "first_data":[31,26,32,14,76,34,56,22,33,44],
            "second_data":[88,77,66,33,44,55,11,22,99,48]
        }   
        return Response(data)
    

