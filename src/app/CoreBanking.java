package app;


import java.sql.*;
import java.util.Scanner;
import java.util.SimpleTimeZone;

public class CoreBanking {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        boolean condition = true;

        //Normal variable
        String accountName = "";
        String accountNumber = "";
        String accountType = "";
        double amount = 0.0;

        //Database variable
        String jdUrl = "jdbc:postgresql://localhost/java_thursday_2025";
        String jdUserName = "postgres";
        String jdPassword = "root";



        while (condition){
            System.out.println("----------------------------------------------------");
            System.out.println("=====MENU======");
            System.out.println("1. Register (Create an account): ");
            System.out.println("2. Display account details: ");
            System.out.println("3. Update account info: ");
            System.out.println("4. Search account info: ");
            System.out.println("5. Delete account: ");
            System.out.println("0. Exit");
            System.out.println("----------------------------------------------------");
            System.out.print("Select your option: ");
            Integer choice = input.nextInt();


            switch (choice){
                case 1:
                    System.out.println("====Registering your account====");
                    System.out.print("Enter your Name: ");
                    accountName = input.next();
                    System.out.print("Create/Enter the account number:  ");
                    accountNumber = input.next();
                    System.out.print("Enter the account type: ");
                    accountType = input.next();
                    System.out.print("Enter the first amount to be deposited: ");
                    amount = input.nextDouble();
                    try {
                        Connection con_register = DriverManager.getConnection(jdUrl ,jdUserName ,jdPassword);
                        Statement st = con_register.createStatement();

                        String sql = String.format("INSERT INTO bank_account (account_number ,account_name ,account_type ,amount) values ('%s','%s','%s',%f)" ,accountNumber ,accountName ,accountType ,amount);
                        int rowAffected = st.executeUpdate(sql);
                        if (rowAffected > 0){
                            System.out.println("Data has been saved successfully.");
                        }else{
                            System.out.println("No data saved");
                            con_register.close();
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }


                    System.out.println("=====================================================");
                    System.out.println("Do you wish to continue this program Y/N");
                    String out_register = input.next();
                    if(out_register.equalsIgnoreCase("Y")){
                        condition = true;
                    }else {
                        System.out.println("====The End====");
                        condition = false;
                    }

                    break;
                case 2:
                    try {
                        Connection con_display = DriverManager.getConnection(jdUrl ,jdUserName ,jdPassword);
                        String sql = "select * from bank_account";
                        PreparedStatement pst = con_display.prepareStatement(sql);
                        ResultSet rs = pst.executeQuery();
                        int counter = 0;
                        while (rs.next()){
                            counter++;
                            System.out.println("");
                            System.out.println("    Account: "+counter);
                            System.out.println("-----------------");
                            System.out.println("Name  : "+rs.getString("account_name"));
                            System.out.println("Number: "+rs.getString("account_number"));
                            System.out.println("Type  : "+rs.getString("account_type"));
                            System.out.println("Amount: $"+rs.getDouble("amount"));
                            System.out.println("****************");

                        }

                        con_display.close();
                        System.out.println("=====================================================");
                        System.out.println("Do you wish to continue this program Y/N");
                        String out_display = input.next();
                        if(out_display.equalsIgnoreCase("Y")){
                            condition = true;
                        }else {
                            System.out.println("====The End====");
                            condition = false;
                        }

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    break;
                case 3:
                    String number_to_ulter;
                    System.out.println("===================================");
                    System.out.print("Enter the account number of the record you want to ulter: ");
                     number_to_ulter = input.next();

                    String sql = "Select * from bank_account where account_number = ?";
                    try (Connection con_update = DriverManager.getConnection(jdUrl,jdUserName,jdPassword);
                    PreparedStatement pst_update = con_update.prepareStatement(sql)){

                        pst_update.setString(1,number_to_ulter);
                        ResultSet rs_update = pst_update.executeQuery();

                        if (rs_update.next()){
                            System.out.println("USER FOUND");
                            System.out.println("Name  : "+rs_update.getString("account_name"));
                            System.out.println("Number: "+rs_update.getString("account_number"));
                            System.out.println("Type  : "+rs_update.getString("account_type"));
                            System.out.println("Amount: $"+rs_update.getDouble("amount"));
                            System.out.println("");
                            System.out.print("Do you wish to update the entire record(enter yes/no): ");
                            String out_update0 = input.next();
                            if (out_update0.equalsIgnoreCase("YES")){
                                System.out.print("Enter new name: ");
                                String newName = input.next();
                                System.out.print("Enter new type of account: ");
                                String newType = input.next();
                                System.out.print("Enter new amount: ");
                                double newAmount = input.nextDouble();

                                try {
                                    Connection conUpdateAll = DriverManager.getConnection(jdUrl ,jdUserName ,jdPassword);
                                    Statement StUpdateAll  = conUpdateAll.createStatement();

                                    String sqlAll = String.format("UPDATE bank_account SET account_name ='%s', account_type = '%s', amount = %f ",newName ,newType ,newAmount);
                                    int rowsAffected = StUpdateAll.executeUpdate(sqlAll);
                                    if (rowsAffected > 0){
                                        System.out.println("The record has been updated successfully.");
                                    }else{
                                        System.out.println("Nothing happened");
                                    }
                                    conUpdateAll.close();
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }

                            }else {
                                System.out.print("What do you want to update(Name/Type/Amount): ");
                                String choice0 = input.next();

                                switch (choice0){
                                    case "Name":
                                        System.out.print("Enter the new name: ");
                                        String nameToUpdate = input.next();

                                        try {
                                            Connection conUpate1 = DriverManager.getConnection(jdUrl ,jdUserName ,jdPassword);
                                            Statement stUpdate = conUpate1.createStatement();

                                            String sql1 = String.format("UPDATE bank_account SET account_name = '%s' WHERE account_number = '%s'",nameToUpdate,number_to_ulter );
                                            int rowAffected = stUpdate.executeUpdate(sql1);
                                            if (rowAffected > 0){
                                                System.out.println("Name has been updated successfully.");
                                            }else {
                                                System.out.println("Name not updated.");
                                            }
                                            conUpate1.close();
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                        break;
                                    case "Type":
                                        System.out.print("Enter the new type of account(Saving/Recurring/Current/Fixed): ");
                                        String newType = input.next();

                                        try {
                                            Connection conUpdate2 = DriverManager.getConnection(jdUrl ,jdUserName ,jdPassword);
                                            Statement stUpdate1 = conUpdate2.createStatement();

                                            String sql2 = String.format("UPDATE bank_account SET account_type = '%s' WHERE account_number = '%s'",newType ,number_to_ulter);
                                            int rowsAffected1 = stUpdate1.executeUpdate(sql2);
                                            if (rowsAffected1 > 0){
                                                System.out.println("Account type has been updated successfully.");
                                            }else {
                                                System.out.println("Nothing has happened.");
                                            }
                                            conUpdate2.close();
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                        break;
                                    case "Amount":
                                        System.out.print("Enter the new amount: ");
                                        double newAmount = input.nextDouble();

                                        try {
                                            Connection conUpdateAmount = DriverManager.getConnection(jdUrl ,jdUserName ,jdPassword);
                                            Statement stUpdateAmount = conUpdateAmount.createStatement();

                                            String sql2 = String.format("UPDATE bank_account SET amount = %f WHERE account_number = '%s' ",newAmount ,number_to_ulter);
                                            int rowsAffected3 = stUpdateAmount.executeUpdate(sql2);
                                            if (rowsAffected3 > 0){
                                                System.out.println("Amount has been updated.");
                                            }else{
                                                System.out.println("Nothing happened.");
                                            }
                                            conUpdateAmount.close();
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                        break;
                                    default:
                                        System.out.println("jnj");
                                }
                            }
                        }else{
                            System.out.println("==>==>User not found........");
                        }
                        con_update.close();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    break;
                case 4:
                    System.out.println("");
                    break;
                case 5:
                    System.out.println("");
                    break;
                case 0:
                    System.out.println("Thank you for using our system");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice try again.");
            }
        }


    }
}
