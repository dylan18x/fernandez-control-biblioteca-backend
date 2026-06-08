from rest_framework import viewsets
from rest_framework.filters import SearchFilter, OrderingFilter
from django_filters.rest_framework import DjangoFilterBackend

from biblioteca.models                   import LoanDetail
from biblioteca.serializers.loan_detail  import LoanDetailSerializer
from biblioteca.permissions              import IsStaffOrReadOnly
from biblioteca.filters                  import LoanDetailFilter
from biblioteca.pagination               import StandardPagination

class LoanDetailViewSet(viewsets.ModelViewSet):
    serializer_class   = LoanDetailSerializer
    permission_classes = [IsStaffOrReadOnly]
    pagination_class   = StandardPagination
    filter_backends    = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_class    = LoanDetailFilter
    search_fields      = ['book__name', 'observation']
    ordering_fields    = ['delivery_date']
    ordering           = ['id']

    def get_queryset(self):
        user = self.request.user
        
        if not user.is_authenticated:
            return LoanDetail.objects.none()
            
        if user.is_staff:
            return LoanDetail.objects.select_related('loan', 'book').all()
            
        return LoanDetail.objects.select_related('loan', 'book').filter(loan__reader__user=user)