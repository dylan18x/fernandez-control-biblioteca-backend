# biblioteca/views/category_book.py
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.filters import SearchFilter, OrderingFilter
from django_filters.rest_framework import DjangoFilterBackend
from django.db.models import Count

from biblioteca.models               import CategoryBook
from biblioteca.serializers.category_book import CategoryBookSerializer
from biblioteca.permissions          import IsStaffOrReadOnly
from biblioteca.filters              import CategoryBookFilter
from biblioteca.pagination           import StandardPagination


class CategoryBookViewSet(viewsets.ModelViewSet):
    queryset           = CategoryBook.objects.all()
    serializer_class   = CategoryBookSerializer
    permission_classes = [IsStaffOrReadOnly]
    pagination_class   = StandardPagination
    filter_backends    = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_class    = CategoryBookFilter
    search_fields      = ['name', 'description']
    ordering_fields    = ['name', 'created_at']
    ordering           = ['name']

    @action(detail=True, methods=['get'], url_path='books')
    def active_books(self, request, pk=None):
        return Response([])

    @action(detail=False, methods=['get'], url_path='stats')
    def stats(self, request):
        qs = CategoryBook.objects.annotate(num_books=Count('book', distinct=True))
        return Response({
            'total':    qs.count(),
            'active':   qs.filter(is_active=True).count(),
            'inactive': qs.filter(is_active=False).count(),
            'detail': [
                {
                    'id':           c.id,
                    'name':         c.name,
                    'num_books': c.num_books,
                    'is_active':    c.is_active,
                }
                for c in qs.order_by('name')
            ],
        })