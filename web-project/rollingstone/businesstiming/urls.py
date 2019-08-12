from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('keyword', views.keyword, name='keyword'),
    path('graph', views.graph, name='graph'),
]
