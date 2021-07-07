package jdbc.driver;

import oracle.jdbc.proxy.annotation.Pre;
import utils.Customer;
import utils.Ipoteka;
import utils.MonthPayment;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;

public class OracleFunctions implements JdbcFunctions {


    @Override
    public long getIpotekaId(Connection connection) {
        long result=10;
        String sql="select ipotekasqn.nextval from dual";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try {
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                result=resultSet.getLong(1);
                return result;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

        return result;
    }

    @Override
    public long getCustomerId(Connection connection) {
        long result=1000;
        String sql="select customersqn.nextval from dual";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                result=resultSet.getLong(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }
        return result;
    }

    @Override
    public long getMonthPaymentId(Connection connection) {
        long result=100;
        String sql="select monthsqn.nextval from dual";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                result=resultSet.getLong(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }
        return result;
    }

    @Override
    public void addCustomer(Connection connection, Customer customer) {
        String sql="insert  into customers(first_name,last_name,birthday,home_value,salary,fincode,id,start_date)  values (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=null;
        long id=this.getCustomerId(JdbcUtils.open(null));
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,customer.getFirst_name());
            preparedStatement.setString(2, customer.getLast_name());
            preparedStatement.setDate(3, Date.valueOf(customer.getBirthday()));
            preparedStatement.setBigDecimal(4, customer.getHome_value());
            preparedStatement.setBigDecimal(5, customer.getSalary());
            preparedStatement.setString(6,customer.getFinCode());
            preparedStatement.setLong(7,id);
            preparedStatement.setDate(8, Date.valueOf(LocalDate.now()));

            int count=preparedStatement.executeUpdate();
            if (count==1){
                System.out.println("Musteri elave edildi");
                customer.setId(id);
            }
            else  System.out.println("Problem bas verdi");



        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Musteri var artiq");
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        addCustomer(JdbcUtils.open(null),customer);
    }


    @Override
    public void addIpoteka(Connection connection, Ipoteka ipoteka) {
        String sql="insert  into ipoteka(count_of_years,percentage,value_of_ipoteka,customer_fin,ipoteka_id,start_date) " +
                "values (?,?,?,?,?,?)";

        PreparedStatement preparedStatement=null;
        long id=this.getIpotekaId(JdbcUtils.open(null));
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,ipoteka.getCountOfYears());
            preparedStatement.setLong(2,ipoteka.getPercentageOfYear());
            preparedStatement.setBigDecimal(3,ipoteka.getValueOfIpoteka());
            preparedStatement.setString(4,ipoteka.getCustomer().getFinCode());
            preparedStatement.setLong(5,id);
            preparedStatement.setDate(6, Date.valueOf(LocalDate.now()));
            int count=preparedStatement.executeUpdate();
            if (count==1){
                System.out.println("ipoteka ugurlu oldu");
                ipoteka.setId(id);
            }else {
                System.out.println("Problem bas verdi");
            }
        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Cari musteri ucun artiq ipoteka var ");
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    @Override
    public void addIpoteka(Ipoteka ipoteka) {
        addIpoteka(JdbcUtils.open(null),ipoteka);
    }

