# biblioteca/urls.py
from django.urls import path, include
from rest_framework.routers import DefaultRouter
from rest_framework_simplejwt.views import TokenRefreshView, TokenVerifyView

from biblioteca.views.health   import health_check
from biblioteca.views.auth     import RegisterView, LogoutView
from biblioteca.views.loan_detail import LoanDetailViewSet
from biblioteca.views.user     import UserViewSet
from biblioteca.views.category_book import CategoryBookViewSet
from biblioteca.serializers.auth import CustomTokenView
from biblioteca.views.book import BookViewSet
from biblioteca.views.reader import ReaderViewSet
from biblioteca.views.loan import LoanViewSet

router = DefaultRouter()
router.register('users',      UserViewSet,      basename='user')
router.register('category-books', CategoryBookViewSet,  basename='category-book')
router.register('books', BookViewSet, basename='book')
router.register('reader', ReaderViewSet, basename='reader')
router.register('loan', LoanViewSet, basename='loan')
router.register('loan-details', LoanDetailViewSet, basename='loan-details')

urlpatterns = [
    path('health/',             health_check),
    path('auth/register/',      RegisterView.as_view()),
    path('auth/login/',         CustomTokenView.as_view()),
    path('auth/token/refresh/', TokenRefreshView.as_view()),
    path('auth/token/verify/',  TokenVerifyView.as_view()),
    path('auth/logout/',        LogoutView.as_view()),
    path('', include(router.urls)),
]