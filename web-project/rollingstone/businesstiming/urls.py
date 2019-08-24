from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('keyword', views.keyword, name='keyword'),
    path('api/chart/data', views.ChartData.as_view()),
    path('api/autocomplete', views.AutocompleteData.as_view()),
]
