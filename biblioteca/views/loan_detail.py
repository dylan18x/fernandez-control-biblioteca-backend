from rest_framework import viewsets
from rest_framework.filters import SearchFilter, OrderingFilter
from django_filters.rest_framework import DjangoFilterBackend

from biblioteca.models                   import LoanDetail
from biblioteca.serializers.loan_detail  import LoanDetailSerializer
from biblioteca.permissions              import IsStaffOrReadOnly
from biblioteca.filters                  import LoanDetailFilter
from biblioteca.pagination               import StandardPagination


class LoanDetailViewSet(viewsets.ModelViewSet):
    queryset           = LoanDetail.objects.select_related('loan', 'book').all()
    serializer_class   = LoanDetailSerializer
    permission_classes = [IsStaffOrReadOnly]
    pagination_class   = StandardPagination
    filter_backends    = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_class    = LoanDetailFilter
    search_fields      = ['book__name', 'observation']
    ordering_fields    = ['delivery_date']
    ordering           = ['id']