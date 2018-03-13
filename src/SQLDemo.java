import java.sql.ResultSet;
import java.sql.SQLException;

import db.MySQL;

public class SQLDemo
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		MySQL mySQL = new MySQL();
		mySQL.setAddress("localhost");
		mySQL.setPort(3306);
		mySQL.setUsername("luke");
		mySQL.setPassword("123");
		mySQL.setDatabaseName("user_system");//Name of data base
		
		String cmdStr = "";
		//initial
		if( mySQL.initial() == false )
		{
			System.out.println(mySQL.getErrorMessage());
		}		
		
		System.out.println("Connecting to SQL");
		if( mySQL.connectToMySQL() == false )
		{
			System.out.println(mySQL.getErrorMessage());
		}	
		
		//create table
		cmdStr = "create table mytable(name char(10),id int);";
		if( mySQL.insertData_raw(cmdStr) == false )
		{
			System.out.println(mySQL.getErrorMessage());
			return;				
		}		
		System.out.println("create table finished");
		
		//insert Value by raw cmd
		String name = "Hello";
		String idStr = "1";
		cmdStr = "insert into mytable(name, id) values(\"" + name + "\", " + idStr + ");";
		if( mySQL.insertData_raw(cmdStr) == false )
		{
			System.out.println(mySQL.getErrorMessage());
			return;				
		}		
		
		//insert Value by function
		String tableName = "mytable";
		Object[] valueList = new Object[2];
		valueList[0] = new String("World");
		valueList[1] = new Integer(2);
		if( mySQL.insertData(tableName, valueList) == false )
		{
			System.out.println(mySQL.getErrorMessage());
			return;		
		}
		System.out.println("insert value finished");
		
		
		//Output Value
		System.out.println("Get data from mytable:");
		tableName = "mytable";
		String[] columnList = new String[2];
		columnList[0] = "name";
		columnList[1] = "id";
		//columnList[2] = "passwd";
		ResultSet result = mySQL.outputData(tableName, columnList, false);
		if( result == null )	{ System.out.println(mySQL.getErrorMessage()); }
		else
		{
			try
			{
				System.out.println("name\t\tID\t\t"); 
				while(result.next())
				{ 
				    System.out.println(result.getString("name")+"\t\t"+ result.getInt("id")+"\t\t");     
				}		
			}
			catch(SQLException ignore) 
			{ 
				System.out.println("result get errer");
			} 			
		}		
				
		
		//destory table
		cmdStr = "drop table mytable;";
		if( mySQL.insertData_raw(cmdStr) == false )
		{
			System.out.println(mySQL.getErrorMessage());
			return;				
		}		
		System.out.println("drop table finished");			
		
		if( mySQL.disConnect() == false )
		{
			System.out.println(mySQL.getErrorMessage());
		}
		System.out.println("Disconnect from SQL");		

	}

}
