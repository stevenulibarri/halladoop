from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from namenode.serializers import serializers


class Registration(APIView):

    def post(self, request, format=None):
        serializer = serializers.RegistrationSerializer(data=request.data)
        if serializer.is_valid():
            reg = serializer.create(serializer.validated_data)
            print(reg.nodeID)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class HeartBeat(APIView):

    def post(self, request, format=None):
        return


class Finalize(APIView):

    def post(self, request, format=None):
        return


class File(APIView):

    def post(self, request, format=None):
        return

    def get(self, request, format=None):
        return

    def delete(self, request, format=None):
        return
