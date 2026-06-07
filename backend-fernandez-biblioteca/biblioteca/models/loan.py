from django.db import models
from biblioteca.models.reader import Reader


class Loan(models.Model):

    STATUS_CHOICES = [
        ('active',   'Active'),
        ('returned', 'Returned'),
        ('overdue',  'Overdue'),
    ]

    reader      = models.ForeignKey(Reader, on_delete=models.CASCADE, related_name='loans')
    loan_date   = models.DateField(auto_now_add=True)
    return_date = models.DateField(null=True, blank=True)
    status      = models.CharField(max_length=20, choices=STATUS_CHOICES, default='active')

    class Meta:
        verbose_name        = 'Loan'
        verbose_name_plural = 'Loans'
        ordering            = ['-loan_date']

    def __str__(self):
        return f'{self.reader} - {self.loan_date} ({self.status})'