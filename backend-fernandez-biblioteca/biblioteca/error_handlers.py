from rest_framework.views import exception_handler
from rest_framework.response import Response
from rest_framework import status
import traceback

def custom_exception_handler(exc, context):
    print("ERROR DETECTADO:")
    traceback.print_exc()

    response = exception_handler(exc, context)

    if response is not None:
        return response

    return Response(
        {
            "error": str(exc),
            "tipo": exc.__class__.__name__
        },
        status=status.HTTP_500_INTERNAL_SERVER_ERROR
    )