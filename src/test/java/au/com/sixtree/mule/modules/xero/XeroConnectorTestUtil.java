package au.com.sixtree.mule.modules.xero;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Random;

import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.tck.junit4.AbstractMuleContextTestCase;
import org.mule.tck.junit4.FunctionalTestCase;

public class XeroConnectorTestUtil extends FunctionalTestCase{
	
	private static String configResourceFile = "xero-connector-config.xml"; 
	
    public static String getConfigResourceFile() {
		return configResourceFile;
	}

	protected Flow lookupFlowConstruct(String name)
    {
		
        Flow result = (Flow)AbstractMuleContextTestCase.muleContext.getRegistry().lookupFlowConstruct(name);
        return result;
    }
    
    public MuleEvent setupGenericGetObjectTest(String testConstructName) throws Exception
    {
    	Flow flow = lookupFlowConstruct(testConstructName);
    	MuleEvent event = getTestEvent(null);
        MuleEvent responseEvent = flow.process(event);
                
        return responseEvent;
    }
    
    public MuleEvent setupGenericGetObjectByIdTest(String testConstructName, String id) throws Exception
    {
    	Flow flow = lookupFlowConstruct(testConstructName);
    	MuleEvent event = getTestEvent(id);
        MuleEvent responseEvent = flow.process(event);
                
        return responseEvent;
    }
    
    public MuleEvent setupGenericCreateObjectTest(String testConstructName, String payload) throws Exception
    {
    	Flow flow = lookupFlowConstruct(testConstructName);
    	MuleEvent event = getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);
                
        return responseEvent;
    }
    
    public MuleEvent setupGenericUpdateObjectTest(String testConstructName, String payload) throws Exception
    {
    	Flow flow = lookupFlowConstruct(testConstructName);
    	MuleEvent event = getTestEvent(payload);
        MuleEvent responseEvent = flow.process(event);
                
        return responseEvent;
    }
    
    public String readFile(String path) throws IOException {
    	FileInputStream stream = new FileInputStream(new File(path));
    	try {
    		FileChannel fc = stream.getChannel();
    		MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
    		/* Instead of using default, pass in a decoder. */
    		return Charset.defaultCharset().decode(bb).toString();
    	}
    	finally {
    		stream.close();
    	}
    }
    
    public String generateRandomString(){
    	long currentTime = System.currentTimeMillis();
    	Random randGenerator = new Random(currentTime);
    	int randomNumberInt = randGenerator.nextInt(99999999) + 10000000;
    	String randomNumberString = Integer.toString(randomNumberInt);
    	
    	return randomNumberString;
    }

	@Override
	protected String getConfigResources() {
		return configResourceFile;
	}

}
