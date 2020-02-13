package com.phlink.bus.core.iotdata.rpcservice;

import com.phlink.bus.core.iotdata.service.DeviceInfoService;
import io.grpc.stub.StreamObserver;
import io.rpc.core.device.CommonReply;
import io.rpc.core.device.EWatchInfo;
import io.rpc.core.device.EWatchServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;


@GrpcService
public class EWatchService extends EWatchServiceGrpc.EWatchServiceImplBase {
    @Autowired
    private DeviceInfoService deviceInfoService;

    @Override
    public void save(EWatchInfo request, StreamObserver<CommonReply> responseObserver) {
        deviceInfoService.saveDeviceInfo(request);
        CommonReply reply = CommonReply.newBuilder().setMessage("保存成功：deviceId: " + request.getDeviceId()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
