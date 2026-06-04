# store/filters.py
import django_filters
from biblioteca.models import CategoryBook
from biblioteca.models import Book
from biblioteca.models import Reader
from biblioteca.models import Loan
from biblioteca.models import LoanDetail


class CategoryBookFilter(django_filters.FilterSet):
    name = django_filters.CharFilter(lookup_expr='icontains')

    class Meta:
        model  = CategoryBook
        fields = ['is_active']

class BookFilter(django_filters.FilterSet):
    name = django_filters.CharFilter(lookup_expr='icontains')

    class Meta:
        model  = Book
        fields = ['available']

class ReaderFilter(django_filters.FilterSet):
    full_name = django_filters.CharFilter(lookup_expr='icontains')

    class Meta:
        model  = Reader
        fields = ['available']

class LoanFilter(django_filters.FilterSet):
    reader = django_filters.CharFilter(lookup_expr='icontains')

    class Meta:
        model  = Loan
        fields = ['status']

class LoanDetailFilter(django_filters.FilterSet):
    book = django_filters.CharFilter(field_name='book__name', lookup_expr='icontains')

    class Meta:
        model  = LoanDetail
        fields = ['loan', 'book']