    @Override
    public void addMonthPayment(Connection connection, MonthPayment monthPayment) {
        PreparedStatement preparedStatement=null;
        String sql="insert  into monthly_payment(ipoteka_id,umumi_faiz,ayliq_odenis,ilkin_odenis,payment_id,start_date,odenildi,qaliq_pul,umumi_mebleg,fincode) " +
                "values (?,?,?,?,?,?,?,?,?,?)";
        long id=this.getMonthPaymentId(JdbcUtils.open(null));
        try {
            if (monthPayment.getIpoteka().getId()!=0) {

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, monthPayment.getIpoteka().getId());
                preparedStatement.setDouble(2, monthPayment.getGeneralPercentage());
                preparedStatement.setBigDecimal(3, monthPayment.getAyliqOdenis());
                preparedStatement.setBigDecimal(4, monthPayment.getIlkinOdenis());
                preparedStatement.setLong(5, id);
                preparedStatement.setDate(6, Date.valueOf(LocalDate.now()));
                preparedStatement.setBigDecimal(7, monthPayment.getIlkinOdenis());
                preparedStatement.setBigDecimal(8, monthPayment.getBalance());
                preparedStatement.setBigDecimal(9, monthPayment.getIpoteka().getValueOfIpoteka());
                preparedStatement.setString(10, monthPayment.getIpoteka().getCustomer().getFinCode());

                monthPayment.setId(id);
                int count = preparedStatement.executeUpdate();
                if (count == 1) {
                    System.out.println("Ayliq odenis hazirlandi");
                } else System.out.println("Problem bas verdi");
            }
            else {

                System.out.println(monthPayment.getIpoteka().getCustomer().getFinCode()+" Cari kredit ucun ayliq odenis hazirdir artiq");
            }

        }catch (SQLIntegrityConstraintViolationException e){

            System.out.println(monthPayment.getIpoteka().getCustomer().getFinCode()+"Cari kredit ucun ayliq odenis hazirdir artiq");
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }


    @Override
    public void addMonthPayment(MonthPayment monthPayment) {
        addMonthPayment(JdbcUtils.open(null),monthPayment);
    }

