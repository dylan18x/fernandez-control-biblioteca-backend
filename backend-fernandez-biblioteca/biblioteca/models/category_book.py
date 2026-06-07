# store/models/category.py
from django.db import models


class CategoryBook(models.Model):
    name        = models.CharField(max_length=100, unique=True)
    description = models.TextField(blank=True, default='')
    is_active   = models.BooleanField(default=True)
    created_at  = models.DateTimeField(auto_now_add=True)

    class Meta:
        verbose_name        = 'Category Book'
        verbose_name_plural = 'Category Books'
        ordering            = ['name']

    def __str__(self):
        return self.name