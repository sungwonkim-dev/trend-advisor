from django.shortcuts import render

# Create your views here.
def index(request):
    return render(request, 'businesstiming/index.html')

def keyword(request):
    return render(request, 'businesstiming/keyword.html')

def graph(request):
    return render(request, 'businesstiming/graph.html')