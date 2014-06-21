package com.ccvb.utils.javaxmlquery;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.ParseException;
import org.xml.sax.SAXException;

public class XML_Parser
{
	private XML_Element data;
	
	/**
	 * Let's Parse some XML data
	 * @param what
	 */
	public XML_Parser(InputStream input) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser parser = saxParserFactory.newSAXParser();
		
		XML_ParserHandler handler = new XML_ParserHandler();
		
		parser.parse(input, handler);
		
		this.data = handler.datas;
	}
	
	/**
	 * @return parsed data
	 */
	public XML_Element getData()
	{
		return this.data;
	}
}