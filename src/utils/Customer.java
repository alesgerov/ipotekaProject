package utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

public class Customer {
    private long id;
    private String first_name;
    private String last_name;
    private LocalDate birthday;
    private BigDecimal home_value;
    private BigDecimal salary;
    private String finCode;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", birthday=" + birthday +
                ", home_value=" + home_value +
                ", salary=" + salary +
                ", finCode='" + finCode + '\'' +
                '}';
    }

    public String getFinCode() {
        return finCode;
    }

    public void setFinCode(String finCode) {
        this.finCode = finCode.toLowerCase(Locale.ROOT);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Customer(String first_name, long id, String last_name, LocalDate birthday, BigDecimal home_value, BigDecimal salary, String finCode) {
        this.first_name = first_name;
        this.id = id;
        this.last_name = last_name;
        this.birthday = birthday;
        this.home_value = home_value;
        this.salary = salary;
        this.finCode = finCode.toLowerCase(Locale.ROOT);
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getHome_value() {
        return home_value;
    }

    public void setHome_value(BigDecimal home_value) {
        this.home_value = home_value;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Customer() {
    }
}
