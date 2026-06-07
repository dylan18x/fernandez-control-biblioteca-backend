# config/urls.py
from django.contrib import admin
from django.urls import path, include
from django.http import JsonResponse

def handler404(request, exception):
    return JsonResponse(
        {"error": "Recurso no encontrado."},
        status=404
    )

def handler500(request):
    return JsonResponse(
        {"error": "Error interno del servidor."},
        status=500
    )

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('biblioteca.urls')),
]