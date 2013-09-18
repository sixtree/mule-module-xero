Welcome
=======
This is a Mule Connector for the Xero online accounting system - www.xero.com

### Changelog

###### 1.1.0 - 2013-09-18

- Significant changes to pom.xml to leverage http://github.com/sixtree/mule-module-sixtree-parent
- Added Sixtree repositories
- Reset git repo as old one was very large (>100mb) due to binaries
- Added Xero XSDs and converted to JAXB classes for simpler usage in Mule
- Removed superfluous test case output, and only activate a single test by default
- Added large sample project

###### 1.0.0

- Initial release on old git repo (no longer available)

About this Cloud Connector
==========================
The Mule cloud connector for Xero supports the following operations of the Xero API:

![Xero API Support](/doc/APISupportTable.jpg)

For full details of the Xero API, refer to the Xero API reference - http://developer.xero.com/api/

There are four different types of operations supported by this cloud connector:

1. Retrieve a single Xero resource - Pass a unique resource identifier (such as an InvoiceNumber) as the input payload to any 'Get' operation supported by the Xero cloud connector (such as GetInvoice) to retrieve and XML-formatted string object representation of that resource.

2. Retrieve a list of Xero resources - Pass a blank payload request to any 'Get List' operation supported by the Xero cloud connector (such as GetInvoicesList) to retrieve a list of Xero resources. Filter parameters can also be applied to requests to refine the returned list - refer to the Xero API reference for further details of supported filter parameters.

3. Create Or Update Xero resource(s) - When passed an XML-formatted string object(s), this operation will either 

  - Create a new Xero resource if a resource matching the input doesn't exist, or 
  - Update a Xero resource if a resource matching the input already exists
    
  The operation will reply back with an XML-formatted string response message.

4. Create Xero resource(s) - When passed an XML-formatted string object(s), this operation will Create a new Xero resource (if a resource matching the input doesn't already exist).

All resources supported by this cloud connector conform to the Xero API XML Schema Definitions available here: https://github.com/XeroAPI/XeroAPI-Schemas

For an example of how to use this connector, refer to the sample project available under the 'sample' folder of this repository.

Limitations & Issues
====================

This connector currently has a number of limitations, which will be addressed in future iterations. These have been raised as issues in this project.

- Only XML strings are returned (no objects or JSON). However, the following transformer can be added after any call to get a Java object representation of the result:

```xml
<auto-transformer returnClass="au.com.sixtree.mule.modules.xero.xsd.ResponseType"></auto-transformer>
```

- Exceptions returned by Xero as ApiExceptions (but HTTP successes) do not throw an error. Therefore, this element must be explicitly checked for to ensure correct operation:

```xml
<message-filter doc:name="Drop Exceptions" onUnaccepted="droppedMessageFlow">
    <expression-filter expression="#[xpath('/ApiException') == null]" />
</message-filter>        
<auto-transformer returnClass="au.com.sixtree.mule.modules.xero.xsd.ResponseType"></auto-transformer>
```

- ApiException is defined by Xero as a type, not an element, and therefore conversion using JAXB is not possible

Importing the Cloud Connector into Mule Studio
==============================================
1. Refer to documentation on [installing cloud connectors](http://www.mulesoft.org/documentation/display/current/Additional+Cloud+Connectors)

2. Add the Mule Xero Connector update site - MuleStudio > Help > Install New Software... 
  
  - Click "Add" and enter Location=http://dist.sixtree.com.au/mule/modules/mule-module-xero/update-site/

3. Install the Xero Connector - Select Connectors/Xero Mule Studio Extension then Click 'Next' - Review and Accept license details, then Click 'Finish'

4. Restart MuleStudio when prompted and your new Xero Cloud Connector should now be available under the 'Cloud Connectors' palette

Usage
=====
Set up a Xero developer account and register your application as a 'Private' application as per [the steps outlined here](http://developer.xero.com/api-overview/private-applications/).

We **strongly** recommend using the Xero 'Demo Company' account to avoid corrupting your organisation's data while you are testing your integration.

See [xero-sample.xml](sample/xero-sample.xml) for a sample project that exposes the Xero API using Mule as JSON/HTTP endpoints, perfect for consumption by a web application.

Manually Building the Connector
===============================
If you'd like to make your own modifications and/or you'd just like to rebuild this cloud connector, use the following steps.

* Retrieve the source

```
git clone https://github.com/sixtree/mule-module-xero
```

* Create a file called 'xero.properties' under src/test/resources folder and include the following four properties:

```
xero.consumerKey=[Your Xero Applications OAuth Consumer Key]
xero.consumerSecret=[Your Xero Applications OAuth Consumer Secret]
xero.privateKeyFile=[The path to your Xero Applications private key (.pem) file]
xero.xeroApiUrl=https://api.xero.com/api.xro/2.0/
```

These properties come from the private application set up under [Usage](#usage).

* Build, test and install the application in the local Maven repo:

```
cd mule-module-xero
mvn install
```

Testing
=======
This project contains test classes with test methods for each of the operations supported by the connector. Only the 'create and get invoice' test is activated by default at the moment.
