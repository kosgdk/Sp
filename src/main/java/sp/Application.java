package sp;

import java.text.ParseException;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sp.data.entities.*;
import sp.data.services.ClientServiceImpl;
import sp.data.services.RefererServiceImpl;


public class Application {

	public static void main(String[] args) throws ParseException {
		
		


		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		RefererServiceImpl refService = (RefererServiceImpl) ac.getBean("RefererService");
		ClientServiceImpl clientService = (ClientServiceImpl) ac.getBean("ClientService");
		Referer referer = refService.getById(1);
		Client client = new Client();
		client.setName("КОсяк");
		client.setReferer(referer);
		clientService.save(client);


	}

}
