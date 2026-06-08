
from django.db import models
from django.conf import settings


class Reader(models.Model):
    user = models.OneToOneField(
        settings.AUTH_USER_MODEL, 
        on_delete=models.CASCADE, 
        related_name='reader_profile',
        null=True, 
        blank=True
    )
    full_name = models.CharField(max_length=100, unique=True)
    phone = models.CharField(max_length=20,blank=True, default='')
    email = models.CharField(max_length=50, unique=True)
    created_at  = models.DateTimeField(auto_now_add=True)
    available = models.BooleanField(default=True)

    class Meta:
        verbose_name        = 'Reader'
        verbose_name_plural = 'Readers'
        ordering            = ['full_name']

    def __str__(self):
        return self.full_name