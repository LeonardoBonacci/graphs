package guru.bonacci.dating.model.queryresults;

import java.util.Set;

import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@QueryResult
public class PersonInterestsResult {

    String name;
    Set<String> interests;
}
