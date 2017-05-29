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
        System.err.println("ya");
        System.out.println("avg=" + event.get("avg(timestamp)"));
    }
}
public class App 
{
    public static void main( String[] args )
    {
    	final EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
    	String expression = "select avg(timestamp) from cep.grasia.ucm.Person.win:time(2 sec)";
    	EPStatement statement = epService.getEPAdministrator().createEPL(expression);
    	MyListener listener = new MyListener();
    	statement.addListener(listener);
    	new Thread(){
    		public void run(){    			
    			ObjectMapper mapper = new ObjectMapper();    			
    			BufferedReader streamReader;
				try {
					streamReader = new BufferedReader(
							new InputStreamReader(
									new URL("http://147.96.80.41:8080/live/1/changes?components=position&type=human").openConnection().getInputStream(), 
									"UTF-8"));
					StringBuilder responseStrBuilder = new StringBuilder();    			
	    			String inputStr;
	    			while ((inputStr = streamReader.readLine()) != null){
	    				if (inputStr.startsWith("data:")) {    			    				
	    			     Person[] staff = mapper.readValue(inputStr.substring("data:".length()).getBytes(), 
	    			    		 Person[].class);
	    			     epService.getEPRuntime().sendEvent(staff[0]);
	    				}
	    			}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
    			
    			
    			
    		}
    	
    }.start();
    }
}
