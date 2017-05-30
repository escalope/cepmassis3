package cep.grasia.ucm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.fasterxml.jackson.databind.ObjectMapper;

class MyListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		EventBean event = newEvents[0];
        System.out.println("Number Object=" + newEvents.length + " - entity=" + event.get("entityId") + " - entityType=" + event.get("changeType"));
    }
}
public class App 
{
    public static void main( String[] args )
    {
    	final EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
    	// the person is stuck if during 20 seconds, the person has not moved from its location
    	String peoplestuck = " select * from cep.grasia.ucm.Person.win:time(20 sec) as person1  where "
    			+ " 0.5 > all "
    			+ " (select avedev(components[0].data.x) from cep.grasia.ucm.Person.win:time(20 sec) as person2 where person1.entityId=person2.entityId) "
    			+ " and 0.5 > all "
    			+ " (select avedev(components[0].data.y) from cep.grasia.ucm.Person.win:time(20 sec) as person2 where person1.entityId=person2.entityId) "
    			+ " and 0.5 > all "
    			+ " (select avedev(components[0].data.z) from cep.grasia.ucm.Person.win:time(20 sec) as person2 where person1.entityId=person2.entityId) ";
    	    	    
    	EPStatement statement = epService.getEPAdministrator().createEPL(peoplestuck);
    	MyListener listener = new MyListener();
    	statement.addListener(listener);
    	
    	new Thread(){
    		public void run(){    			
    			ObjectMapper mapper = new ObjectMapper();    			
    			BufferedReader streamReader;
				try {
					streamReader = new BufferedReader(
							new InputStreamReader(
									new URL("http://147.96.80.41:8080/live/1/changes?components=position&type=Human").openConnection().getInputStream(),
									"UTF-8"));
					StringBuilder responseStrBuilder = new StringBuilder();    			
	    			String inputStr;
	    			while ((inputStr = streamReader.readLine()) != null){
	    				if (inputStr.startsWith("data:")) {    			    				
	    			     Person[] staff = mapper.readValue(inputStr.substring("data:".length()).getBytes(), 
	    			    		 Person[].class);	    
	    			     for (Person person : staff)
	    			    	 epService.getEPRuntime().sendEvent(person);
	    				}
	    			}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
    			
    			
    			
    		}
    	
    }.start();
    }
}
