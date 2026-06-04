# store/serializers/category.py
from rest_framework import serializers
from biblioteca.models import Loan


class LoanSerializer(serializers.ModelSerializer):
    class Meta:
        model  = Loan
        fields = [
            'id', 'loan_date','return_date','reader','status']
        read_only_fields = ['id', 'loan_date']
