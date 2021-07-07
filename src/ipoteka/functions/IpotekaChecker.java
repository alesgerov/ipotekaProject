package ipoteka.functions;

import jdbc.driver.JdbcUtils;
import jdbc.driver.OracleFunctions;
import utils.Customer;
import utils.Ipoteka;
import utils.MonthPayment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class IpotekaChecker {
    private Customer customer;
    private Ipoteka ipoteka;
    private MonthPayment monthPayment;

    public MonthPayment getMonthPayment() {
        return monthPayment;
    }

    public IpotekaChecker(Customer customer, Ipoteka ipoteka, MonthPayment monthPayment) {
        this.customer = customer;
        this.ipoteka = ipoteka;
        this.monthPayment = monthPayment;
    }

    public void setIpoteka(Ipoteka ipoteka) {
        this.ipoteka = ipoteka;
    }

    public void setMonthPayment(MonthPayment monthPayment) {
        this.monthPayment = monthPayment;
    }

    public IpotekaChecker() {
    }



    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getAllowedYear() {
        int years = maxAllowedYear();
        final int pensionYear = 65;
        int now = LocalDate.now().getYear();
        int localDate = this.customer.getBirthday().getYear();

        if (now - localDate >= pensionYear) {
            years = 0;
            System.out.println("Yasinizdan dolayi sizin ucun ipoteka uygun deyil.");
        } else if (now - localDate < pensionYear) {
            years = pensionYear - (now - localDate);
        }
        if (years>25){
            years=maxAllowedYear();
        }
        return years;
    }

    private   BigDecimal monthlyPaymentCalculate(){
        BigDecimal result=BigDecimal.ZERO;
        double generalPercentage=(double) this.monthPayment.getGeneralPercentage()/100;
        BigDecimal valueForDividingMonth=this.ipoteka.getValueOfIpoteka().subtract(initialPaymentCalculate());

        valueForDividingMonth=valueForDividingMonth.add(valueForDividingMonth.multiply(BigDecimal.valueOf(generalPercentage)));


        long months=getAllowedYear()* 12L;
        result=valueForDividingMonth.divide(BigDecimal.valueOf(months), RoundingMode.HALF_UP);


        return  result;
    }

    private BigDecimal initialPaymentCalculate(){
        BigDecimal result=BigDecimal.ZERO;
        if (this.ipoteka.getValueOfIpoteka().compareTo(BigDecimal.valueOf(150000))<=0) {
            result = this.ipoteka.getValueOfIpoteka().multiply(BigDecimal.valueOf(0.1));
        }else {
            result=BigDecimal.valueOf(150000).multiply(BigDecimal.valueOf(0.1));
        }
        result=result.add(ifValueMoreThan150000());
        return result;
    }

    private BigDecimal ifValueMoreThan150000(){
        BigDecimal result=BigDecimal.ZERO;
        if (this.ipoteka.getValueOfIpoteka().compareTo(BigDecimal.valueOf(150000))>0){
            result=this.ipoteka.getValueOfIpoteka().subtract(BigDecimal.valueOf(150000));
        }
        return result;
    }

    private int maxAllowedYear(){
        int year=this.ipoteka.getCountOfYears();
        if (year>25){
            year=25;
        }
        return  year;
    }


    private boolean isAllowedForSalary(){
        boolean result=true;
        BigDecimal salaryCalculator=this.customer.getSalary().multiply(BigDecimal.valueOf(0.7));
        if (monthlyPaymentCalculate().compareTo(salaryCalculator)>0){
            System.out.println("Maasiniz bu ipotekaya icaze vermir.");
            result=false;
        }
        return result;
    }

    public void setMonthPaymentUtils(){
        monthPayment.setIlkinOdenis(initialPaymentCalculate());
        monthPayment.setAyliqOdenis(monthlyPaymentCalculate());
        monthPayment.setBalance();
        monthPayment.setBalance(calculateBalance(initialPaymentCalculate()));
    }



    public void addIpotekaToDatabase(){
        OracleFunctions functions=new OracleFunctions();

        functions.addCustomer(customer);
        functions.addIpoteka(ipoteka);
        functions.addMonthPayment(monthPayment);

        System.out.println("Proses ugurla basa catdi");

    }

    public BigDecimal calculateBalance(BigDecimal number){
        BigDecimal result=this.monthPayment.getBalance().subtract(number);
        return result;
    }

    public void payment(BigDecimal value,String fincode){
        OracleFunctions oracleFunctions=new OracleFunctions();
        oracleFunctions.pay(JdbcUtils.open(null),value,fincode);
    }

    public boolean isAvailable(){
        if (getAllowedYear()>0  && isAllowedForSalary() ){
            setMonthPaymentUtils();
            return true;
        }else {
            return false;
        }
    }





}
