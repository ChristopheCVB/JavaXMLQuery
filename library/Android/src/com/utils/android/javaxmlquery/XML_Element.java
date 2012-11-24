package com.utils.android.javaxmlquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.Attributes;

import android.text.TextUtils;
import android.util.Log;


public class XML_Element implements Serializable
{
	private static final long serialVersionUID = 2236803605050972676L;
	
	private static final String ATTRIBUTE_EQUALS			= "=";
	private static final String ATTRIBUTE_NOT_EQUALS		= "!=";
	private static final String ATTRIBUTE_CONTAINS			= "~=";
	private static final String ATTRIBUTE_STARTS_WITH		= "^=";
	private static final String ATTRIBUTE_ENDS_WITH			= "$=";
	
	public String name;
	public XML_Element parent;
	public HashMap<String, String> attributes;
	public ArrayList<XML_Element> children;
	protected String value;

	public XML_Element(XML_Element parent, String name)
	{
		this.parent = parent;
		this.name = name;
		this.attributes = new HashMap<String, String>();
		this.children = new ArrayList<XML_Element>();
	}

	/**
	 * adds in the HashMap this.attributes, the different associated key/value
	 * @param attributes of the object
	 */
	protected void addAttributes(Attributes attributes)
	{
		int attributesLength = attributes.getLength();
		for (int attributeIndex = 0; attributeIndex < attributesLength; attributeIndex++)
		{
			this.attr(attributes.getLocalName(attributeIndex), attributes.getValue(attributeIndex));
		}
	}

	/**
	 * adds in the HashMap this.attributes, the a key/value object
	 * @param key String
	 * @param value String
	 */
	protected void attr(String key, String value)
	{
		this.attributes.put(key, value);
	}

	/**
	 * creates a PJ_Element child of this, and adds it in this.children
	 * @param childName name of the tag
	 * @return
	 */
	protected XML_Element addChild(String childName)
	{
		XML_Element child = new XML_Element(this, childName);
		this.children.add(child);
		return child;
	}
	
	public XML_Element firstChild()
	{
		if (this.children.size() > 0)
		{
			return this.children.get(0);
		}
		return null;
	}

	/**
	 * gets an attribute linked to this XML_Element
	 * @param attrName key to find
	 * @return the value associated to the key tagName, or <b>""</b> otherwise
	 */
	public String attr(String attrName)
	{
		String value = this.attributes.get(attrName);
		if (value == null)
		{
			value = "";
		}
		return value;
	}
	
	public String val()
	{
		return this.value;
	}

	/**
	 * finds all the XML_Element children named <i>tagName</i>. This method can have in param a css3 like selector (using <i>">"</i>, <i>"[...=...]"</i>)
	 * @param selector name of the XML_Element children. 
	 * @return a <b>XML_Elements</b> containing all the XML_Element children
	 */
	public XML_Elements find(String selector)
	{
		long start = new Date().getTime();
		XML_Elements elements = new XML_Elements();
		if (TextUtils.isEmpty(selector))
		{
			elements.add(this);
		}
		else if (selector.contains(">"))
		{
			elements = this.selectByArrow(selector);
		}
		else if (selector.contains("["))
		{
			elements = this.selectByAttribute(selector, false);
		}
		else
		{
			for (XML_Element element : this.children)
			{
				if (element.name.equals(selector))
				{
					elements.add(element);
				}
				elements.addAll(element.find(selector));
			}
		}

		long end = new Date().getTime();
		long duration = end - start;
		if (duration > 1)
		{
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			StackTraceElement caller = stackTraceElements[3];
			String fullClassName = caller.getClassName();
			Log.w("XML_Element", "find(" + selector + ") " + elements.size() + " elements in " + duration + "ms called by " + fullClassName.substring(fullClassName.lastIndexOf(".")+1) + "." + caller.getMethodName());
		}
		return elements;
	}

	/**
	 * finds all the direct XML_Element children named <i>tagName</i>. This method can have in param a css3 like selector (using <i>">"</i>, <i>"[...=...]"</i>)
	 * @param tagName name of the XML_Element children. 
	 * @return a <b>XML_Elements</b> containing all the XML_Element children
	 */
	protected XML_Elements findChildren(String tagName)
	{
		XML_Elements elements = new XML_Elements();
		if (TextUtils.isEmpty(tagName))
		{
			elements.addAll(this.children);
		}
		else if (tagName.contains("["))
		{
			elements = this.selectByAttribute(tagName, true);
		}
		else
		{
			for (XML_Element element : this.children)
			{
				if (element.name.equals(tagName))
				{
					elements.add(element);
				}
			}
		}
		return elements;
	}

	/**
	 * @param key name of the attribute
	 * @param value 
	 * @return true if has an attribute with key as name and value associated to key. If value is null, return true if has an attribute with key as name.
	 */
	protected boolean hasAttribute(String key, String value)
	{
		if (value == null)
		{
			return this.hasAttribute(key);
		}
		return value.equals(this.attributes.get(key));
	}
	
	/**
	 * @param key name of the attribute
	 * @param valueExp 
	 * @return true if has an attribute with key as name and value associated to key. If valueExp is null, return true if has an attribute with key as name.
	 */
	protected boolean matchesAttribute(String key, String valueExp)
	{
		if (valueExp == null)
		{
			return this.hasAttribute(key);
		}
		String value = this.attributes.get(key);
		return value != null && value.matches(valueExp);
	}

