package homework;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CustomerService {

    private final SortedMap<Customer, String> customers = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return customers.entrySet().iterator().next();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        SortedMap<Customer, String> newMap = customers.tailMap(customer);

        for (Map.Entry<Customer, String> entry : newMap.entrySet()) {
            if (entry.getKey().equals(customer)) {
                continue;
            }
            return entry;
        }
        return null;
    }

    public void add(Customer customer, String data) {
        customer.setKeyMap(true);
        customers.put(customer, data);
    }
}
