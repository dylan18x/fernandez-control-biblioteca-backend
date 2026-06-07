from rest_framework import serializers
from biblioteca.models import LoanDetail


class LoanDetailSerializer(serializers.ModelSerializer):
    class Meta:
        model  = LoanDetail
        fields = [
            'id', 'loan', 'book', 'delivery_date', 'observation'
        ]
        read_only_fields = ['id']