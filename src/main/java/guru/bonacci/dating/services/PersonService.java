package guru.bonacci.dating.services;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.bonacci.dating.model.Person;
import guru.bonacci.dating.repos.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonService {

	private final PersonRepository repo;


    @Transactional(readOnly = true)
    public Person findByTitle(String name) {
        Person result = repo.findByName(name);
        return result;
    }

    @Transactional(readOnly = true)
    public Stream<Person> findByTitleLike(String name) {
        Stream<Person> result = repo.findByNameLike(name);
        result.peek(p -> log.info(p.toString()));
        return result;
    }
}