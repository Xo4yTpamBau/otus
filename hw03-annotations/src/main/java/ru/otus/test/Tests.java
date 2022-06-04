package ru.otus.test;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class Tests {

    @Before
    void before() {
        System.out.println("@Before");
    }

    @After
    void after() {
        System.out.println("@After");
    }

    @Test
    void setterCustomerTest() {
        System.out.println("@Test");
        //given
        String expectedName = "updatedName";
        String name = "nameVas";
        Customer customer = new Customer(1, name, 2);

        //when
        customer.setName(expectedName);

        //then
        if (!expectedName.equals(customer.getName())) {
            System.out.println("@Test");
            throw new RuntimeException("Тест провален");
        }
    }

    @Test
    void failedTest() {
        System.out.println("@Test");
        throw new RuntimeException();
    }

    @Test
    void setterCustomerTest2() {
        System.out.println("@Test");
        //given
        String expectedName = "updatedName";
        String name = "nameVas";
        Customer customer = new Customer(1, name, 2);

        //when
        customer.setName(expectedName);

        //then
        if (!expectedName.equals(customer.getName())) {
            throw new RuntimeException("Тест провален");
        }
    }

}
