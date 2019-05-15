package guru.bonacci.dating.repos;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import guru.bonacci.dating.model.Person;
import guru.bonacci.dating.model.queryresults.PersonInterestsResult;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

	@Depth(value = 0)	
	Person findByName(String name);

	@Depth(value = 0)
	Stream<Person> findByNameLike(String name);


	@Query( "MATCH (p:Person)-[i:INTERESTED_IN]->(t:Topic)<-[:INTERESTED_IN]-(sameInterest:Person) " + 
			"WHERE p.name = {name} AND p <> sameInterest " + 
			"RETURN sameInterest.name as name, COLLECT(DISTINCT(t.name)) as interests")
	List<PersonInterestsResult> findMatchByInterests(@Param("name") String name);

	
	@Query( "MATCH (p:Person)-[:LIKES]->(:Person)<-[:LIKES]-(lookALiker:Person)-[:LIKES]->(potential:Person) " + 
		    "WHERE p.name = {name} AND p <> lookALiker AND potential.gender IN p.genderPreferences AND p.gender IN potential.genderPreferences " + 
		    "RETURN potential") 
	@Depth(value = 0)
	Stream<Person> findMatchByLikes(@Param("name") String name);

	
	@Query( "MATCH (p:Person)-[:LIKES]->(:Person)<-[:LIKES]-(lookALiker:Person)-[:LIKES]->(potential:Person) " + 
		    "WHERE p.name = {name} AND p <> lookALiker AND potential.gender IN p.genderPreferences AND p.gender IN potential.genderPreferences " + 
		    "WITH potential " +
		    "MATCH (potential)-[:IS_OF_TYPE]->(t:Type) " + 
		    "WHERE t.name = {type} " +
			"RETURN potential") 
	@Depth(value = 0)
	Stream<Person> findMatchByLikesOnType(@Param("name") String name, @Param("type") String type);


	@Query( "MATCH (p:Person)-[:LIKES]->(:Person)<-[:LIKES]-(lookALiker:Person)-[:LIKES]->(potential:Person) " + 
		    "WHERE p.name = {name} AND p <> lookALiker AND potential.gender IN p.genderPreferences AND p.gender IN potential.genderPreferences " + 
		    "WITH p, potential " +
		    "MATCH (p)-[:LIVES_IN]->(homeP:Location) " +
		    "MATCH (potential)-[:LIVES_IN]->(homePotential:Location) " + 
		    "WITH potential, point({ longitude: homeP.lon, latitude: homeP.lat }) AS here, point({ longitude: homePotential.lon, latitude: homePotential.lat }) AS there " + 
		    "WHERE toInteger(distance(here, there)/1000) < {distance} " +
			"RETURN potential") 
	@Depth(value = 0)
	Stream<Person> findMatchByLikesOnDistance(@Param("name") String name, @Param("distance") Integer distance);

	
	@Query( "MATCH (p:Person)-[pDate:DATED]->(:Person)<-[sameDate:DATED]-(sameDater:Person)-[potentialDate:DATED]->(potential:Person) " + 
            "WHERE p.name = {name} AND p <> sameDater AND pDate.rating >= {rating} AND sameDate.rating = pDate.rating AND potentialDate.rating > pDate.rating " + 
			"RETURN potential") 
	@Depth(value = 0)
	Stream<Person> findMatchByDateRate(@Param("name") String name, @Param("rating") Integer rating);
	

	@Query( "MATCH (p:Person)-[pDate:DATED]->(:Person)<-[sameDate:DATED]-(sameDater:Person)-[potentialDate:DATED]->(potential:Person) " + 
            "WHERE p.name = {name} AND p <> sameDater AND pDate.rating >= {rating} AND sameDate.rating = pDate.rating AND potentialDate.rating > pDate.rating " + 
			"WITH potential " + 
			"MATCH (potential)-[:HAS_CHARACTERISTIC]->()-[:IS_RELATED_TO*0..1]-(c:Characteristic) " + 
			"WHERE c.name = {character} " +
			"RETURN potential") 
	@Depth(value = 0)
	Stream<Person> findMatchByDateRateOnCharacter(@Param("name") String name, @Param("rating") Integer rating, @Param("character") String character);

}
