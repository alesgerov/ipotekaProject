package utils;

import java.math.BigDecimal;

public class MonthPayment {

    private Ipoteka ipoteka;
    private long id;
    private double generalPercentage;
    private BigDecimal ilkinOdenis;
    private BigDecimal ayliqOdenis;
    private BigDecimal paid;
    private BigDecimal balance;




    public MonthPayment(Ipoteka ipoteka, long id, BigDecimal ilkinOdenis, BigDecimal ayliqOdenis, BigDecimal paid, BigDecimal balance) {
        this.ipoteka = ipoteka;
        this.id = id;
        this.ilkinOdenis = ilkinOdenis;
        this.ayliqOdenis = ayliqOdenis;
        this.paid = paid;
        this.balance = balance;

    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid= paid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public void setBalance() {
        this.balance = this.ipoteka.getValueOfIpoteka();
    }




    @Override
    public String toString() {
        return "MonthPayment{" +
                "ipoteka=" + ipoteka +
                ", id=" + id +
                ", generalPercentage=" + generalPercentage +
                ", ilkinOdenis=" + ilkinOdenis +
                ", ayliqOdenis=" + ayliqOdenis +
                ", paid=" + paid +
                ", balance=" + balance +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Ipoteka getIpoteka() {
        return ipoteka;
    }

    public void setIpoteka(Ipoteka ipoteka) {
        this.ipoteka = ipoteka;
        setGeneralPercentage();
    }

    public double getGeneralPercentage() {
        return generalPercentage;
    }

    private void setGeneralPercentage() {
        this.generalPercentage = (double)this.ipoteka.getCountOfYears()*this.ipoteka.getPercentageOfYear()/100;
    }

    public BigDecimal getIlkinOdenis() {
        return ilkinOdenis;
    }

    public void setIlkinOdenis(BigDecimal ilkinOdenis) {
        this.ilkinOdenis = ilkinOdenis;
    }

    public BigDecimal getAyliqOdenis() {
        return ayliqOdenis;
    }

    public void setAyliqOdenis(BigDecimal ayliqOdenis) {
        this.ayliqOdenis = ayliqOdenis;
    }



    public MonthPayment() {
    }
}
