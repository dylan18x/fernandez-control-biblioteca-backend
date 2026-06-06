from rest_framework.views import exception_handler
from rest_framework.response import Response
from rest_framework import status


def custom_exception_handler(exc, context):
    response = exception_handler(exc, context)

    if response is not None:
        if response.status_code == 400:
            return Response(
                {
                    "error": "Solicitud incorrecta",
                    "detalle": response.data
                },
                status=status.HTTP_400_BAD_REQUEST
            )
        if response.status_code == 401:
            return Response(
                {"error": "No autenticado. Proporciona un token válido."},
                status=status.HTTP_401_UNAUTHORIZED
            )
        if response.status_code == 403:
            return Response(
                {"error": "No tienes permisos para realizar esta acción."},
                status=status.HTTP_403_FORBIDDEN
            )
        if response.status_code == 404:
            return Response(
                {"error": "Recurso no encontrado."},
                status=status.HTTP_404_NOT_FOUND
            )
        return response

    return Response(
        {"error": "Error interno del servidor."},
        status=status.HTTP_500_INTERNAL_SERVER_ERROR
    )