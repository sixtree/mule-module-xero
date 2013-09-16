package au.com.sixtree.mule.modules.xero;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import net.oauth.OAuth;
import net.oauth.OAuth.Parameter;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.ParameterStyle;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient3.HttpClient3;
import net.oauth.http.HttpMessage;
import net.oauth.http.HttpMessageDecoder;
import net.oauth.http.HttpResponseMessage;
import net.oauth.signature.RSA_SHA1;

/**
 * A helper class which encapsulates client logic (connection & 
 * authentication) for comms to Xero.  
 * 
 * @author Sixtree 
 */
public class XeroConnectorClient {
	
    private String xeroApiUrl;
    private String consumerKey;
    private String consumerSecret;
    private String privateKeyFile;

    public XeroConnectorClient(String xeroApiUrl, String consumerKey, String consumerSecret, String privateKeyFile) {
        this.xeroApiUrl = xeroApiUrl;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.privateKeyFile = privateKeyFile;
    }
		
	
    /**
     * createOAuthAccessor sets up the OAuth accessor credentials to be passed in all requests to the Xero API
     * 
     * @return an OAuth accessor with required Xero access credentials set
     * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
     */
    public OAuthAccessor createOAuthAccessor() throws XeroConnectorClientUnexpectedException {

        OAuthConsumer consumer = new OAuthConsumer(null, consumerKey, null, null);
        
        //Load private key from File
        String privateKey = loadOAuthPrivateKey();
        
        consumer.setProperty(RSA_SHA1.PRIVATE_KEY, privateKey);
        consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.RSA_SHA1);
        consumer.setProperty(OAuth.OAUTH_NONCE, UUID.randomUUID());

        OAuthAccessor accessor = new OAuthAccessor(consumer);
        accessor.accessToken = consumerKey;
        accessor.tokenSecret = consumerSecret;

