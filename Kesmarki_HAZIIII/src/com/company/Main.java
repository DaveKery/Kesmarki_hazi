package com.company;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static final String ANSI_GREEN = "\u001B[32m";       // GREEN color
    public static final String ANSI_RED = "\u001B[31m";         // RED color
    public static final String ANSI_RESET = "\u001B[0m";        // color reset

    public static void main(String[] args) {
        System.out.println(ANSI_RED + "Start" + ANSI_RESET);    // Indicating the start of handling DB with red color
        startHandlingDatabase();                                // Start Console application
        System.out.println(ANSI_RED + "End" + ANSI_RESET);      // Indicating the end of handling DB with red color
    }

    /* @method name: startHandlingDatabase
     * @type: void
     * @param: ---
     * @return: ---
     * @description: It is used for calling the basic methods (set up connection to DB, create SQL statement then execute that) */
    public static void startHandlingDatabase() {

        try {
            Connection connectionSucceeded = configureDatabase();
            System.out.println("Successfully connected to PostgreSQL database!");

            Statement statement = connectionSucceeded.createStatement();

            executeCommand(statement, chooseCommand());

        }catch(Exception e){
            System.out.println("ERROR: " + e);
        }
    }

    /* @method name: configureDatabase
     * @type: Connection
     * @param: ---
     * @return: created connection / null
     * @description: It is used for configuring database with the required parameters of connection of DriverManager */
    public static Connection configureDatabase(){

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost:5432/";
            String userName = "postgres";
            String password = "************";

            Connection conn = DriverManager.getConnection(url, userName, password);

            return conn;

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    /* @method name: columnExists
     * @type: boolean
     * @param: ResultSet, String
     * @return: true/false
     * @description: It is used for checking if the specific column exists in ResultSet or not */
    private static boolean columnExists(ResultSet rs, String column){
        try{
            rs.findColumn(column);  // maps the given ResultSet column label to its ResultSet column index
            return true;
        } catch (SQLException sqlex){
            System.out.println("This column: " + column + " does not exist in your table!");
        }
        return false;
    }

    /* @method name: executeCommand
     * @type: void
     * @param: Statement, String[]
     * @return: ---
     * @description: It is used for executing the command by checking which command number the user gave and existence of column */
    private static void executeCommand(Statement st, String[] cmdFeatures){

        try {

            if(cmdFeatures[0].startsWith("SELECT")){

                ResultSet resultSet = st.executeQuery(cmdFeatures[0]);    // Executing SQL statement and returning ResultSet object

                if (!resultSet.isBeforeFirst()) {  // check if ResultSet is empty
                    System.out.println("ResultSet in empty!");
                    throw new SQLException("ResultSet is empty, user did not give any order!");
                }

                if (cmdFeatures[1].equals("1") && columnExists(resultSet, "userid") && columnExists(resultSet, "username") && columnExists(resultSet, "ageOfEmployee") && columnExists(resultSet, "hasCar") && columnExists(resultSet, "birthday")) {

                    System.out.println("Command is executed: SELECT * FROM Company2;");
                    System.out.printf(ANSI_GREEN + "%25s \t\t%s \t\t%s \t\t%s \t\t%s\n","userid","username", "ageOfEmployee", "hasCar", "birthday" + ANSI_RESET);
                    while (resultSet.next()) {
                        System.out.printf("%25s %15s %15s %15s %15s\n", resultSet.getString("userid"), resultSet.getString("username"), resultSet.getString("ageOfEmployee"), resultSet.getString("hasCar"), resultSet.getString("birthday"));
                    }
                    System.out.println("Data query was successful!");

                } else if (cmdFeatures[1].equals("2") && columnExists(resultSet, "userid") && !columnExists(resultSet, "username")) {   // to be sure if only 'userid' exists

                    System.out.println("Command is executed: SELECT userid FROM Company2;");
                    System.out.printf(ANSI_GREEN + "%30s \n","userid" + ANSI_RESET);
                    while (resultSet.next()) {
                        System.out.printf("%25s\n", resultSet.getString("userid"));
                    }
                    System.out.println("Data query was successful!");

                } else if (cmdFeatures[1].equals("3") && !columnExists(resultSet, "userid") && columnExists(resultSet, "username")) {   // to be sure if only 'username' exists

                    System.out.println("Command is executed: SELECT username FROM Company2;");
                    System.out.printf(ANSI_GREEN + "%30s \n","username" + ANSI_RESET);
                    while (resultSet.next()) {
                        System.out.printf("%25s\n", resultSet.getString("username"));
                    }
                    System.out.println("Data query was successful!");

                } else if (cmdFeatures[1].equals("4") && columnExists(resultSet, "NumberOfEmployee")){    // check this column name exists or not (this is own created!)

                    System.out.println("Command is executed: SELECT COUNT(username) AS NumberOfEmployee FROM Company2;");
                    while (resultSet.next()) {
                        System.out.printf("\t\t\tNumber of employee: %s\n", resultSet.getString("NumberOfEmployee"));
                    }
                    System.out.println("Data query was successful!");

                } else if (cmdFeatures[1].equals("5") && columnExists(resultSet, "AverageAge")){

                    System.out.println("Command is executed: SELECT AVG(ageOfEmployee) AS AverageAge FROM Company2;");
                    while (resultSet.next()) {
                        System.out.printf("\t\t\tAverage age of employee: %s\n", resultSet.getString("AverageAge"));
                    }
                    System.out.println("Data query was successful!");

                } else if (cmdFeatures[1].equals("6") && columnExists(resultSet, "NumberOfCars")){

                    System.out.println("Command is executed: SELECT COUNT(nullif(hasCar = false, true)) AS NumberOfCars FROM company2;");
                    while (resultSet.next()) {
                        System.out.printf("\t\t\tNumber of cars: %s\n", resultSet.getString("NumberOfCars"));
                    }
                    System.out.println("Data query was successful!");

                } else if (cmdFeatures[1].equals("7") && columnExists(resultSet, "HowManyDays")){

                    System.out.println("Command is executed: SELECT username, current_date - birthday+1 AS HowManyDays FROM Company2;");
                    System.out.printf(ANSI_GREEN + "%25s \t\t%s\n","username","HowManyDays"  + ANSI_RESET);
                    while (resultSet.next()) {
                        System.out.printf("%25s %15s\n", resultSet.getString("username"), resultSet.getString("HowManyDays"));
                    }
                    System.out.println("Data query was successful!");

                }
            }
            else if(cmdFeatures[0].startsWith("INSERT")){
                st.executeUpdate(cmdFeatures[0]);
                System.out.println("Data insertion was successful!");
            }
            else if(cmdFeatures[0].startsWith("UPDATE")){
                st.executeUpdate(cmdFeatures[0]);
                System.out.println("Data update was successful!");
            }
            else if(cmdFeatures[0].startsWith("DELETE")){
                st.executeUpdate(cmdFeatures[0]);
                System.out.println("Data deletion was successful!");
            }
            else if(cmdFeatures[0].equals("EXIT")){
                System.out.println("Exit from application...");
            }

        }catch (Exception ex){  // catch each type of exceptions
            System.out.println(ex);
        }
    }

    /* @method name: chooseCommand
     * @type: String[]
     * @param: ---
     * @return: String[] array
     * @description: It is used for presenting a list of commands to user, and putting the chosen one into an array */
    private static String[] chooseCommand(){

        String[] commandFeatures = {"",""};

        System.out.print("Following commands are available: "
                + "\n    1 - query each fields from Company2 (SELECT * FROM Company2;)"
                + "\n    2 - query 'userid' field from Company2 (SELECT userid FROM Company2;)"
                + "\n    3 - query 'username' field from Company2 (SELECT username FROM Company2;)"
                + "\n    4 - query number of employee in Company2 (SELECT COUNT(username) AS NumberOfEmployee FROM Company2;)"
                + "\n    5 - query average age of employee in Company2 (SELECT AVG(ageOfEmployee) AS AverageAge FROM Company2;)"
                + "\n    6 - query number of cars in Company2 (SELECT COUNT(nullif(hasCar = false, true)) AS NumberOfCars FROM company2;)"
                + "\n    7 - query how many days the employee was born before in Company2 (SELECT username, current_date - birthday+1 AS HowManyDays FROM Company2;)"
                + "\n    8 - insert new record into Company2"
                + "\n    9 - insert new record into Company1"
                + "\n    10 - update an existing record in Company2"
                + "\n    11 - update an existing record in Company1"
                + "\n    12 - delete an existing record from Company2"
                + "\n    13 - delete an existing record from Company1"
                + "\n    EXIT - Escape from the whole application"
                + "\nYour chosen query: ");
        Scanner string = new Scanner(System.in);
        String userChoice = string.nextLine();

        String SQL_command = "";

        try{

            if(userChoice.equals("1")){
                SQL_command = "SELECT * FROM Company2;";
            } else if(userChoice.equals("2")) {
                SQL_command = "SELECT userid FROM Company2;";
            } else if(userChoice.equals("3")) {
                SQL_command = "SELECT username FROM Company2;";
            } else if(userChoice.equals("4")) {
                SQL_command = "SELECT COUNT(username) AS NumberOfEmployee FROM Company2;";
            } else if(userChoice.equals("5")) {
                SQL_command = "SELECT AVG(ageOfEmployee) AS AverageAge FROM Company2;";
            } else if(userChoice.equals("6")){
                SQL_command = "SELECT COUNT(nullif(hasCar = false, true)) AS NumberOfCars FROM company2;";
            } else if(userChoice.equals("7")){
                SQL_command = "SELECT username, current_date - birthday+1 AS HowManyDays FROM Company2;";
            } else if(userChoice.equals("8")){
                SQL_command = getInsertionValues("Company2");
            } else if(userChoice.equals("9")){
                SQL_command = getInsertionValues("Company1");
            } else if(userChoice.equals("10")){
                SQL_command = getUpdateValues("Company2");
            } else if(userChoice.equals("11")){
                SQL_command = getUpdateValues("Company1");
            } else if(userChoice.equals("12")){
                SQL_command = getDeleteValues("Company2");
            } else if(userChoice.equals("13")){
                SQL_command = getDeleteValues("Company1");
            }
            else if(userChoice.equals("EXIT")){
                SQL_command = "EXIT";
            }
            else{
                throw new SQLException("Your command does not exist or incorrectly typed!");
            }

            if(SQL_command == null){
                throw new SQLException("Your command includes incorrect type of attribute!");
            }

        }catch(SQLException ex){
            System.out.println(ex);
        }

        commandFeatures[0] = SQL_command;
        commandFeatures[1] = userChoice;

        return commandFeatures;
    }

    /* @method name: getInsertionValues
     * @type: String
     * @param: String
     * @return: String
     * @description: It is used for building an INSERT SQL statement by asking attribute(s) from user */
    private static String getInsertionValues(String whichTable){

        String insertCommand = "INSERT INTO " + whichTable + " values(";

        System.out.println("Give attributes for insertion:");

        System.out.print("userid: ");
        Scanner value = new Scanner(System.in);
        String userid = value.nextLine();
        try{
            Integer.parseInt(userid);
        }catch (NumberFormatException ne){
            return null;
        }
        insertCommand += userid + ", ";

        System.out.print("username: ");
        value = new Scanner(System.in);
        String username = value.nextLine();
        insertCommand += "'" + username + "', ";

        System.out.print("ageOfEmployee: ");
        value = new Scanner(System.in);
        String ageOfEmployee = value.nextLine();
        try{
            Integer.parseInt(ageOfEmployee);
        }catch (NumberFormatException ne){
            return null;
        }
        insertCommand += ageOfEmployee + ", ";

        System.out.print("hasCar: ");
        value = new Scanner(System.in);
        String hasCar = value.nextLine();
        if(hasCar.equals("true") || hasCar.equals("false")){
            insertCommand += hasCar + ", '";
        } else {
            return null;
        }

        System.out.print("birthday: ");
        value = new Scanner(System.in);
        String birthday = value.nextLine();
        insertCommand += birthday + "');";

        return insertCommand;
    }

    /* @method name: getUpdateValues
     * @type: String
     * @param: String
     * @return: String
     * @description: It is used for building an UPDATE SQL statement by asking attribute(s) from user */
    private static String getUpdateValues(String whichTable){

        String updateCommand = "UPDATE " + whichTable + " SET ";

        System.out.println("Give attributes for updating:");

        try{
            System.out.print("UPDATE " + whichTable + " SET ");
            Scanner value = new Scanner(System.in);
            String column1 = value.nextLine().toLowerCase();
            updateCommand += column1 + " = ";

            if(!(column1.equals("userid")) && (!(column1.equals("username"))) && (!(column1.equals("ageofemployee"))) && (!(column1.equals("hascar"))) && (!(column1.equals("birthday")))){
                throw new SQLException("Wrong table name: There is no column \"" + column1 + "\" like that in " + whichTable + " table!");
            }

            System.out.print(updateCommand);
            value = new Scanner(System.in);
            String newValue = value.nextLine();
            updateCommand += newValue + " WHERE ";

            System.out.print(updateCommand);
            value = new Scanner(System.in);
            String column2 = value.nextLine().toLowerCase();
            updateCommand += column2 + " = ";

            if(!(column2.equals("userid")) && (!(column2.equals("username"))) && (!(column2.equals("ageofemployee"))) && (!(column2.equals("hascar"))) && (!(column2.equals("birthday")))){
                throw new SQLException("Wrong table name: There is no column \"" + column2 + "\" like that in " + whichTable + " table!");
            }

            System.out.print(updateCommand);
            value = new Scanner(System.in);
            String newValue2 = value.nextLine();
            updateCommand += newValue2 + ";";

        }catch (Exception ex){
            updateCommand = "";
            System.out.println(ex);
            System.out.println("Data update has been failed!");
        }

        return updateCommand;
    }

    /* @method name: getDeleteValues
     * @type: String
     * @param: String
     * @return: String
     * @description: It is used for building a DELETE SQL statement by asking attribute(s) from user */
    private static String getDeleteValues(String whichTable){

        String deleteCommand = "DELETE FROM " + whichTable + " WHERE ";

        System.out.println("Give attributes for updating:");

        try{
            System.out.print("DELETE FROM " + whichTable + " WHERE ");
            Scanner value = new Scanner(System.in);
            String column = value.nextLine().toLowerCase();
            deleteCommand += column + " = ";

            if(!(column.equals("userid")) && (!(column.equals("username"))) && (!(column.equals("ageofemployee"))) && (!(column.equals("hascar"))) && (!(column.equals("birthday")))){
                throw new SQLException("Wrong table name: There is no column \"" + column + "\" like that in " + whichTable + " table!");
            }

            System.out.print(deleteCommand);
            value = new Scanner(System.in);
            String existingValue = value.nextLine();
            deleteCommand += existingValue;

        }catch (Exception ex){
            deleteCommand = "";
            System.out.println(ex);
            System.out.println("Data deletion has been failed!");
        }

        return deleteCommand;
    }
}