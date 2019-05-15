package guru.bonacci.dating.model;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
public class Characteristic {

	@Id @GeneratedValue
	Long id;
	
	String name;

	@Relationship(type = "IS_RELATED_TO")
	@Builder.Default List<Characteristic> related = new ArrayList<>();
	

	public void addRelated(Characteristic... chars) {
        this.related.addAll(asList(chars));
    }
}
