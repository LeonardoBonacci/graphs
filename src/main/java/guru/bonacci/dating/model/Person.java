package guru.bonacci.dating.model;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Getter
@Builder
@ToString
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
public class Person {

	@Id @GeneratedValue
	Long id;
	
	@Index(unique=true) String name;

	Gender gender;
	@Singular Set<Gender> genderPreferences;

	@Relationship(type = "LIVES_IN")
	Location home;

	@Relationship(type = "IS_OF_TYPE")
	Type type;

	@Relationship(type = "HAS_CHARACTERISTIC")
	@Singular List<Characteristic> characteristics;

	@Relationship(type = "LIKES")
	@Builder.Default List<Person> likes = new ArrayList<>();

	@Relationship(type = "DATED")
	@Builder.Default List<DateRate> dated = new ArrayList<>();

	@Relationship(type = "INTERESTED_IN")
	@Singular List<Topic> interests;
	

	public void addLikes(Person... likedPersons) {
        this.likes.addAll(asList(likedPersons));
    }

	public void addDateRate(Person rated, Integer rating) {
		dated.add(DateRate.builder().rater(this).rated(rated).rating(rating).build());
	}
}
