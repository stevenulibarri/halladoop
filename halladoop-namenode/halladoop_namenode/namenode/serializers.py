from models import Registration

from rest_framework import serializers


class RegistrationSerializer(serializers.ModelSerializer):

    class Meta:
        model = Registration
        fields = ('nodeId', 'nodeIP', 'totalDiskSpaceMb', 'availableDiskSpaceMB')

        def create(self, validated_data):
            return Registration(**validated_data)