        return accessor;
    }
    
    private String loadOAuthPrivateKey() throws XeroConnectorClientUnexpectedException{
    	
    	String privateKey = null;

        try{
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream keyFile = cl.getResourceAsStream(privateKeyFile);
            
            //If the key file is not on the classpath, look for it by absolute path
            if (keyFile == null){
            	keyFile = new FileInputStream(privateKeyFile);
            }
	        
        	BufferedReader reader = new BufferedReader(new InputStreamReader(keyFile));
	        StringBuilder stringBuilder = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            stringBuilder.append(line).append("\n");
	        }
	        privateKey = stringBuilder.toString();
        }
	    catch (IOException e)
	    {
	    	throw new XeroConnectorClientUnexpectedException(e);
	    }
    	
		return privateKey;
    }
    
	/**
	 * getXeroObjectList retrieves a list of xero objects based on the 'objectType' parameter
	 * 
	 * @param objectType the Xero object type
	 * @return a Xero xml response string
	 * @throws XeroConnectorClientException if an error is returned from the Xero API
	 * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
	 */
	public String getXeroObjectList(String objectType) throws XeroConnectorClientException, XeroConnectorClientUnexpectedException
	{
		String responseString = null;
		
		try {
			OAuthClient client = new OAuthClient(new HttpClient3());
			OAuthAccessor accessor = createOAuthAccessor();
			
			//Invoke Xero
			OAuthMessage response = client.invoke(accessor, OAuthMessage.GET, buildRequestUri(objectType), null);
			
			responseString = response.readBodyAsString();
			
		} catch (OAuthException e) {
			throw new XeroConnectorClientException((OAuthProblemException) e);
		} catch (Exception e) {
			throw new XeroConnectorClientUnexpectedException(e);
		}
		
		return responseString;
	}
	
	/**
	 * getXeroObjectList retrieves a list of xero objects based on the 'objectType' parameter. Method overloaded to allow: 
	 * 	- an optional 'where' clause to be passed for filtering of the response list
	 * 
	 * @param objectType the Xero object type
	 * @param whereClause a 'where' clause passed for filtering of the response list 
	 * @return a Xero xml response string
	 * @throws XeroConnectorClientException if an error is returned from the Xero API
	 * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
	 */
	public String getXeroObjectList(String objectType, String whereClause) throws XeroConnectorClientException, XeroConnectorClientUnexpectedException
	{
		String responseString = null;
		
		try {
			OAuthClient client = new OAuthClient(new HttpClient3());
			OAuthAccessor accessor = createOAuthAccessor();
			
			//Invoke Xero
			OAuthMessage response = client.invoke(accessor, OAuthMessage.GET, buildRequestUri(objectType), addRequestParams(whereClause, null));
			
			responseString = response.readBodyAsString();
			
		} catch (OAuthException e) {
			throw new XeroConnectorClientException((OAuthProblemException) e);
		} catch (Exception e) {
			throw new XeroConnectorClientUnexpectedException(e);
		}
		
		return responseString;
	}
	
	/**
	 * getXeroObjectList retrieves a list of xero objects based on the 'objectType' parameter. Method overloaded to allow: 
	 * 	- an optional 'where' clause to be passed for filtering of the response list
	 * 	- an optional 'order' clause for ordering the response list
	 * 
	 * @param objectType the Xero object type
	 * @param whereClause a 'where' clause passed for filtering of the response list 
	 * @param orderClause an 'order' clause for ordering the response list
	 * @return a Xero xml response string
	 * @throws XeroConnectorClientException if an error is returned from the Xero API
	 * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
	 */
	public String getXeroObjectList(String objectType, String whereClause, String orderClause) throws XeroConnectorClientException, XeroConnectorClientUnexpectedException
	{
		String responseString = null;
		
		try {
			OAuthClient client = new OAuthClient(new HttpClient3());
			OAuthAccessor accessor = createOAuthAccessor();
			
			//Invoke Xero
			OAuthMessage response = client.invoke(accessor, OAuthMessage.GET, buildRequestUri(objectType), addRequestParams(whereClause, orderClause));
			
			responseString = response.readBodyAsString();
			
		} catch (OAuthException e) {
			throw new XeroConnectorClientException((OAuthProblemException) e);
		} catch (Exception e) {
			throw new XeroConnectorClientUnexpectedException(e);
		}
		
		return responseString;
	}
	
	/**
	 * getXeroObject method retrieves an individual xero object based on the given 'objectType' parameter
	 * 
	 * @param objectType the Xero object type
	 * @return a Xero xml response string
	 * @throws XeroConnectorClientException if an error is returned from the Xero API
	 * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
	 */
	public String getXeroObject(String objectType) throws XeroConnectorClientException, XeroConnectorClientUnexpectedException
	{
		String responseString = null;
		
		try {
			OAuthClient client = new OAuthClient(new HttpClient3());
			OAuthAccessor accessor = createOAuthAccessor();
			OAuthMessage response = client.invoke(accessor, OAuthMessage.GET, buildRequestUri(objectType), null);
			
			responseString = response.readBodyAsString();
			
		} catch (OAuthException e) {
			throw new XeroConnectorClientException((OAuthProblemException) e);
		} catch (Exception e) {
			throw new XeroConnectorClientUnexpectedException(e);
		}
		
		return responseString;
	}

	/**
	 * getXeroObject method retrieves an individual xero object based on the given 'objectType' parameter. Method overloaded to allow:
	 * 	- an optional 'objectId' field to identify a unique Xero object
	 * 
	 * @param objectType the Xero object type
	 * @param objectId unique Xero object identifier
	 * @return a Xero xml response string
	 * @throws XeroConnectorClientException if an error is returned from the Xero API
	 * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
	 */
	public String getXeroObject(String objectType, String objectId) throws XeroConnectorClientException, XeroConnectorClientUnexpectedException
	{
		String responseString = null;
		
		try {
			OAuthClient client = new OAuthClient(new HttpClient3());
			OAuthAccessor accessor = createOAuthAccessor();
			OAuthMessage response = client.invoke(accessor, OAuthMessage.GET, buildRequestUri(objectType) + "/" + objectId, null);
			
			responseString = response.readBodyAsString();
			
		} catch (OAuthException e) {
			throw new XeroConnectorClientException((OAuthProblemException) e);
		} catch (Exception e) {
			throw new XeroConnectorClientUnexpectedException(e);
		}
		
		return responseString;
	}
	
	/**
	 * updateXeroObject method updates a xero object based on the given 'objectType' and 'payload' parameters. 
	 * 
	 * @param objectType the Xero object type
	 * @param payload xml string request payload
	 * @return a Xero xml response string
	 * @throws XeroConnectorClientException if an error is returned from the Xero API
	 * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
	 */
	public String updateXeroObject(XeroObjectTypes.XeroPostTypes objectType,
						 String payload) throws XeroConnectorClientException, XeroConnectorClientUnexpectedException
	{
		String responseString = null;
		String stringObjectType = objectType.toString();
				
		try {
			OAuthClient client = new OAuthClient(new HttpClient3());
			
			OAuthAccessor accessor = createOAuthAccessor();			
			OAuthMessage response = client.invoke(accessor, OAuthMessage.POST, buildRequestUri(stringObjectType), OAuth.newList("xml", payload));
			responseString = response.readBodyAsString();
			
		} catch (OAuthException e) {
			throw new XeroConnectorClientException((OAuthProblemException) e);
		} catch (Exception e) {
			throw new XeroConnectorClientUnexpectedException(e);
		}
				
		return responseString;
	}
	
	
	/**
	 * createXeroObject method creates a xero object based on the given 'objectType' and 'payload' parameters. 
	 * 
	 * @param objectType the Xero object type
	 * @param payload xml string request payload
	 * @return a Xero xml response string
	 * @throws XeroConnectorClientException if an error is returned from the Xero API
	 * @throws XeroConnectorClientUnexpectedException if an unexpected exception occurs
	 */
	public String createXeroObject(XeroObjectTypes.XeroPutTypes objectType,
			 String payload) throws XeroConnectorClientException, XeroConnectorClientUnexpectedException
	{
		String responseString = null;
		String stringObjectType = objectType.toString();
			
		try {
			//OAuthClient client = new OAuthClient(new HttpClient3());
			
			OAuthAccessor accessor = createOAuthAccessor();			
			OAuthMessage request = accessor.newRequestMessage(
					OAuthMessage.PUT,
					buildRequestUri(stringObjectType),
					null,
					new ByteArrayInputStream(payload.getBytes("UTF-8")));
			
			HttpMessage httpRequest = HttpMessage.newRequest(request, ParameterStyle.AUTHORIZATION_HEADER);

			HttpResponseMessage httpResponse = new HttpClient3().execute(httpRequest, new HashMap<String, Object>());
			//int statusCode = httpResponse.getStatusCode();
			httpResponse = HttpMessageDecoder.decode(httpResponse);
			InputStream responseStream = httpResponse.getBody();
			BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
			StringBuilder sb = new StringBuilder();
			String line = null;
		    
			while ((line = reader.readLine()) != null) {
		      sb.append(line + "\n");
		    }
		    
			responseStream.close();
			responseString = sb.toString();
		
		} catch (OAuthException e) {
			throw new XeroConnectorClientException((OAuthProblemException) e);
		} catch (Exception e) {
			throw new XeroConnectorClientUnexpectedException(e);
		}
		
	return responseString;
	}
	
	/**
	 * buildRequestUri builds the full target URI for the Xero request to be directed to
	 * 
	 * @param objectType to be appended to the base Xero API URI
	 * @return full Xero API URI
	 */
	public String buildRequestUri(String objectType)
	{
		String fullApiUri = xeroApiUrl + objectType;	
		
		return fullApiUri;
	}
	
	/**
	 * addRequestParams adds additional parmeters to the API request
	 * 
	 * @param whereClause value of the 'where' parameter to be added to the request URI
	 * @param orderClause value of the 'order' parameter to be added to the request URI
	 * @return parameters list
	 */
	public Collection<OAuth.Parameter> addRequestParams(String whereClause, String orderClause)
	{
		Collection<OAuth.Parameter> parameters = new ArrayList<OAuth.Parameter>();
		
		if (whereClause!=null){
			Parameter whereParam = new Parameter("where", whereClause);
			parameters.add(whereParam);	
		}
		
		if (orderClause!=null){
			Parameter orderParam = new Parameter("order", orderClause);
			parameters.add(orderParam);	
		}
		
		return parameters;
	}
}
