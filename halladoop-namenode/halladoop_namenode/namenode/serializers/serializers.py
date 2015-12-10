from rest_framework import serializers
from namenode.models import requestmodels, responsemodels


class RegistrationRequestSerializer(serializers.Serializer):

    nodeIP = serializers.IPAddressField()
    totalDiskSpaceMb = serializers.IntegerField()
    availableDiskSpaceMB = serializers.IntegerField()

    def create(self, validated_data):
        return requestmodels.RegistrationRequest(**validated_data)


class HeartbeatSerializer(serializers.Serializer):

    nodeID = serializers.IntegerField()
    availableDiskSpaceMB = serializers.IntegerField()
    blockManifest = serializers.JSONField()

    def create(self, validated_data):
        return requestmodels.Heartbeat(**validated_data)


class FinalizeRequestSerializer(serializers.Serializer):

    blockId = serializers.CharField()
    nodes = serializers.ListField(child=serializers.IntegerField())

    def create(self, validated_data):
        return requestmodels.FinalizeRequest(**validated_data)


class WriteRequestSerializer(serializers.Serializer):

    path = serializers.FilePathField()
    numBlocks = serializers.IntegerField()

    def create(self, validated_data):
        return requestmodels.WriteRequest(**validated_data)


class RegistrationResponseSerializer(serializers.Serializer):

    nodeID = serializers.IntegerField()


class HeartbeatResponseSerializer(serializers.Serializer):

    delete = serializers.ListField(child=serializers.CharField())
    replicate = serializers.JSONField()


class WriteResponseSerializer(serializers.Serializer):

    nodes = serializers.JSONField()


class ReadResponseSerializer(serializers.Serializer):

    manifest = serializers.JSONField()
