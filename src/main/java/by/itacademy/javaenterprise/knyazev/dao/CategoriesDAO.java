package by.itacademy.javaenterprise.knyazev.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.queries.Saver;
import by.itacademy.javaenterprise.knyazev.queries.Selecter;

public class CategoriesDAO {
	
	private final static String TABLE_NAME = "categories";
	private final static String[] COLUMN_NAMES = {"name", "description"};
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Saver saver;
	private Selecter selecter;
	
	
	public CategoriesDAO(Saver saver, Selecter selecter) {
		this.saver = saver;
		this.selecter = selecter;
	}
	
	public void save() {
		
		try {
			saver.save(TABLE_NAME, COLUMN_NAMES, new String[] {"фрукты", "Фрукт — сочный съедобный плод растения. Пример: яблоко, груша и т.д."}, Saver.SAVING_ID);
			saver.save(TABLE_NAME, COLUMN_NAMES, new String[] {"овощи", "Пример: Картофельк морковь, свекла и т.д."}, Saver.SAVING_ID);
			saver.save(TABLE_NAME, COLUMN_NAMES, new String[] {"зелень", "Пример: петрушка, укроп, зеленый лук и т.д."}, Saver.SAVING_ID);
			saver.save(TABLE_NAME, COLUMN_NAMES, new String[] {"ягоды", "Пример: брусника, клубника, смародина и т.д."}, Saver.SAVING_ID);
			saver.closeStatement();
		} catch (SQLException e) {
			logger.error("Error occured when saving in table categories: " + e.getMessage());
		}
	}
	
	public void update(int id) {
		try {
			saver.save(TABLE_NAME, COLUMN_NAMES, new String[] { "Хурма", "Хурма для детей" }, id);
			saver.closeStatement();
		} catch (SQLException e) {
			logger.error("Error occured when updating in table categories by id = " + id + " " + e.getMessage());
		}
	}
	
	public Category select(int id) {
		Category category = new Category();
		
		if (id < 0) return category;
		
		try {
			ResultSet rs = selecter.selectNative("SELECT * FROM " + TABLE_NAME + " WHERE id = " + id);
			rs.next();
			category.setId(rs.getInt("id"));
			category.setName(rs.getString("name"));
			category.setDescription(rs.getString("description"));
			rs.close();
			selecter.closeStatement();
		} catch (SQLException e) {
			logger.error("Error to select category by id = " + id + ": " + e.getMessage());
		}
		
		return category;		
	}
	
	public List<Category> select() {
		List<Category> categories = new ArrayList<>();
		
		try {
			ResultSet rs = selecter.selectNative("SELECT * FROM " + TABLE_NAME);
			while (rs.next()) {
			Category category = new Category();
			category.setId(rs.getInt("id"));
			category.setName(rs.getString("name"));
			category.setDescription(rs.getString("description"));	
			categories.add(category);
			}
			rs.close();
			selecter.closeStatement();
		} catch (SQLException e) {
			logger.error("Error to select all categories: " + e.getMessage());
		}
		
		return categories;
	}

}
