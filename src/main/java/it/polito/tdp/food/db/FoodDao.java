package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> getVertici(Integer n){
		
		String sql = "SELECT DISTINCT f.food_code, f.display_name, COUNT(DISTINCT p.portion_id) as count "
				+ "FROM portion p, food f "
				+ "WHERE p.food_code = f.food_code "
				+ "GROUP BY f.food_code, f.display_name "
				+ "HAVING count > ? ";
		
		List<Food> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, n);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				try {
					list.add(new Food(res.getInt("f.food_code"),
							res.getString("f.display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
		
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Food> idMap){
		
		String sql = "SELECT DISTINCT f1.food_code as c1, f2.food_code as c2, (AVG(p1.saturated_fats) - AVG(p2.saturated_fats)) as peso "
				+ "FROM portion p1, portion p2, food f1, food f2 "
				+ "WHERE p1.food_code = f1.food_code AND "
				+ "p2.food_code = f2.food_code AND "
				+ "f1.food_code > f2.food_code "
				+ "GROUP BY f1.food_code, f2.food_code";
		
		List<Adiacenza> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				if(idMap.containsKey(res.getInt("c1")) && idMap.containsKey(res.getInt("c2"))) {
					
					result.add(new Adiacenza(idMap.get(res.getInt("c1")), idMap.get(res.getInt("c2")), res.getDouble("peso")));
					
				}
				
			}	
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
}
