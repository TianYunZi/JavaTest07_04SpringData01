package org.test.springdata.service;

import org.test.springdata.domain.Person;

import java.util.List;

/**
 * Created by XJX on 2017/3/11.
 */
public interface GetPersonService {

    Person getByLastName(String lastName);

    void updatePersonEmail(String email, Integer id);

    void savePersons(List<Person> persons);

    void pageAndSortRepository();

    void jpaSpecificExecutorService();
}
