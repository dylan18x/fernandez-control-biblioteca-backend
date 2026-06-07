from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.filters import SearchFilter, OrderingFilter
from django_filters.rest_framework import DjangoFilterBackend
from django.db.models import Count

from biblioteca.models               import Book
from biblioteca.serializers.book import BookSerializer
from biblioteca.permissions          import IsStaffOrReadOnly
from biblioteca.filters              import BookFilter
from biblioteca.pagination           import StandardPagination


class BookViewSet(viewsets.ModelViewSet):
    queryset           = Book.objects.all()
    serializer_class   = BookSerializer
    permission_classes = [IsStaffOrReadOnly]
    pagination_class   = StandardPagination
    filter_backends    = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_class    = BookFilter
    search_fields      = ['name', 'title','editorial']
    ordering_fields    = ['name','year_publication','created_at']
    ordering           = ['name']

    @action(detail=False, methods=['get'], url_path='stats')
    def stats(self, request):
        qs = self.get_queryset()
        total = qs.count()
        active = qs.filter(available = True).count()
        inactive = total - active
        return Response({
            'total':    total,
            'active': active,
            'inactive': inactive
        })