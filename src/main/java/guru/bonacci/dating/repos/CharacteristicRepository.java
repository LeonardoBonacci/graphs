package guru.bonacci.dating.repos;

import guru.bonacci.dating.model.Characteristic;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CharacteristicRepository extends Neo4jRepository<Characteristic, Long> {
}
