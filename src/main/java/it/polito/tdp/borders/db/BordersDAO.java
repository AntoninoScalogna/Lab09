package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public Map<Integer,Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		Map<Integer,Country> result = new HashMap<Integer,Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.put(rs.getInt("ccode"),new Country( rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme")));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	Map<Integer,Country> paesi=loadAllCountries();
	

	public List<Border> getCountryPairs(int anno) {
		String sql="SELECT state1no, state2no,year "
				+ "FROM contiguity "
				+ "WHERE year=? AND conttype=1";
		
		try {
			LinkedList<Border> result=new LinkedList<Border>();
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			
			ResultSet rs = st.executeQuery();
			
			
			while(rs.next()) {
				if(rs.getInt("year")==anno) {
				Country c1=paesi.get(rs.getInt("state1no"));
				Country c2=paesi.get(rs.getInt("state2no"));
				result.add(new Border(c1,c2,rs.getInt("year")));
				}
			}
			conn.close();
			rs.close();
			st.close();
			return result;
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
			
		}
	}
	
	
}
