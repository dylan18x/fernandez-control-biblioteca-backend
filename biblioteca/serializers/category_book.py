# store/serializers/category.py
from rest_framework import serializers
from biblioteca.models import CategoryBook


class CategoryBookSerializer(serializers.ModelSerializer):
    class Meta:
        model  = CategoryBook
        fields = [
            'id', 'name','description',
            'is_active', 'created_at',
        ]
        read_only_fields = ['id', 'created_at']

    def validate_name(self, value):
        qs = CategoryBook.objects.filter(name__iexact=value)
        if self.instance:
            qs = qs.exclude(pk=self.instance.pk)
        if qs.exists():
            raise serializers.ValidationError('A category with this name already exists.')
        return value