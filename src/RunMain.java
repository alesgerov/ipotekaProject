import ipoteka.functions.IpotekaChecker;
import jdbc.driver.JdbcUtils;
import jdbc.driver.OracleFunctions;
import utils.Customer;
import utils.Ipoteka;
import utils.MonthPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class RunMain {
    public static void main(String[] args) {
        OracleFunctions oracleFunctions1=new OracleFunctions();
        oracleFunctions1.createTable();

        Scanner scanner = new Scanner(System.in);
        int a = -1;

        while (a != 0) {
            System.out.println("Ipoteka yaratmaq ucun 1 yazin");
            System.out.println("Ipoteka odenisi almaq ucun 2 yazin");
            System.out.println("Qaliq borc ucun 3");
            System.out.println("Umumi odenilen mebleg ucun 4 ");
            System.out.println("Cixis ucun 0");
            System.out.print("Secimi daxil edin: ");
            a = scanner.nextInt();
            scanner.nextLine();
            if (a == 1) {
                Customer customer = addCustomer();
                Ipoteka ipoteka = addIpoteka();
                ipoteka.setCustomer(customer);
                MonthPayment monthPayment = new MonthPayment();
                monthPayment.setIpoteka(ipoteka);
                IpotekaChecker ipotekaChecker = new IpotekaChecker(customer, ipoteka, monthPayment);
                if (ipotekaChecker.isAvailable()) {
                    System.out.println("gosterilen sertlerle siz ipoteka uygundur");
                    System.out.println("Ilkin odenis: " + ipotekaChecker.getMonthPayment().getIlkinOdenis());
                    System.out.println("Ayliq odenis: " + ipotekaChecker.getMonthPayment().getAyliqOdenis());
                    System.out.println("Vaxt: " + ipotekaChecker.getAllowedYear());
                    System.out.print("Ipotekaniz aktiv edilsinmi? (y/n): ");
                    String ans = scanner.next().toLowerCase(Locale.ROOT);
                    if (ans.equals("y")) {
                        ipotekaChecker.addIpotekaToDatabase();
//                        monthPaymentMap.put(customer.getFinCode(), monthPayment);
                    } else {
                        System.out.println("Tesekkurler");
                    }
                }
                customer = null;
                ipoteka = null;
                monthPayment = null;
            }else if (a==2){
                System.out.print("Fincodeu daxil edin: ");
                String fin=scanner.next();
                scanner.nextLine();
                System.out.print("odenis miqdari: ");
                BigDecimal value=scanner.nextBigDecimal();
                IpotekaChecker ipotekaChecker=new IpotekaChecker();
                ipotekaChecker.payment(value,fin);

            }else if (a==3){
                System.out.print("Fincodeu daxil edin: ");
                String fin=scanner.next();
                scanner.nextLine();
//                MonthPayment monthPayment=monthPaymentMap.getOrDefault(fin,null);

                OracleFunctions oracleFunctions=new OracleFunctions();
                BigDecimal value=oracleFunctions.getBalance(JdbcUtils.open(null),fin);
                if (value!=null){
                    System.out.println(fin+" musterinin borcu "+value);
                }else {
                    System.out.println("Bele bir musteri yoxdur");
                }
            }

        }


        //todo eger isteyirse  dbya yazilsin


    }

    public static Customer addCustomer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();
        System.out.print("Adinizi daxil edin: ");
        String first_name = scanner.nextLine();
        customer.setFirst_name(first_name);
        scanner.nextLine();


        System.out.print("Soyadinizi daxil edin: ");
        String last_name = scanner.nextLine();
        customer.setLast_name(last_name);
        scanner.nextLine();

        System.out.print("Dogum tarixinizi daxil edin(meselen 20.09.1967): ");
        String tarix = scanner.nextLine();
        customer.setBirthday(LocalDate.parse(tarix, formatter));
        scanner.nextLine();

        System.out.print("Fincode daxil edin: ");
        String finCode = scanner.nextLine();
        customer.setFinCode(finCode);
        scanner.nextLine();

        System.out.print("Maasinizi daxil edin: ");
        BigDecimal salary = scanner.nextBigDecimal();
        customer.setSalary(salary);
        scanner.nextLine();

        System.out.print("Evin qiymetini daxil edin: ");
        BigDecimal home_value = scanner.nextBigDecimal();
        customer.setHome_value(home_value);
        scanner.nextLine();
        return customer;
    }

    public static Ipoteka addIpoteka() {
        Ipoteka ipoteka = new Ipoteka();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nece illik ipoteka isteyirsiniz?: ");
        int years = scanner.nextInt();
        scanner.nextLine();
        ipoteka.setCountOfYears(years);
        ipoteka.setPercentageOfYear(8);
        return ipoteka;
    }
}
