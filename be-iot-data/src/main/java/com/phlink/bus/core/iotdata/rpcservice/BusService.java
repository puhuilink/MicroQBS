package com.phlink.bus.core.iotdata.rpcservice;

import com.phlink.bus.core.iotdata.service.DeviceInfoService;
import io.grpc.stub.StreamObserver;
import io.rpc.core.device.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;


@GrpcService
public class BusService extends BusServiceGrpc.BusServiceImplBase {

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Override
    public void save(BusInfo request, StreamObserver<CommonReply> responseObserver) {
        deviceInfoService.saveBusInfo(request);
        CommonReply reply = CommonReply.newBuilder().setMessage("保存成功：busId: " + request.getBusId()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
