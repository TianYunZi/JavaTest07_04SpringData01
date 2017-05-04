package org.test.springdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.springdata.domain.Person;
import org.test.springdata.repository.PersonRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by XJX on 2017/3/11.
 */
@Service
public class GetPersonServiceImpl implements GetPersonService {

    private PersonRepository personRepository;

    public GetPersonServiceImpl() {
    }

    @Autowired
    public GetPersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person getByLastName(String lastName) {
        System.out.println("------开始打印------" + personRepository.getByLastNameStartingWithAndIdLessThan("a", 10));
        return personRepository.getByLastName(lastName);
    }

    /**
     * 更新操作需要@Modifying和事务.
     *
     * @param email
     * @param id
     */
    @Override
    public void updatePersonEmail(String email, Integer id) {
        personRepository.updatePersonEmail(id, email);
        System.out.println("------更新操作完成------");
    }

    @Override
    @Transactional
    public void savePersons(List<Person> persons) {
        personRepository.save(persons);
    }

    @Override
    public void pageAndSortRepository() {
        int pageNo = 3 - 1;
        int pageSize = 5;
        //Pageable 接口通常使用的其 PageRequest 实现类. 其中封装了需要分页的信息
        //排序相关的. Sort 封装了排序的信息
        //Order 是具体针对于某一个属性进行升序还是降序.
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "id");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "email");
        Sort sort = new Sort(order1, order2);
        Pageable pageable = new PageRequest(pageNo, pageSize, sort);
        Page<Person> page = personRepository.findAll(pageable);
        System.out.println("总记录数：" + page.getTotalElements());
        System.out.println("当前第几页：" + (page.getNumber() + 1));
        System.out.println("总页数：" + page.getTotalPages());
        System.out.println("当前页面的List：" + page.getContent());
        System.out.println("当前页面的记录数：" + page.getNumberOfElements());
    }

    /**
     * 目标: 实现带查询条件的分页. id > 5 的条件
     * <p>
     * 调用 JpaSpecificationExecutor 的 Page<T> findAll(Specification<T> spec, Pageable pageable);
     * Specification: 封装了 JPA Criteria 查询的查询条件
     * Pageable: 封装了请求分页的信息: 例如 pageNo, pageSize, Sort
     */
    @Override
    public void jpaSpecificExecutorService() {
        Pageable pageable = new PageRequest(0, 5);

//        //通常使用 Specification 的匿名内部类
//        Specification<Person> specification = new Specification<Person>() {
//
//            /**
//             * @param *root: 代表查询的实体类.
//             * @param query: 可以从中可到 Root 对象, 即告知 JPA Criteria 查询要查询哪一个实体类. 还可以
//             * 来添加查询条件, 还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象.
//             * @param *cb: CriteriaBuilder 对象. 用于创建 Criteria 相关对象的工厂. 当然可以从中获取到 Predicate 对象
//             * @return: *Predicate 类型, 代表一个查询条件.
//             */
//            @Override
//            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Path<Number> path = root.get("id");
//                Predicate predicate = cb.ge(path, 50);
//                return predicate;
//            }
//        };

        Specification<Person> specification = (root, query, cb) -> {
            Path<Number> path = root.get("id");
            Predicate predicate = cb.ge(path, 50);
            return predicate;
        };
        Page<Person> page = personRepository.findAll(specification, pageable);

        System.out.println("总记录数：" + page.getTotalElements());
        System.out.println("当前第几页：" + (page.getNumber() + 1));
        System.out.println("总页数：" + page.getTotalPages());
        System.out.println("当前页面的List：" + page.getContent());
        System.out.println("当前页面的记录数：" + page.getNumberOfElements());

    }
}
