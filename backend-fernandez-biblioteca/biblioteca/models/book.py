
from django.db import models
from biblioteca.models.category_book import CategoryBook


class Book(models.Model):
    title = models.CharField(blank=True, default='')
    author = models.CharField(max_length=100)
    editorial = models.CharField(max_length=100)
    year_publication = models.CharField(max_length=4)
    available  = models.BooleanField(default=True)
    created_at  = models.DateTimeField(auto_now_add=True)
    category = models.ForeignKey(CategoryBook, on_delete=models.CASCADE)

    class Meta:
        verbose_name        = 'Book'
        verbose_name_plural = 'Books'
        ordering            = ['title']

    def __str__(self):
        return self.name