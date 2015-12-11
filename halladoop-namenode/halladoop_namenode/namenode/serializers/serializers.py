from rest_framework import serializers
from namenode.models import requestmodels


class RegistrationRequestSerializer(serializers.Serializer):

    node_ip = serializers.IPAddressField()
    total_disk_space_mb = serializers.IntegerField()
    available_disk_space_mb = serializers.IntegerField()

    def create(self, validated_data):
        return requestmodels.RegistrationRequest(**validated_data)


class HeartbeatSerializer(serializers.Serializer):

    node_id = serializers.IntegerField()
    available_disk_space_mb = serializers.IntegerField()
    block_manifest = serializers.JSONField()

    def create(self, validated_data):
        return requestmodels.Heartbeat(**validated_data)


class FinalizeRequestSerializer(serializers.Serializer):

    block_id = serializers.CharField()
    nodes = serializers.ListField(child=serializers.IntegerField())

    def create(self, validated_data):
        return requestmodels.FinalizeRequest(**validated_data)


class WriteRequestSerializer(serializers.Serializer):

    file_path = serializers.CharField()
    num_blocks = serializers.IntegerField()

    def create(self, validated_data):
        return requestmodels.WriteRequest(**validated_data)


class RegistrationResponseSerializer(serializers.Serializer):

    node_id = serializers.IntegerField()


class HeartbeatResponseSerializer(serializers.Serializer):

    delete = serializers.ListField(child=serializers.CharField())
    replicate = serializers.JSONField()


class WriteResponseSerializer(serializers.Serializer):

    nodes = serializers.JSONField()


class ReadResponseSerializer(serializers.Serializer):

    manifest = serializers.JSONField()
