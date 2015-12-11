from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from namenode.serializers import serializers
from namenode.requesthandlers import requesthandlers


class Registration(APIView):

    def post(self, request, format=None):
        # try:
            serializer = serializers.RegistrationRequestSerializer(data=request.data)
            if serializer.is_valid():
                reg = serializer.create(serializer.validated_data)
                resp = requesthandlers.handle_register(reg)
                deserializer = serializers.RegistrationResponseSerializer(resp)
                return Response(deserializer.data, status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        # except:
            # pass


class Heartbeat(APIView):

    def post(self, request, format=None):
        # try:
            serializer = serializers.HeartbeatSerializer(data=request.data)
            if serializer.is_valid():
                heartbeat = serializer.create(serializer.validated_data)
                resp = requesthandlers.handle_heartbeat(heartbeat)
                deserializer = serializers.HeartbeatResponseSerializer(resp)
                return Response(deserializer.data, status=status.HTTP_200_OK)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        # except:
        #     pass


class Finalize(APIView):

    def post(self, request, format=None):
        # try:
            serializer = serializers.FinalizeRequestSerializer(data=request.data)
            if serializer.is_valid():
                finalize_request = serializer.create(serializer.validated_data)
                requesthandlers.handle_finalize(finalize_request)
                return Response(status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        # except:
        #     pass


class File(APIView):

    def post(self, request, format=None):
        # try:
            serializer = serializers.WriteRequestSerializer(data=request.data)
            if serializer.is_valid():
                write_request = serializer.create(serializer.validated_data)
                resp = requesthandlers.handle_write(write_request)
                deserializer = serializers.WriteResponseSerializer(resp)
                return Response(deserializer.data, status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        # except:
        #     pass

    def get(self, request, format=None):
        # try:
            resp = requesthandlers.handle_read(request.path)
            deserializer = serializers.ReadResponseSerializer(resp)
            return Response(deserializer.data, status=status.HTTP_200_OK)
        # except:
        #     pass

    def delete(self, request, format=None):
        # try:
            requesthandlers.handle_delete(request.path)
            return Response(status=status.HTTP_200_OK)
        # except:
        #     pass


class Cluster(APIView):

    def get(self, request, format=None):
        resp = requesthandlers.cluster_query()
        deserializer = serializers.NodeManagerResponseSerializer(resp)
        return Response(deserializer.data, status=status.HTTP_200_OK)
