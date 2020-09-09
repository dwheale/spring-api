package com.springapi.springapi.dao;

import com.springapi.springapi.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        final String sql = "INSERT INTO person(id, name) VALUES (?::uuid, ?)";
        return jdbcTemplate.update(sql, id, person.getName());
    }

    @Override
    public List<Person> selectAllPeople() {
        final String sql = "SELECT id, name FROM person";
        return jdbcTemplate.query(sql, (resultSet, i) -> new Person(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name")
        ));
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        final String sql = "SELECT id, name FROM person WHERE id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> new Person(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name")
        )));
    }

    @Override
    public int deletePersonById(UUID id) {
        final String sql = "DELETE FROM person WHERE id = ?::uuid";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        final String sql = "UPDATE person SET name = ? WHERE id = ?::uuid";
        return jdbcTemplate.update(sql, person.getName(), id);
    }
}
