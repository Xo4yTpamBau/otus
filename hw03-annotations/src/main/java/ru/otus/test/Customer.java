package ru.otus.test;

public class Customer implements Comparable<Customer> {
    private final long id;
    private String name;
    private long scores;

    private boolean keyMap = false;

    public Customer(long id, String name, long scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = keyMap ? this.name : name;
    }

    public long getScores() {
        return scores;
    }

    public void setScores(long scores) {
        this.scores = keyMap ? this.scores : scores;
    }

    public void setKeyMap(boolean keyMap) {
        this.keyMap = keyMap;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scores=" + scores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return id == customer.getId();
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public int compareTo(Customer o) {
        return Long.compare(scores, o.getScores());
    }
}