	/**
	 * @param key
	 * @return true if has an attribute with key as name.
	 */
	protected boolean hasAttribute(String key)
	{
		return this.attributes.containsKey(key);
	}

	/**
	 * @param key
	 * @return a XML_Elements containing all the children which have the attribute key
	 */
	protected XML_Elements findByAttribute(String key)
	{
		XML_Elements elements = new XML_Elements();
		for (XML_Element element : this.children)
		{
			if (element.attr(key) != null)
			{
				elements.add(element);
			}
		}
		return elements;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return a XML_Elements containing all the children which have the attribute key with the value passed in params.
	 */
	protected XML_Elements findByAttribute(String key, String value)
	{
		XML_Elements elements = new XML_Elements();
		if (value == null)
		{
			elements = this.findByAttribute(key);
		}
		else
		{
			for (XML_Element element : this.children)
			{
				if (value.equals(element.attr(key)))
				{
					elements.add(element);
				}
			}
		}
		return elements;
	}

	/**
	 * is used in the particular case where the is ">" in the tagName.
	 * @param tagName
	 * @return XML_Elements containing all the children linked to tagName.
	 */
	protected XML_Elements selectByArrow(String tagName)
	{
		XML_Elements elements = new XML_Elements();
		final String[] tags = tagName.split(">");

		String currentTag = tags[0].trim();
		elements = this.find(currentTag);

		for (int tagIndex = 1; tagIndex < tags.length; tagIndex++)
		{
			currentTag = tags[tagIndex].trim();
			elements = elements.findChildren(currentTag);
		}

		return elements;
	}

	/**
	 * is used in the particular case where the is "[" in the tagName.
	 * @param selector
	 * @return XML_Elements containing all the children linked to tagName.
	 */
	protected XML_Elements selectByAttribute(String selector, boolean isChild)
	{
		XML_Elements elements = new XML_Elements();
		final String[] tmpTagNames = selector.split("\\[");
		ArrayList<String> attributeSelectors = new ArrayList<String>();

		String currentTag = tmpTagNames[0].trim();
		if (isChild)
		{
			elements = this.findChildren(currentTag);
		}
		else
		{
			elements = this.find(currentTag);
		}

		for (int tmpTagIndex = 1; tmpTagIndex < tmpTagNames.length; tmpTagIndex++)
		{
			currentTag = tmpTagNames[tmpTagIndex].split("\\]")[0].trim();
			attributeSelectors.add(currentTag);
		}
		XML_Elements finalElements = new XML_Elements();
		for (String attributeSelector : attributeSelectors)
		{
			String key = null;
			String valueExp = null;
			boolean isPositive = true;
			if (attributeSelector.contains(XML_Element.ATTRIBUTE_NOT_EQUALS))
			{
				key = attributeSelector.split(XML_Element.ATTRIBUTE_NOT_EQUALS)[0];
				valueExp = attributeSelector.split(XML_Element.ATTRIBUTE_NOT_EQUALS)[1];
				isPositive = false;
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_STARTS_WITH))
			{
				key = attributeSelector.split(XML_Element.ATTRIBUTE_STARTS_WITH)[0];
				valueExp = attributeSelector.split(XML_Element.ATTRIBUTE_STARTS_WITH)[1] + ".*";
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_ENDS_WITH))
			{
				key = attributeSelector.split(XML_Element.ATTRIBUTE_ENDS_WITH)[0];
				valueExp = ".*" + attributeSelector.split(XML_Element.ATTRIBUTE_ENDS_WITH)[1] + "$";
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_CONTAINS))
			{
				key = attributeSelector.split(XML_Element.ATTRIBUTE_CONTAINS)[0];
				valueExp = ".*" + attributeSelector.split(XML_Element.ATTRIBUTE_CONTAINS)[1] + ".*";
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_EQUALS)) // Must be the last tested
			{
				key = attributeSelector.split(XML_Element.ATTRIBUTE_EQUALS)[0];
				valueExp = attributeSelector.split(XML_Element.ATTRIBUTE_EQUALS)[1];
			}
			else
			{
				key = attributeSelector;
			}
			
			for (XML_Element element : elements)
			{
				if (isPositive)
				{
					if (element.matchesAttribute(key, valueExp))
					{
						finalElements.add(element);
					}
				}
				else if (!element.hasAttribute(key, valueExp))
				{
					finalElements.add(element);
				}
			}
		}
		return finalElements;
	}

	/**
	 * return a stringified version of the object
	 */
	public String toString()
	{
		return this.toString(0);
	}

	/**
	 * return a stringified version of the object
	 * @param index : level of indents in the logs.
	 */
	protected String toString(int index)
	{
		final int nextRound = index + 1;
		String text = "<" + this.name;

		Iterator<String> iterator = this.attributes.keySet().iterator();
		while(iterator.hasNext())
		{
			String key = iterator.next();
			text += " " + key + "=\"" + this.attributes.get(key) + "\"";
		}
		if (this.children.size() == 0 && TextUtils.isEmpty(this.value))
		{
			text += "/>";
		}
		else
		{
			text += ">";
			if (!TextUtils.isEmpty(this.value))
			{
				text += this.value;
			}
			for (XML_Element element : this.children)
			{
				text += "\n";
				for (int i = 0; i < nextRound; i++)
				{
					text += "\t";
				}
				text += element.toString(nextRound);
			}
			for (int i = 0; i < nextRound - 1; i++)
			{
				text += "\t";
			}
			text += "</"+this.name+">";
		}
		return text + "\n";
	}
}