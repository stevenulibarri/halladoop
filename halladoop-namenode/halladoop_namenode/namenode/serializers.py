from rest_framework import serializers
from namenode.models import Registration


class RegistrationSerializer(serializers.Serializer):

    nodeID = serializers.IntegerField()
    nodeIP = serializers.IPAddressField()
    totalDiskSpaceMb = serializers.IntegerField()
    availableDiskSpaceMB = serializers.IntegerField()

    def create(self, validated_data):
        return Registration(**validated_data)
