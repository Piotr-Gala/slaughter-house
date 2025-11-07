package via.pro3.slaughterhouse.grpcserver;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import via.pro3.slaughterhouse.generated.GetAnimalsByProductRequest;
import via.pro3.slaughterhouse.generated.GetAnimalsByProductResponse;
import via.pro3.slaughterhouse.generated.GetProductsByAnimalRequest;
import via.pro3.slaughterhouse.generated.GetProductsByAnimalResponse;
import via.pro3.slaughterhouse.generated.TraceServiceGrpc;
import via.pro3.slaughterhouse.repo.TraceRepository;

import java.util.List;
import java.util.UUID;

@GrpcService
public class TraceServiceImpl extends TraceServiceGrpc.TraceServiceImplBase {

    private final TraceRepository repo;

    public TraceServiceImpl(TraceRepository repo) {
        this.repo = repo;
    }

    @Override
    public void getAnimalsByProduct(GetAnimalsByProductRequest req,
                                    StreamObserver<GetAnimalsByProductResponse> out) {
        UUID productId = UUID.fromString(req.getProductId());
        List<String> regs = repo.findAnimalRegistrationNumbersByProductId(productId);

        GetAnimalsByProductResponse resp = GetAnimalsByProductResponse.newBuilder()
                .addAllAnimalRegistrationNumbers(regs)
                .build();

        out.onNext(resp);
        out.onCompleted();
    }

    @Override
    public void getProductsByAnimal(GetProductsByAnimalRequest req,
                                    StreamObserver<GetProductsByAnimalResponse> out) {

        // repo zwraca List<UUID>:
        List<String> ids = repo.findProductIdsByAnimalRegistrationNumber(req.getAnimalRegistrationNumber())
                .stream()
                .map(UUID::toString)
                .toList(); // jeśli kompilator marudzi, użyj .collect(Collectors.toList())

        GetProductsByAnimalResponse resp = GetProductsByAnimalResponse.newBuilder()
                .addAllProductIds(ids)   // tu już jest List<String>
                .build();

        out.onNext(resp);
        out.onCompleted();
    }
}