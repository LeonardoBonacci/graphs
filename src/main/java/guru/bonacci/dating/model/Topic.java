package guru.bonacci.dating.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

	@Id @GeneratedValue private Long id;
	private String name;
}
