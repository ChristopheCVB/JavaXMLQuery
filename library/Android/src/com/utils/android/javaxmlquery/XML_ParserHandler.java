package com.utils.android.javaxmlquery;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XML_ParserHandler extends DefaultHandler
{
	private StringBuffer buffer;
	private XML_Element datas;
	private XML_Element currentChild = null;
	
	@Override
	public void processingInstruction(String target, String data) throws SAXException
	{
		super.processingInstruction(target, data);
	}

	public XML_ParserHandler()
	{
		super();
	}
	
	@Override
	public void startDocument() throws SAXException
	{
		super.startDocument();
		this.datas = new XML_Element(null, "root");
		this.currentChild = this.datas;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		this.buffer = new StringBuffer();
		this.currentChild = this.currentChild.addChild(qName);
		this.currentChild.addAttributes(attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{
		if (this.currentChild != null)
		{
			this.currentChild.value = this.buffer.toString().trim();
			this.buffer = new StringBuffer();
			this.currentChild = this.currentChild.parent;
		}
	}
	
	public void characters(char[] chars,int start, int length)	throws SAXException
	{
		if(this.buffer != null)
		{
			this.buffer.append(chars, start, length);
		}
	}
	
	/**
	 * @return parsed Data
	 */
	public XML_Element getData()
	{
		return this.datas;
	}
}