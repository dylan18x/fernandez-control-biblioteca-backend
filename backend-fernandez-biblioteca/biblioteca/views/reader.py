from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.filters import SearchFilter, OrderingFilter
from django_filters.rest_framework import DjangoFilterBackend

from biblioteca.models               import Reader
from biblioteca.serializers.reader import ReaderSerializer
from biblioteca.permissions          import IsStaffOrReadOnly
from biblioteca.filters              import ReaderFilter
from biblioteca.pagination           import StandardPagination


class ReaderViewSet(viewsets.ModelViewSet):
    queryset           = Reader.objects.all()
    serializer_class   = ReaderSerializer
    permission_classes = [IsStaffOrReadOnly]
    pagination_class   = StandardPagination
    filter_backends    = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_class    = ReaderFilter
    search_fields      = ['full_name','email']
    ordering_fields    = ['full_name','created_at']
    ordering           = ['full_name']

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