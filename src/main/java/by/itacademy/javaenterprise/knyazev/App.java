package by.itacademy.javaenterprise.knyazev;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.itacademy.javaenterprise.knyazev.dao.CategoriesDAO;
import by.itacademy.javaenterprise.knyazev.db.DbConnection;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Product;
import by.itacademy.javaenterprise.knyazev.queries.Saver;
import by.itacademy.javaenterprise.knyazev.queries.Selecter;

public class App {

	final static Logger logger = LoggerFactory.getLogger(App.class.getName());

	public static void main(String[] args) {		
		categoriesService();	
		nativeSaveQueries();
		nativeSelectQueries();
	}
	
	private static void nativeSaveQueries() {
		try (Connection connection = DbConnection.getDBO().getConnection()) {
			Saver saver = new Saver(connection);
			saver.saveNative("Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО \"Ягода-малина\"', '212030', 'Республика Беларусь', 'Минская обл.', 'деревня Гудки', 'ул. Красная', '6')");
			saver.saveNative("Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО \"Плодоовощное\"', '220265', 'Республика Беларусь', 'Минская обл.', 'д. Верба', 'ул. Синяя', '18')");
			saver.saveNative("Insert Into producers(name, postal_code, country, region, locality) values('ООО \"Урожай\"', '245879', 'Республика Беларусь', 'Минская обл.', 'д. Весна')");
			
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Яблоко', 'Черный принц', 'Яблоко для производства сока', 1, 3)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Груша', 'Дюймовочка', 'Груша для производства сока', 1, 2)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Банан', 'Медовый', 'Банан мелкий сладкий и сочный', 1, 1)");
			
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Картофель', 'Адретта', 'Картофель для жарки', 2, 2)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Морковь', 'Московская зимняя А 515', 'Морковь для хранения на зиму', 2, 3)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Лук репчатый', 'Сноуболл', 'Лук для салата', 2, 1)");
			
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Укроп', 'Зонтик', 'Укроп для салата', 3, 1)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Петрушка', 'Сахарная', 'Петрушка для сушки и замораживания', 3, 2)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Лук зеленый', 'Порей', 'Лук для супа', 3, 3)");
			
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Малина', 'Глория', 'Малина ароматная с кислинкой', 4, 1)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Малина', 'Золотой гигант', 'Малина ароматная сладкая', 4, 2)");
			saver.saveNative("Insert Into goods(name, sort, description, category_id, producer_id) values('Клубника', 'Садовая', 'Клубника сладкая средняя', 4, 3)");
			
			saver.closeStatement();	
			
		} catch (SQLException | ReflectiveOperationException e) {
			logger.error(e.getMessage());
		}		
	}
	
	private static void nativeSelectQueries() {
		try (Connection connection = DbConnection.getDBO().getConnection()) {
			Selecter selecter = new Selecter(connection);
			
			ResultSet rs = selecter.selectNative("SELECT goods.name, goods.sort, categories.name as category, producers.name as producer FROM goods, categories, producers WHERE goods.producer_id BETWEEN 1 AND 2 AND categories.name = 'фрукты' ORDER BY categories.name ASC LIMIT 1 OFFSET 1");
			
			List<Product> products = new ArrayList<>();
			
			while(rs.next()) {
				Product product = new Product();
				
				product.setName(rs.getString("name"));
				product.setSort(rs.getString("sort"));
				product.setCategory(rs.getString("category"));
				product.setProducer(rs.getString("producer"));
				
				products.add(product);
			}
			rs.close();
			selecter.closeStatement();
			
			System.out.println("Selected product from query: ");
			products.forEach(System.out::println);
			
			//2
			Selecter selecter2 = new Selecter(connection);
			
			ResultSet resultSet = selecter2.selectNative("SELECT name as product, sort FROM goods WHERE name like '%ый' OR name like '%а' order by name asc LIMIT 3 OFFSET 2");
			
			Map<String, String> productSort = new LinkedHashMap<>();
			
			while (resultSet.next()) {
				productSort.put(resultSet.getString("product"), resultSet.getString("sort"));
			}
			
			resultSet.close();
			selecter2.closeStatement();
			
			System.out.println("Selected product and sort from query: ");
			productSort.forEach((k, v) -> System.out.println("product = " + k + ", sort = " + v));
			
		} catch (SQLException | ReflectiveOperationException e) {
			logger.error(e.getMessage());
		}
	}

	private static void categoriesService() {
		try (Connection connection = DbConnection.getDBO().getConnection()) {
			Saver saver = new Saver(connection);
			Selecter selecter = new Selecter(connection);
			
			CategoriesDAO categoriesDao = new CategoriesDAO(saver, selecter);
			
			categoriesDao.save();
			
			categoriesDao.update(5);
			
			Category category = categoriesDao.select(2);		
			logger.info("Object " + category.toString() + " have been selected");
			
			List<Category> categories = categoriesDao.select();
			System.out.println("Object selected from categories: ");
			categories.forEach(System.out::println);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
