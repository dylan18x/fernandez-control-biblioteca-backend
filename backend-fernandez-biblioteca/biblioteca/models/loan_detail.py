from django.db import models
from biblioteca.models.loan import Loan
from biblioteca.models.book import Book


class LoanDetail(models.Model):
    loan          = models.ForeignKey(Loan, on_delete=models.CASCADE, related_name='details')
    book          = models.ForeignKey(Book, on_delete=models.CASCADE, related_name='loan_details')
    delivery_date = models.DateField(null=True, blank=True)
    observation   = models.TextField(blank=True, default='')

    class Meta:
        verbose_name        = 'Loan Detail'
        verbose_name_plural = 'Loan Details'
        ordering            = ['id']

    def __str__(self):
        return f'{self.loan} - {self.book}'