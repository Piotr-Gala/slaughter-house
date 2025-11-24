package via.pro3.slaughterhouse.grpcserver;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import via.pro3.slaughterhouse.application.PackagingService;
import via.pro3.slaughterhouse.generated.*;

import via.pro3.slaughterhouse.generated.Empty;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class PackagingGrpcService extends PackagingServiceGrpc.PackagingServiceImplBase {

    private final PackagingService packaging;

    public PackagingGrpcService(PackagingService packaging) {
        this.packaging = packaging;
    }

    @Override
    public void createSameTypeProduct(PartIds req, StreamObserver<ProductId> out) {
        try {
            List<Long> ids = req.getPartIdList().stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            var p = packaging.createSameTypeProduct(ids);
            out.onNext(ProductId.newBuilder().setId(p.getId().toString()).build());
            out.onCompleted();
        } catch (IllegalArgumentException e) {
            out.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (IllegalStateException e) {
            out.onError(Status.FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            out.onError(Status.INTERNAL.withDescription("Unexpected error").asRuntimeException());
        }
    }

    @Override
    public void createHalfAnimalProduct(PartIds req, StreamObserver<ProductId> out) {
        try {
            List<Long> ids = req.getPartIdList().stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            var p = packaging.createHalfAnimalProduct(ids);
            out.onNext(ProductId.newBuilder().setId(p.getId().toString()).build());
            out.onCompleted();
        } catch (IllegalArgumentException e) {
            out.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (IllegalStateException e) {
            out.onError(Status.FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            out.onError(Status.INTERNAL.withDescription("Unexpected error").asRuntimeException());
        }
    }



    @Override
    public void getHalfAnimalRequiredTypes(Empty request,
                                           io.grpc.stub.StreamObserver<Types> out) {
        var types = Types.newBuilder().addAllType(packaging.getHalfAnimalRequired()).build();
        out.onNext(types);
        out.onCompleted();
    }

}
