from django import forms

class SearchForm(forms.Form):
    keyword = forms.CharField(max_length=50)
    # date = forms.CharField(max_length=50)
    # rise_and_fall = forms.CharField(max_length=10)