package sp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sp.data.dao.hibernate.SpDaoHibernateImpl;
import sp.data.dao.interfaces.OrderDao;
import sp.data.dao.interfaces.ProductDao;
import sp.data.dao.interfaces.OrderDao;
import sp.data.dao.interfaces.RefererDao;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Order;
import sp.data.entities.Product;
import sp.data.entities.Order;
import sp.data.entities.Referer;
import sp.data.services.interfaces.ProductService;


public class Application {

	public static void main(String[] args) throws ParseException {
		
		
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date convertedCurrentDate = sdf.parse("2013-09-18");
	    System.out.println(convertedCurrentDate);
		System.out.println("Hello world!");
		
		/*
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		SpDao service = (SpDao) ac.getBean("SpDaoHibernateImpl");
		
		System.out.println(service.getLastId());
		*/
	}

}
