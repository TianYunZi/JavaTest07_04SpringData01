package org.test.springdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.test.springdata.domain.Person;
import org.test.springdata.service.GetPersonService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XJX on 2017/3/11.
 */
@Controller
@RequestMapping("/spring")
public class HandlerController {

    private GetPersonService getPersonService;

    public HandlerController() {
    }

    @Autowired
    public HandlerController(GetPersonService getPersonService) {
        this.getPersonService = getPersonService;
    }

    @RequestMapping("/home")
    public String goHome(String lastName) {
        List<Person> persons = new ArrayList<>();

        for (int i = 'a'; i <= 'z'; i++) {
            Person person = new Person();
            person.setBirth(new Date());
            person.setEmail((char) i + "" + (char) i + "@atguigu.com");
            person.setLastName((char) i + "" + (char) i);

            persons.add(person);
        }

        getPersonService.savePersons(persons);
        getPersonService.updatePersonEmail("aaaaaa@163.com", 1);
        Person person = getPersonService.getByLastName(lastName);
        System.out.println("打印Person: " + person);
        return "home";
    }

    @RequestMapping("/home2")
    public String goHome2() {
        getPersonService.pageAndSortRepository();
        return "home";
    }

    @RequestMapping("/home3")
    public String goHome3() {
        getPersonService.jpaSpecificExecutorService();
        return "home";
    }
}
