package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {
				
		Connection conn = null;
		Statement st = null;
		try {
			conn = DB.getConnection(); 
			
			// Transa��es
			
			conn.setAutoCommit(false); // setAutoCommit para deixar pendente a confirma��o das altera��es
			
			st = conn.createStatement();
			
			int linhas1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			
			//Erro no meio da transa��o e o segundo comando n�o seria executado
			int x = 1;
			if(x < 2) {
				throw new SQLException("Fake Error");
			}
		
			int linhas2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");
			
			conn.commit();  // Confirma��o das duas altera��es
			
			System.out.println("linhas1: " + linhas1);
			System.out.println("linhas2: " + linhas2);	
									
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Transa��o Cancelada! ERRO: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("N�o foi possivel cancelar a transa��o! ERRO: " + e1.getMessage());
			}		
		}
		finally {
			DB.closeStatement(st);
			DB.CloseConnection();
		}
			
	}

}
