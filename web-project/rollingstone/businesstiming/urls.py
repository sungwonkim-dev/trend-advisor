from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('keyword', views.keyword, name='keyword'),
    path('graph', views.GraphView.as_view(), name='graph'),
    path('api/data', views.get_data, name="get-data"),
    path('api/chart/data', views.ChartData.as_view()),

]
