package edu.bbte.projectbluebook.datacatalog.assets.repository;

import edu.bbte.projectbluebook.datacatalog.assets.model.Asset;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AssetRepository extends ReactiveMongoRepository<Asset, String> {
    Flux<Asset> findAllByNameContaining(String name);
}
