from django.contrib import admin
from biblioteca.models import CategoryBook
from biblioteca.models import Book
from biblioteca.models import Reader
from biblioteca.models import Loan
from biblioteca.models import LoanDetail


@admin.register(CategoryBook)
class CategoryBookAdmin(admin.ModelAdmin):
    list_display  = ['id', 'name', 'is_active', 'created_at']
    list_filter   = ['is_active']
    search_fields = ['name', 'description']


@admin.register(Book)
class BookAdmin(admin.ModelAdmin):
    list_display  = ['id', 'author', 'category', 'available', 'created_at']
    list_filter   = ['available', 'category'] 
    search_fields = ['name', 'title', 'author', 'editorial']

@admin.register(Reader)
class ReaderAdmin(admin.ModelAdmin):
    list_display  = ['id', 'full_name', 'phone', 'email', 'available', 'created_at']  
    list_filter   = ['available'] 
    search_fields = ['full_name', 'phone', 'email']

@admin.register(Loan)
class LoanAdmin(admin.ModelAdmin):
    list_display  = ['id', 'reader', 'loan_date', 'return_date', 'status']
    list_filter   = ['status']
    search_fields = ['reader__full_name']

@admin.register(LoanDetail)
class LoanDetailAdmin(admin.ModelAdmin):
    list_display  = ['id', 'loan', 'book', 'delivery_date', 'observation']
    list_filter   = ['book']
    search_fields = ['book__name', 'observation']