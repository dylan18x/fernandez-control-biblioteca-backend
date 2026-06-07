# store/serializers/category.py
from rest_framework import serializers
from biblioteca.models import Book


class BookSerializer(serializers.ModelSerializer):
    class Meta:
        model  = Book
        fields = [
            'id', 'name','title', 'author', 'editorial',
            'year_publication', 'available', 'created_at', 'category',
        ]
        read_only_fields = ['id', 'created_at']

    def validate_name(self, value):
        qs = Book.objects.filter(name__iexact=value)
        if self.instance:
            qs = qs.exclude(pk=self.instance.pk)
        if qs.exists():
            raise serializers.ValidationError('A book with this name already exists.')
        return value