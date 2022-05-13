package homework;


import java.util.*;

public class CustomerReverseOrder {
    Deque<Customer> customers = new LinkedList<>();

    public void add(Customer customer) {
        customers.addLast(customer);
    }

    public Customer take() {
        return customers.pollLast();
    }
}
