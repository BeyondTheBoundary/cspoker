/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.request.abstracts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.client.commands.CommandExecutor;
import org.cspoker.client.exceptions.ExceptionParser;
import org.cspoker.client.exceptions.FailedAuthenticationException;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
public abstract class HttpRequest implements CommandExecutor{

    private URL url;

    public HttpRequest(String address) throws MalformedURLException {
	this.url=new URL(address+getPath());
    }
    
    protected URL getURL(){
	return url;
    }
    
    public String execute(String... args) throws Exception{
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setConnectTimeout(20000);
	connection.setAllowUserInteraction(true);
	connection.setInstanceFollowRedirects(false);
	connection.setDoOutput(isDoOutput());
	connection.setRequestMethod(getRequestMethod());
	
	if (isDoOutput()) {
	    StreamResult requestResult = new StreamResult(connection
		    .getOutputStream());
	    SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
		    .newInstance();
	    TransformerHandler request = tf.newTransformerHandler();
	    request.setResult(requestResult);
	    request.startDocument();
	    doOutput(request, args);
	    request.endDocument();
	    connection.getOutputStream().flush();
	    connection.getOutputStream().close();
	}
	if(connection.getResponseCode()==401){
	    throw new FailedAuthenticationException("Authentication failed.");
	}else if(connection.getResponseCode()/100==4||connection.getResponseCode()/100==5){
	    throw getException(connection);
	}
	XMLReader xr = XMLReaderFactory.createXMLReader();
	xr.setContentHandler(getContentHandler());
	xr.parse(new InputSource(connection.getInputStream()));

	return getResult();
    }

    protected abstract ContentHandler getContentHandler();

    private Exception getException(HttpURLConnection connection) throws SAXException, IOException {
	XMLReader xr = XMLReaderFactory.createXMLReader();
	ExceptionParser parser=new ExceptionParser();
	xr.setContentHandler(parser);
	xr.setErrorHandler(parser);
	xr.parse(new InputSource(connection.getErrorStream()));
	return parser.getException();
    }

    protected abstract String getResult();
    
    protected abstract String getPath();
    
    protected abstract boolean isDoOutput();
    
    protected abstract void doOutput(TransformerHandler request, String... args) throws SAXException;
    
    protected abstract String getRequestMethod();

    public boolean requiresEventUpdate(){
	return true;
    }
    
}
