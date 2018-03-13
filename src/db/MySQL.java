package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.omg.CORBA.PRIVATE_MEMBER;

public class MySQL
{
	private String errorMessage;
	
	private String driver = "com.mysql.jdbc.Driver";
	//String url = "jdbc:mysql://localhost:3306/user_system?useSSL=false";
	private int port = 3306;
	private String databaseName = "database";
	private String address = "127.0.0.1";	
	private String username = "luke";   
	private String password = "123";
	
	private Connection con = null;
	Statement state = null;
	
	public MySQL()
	{
		// TODO Auto-generated constructor stub
	}
	
	public MySQL(String address, int port, String databasesName, String username, String password)
	{
		this.address = address;
		this.port = port;
		this.databaseName = databasesName;
		this.username = username;
		this.password = password;
	}
	
	public boolean initial()
	{
		//read driver
		try
		{
//		    System.out.println("Loading driver...");
		    Class.forName(driver);
//		    System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) 
		{
		    //throw new RuntimeException("Cannot find the driver in the classpath!", e);
			errorMessage = "Cannot find the driver in the classpath!\n";
			return false;
		}				
		return true;
	}
	
	//connecting to MySQL
	public boolean connectToMySQL()
	{
		String url = "";
		url = "jdbc:mysql://"+address+":"+port+"/"+databaseName+"?useSSL=false";		
		try 
		{
//		    System.out.println("Connecting database...");
		    con = DriverManager.getConnection(url, username, password);
//		    System.out.println("Database connected!");	    
		    state = con.createStatement();
		    
		} catch (SQLException e) 
		{
//		    throw new RuntimeException("Cannot connect the database!", e);
			errorMessage = "Cannot connect the database!\n" + e.toString();;
		    return false;
		} 		
		return true;
	}
	
	//disconnect from mySQL
	public boolean disConnect()
	{
    	try 
    	{   		
    	    if (con != null) 
    	    {
    	    	con.close();
    	    	con = null;
    	    }
        	if( state != null)
        	{ 
        		state.close(); 
        		state = null; 
        	}	   
    	} 
    	catch (SQLException e) 
    	{
    		errorMessage = "disConnect fault! \n" + e.toString();; 
    		return false;
    	}
    	return true;
	}
	 
    protected void finalize() 
    { 
    	//disconnect
       disConnect();
    }
    
	//insert table
    public boolean insertData_raw(String stateStr)
    {
    	try
    	{
    		state.executeUpdate(stateStr); 
    		
 		
    	}
    	catch(SQLException e) 
    	{
    		//System.out.println("InsertDB Exception :" + e.toString()); 
    		errorMessage = "InsertDB_raw Exception\n" + e.toString();
    		return false;
    	}   	    		
    	return true;
    }
    
    public boolean insertData(String tableName, Object[] valueList)
    {
    	String stateStr = "insert into "+tableName+" values(";
    	String tempStr = "";
    	for(int i=0;i<valueList.length;i++)
    	{
    		Object object = valueList[i];
    		if( i == (valueList.length-1) )
    		{ 
       			if(object instanceof Integer)
    			{
    				Integer value = (Integer) object;
    				tempStr = tempStr + value + ");";
    			}
    			else if(object instanceof String)
    			{
    				String value = (String) object;
    				tempStr = tempStr + "\"" + value + "\"" + ");";
    			}
    			else
    			{
    				errorMessage = "Insert Date fault!\n Unknown DataType!";
    				return false;
				}    			
    		}
    		else
    		{ 	
    			if(object instanceof Integer)
    			{
    				Integer value = (Integer) object;
    				tempStr = tempStr + value + ",";
    			}
    			else if(object instanceof String)
    			{
    				String value = (String) object;
    				tempStr = tempStr + "\"" + value + "\"" + ",";
    			}
    			else
    			{
    				errorMessage = "Insert Date fault!\n Unknown DataType!";
    				return false;
				}
    		}
    		
    	}
    	stateStr = stateStr + tempStr;
    	try
    	{
    		int number = state.executeUpdate(stateStr); 
    		if( number != 1 )
    		{
    			errorMessage = "doesn't insert values sucess!\n";
    			return false;
    		}	
    	}
    	catch(SQLException e) 
    	{
    		//System.out.println("InsertDB Exception :" + e.toString()); 
    		errorMessage = "InsertDB Exception\n" + e.toString();
    		return false;
    	}   	    		
    	return true;
    }    
    
    //outputData
    public ResultSet outputData_raw(String stateStr)
    {
    	ResultSet result = null;
    	try
    	{
    		result = state.executeQuery(stateStr); 
    	}
    	catch (Exception e) 
    	{
			// TODO: handle exception
    		errorMessage = "OutputData_raw fault! " + e.toString();
    		return null;
		}
    	return result;
    }
    public ResultSet outputData(String tableName, String[] columnList, boolean isOutputAllColumn)
    {
    	ResultSet result = null;
    	try
    	{
    		String stateStr = "";
    		if( isOutputAllColumn == true )
    		{
    			stateStr = "select * from " + tableName + ";";
    		}
    		else
    		{
    			stateStr = "select ";
    			for(int i = 0; i<columnList.length; i++)
    			{
    				if( i != (columnList.length-1 ))
    				{
    					stateStr = stateStr + columnList[i] + ",";
    				}
    				else
    				{
    					stateStr = stateStr + columnList[i] + " from " + tableName + ";";
    				} 				
    			}
    		}
    		result = state.executeQuery(stateStr);  
    	}
    	catch(SQLException e) 
    	{
    		errorMessage = "OutputData fault! " + e.toString();
    		return null;
    	}  	
    	return result;
    }
       
    
    public String toString()
    {
    	String str = "This is a class for connecting to mySQL";
    	return str;
    }

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	} 	
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	//if(object instanceof Employee)
	
}
