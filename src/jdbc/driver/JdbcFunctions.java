package jdbc.driver;

import utils.Customer;
import utils.Ipoteka;
import utils.MonthPayment;

import java.math.BigDecimal;
import java.sql.Connection;

public interface JdbcFunctions {

    void addCustomer (Connection connection,Customer customer);
    void addCustomer (Customer customer);


    void addIpoteka (Connection connection, Ipoteka ipoteka);
    void addIpoteka(Ipoteka ipoteka);


    void addMonthPayment (Connection connection, MonthPayment monthPayment);
    void addMonthPayment(MonthPayment monthPayment);


//    void updateCustomer (Connection connection,Customer customer);
//    void updateCustomer (Customer customer);


//    void updateIpoteka (Connection connection, Ipoteka ipoteka);
//    void updateIpoteka(Ipoteka customer);
//
//
//    void updateMonthPayment (Connection connection, MonthPayment monthPayment);
//    void updateMonthPayment(MonthPayment customer);

    void getCustomer(Connection connection,String fincode);
    void getMonthPayment(Connection connection,long id);
    void getIpoteka(Connection connection,long id);

    long  getIpotekaId(Connection connection);
    long  getCustomerId(Connection connection);
    long  getMonthPaymentId(Connection connection);

    BigDecimal getOdenildi(Connection connection,String fin);
    BigDecimal getBalance(Connection connection,String fin);

    void  pay(Connection connection, BigDecimal value,String fincode);

    void createCustomersTable(Connection connection);
    void createIpotekaTable(Connection connection);
    void createMonthlyPaymentTable(Connection connection);

    boolean ifExists(Connection connection,String table);
}
