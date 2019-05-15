package guru.bonacci.dating.repos;

import static guru.bonacci.dating.model.Gender.F;
import static guru.bonacci.dating.model.Gender.M;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import guru.bonacci.dating.model.Characteristic;
import guru.bonacci.dating.model.Location;
import guru.bonacci.dating.model.Person;
import guru.bonacci.dating.model.Topic;
import guru.bonacci.dating.model.Type;
import guru.bonacci.dating.model.queryresults.PersonInterestsResult;
import lombok.Cleanup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class PersonRepositoryTests {

	@Autowired
	private PersonRepository repo;

	@Autowired
	private CharacteristicRepository charRepo;

	
	@Before
	public void setUp() {
		// CHARACTERISTICS
		Characteristic cour = Characteristic.builder().name("Courageous").build();
		Characteristic dari = Characteristic.builder().name("Daring").build();
		Characteristic brav = Characteristic.builder().name("Brave").build();
		cour.addRelated(dari);
		dari.addRelated(brav);
		
		Characteristic sent = Characteristic.builder().name("Sentimentality").build();
		Characteristic affe = Characteristic.builder().name("Affection").build();
		sent.addRelated(affe);
		
		Characteristic cunn = Characteristic.builder().name("Cunning").build();
		Characteristic immo = Characteristic.builder().name("Immoral").build();
		Characteristic veng = Characteristic.builder().name("Vengeful").build();
		Characteristic mani = Characteristic.builder().name("Maniacal").build();
		cunn.addRelated(immo);
		immo.addRelated(veng);
		veng.addRelated(mani);
		mani.addRelated(cunn);
		charRepo.saveAll(asList(cour, dari, brav, sent, affe, cunn, immo, veng, mani));

		// TYPES
		Type blondie = Type.builder().name("Blondie").build();

		// LOCATIONS
		Location adam = Location.builder().name("Amsterdam").lon(4.8936041f).lat(52.3727598f).build();
		Location delft = Location.builder().name("Delft").lon(4.35839f).lat(52.0114017f).build();

		// TOPICS
		Topic movies = Topic.builder().name("Movies").build();
		Topic golf = Topic.builder().name("Golf").build();
		Topic neo4j = Topic.builder().name("Neo4j").build();
		Topic lit = Topic.builder().name("Literature").build();
		Topic pro = Topic.builder().name("Programming").build();

		// PERSONS
		Person pa = Person.builder()
						.name("A")
						.home(adam)
						.gender(M)
						.genderPreference(F)
						.genderPreference(M)
						.interest(movies)
						.interest(neo4j)
						.interest(pro)
						.build();

		Person pb = Person.builder()
						.name("B")
						.gender(F)
						.genderPreference(F)
						.genderPreference(M)
						.interest(movies)
						.interest(neo4j)
						.interest(pro)
						.build();
		repo.save(pb);

		Person pc = Person.builder()
						.name("C")
						.gender(M)
						.genderPreference(F)
						.interest(golf)
						.interest(lit)
						.interest(pro)
						.build();
		
		Person pd = Person.builder()
						.name("D")
						.home(delft)
						.gender(M)
						.genderPreference(M)
						.type(blondie)
						.characteristic(cunn)
						.characteristic(brav)
						.interest(lit)
						.interest(pro)
						.build();

		Person pe = Person.builder()
						.name("E")
						.gender(F)
						.genderPreference(F)
						.interest(neo4j)
						.interest(pro)
						.build();

		repo.saveAll(asList(pa, pb, pc, pd, pe));

		// SOCIAL
		pa.addLikes(pb); // A likes B
		pb.addLikes(pa, pc); // B likes A and C
		pc.addLikes(pb, pd); // C likes B and D

		pa.addDateRate(pb, 7); // A rates B with 7
		pc.addDateRate(pb, 7); // C rates B with 7
		pc.addDateRate(pd, 8); // C rates D with 8
		pc.addDateRate(pe, 6); // C rates E with 6
		repo.saveAll(asList(pa, pb, pc, pd, pe));
	}

	@Test
	public void testFindByName() {
		String name = "A";
		Person result = repo.findByName(name);
		assertNotNull(result);
		assertEquals(M, result.getGender());
	}

	@Test
	public void testFindByNameContaining() {
		String name = "A*";
		@Cleanup Stream<Person> result = repo.findByNameLike(name);
		assertNotNull(result);
		assertEquals(1l, result.count());
	}

	@Test
	public void testMatchByInterests() {
		List<PersonInterestsResult> matching = repo.findMatchByInterests("A");
		assertEquals(4l, matching.size());
	}

	@Test
	public void testMatchByLikes() {
		List<Person> matching = repo.findMatchByLikes("A").collect(toList());
		assertEquals(1l, matching.size());
		assertEquals("D", matching.get(0).getName());
	}

	@Test
	public void testMatchByLikesOnType() {
		List<Person> matching = repo.findMatchByLikesOnType("A", "Blondie").collect(toList());
		assertEquals(1l, matching.size());
		assertEquals("D", matching.get(0).getName());
		
		@Cleanup Stream<Person> matchStream = repo.findMatchByLikesOnType("A", "Intellectual");
		assertEquals(0l, matchStream.count());
	}

	@Test
	public void testMatchByLikesOnDistanceype() {
		List<Person> matching = repo.findMatchByLikesOnDistance("A", 100).collect(toList());
		assertEquals(1l, matching.size());
		assertEquals("D", matching.get(0).getName());
		
		@Cleanup Stream<Person> matchStream = repo.findMatchByLikesOnDistance("A", 40);
		assertEquals(0l, matchStream.count());
	}

	@Test
	public void testMatchByDateRate() {
		List<Person> matching = repo.findMatchByDateRate("A", 7).collect(toList());
		assertEquals(1l, matching.size());
		assertEquals("D", matching.get(0).getName());
		
		@Cleanup Stream<Person> matchStream = repo.findMatchByDateRate("A", 8);
		assertEquals(0l, matchStream.count());
	}

	@Test
	public void testMatchByDateRateOnCharacter() {
		List<Person> matching = repo.findMatchByDateRateOnCharacter("A", 7, "Maniacal").collect(toList());
		assertEquals(1l, matching.size());
		assertEquals("D", matching.get(0).getName());
		
		@Cleanup Stream<Person> matchStream = repo.findMatchByDateRateOnCharacter("A", 7, "Vengeful");
		assertEquals(0l, matchStream.count());
	}

}