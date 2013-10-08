package com.utils.android.javaxmlquery;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.TextUtils;

public class XML_ParserHandler extends DefaultHandler
{
	private StringBuilder stringBuilder = new StringBuilder(1024);
	protected XML_Element datas = new XML_Element(null, "root");
	private XML_Element currentChild = this.datas;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		this.currentChild = this.currentChild.addChild(!TextUtils.isEmpty(localName) ? localName : qName);
		this.currentChild.addAttributes(attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{
		this.currentChild.value = this.stringBuilder.toString().trim();
		this.stringBuilder.setLength(0);
		this.currentChild = this.currentChild.parent;
	}
	
	@Override
	public void characters(char[] chars,int start, int length)	throws SAXException
	{
		this.stringBuilder.append(chars, start, length);
	}
}