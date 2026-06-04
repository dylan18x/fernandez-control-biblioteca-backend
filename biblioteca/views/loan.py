from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.filters import SearchFilter, OrderingFilter
from django_filters.rest_framework import DjangoFilterBackend

from biblioteca.filters import LoanFilter
from biblioteca.models               import Loan
from biblioteca.serializers.loan import LoanSerializer
from biblioteca.permissions          import IsStaffOrReadOnly
from biblioteca.pagination           import StandardPagination


class LoanViewSet(viewsets.ModelViewSet):
    queryset           = Loan.objects.select_related('reader').all()
    serializer_class   = LoanSerializer
    permission_classes = [IsStaffOrReadOnly]
    pagination_class   = StandardPagination
    filter_backends    = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_class    = LoanFilter
    search_fields      = ['reader__full_name']
    ordering_fields    = ['loan_date', 'return_date', 'status']
    ordering           = ['-loan_date']

    @action(detail=False, methods=['get'], url_path='stats')
    def stats(self, request):
        qs = self.get_queryset()
        total = qs.count()
        active   = qs.filter(status='active').count()
        returned = qs.filter(status='returned').count()
        overdue  = qs.filter(status='overdue').count()
        return Response({
            'total':    total,
            'active':   active,
            'returned': returned,
            'overdue':  overdue,
        })