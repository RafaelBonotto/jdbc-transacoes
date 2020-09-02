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
			
			// Transações
			
			conn.setAutoCommit(false); // setAutoCommit para deixar pendente a confirmação das alterações
			
			st = conn.createStatement();
			
			int linhas1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			
			//Erro no meio da transação e o segundo comando não seria executado
			int x = 1;
			if(x < 2) {
				throw new SQLException("Fake Error");
			}
		
			int linhas2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");
			
			conn.commit();  // Confirmação das duas alterações
			
			System.out.println("linhas1: " + linhas1);
			System.out.println("linhas2: " + linhas2);	
									
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Transação Cancelada! ERRO: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Não foi possivel cancelar a transação! ERRO: " + e1.getMessage());
			}		
		}
		finally {
			DB.closeStatement(st);
			DB.CloseConnection();
		}
			
	}

}
