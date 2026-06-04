# store/serializers/category.py
from rest_framework import serializers
from biblioteca.models import Reader


class ReaderSerializer(serializers.ModelSerializer):
    class Meta:
        model  = Reader
        fields = [
            'id', 'full_name','phone', 'email', 'created_at']
        read_only_fields = ['id', 'created_at']

    def validate_full_name(self, value):
        qs = Reader.objects.filter(full_name__iexact=value)
        if self.instance:
            qs = qs.exclude(pk=self.instance.pk)
        if qs.exists():
            raise serializers.ValidationError('A reader with this name already exists.')
        return value