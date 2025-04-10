package com.example.task41;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Database {
    private String url = "jdbc:mysql://10.0.2.2:3306/taskmanager?useSSL=false&allowPublicKeyRetrieval=true";
    private String user = "user";
    private String pass = "admin";
    private Statement statement;

    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
            Log.e("DATABASE_ERROR:",e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<Board> getAllBoards() {
        ArrayList<Board> boards = new ArrayList<>();
        try {
            String select = "SELECT * FROM boards";
            ResultSet rs = statement.executeQuery(select);
            while (rs.next()) {
                Board b = new Board(rs.getInt("ID"), rs.getString("BoardName"));
                boards.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boards;
    }

    public int getNextBoardID() {
        int id = 0;
        ArrayList<Board> boards = getAllBoards();
        int size = boards.size();
        if (size!=0) {
            int last = size-1;
            Board lastBoard = boards.get(last);
            id = lastBoard.getID() + 1;
        }
        return id;
    }

    public void addBoard(Board b) {
        try {
            String insert = "INSERT INTO `boards`(`ID`, `BoardName`) VALUES ('" +
                    b.getID()+"','"+b.getBoardName()+"');";
            statement.execute(insert);
            String create1 = "CREATE TABLE IF NOT EXISTS `"+b.getID()+
                    " - tasks` (`ID` integer, `TaskTitle` text, `TaskDescription` text, `Date` text) ;";
            statement.execute(create1);
            String create2 = "CREATE TABLE IF NOT EXISTS `"+b.getID()+
                    " - persons` (`ID` integer, `FirstName` text,`LastName` text, `Email` text,`Tel` text) ;";
            statement.execute(create2);
        } catch (Exception e) {
            Log.e("DATABASE_ERROR","Error adding board: "+e.toString());
            e.printStackTrace();
        }
    }

    public void deleteBoard(int id) {
        try {
            String delete = "DELETE FROM `boards` WHERE `ID` = "+id+" ;";
            statement.execute(delete);
            for (Task t: getTasks(id)) {
                deleteTask(id, t.getID());
            }
            String drop1 = "DROP TABLE `"+id+" - tasks`;";
            String drop2 = "DROP TABLE `"+id+" - persons`;";
            statement.execute(drop1);
            statement.execute(drop2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Person> getPersons(int id) {
        ArrayList<Person> persons = new ArrayList<>();
        try {
            String select = "SELECT * FROM `"+id+" - persons`;";
            ResultSet rs = statement.executeQuery(select);
            while (rs.next()) {
                Person p = new Person();
                p.setID(rs.getInt("ID"));
                p.setFirstName(rs.getString("FirstName"));
                p.setLastName(rs.getString("LastName"));
                p.setEmail(rs.getString("Email"));
                p.setTel(rs.getString("Tel"));
                persons.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persons;
    }

    public int getNextPersonID(int boardID) {
        int id = 0;
        ArrayList<Person> persons = getPersons(boardID);
        int size = persons.size();
        if (size!=0) {
            int last = size-1;
            Person lastPerson = persons.get(last);
            id = lastPerson.getID() + 1;
        }
        return id;
    }

    public Person getPerson(int boardID, int personID) {
        Person person = new Person();
        try {
            String select = "SELECT `ID`, `FirstName`, `LastName`, `Email`, `Tel` FROM `"+
                    boardID+" - persons` WHERE `ID` = "+personID+" ;";
            ResultSet rs = statement.executeQuery(select);
            rs.next();
            person.setID(rs.getInt("ID"));
            person.setFirstName(rs.getString("FirstName"));
            person.setLastName(rs.getString("LastName"));
            person.setEmail(rs.getString("Email"));
            person.setTel(rs.getString("Tel"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }

    public void addPerson(int boardID, Person p) {
        try {
            String insert = "INSERT INTO `"+boardID+" - persons` (`ID`, `FirstName`, `LastName`, `Email`, `Tel`)" +
                    " VALUES ('"+p.getID()+"','"+p.getFirstName()+"','"+p.getLastName()+"','"+p.getEmail()+"','"+p.getTel()+"') ;";
            statement.execute(insert);
        } catch (Exception e) {
            Log.e("DATABASE_ERROR","Adding person failed: "+e.toString());
            e.printStackTrace();
        }
    }

    public void updatePerson(int boardID, Person p) {
        try {
            String update = "UPDATE `"+boardID+" - persons` SET " +
                    "`FirstName` = '"+p.getFirstName()+"', " +
                    "`LastName` = '"+p.getLastName()+"', " +
                    "`Email` = '"+p.getEmail()+"', " +
                    "`Tel` = '"+p.getTel()+"'" +
                    "WHERE `ID` = "+p.getID()+" ;";
            statement.execute(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(int boardID, int personID) {
        try {
            String delete = "DELETE FROM `"+boardID+" - persons` WHERE `ID` = "+personID+" ;";
            statement.execute(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getTasks(int boardID) {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            String select = "SELECT * FROM `"+boardID+" - Tasks`;";
            ResultSet rs = statement.executeQuery(select);
            while (rs.next()) {
                Task t = new Task();
                t.setID(rs.getInt("ID"));
                t.setTitle(rs.getString("TaskTitle"));
                t.setDescription(rs.getString("TaskDescription"));
                t.setDate(rs.getString("Date"));
                tasks.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public int getNextTaskID(int boardID) {
        int id = 0;
        ArrayList<Task> tasks = getTasks(boardID);
        int size = tasks.size();
        if (size != 0) {
            int last = size-1;
            Task lastTask = tasks.get(last);
            id = lastTask.getID() + 1;
        }
        return id;
    }

    public void addTask(int boardID, Task t) {
        try {
            String insert = "INSERT INTO `"+boardID+" - tasks` (`ID`, `TaskTitle`, `TaskDescription`, `Date`)" +
                    " VALUES ('"+t.getID()+"','"+t.getTitle()+"','"+t.getDescription()+"','"+t.getDate()+"');";
            statement.execute(insert);
            String create = "CREATE TABLE IF NOT EXISTS `"+boardID+" - "+t.getID()+"` (`ID` integer) ;";
            statement.execute(create);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTaskData(int boardID, Task t) {
        try {
            String update = "UPDATE `"+boardID+" - tasks` SET " +
                    "`TaskTitle` = '"+t.getTitle()+"', " +
                    "`TaskDescription` = '"+t.getDescription()+"', " +
                    "`Date` = '"+t.getDate()+"' " +
                    "WHERE `ID` = "+t.getID()+" ;";
            statement.execute(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int boardID, int taskID) {
        try {
            String delete = "DELETE FROM `"+boardID+" - tasks` WHERE `ID` = "+taskID+" ;";
            statement.execute(delete);
            String drop = "DROP TABLE `"+boardID+" - "+taskID+"` ;";
            statement.execute(drop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task getTask(int boardID, int taskID) {
        Task t = new Task();
        try {
            String select = "SELECT `ID`, `TaskTitle`, `TaskDescription`, `Date` FROM `"+
                    boardID+" - tasks` WHERE `ID` = "+taskID+" ;";
            ResultSet rs = statement.executeQuery(select);
            rs.next();
            t.setID(rs.getInt("ID"));
            t.setTitle(rs.getString("TaskTitle"));
            t.setDescription(rs.getString("TaskDescription"));
            t.setDate(rs.getString("Date"));

            String select2 = "SELECT * FROM `"+boardID+" - "+taskID+"` ;";
            ResultSet rs2 = statement.executeQuery(select2);
            ArrayList<Integer> ids = new ArrayList<>();
            while (rs2.next()) {
                ids.add(rs2.getInt("ID"));
            }

            ArrayList<Person> persons = new ArrayList<>();
            for (int id: ids) {
                persons.add(getPerson(boardID, id));
            }
            t.setPersons(persons);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public void addPersonsToTask(int boardID, int taskID, ArrayList<Integer> persons) {
        try {
            for (Integer i: persons) {
                String insert = "INSERT INTO `"+boardID+" - "+taskID+"` (`ID`) VALUES ('"+i+"') ;";
                statement.execute(insert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePersonsFromTask(int boardID, int taskID, ArrayList<Integer> persons) {
        try {
            for (Integer i: persons) {
                String delete = "DELETE `"+boardID+" - "+taskID+"` WHERE `ID` = "+i+" ;";
                statement.execute(delete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
