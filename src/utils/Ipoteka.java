package utils;

import java.math.BigDecimal;

public class Ipoteka {
    private Customer customer;
    private int countOfYears;
    private long percentageOfYear;
    private BigDecimal valueOfIpoteka;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ipoteka{" +
                "customer=" + customer +
                ", countOfYears=" + countOfYears +
                ", percentageOfYear=" + percentageOfYear +
                ", valueOfIpoteka=" + valueOfIpoteka +
                ", id=" + id +
                '}';
    }

    public Ipoteka(Customer customer, long id,int countOfYears, long percentageOfYear, BigDecimal valueOfIpoteka) {
        this.customer = customer;
        this.id=id;
        this.countOfYears = countOfYears;
        this.percentageOfYear = percentageOfYear;
        this.valueOfIpoteka = valueOfIpoteka;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        setValueOfIpoteka();
    }

    public int getCountOfYears() {
        return countOfYears;
    }

    public void setCountOfYears(int countOfYears) {
        this.countOfYears = countOfYears;
    }

    public long getPercentageOfYear() {
        return percentageOfYear;
    }

    public void setPercentageOfYear(long percentageOfYear) {
        this.percentageOfYear = percentageOfYear;
    }

    public BigDecimal getValueOfIpoteka() {
        return valueOfIpoteka;
    }

    private void setValueOfIpoteka() {
        this.valueOfIpoteka = this.customer.getHome_value();
    }

    public Ipoteka() {
    }
}
