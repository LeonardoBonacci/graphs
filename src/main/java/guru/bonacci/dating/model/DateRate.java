package guru.bonacci.dating.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@RelationshipEntity(type = "DATED")
@NoArgsConstructor
@AllArgsConstructor
public class DateRate {

	@Id @GeneratedValue
	Long id;
	
	@Property Integer rating;

	@StartNode Person rater;
	@EndNode Person rated;
}