    @Override
    public void getCustomer(Connection connection, String finCode) {
        PreparedStatement preparedStatement=null;
        String sql="select * from customers where fincode=?";
        ResultSet resultSet=null;

        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,finCode);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){

                System.out.printf("customer_id=%s first_name=%s last_name=%s birthday=%s home_value=%s salary=%s fincode=%s\n",resultSet.getLong("id"),resultSet.getString("first_name"),resultSet.getString("last_name"),resultSet.getDate("birthday"),resultSet.getBigDecimal("home_value"),resultSet.getBigDecimal("salary"),resultSet.getString("fincode"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }
    }

    @Override
    public void getMonthPayment(Connection connection, long id) {
        PreparedStatement preparedStatement=null;
        String sql="select * from monthly_payment where payment_id=?";
        ResultSet resultSet=null;

        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.printf("payment_id=%s ipoteka_id=%s umumi_faiz=%s ayliq_odenis=%s ilkin_odenis=%s\n",resultSet.getLong("payment_id"),resultSet.getLong("ipoteka_id"),resultSet.getLong("umumi_faiz"),resultSet.getBigDecimal("ayliq_odenis"),resultSet.getBigDecimal("ilkin_odenis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

    }

    @Override
    public void getIpoteka(Connection connection, long id) {
        PreparedStatement preparedStatement=null;
        String sql="select * from ipoteka where ipoteka_id=?";
        ResultSet resultSet=null;

        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.printf("ipoteka_id=%s count_of_years=%s percentage=%s value_of_ipoteka=%s customer_fin=%s\n",resultSet.getLong("ipoteka_id"),resultSet.getLong("count_of_years"),resultSet.getLong("percentage"),resultSet.getBigDecimal("value_of_ipoteka"),resultSet.getString("customer_fin"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

    }


    @Override
    public BigDecimal getOdenildi(Connection connection, String fin) {
        BigDecimal result=BigDecimal.ZERO;
        String  sql="select odenildi from monthly_payment " +
                "where fincode=?";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try {
            preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1,fin);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                result=resultSet.getBigDecimal("odenildi");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

        return result;
    }

    @Override
    public BigDecimal getBalance(Connection connection, String fin) {
        BigDecimal result=BigDecimal.ZERO;
        String  sql="select qaliq_pul from monthly_payment " +
                "where fincode=?";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try {
            preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1,fin);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                result=resultSet.getBigDecimal("qaliq_pul");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }
        return result;
    }

    @Override
    public void pay(Connection connection, BigDecimal value,String fincode) {
        PreparedStatement preparedStatement=null;
        BigDecimal odenildi=getOdenildi(JdbcUtils.open(null),fincode);
        BigDecimal balance=getBalance(JdbcUtils.open(null),fincode);
        odenildi=odenildi.add(value);
        balance=balance.subtract(value);
        String sql ="update monthly_payment " +
                "set odenildi=?,qaliq_pul=? " +
                "where fincode=?";
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setBigDecimal(1,odenildi);
            preparedStatement.setBigDecimal(2,balance);
            preparedStatement.setString(3,fincode);
//            System.out.println(monthPayment.getId());
            int count=preparedStatement.executeUpdate();
            if (count==1){
                System.out.println("odenis alindi");
            }else {
                System.out.println("upps problem");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    @Override
    public void createCustomersTable(Connection connection) {
        String sql="CREATE TABLE  CUSTOMERS   \n" +
                "(\n" +
                "  ID NUMBER NOT NULL \n" +
                ", FIRST_NAME VARCHAR2(40 BYTE) NOT NULL \n" +
                ", LAST_NAME VARCHAR2(40 BYTE) NOT NULL \n" +
                ", BIRTHDAY DATE NOT NULL \n" +
                ", HOME_VALUE NUMBER NOT NULL \n" +
                ", SALARY NUMBER NOT NULL \n" +
                ", FINCODE VARCHAR2(20 BYTE) NOT NULL \n" +
                ", START_DATE DATE NOT NULL \n" +
                ", CONSTRAINT CUSTOMERS_PK PRIMARY KEY \n" +
                "  (\n" +
                "    FINCODE \n" +
                "  )\n" +
                ")";
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }


    }

    @Override
    public void createIpotekaTable(Connection connection) {
        String sql="CREATE TABLE  IPOTEKA  " +
                "(" +
                "  IPOTEKA_ID NUMBER NOT NULL " +
                ", COUNT_OF_YEARS NUMBER NOT NULL " +
                ", PERCENTAGE NUMBER NOT NULL " +
                ", VALUE_OF_IPOTEKA NUMBER NOT NULL " +
                ", CUSTOMER_FIN VARCHAR2(20 BYTE) NOT NULL " +
                ", START_DATE DATE NOT NULL " +
                ", CONSTRAINT IPOTEKA_PK PRIMARY KEY " +
                "  (" +
                "    CUSTOMER_FIN \n" +
                "  )" +
                ")" ;
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }

    }

    @Override
    public void createMonthlyPaymentTable(Connection connection) {
        String sql="CREATE TABLE MONTHLY_PAYMENT   " +
                "(" +
                "  PAYMENT_ID NUMBER NOT NULL " +
                ", IPOTEKA_ID NUMBER NOT NULL " +
                ", UMUMI_FAIZ NUMBER NOT NULL " +
                ", AYLIQ_ODENIS NUMBER NOT NULL " +
                ", ILKIN_ODENIS NUMBER NOT NULL " +
                ", START_DATE DATE NOT NULL " +
                ", ODENILDI NUMBER " +
                ", QALIQ_PUL NUMBER " +
                ", UMUMI_MEBLEG NUMBER NOT NULL " +
                ", FINCODE VARCHAR2(20 BYTE) NOT NULL " +
                ", CONSTRAINT MONTHLY_PAYMENT_PK PRIMARY KEY " +
                "  (" +
                "    FINCODE " +
                "  )" +
                ")";
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }

    }

    @Override
    public boolean ifExists(Connection connection, String table) {
        String sql="select COUNT(*) as c from tab " +
                "where  tname=?";
        table=table.toUpperCase(Locale.ROOT);
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1,table);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getLong("c")==1){
                    return true;
                }else {
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

        return false;
    }

    public  void createTable(){
        if (!this.ifExists(JdbcUtils.open(null),"customers")){
            this.createCustomersTable(JdbcUtils.open(null));
            System.out.println("cedvel yaradildi");
        }if (!this.ifExists(JdbcUtils.open(null),"ipoteka")){
            this.createIpotekaTable(JdbcUtils.open(null));
            System.out.println("cedvel yaradildi");
        }if (!this.ifExists(JdbcUtils.open(null),"monthly_payment")){
            this.createMonthlyPaymentTable(JdbcUtils.open(null));
            System.out.println("cedvel yaradildi");

        }
    }
}
