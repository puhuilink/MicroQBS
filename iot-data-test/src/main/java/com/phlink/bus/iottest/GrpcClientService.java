package com.phlink.bus.iottest;

import io.rpc.core.device.*;
import org.springframework.stereotype.Service;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class GrpcClientService {

    @GrpcClient("local-grpc-server")
    private EWatchServiceGrpc.EWatchServiceBlockingStub eWatchServiceBlockingStub;

    @GrpcClient("local-grpc-server")
    private BusServiceGrpc.BusServiceBlockingStub busServiceBlockingStub;

    public String saveBus(final BusInfo busInfo) {
        try {
            final CommonReply response = this.busServiceBlockingStub.save(busInfo);
            return response.getMessage();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }

    }

    public String saveEWatch(final EWatchInfo eWatchInfo) {
        try {
            final CommonReply response = this.eWatchServiceBlockingStub.save(eWatchInfo);
            return response.getMessage();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }
    }
}
