from namenode.models import responsemodels


def handle_register(registration_request):
    return responsemodels.RegistrationResponse(1337)


def handle_heartbeat(heartbeat):
    return responsemodels.HeartbeatResponse(["1", "2", "3"], [{"block_id": "123", "nodes": ["1.2.3.4"]}])


def handle_finalize(finalize_request):
    pass


def handle_write(write_request):
    return responsemodels.WriteResponse(["1.1.1.1", "2.2.2.2"])


def handle_read(file_path):
    return responsemodels.ReadResponse([
            {"block_id": "123", "nodes": ["1.2.3.4.", "4.3.2.1", "8.8.8.8"]},
            {"block_id": "321", "nodes": ["1.2.3.4.", "4.3.2.1", "8.8.8.8"]},
        ])


def handle_delete(file_path):
    pass
