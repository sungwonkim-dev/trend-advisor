from django.http import JsonResponse
from django.shortcuts import render
from django.views.generic import View
from rest_framework.views import APIView
from rest_framework.response import Response

class GraphView(View):
    def get(self, request, *args, **kwargs):
        return render(request, 'businesstiming/graph.html', {})

# Create your views here.
def index(request):
    return render(request, 'businesstiming/index.html')        

def keyword(request):
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
        labels = ["January", "February", "March", "April", "May", "June", "July"]
        data = {
            "labels" : labels,
            "first_data":[31,26,32,14,76,34,56,22,33,44],
            "second_data":[88,77,66,33,44,55,11,22,99,48]
        }   
        return Response(data)