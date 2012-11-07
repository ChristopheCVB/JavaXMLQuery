package com.example.android_javaxmlquery_demo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import android.app.Activity;
import android.os.Bundle;

import com.utils.android.javaxmlquery.XML_Element;
import com.utils.android.javaxmlquery.XML_Elements;
import com.utils.android.javaxmlquery.XML_Parser;

public class MainActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Example 01
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					URL url = new URL("http://google.com");
					URLConnection connection = url.openConnection();
					connection.setConnectTimeout(3500);
					connection.addRequestProperty("Accept-Encoding", "gzip");
					connection.connect();
					InputStream input = connection.getInputStream();
					if ("gzip".equals(connection.getContentEncoding()))
					{
						input = new GZIPInputStream(input);
					}
					
					XML_Element xmlRoot = new XML_Parser(input).getData();
					
					XML_Elements divs = xmlRoot.find("div");
					for (XML_Element div : divs)
					{
						String id = div.attr("id");
						// id cannot be null, Empty String ("") is return instead
						if (id.equals("search"))
						{
							// Do what you want...
						}
					}
					
					XML_Element specificElement = xmlRoot.find("#searchform>input[type=submit]").get(0);
					String readableXMLElement = specificElement.toString();
					// So you can manipulate Object and generate new state like
					specificElement.attributes.put("key", "value");
					String readableXMLElementWithNewAttribute = specificElement.toString();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
		
		
		// Example 01
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					String myXMLDocument = "<foo type='all'><bar id='itsme' /><bar><fooz name='imtheone' /></bar></foo>";
					
					XML_Element xmlRoot = new XML_Parser(new ByteArrayInputStream(myXMLDocument.getBytes())).getData();
					
					XML_Elements barsChildrenOfFoo = xmlRoot.find("foo>bar");
					// ...
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
}
