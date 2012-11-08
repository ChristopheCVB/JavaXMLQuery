package com.utils.android.javaxmlquery;

import java.util.ArrayList;

public class XML_Elements extends ArrayList<XML_Element>
{
	private static final long serialVersionUID = 7559954730655375171L;
	
	/**
	 * gets an attribute linked to the first XML_Element of this XML_Elements
	 * @param attrName key to find
	 * @return the value associated to the key tagName, or <b>null</b> otherwise
	 */
	public String attr(String attrName)
	{
		if (this.size() == 0)
		{
			return "";
		}
		return this.get(0).attr(attrName);
	}
	
	/**
	 * get the node value
	 * @return String
	 */
	public String val()
	{
		if (this.size() == 0)
		{
			return "";
		}
		return this.get(0).val();
	}
	
	/**
	 * finds all the XML_Element children named <i>tagName</i>. This method can have in param a css3 like selector (using <i>">"</i>, <i>"[...=...]"</i>)
	 * @param selector name of the XML_Element children. 
	 * @return a <b>XML_Elements</b> containing all the XML_Element children
	 */
	public XML_Elements find(String selector)
	{
		XML_Elements elements = new XML_Elements();

		for (XML_Element element : this)
		{
			elements.addAll(element.find(selector));
		}
		return elements;
	}

	/**
	 * finds all the direct XML_Element children named <i>tagName</i>. This method can have in param a css3 like selector (using <i>">"</i>, <i>"[...=...]"</i>)
	 * @param tagName name of the XML_Element children. 
	 * @return a <b>XML_Elements</b> containing all the XML_Element children
	 */
	public XML_Elements findChildren(String tagName)
	{
		XML_Elements elements = new XML_Elements();
		
		for (XML_Element element : this)
		{
			elements.addAll(element.findChildren(tagName));
		}
		
		return elements;
	}

	/**
	 * @param key
	 * @param value
	 * @return a XML_Elements containing all the children which have the attribute key with the value passed in params.
	 */
	public XML_Elements findByAttribute(String key, String value)
	{
		XML_Elements elements = new XML_Elements();
		
		for (XML_Element element : this)
		{
			elements.addAll(element.findByAttribute(key, value));
		}
		
		return elements;
	}
}