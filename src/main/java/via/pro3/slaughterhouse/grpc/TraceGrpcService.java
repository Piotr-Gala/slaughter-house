package via.pro3.slaughterhouse.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import via.pro3.slaughterhouse.generated.GetAnimalsByProductRequest;
import via.pro3.slaughterhouse.generated.GetAnimalsByProductResponse;
import via.pro3.slaughterhouse.generated.GetProductsByAnimalRequest;
import via.pro3.slaughterhouse.generated.GetProductsByAnimalResponse;
import via.pro3.slaughterhouse.generated.TraceServiceGrpc;
import via.pro3.slaughterhouse.repository.TraceRepository;

import java.util.List;

@GrpcService
public class TraceGrpcService extends TraceServiceGrpc.TraceServiceImplBase {

    private final TraceRepository repo;

    public TraceGrpcService(TraceRepository repo) {
        this.repo = repo;
    }

    @Override
    public void getAnimalsByProduct(GetAnimalsByProductRequest req,
                                    StreamObserver<GetAnimalsByProductResponse> out) {
        Long productId = Long.valueOf(req.getProductId());
        List<String> regs = repo.findAnimalIdByProductId(productId);

        GetAnimalsByProductResponse resp = GetAnimalsByProductResponse.newBuilder()
                .addAllAnimalIds(regs)
                .build();

        out.onNext(resp);
        out.onCompleted();
    }

    @Override
    public void getProductsByAnimal(GetProductsByAnimalRequest req,
                                    StreamObserver<GetProductsByAnimalResponse> out) {

        // repo zwraca List<UUID>:
        List<String> ids = repo.findProductIdsByAnimalId(req.getAnimalId())
                .stream()
                .map(String::valueOf)
                .toList(); // jeśli kompilator marudzi, użyj .collect(Collectors.toList())

        GetProductsByAnimalResponse resp = GetProductsByAnimalResponse.newBuilder()
                .addAllProductIds(ids)   // tu już jest List<String>
                .build();

        out.onNext(resp);
        out.onCompleted();
    }
}