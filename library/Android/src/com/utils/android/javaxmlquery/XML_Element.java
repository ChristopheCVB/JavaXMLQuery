package com.utils.android.javaxmlquery;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;

import android.text.TextUtils;
import android.util.Log;

public class XML_Element
{
	private static final String TAG = "XML_Element";
	
	private static final String HOOK_START = "[";
	private static final String REGEX_HOOK_START = "\\[";
	private static final String REGEX_HOOK_END = "\\]";
	private static final String REGEX_DOT_STAR = ".*";
	private static final String REGEX_DOLLAR = "$";
	
	private static final String TO_STRING_SPACE = " ";
	private static final String TO_STRING_BRACKET_START = "<";
	private static final String TO_STRING_BRACKET_START_SLASH = "</";
	private static final String TO_STRING_BRACKET_END = ">";
	private static final String TO_STRING_BRACKET_END_SLASH = "/>";
	private static final String TO_STRING_NEW_LINE = "\n";
	private static final String TO_STRING_TABULATION = "\t";
	private static final String TO_STRING_DOUBLE_QUOTE = "\"";
	private static final String TO_STRING_EQUALS_QUOTE = "=\"";
	
	private static final String ATTRIBUTE_EQUALS = "=";
	private static final String ATTRIBUTE_NOT_EQUALS = "!=";
	private static final String ATTRIBUTE_CONTAINS = "~=";
	private static final String ATTRIBUTE_STARTS_WITH = "^=";
	private static final String ATTRIBUTE_ENDS_WITH = "$=";
	
	public String name;
	protected XML_Element parent;
	private HashMap<String, String> attributes;
	private ArrayList<XML_Element> children;
	protected String value;
	
	public XML_Element(XML_Element parent, String name)
	{
		this.parent = parent;
		this.name = name;
	}
	
	/**
	 * adds in the HashMap this.attributes, the different associated key/value
	 * @param attributes of the object
	 */
	protected void addAttributes(Attributes attributes)
	{
		int attributesLength = attributes.getLength();
		if (attributesLength > 0)
		{
			if (this.attributes == null)
			{
				this.attributes = new HashMap<String, String>();
			}
			for (int attributeIndex = 0; attributeIndex < attributesLength; attributeIndex++)
			{
				this.attributes.put(attributes.getLocalName(attributeIndex), attributes.getValue(attributeIndex));
			}
		}
	}

	/**
	 * creates a PJ_Element child of this, and adds it in this.children
	 * @param childName name of the tag
	 * @return
	 */
	protected XML_Element addChild(String childName)
	{
		if (this.children == null)
		{
			this.children = new ArrayList<XML_Element>();
		}
		XML_Element child = new XML_Element(this, childName);
		this.children.add(child);
		return child;
	}
	
	public XML_Element firstChild()
	{
		XML_Element child = null;
		if (this.children != null) // if not null, there is at least one child
		{
			child = this.children.get(0);
		}
		return child;
	}
	
	/**
	 * gets an attribute linked to this XML_Element
	 * @param attrName key to find
	 * @return the value associated to the key tagName, or <b>""</b> otherwise
	 */
	public String attr(String attrName)
	{
		String value = null;
		if (this.attributes != null)
		{
			value = this.attributes.get(attrName);
		}
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
	 * 
	 * @param selector name of the XML_Element children.
	 * @return a <b>XML_Elements</b> containing all the XML_Element children
	 */
	public XML_Elements find(String selector)
	{
		long start = System.currentTimeMillis();
		XML_Elements elements = null;
		if (TextUtils.isEmpty(selector))
		{
			elements = new XML_Elements();
			elements.add(this);
		}
		else if (selector.contains(XML_Element.TO_STRING_BRACKET_END))
		{
			elements = this.selectByArrow(selector);
		}
		else if (selector.contains(XML_Element.HOOK_START))
		{
			elements = this.selectByAttribute(selector, false);
		}
		else
		{
			elements = new XML_Elements();
			if (this.children != null)
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
		}
		
		long end = System.currentTimeMillis();
		long duration = end - start;
		if (duration > 1)
		{
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			StackTraceElement caller = stackTraceElements[3];
			String fullClassName = caller.getClassName();
			StringBuilder messageBuilder = new StringBuilder("find(").append(selector).append(") ");
			messageBuilder.append(elements.size()).append(" elements in ").append(duration).append("ms called by ");
			messageBuilder.append(fullClassName.substring(fullClassName.lastIndexOf(".") + 1));
			messageBuilder.append(".").append(caller.getMethodName());
			Log.w(XML_Element.TAG, messageBuilder.toString());
		}
		return elements;
	}
	
	/**
	 * finds all the direct XML_Element children named <i>tagName</i>. This method can have in param a css3 like selector (using <i>">"</i>, <i>"[...=...]"</i>)
	 * 
	 * @param tagName name of the XML_Element children.
	 * @return a <b>XML_Elements</b> containing all the XML_Element children
	 */
	protected XML_Elements findChildren(String tagName)
	{
		XML_Elements elements = null;
		if (TextUtils.isEmpty(tagName))
		{
			elements = new XML_Elements();
			if (this.children != null)
			{
				elements.addAll(this.children);
			}
		}
		else if (tagName.contains(XML_Element.HOOK_START))
		{
			elements = this.selectByAttribute(tagName, true);
		}
		else
		{
			elements = new XML_Elements();
			if (this.children != null)
			{
				for (XML_Element element : this.children)
				{
					if (element.name.equals(tagName))
					{
						elements.add(element);
					}
				}
			}
		}
		return elements;
	}
	
	/**
	 * @param key name of the attribute
	 * @param value
	 * @return true if has an attribute with key as name and if value is not null, value associated to key.
	 */
	protected boolean hasAttribute(String key, String value)
	{
		boolean hasAttribute = false;
		if (value == null)
		{
			hasAttribute = this.hasAttribute(key);
		}
		else
		{
			hasAttribute = (this.attributes != null) && value.equals(this.attributes.get(key));
		}
		return hasAttribute;
	}
	
	/**
	 * @param key name of the attribute
	 * @param valueExp - String
	 * @return true if has an attribute with key as name and if valueExp is not null, valueExp matching this attribute value .
	 */
	protected boolean matchesAttribute(String key, String valueExp)
	{
		boolean isMatching = false;
		if (valueExp == null)
		{
			isMatching = this.hasAttribute(key);
		}
		else if (this.attributes != null)
		{
			String value = this.attributes.get(key);
			isMatching = (value != null) && value.matches(valueExp);
		}
		
		return isMatching;
	}
	
	/**
	 * @param key
	 * @return true if has an attribute with key as name.
	 */
	protected boolean hasAttribute(String key)
	{
		return (this.attributes != null) && this.attributes.containsKey(key);
	}
	
	/**
	 * @param key
	 * @return a XML_Elements containing all the children which have the attribute key
	 */
	protected XML_Elements findByAttribute(String key)
	{
		XML_Elements elements = new XML_Elements();
		if (this.children != null)
		{
			for (XML_Element element : this.children)
			{
				if (element.attr(key) != null)
				{
					elements.add(element);
				}
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
		else if (this.children != null)
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
		String[] tags = tagName.split(XML_Element.TO_STRING_BRACKET_END);
		String currentTag = tags[0].trim();
		XML_Elements elements = this.find(currentTag);
		
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
		XML_Elements elements = null;
		String[] tmpTagNames = selector.split(XML_Element.REGEX_HOOK_START);
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
		
		for (String tmpTag : tmpTagNames)
		{
			currentTag = tmpTag.split(XML_Element.REGEX_HOOK_END)[0].trim();
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
				String[] tab = attributeSelector.split(XML_Element.ATTRIBUTE_NOT_EQUALS);
				key = tab[0];
				valueExp = tab[1];
				isPositive = false;
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_STARTS_WITH))
			{
				String[] tab = attributeSelector.split(XML_Element.ATTRIBUTE_STARTS_WITH);
				key = tab[0];
				valueExp = tab[1] + XML_Element.REGEX_DOT_STAR;
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_ENDS_WITH))
			{
				String[] tab = attributeSelector.split(XML_Element.ATTRIBUTE_ENDS_WITH);
				key = tab[0];
				valueExp = XML_Element.REGEX_DOT_STAR + tab[1] + XML_Element.REGEX_DOLLAR;
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_CONTAINS))
			{
				String[] tab = attributeSelector.split(XML_Element.ATTRIBUTE_CONTAINS);
				key = tab[0];
				valueExp = XML_Element.REGEX_DOT_STAR + tab[1] + XML_Element.REGEX_DOT_STAR;
			}
			else if (attributeSelector.contains(XML_Element.ATTRIBUTE_EQUALS)) // Must be the last tested
			{
				String[] tab = attributeSelector.split(XML_Element.ATTRIBUTE_EQUALS);
				key = tab[0];
				valueExp = tab[1];
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
	 * return an xml representation of the object
	 */
	@Override
	public String toString()
	{
		return this.toString(0);
	}
	
	/**
	 * return an xml representation of the object
	 * 
	 * @param index : level of indents in the logs.
	 */
	protected String toString(int index)
	{
		int nextRound = index + 1;
		// text = "<" + this.name
		StringBuilder text = new StringBuilder(XML_Element.TO_STRING_BRACKET_START).append(this.name);
		
		if (this.attributes != null)
		{
			for (String key : this.attributes.keySet())
			{
				// text += " " + key + "=\"" + this.attributes.get(value) +"\""
				text.append(XML_Element.TO_STRING_SPACE).append(key).append(XML_Element.TO_STRING_EQUALS_QUOTE).append(this.attributes.get(key)).append(XML_Element.TO_STRING_DOUBLE_QUOTE);
			}
		}
		if ((this.children == null) && TextUtils.isEmpty(this.value)) // if (this.children == null) there is no child
		{
			// text += "/>"
			text.append(XML_Element.TO_STRING_BRACKET_END_SLASH);
		}
		else
		{
			// text += ">"
			text.append(XML_Element.TO_STRING_BRACKET_END);
			if (!TextUtils.isEmpty(this.value))
			{
				// text += this.value
				text.append(this.value);
			}
			if (this.children != null)
			{
				for (XML_Element element : this.children)
				{
					// text += "\n"
					text.append(XML_Element.TO_STRING_NEW_LINE);
					for (int i = 0; i < nextRound; i++)
					{
						// text += "\t"
						text.append(XML_Element.TO_STRING_TABULATION);
					}
					// text += element.toString(nextRound)
					text.append(element.toString(nextRound));
				}
			}
			for (int i = 0; i < index; i++)
			{
				// text += "\t"
				text.append(XML_Element.TO_STRING_TABULATION);
			}
			// text += "</" + this.name + ">"
			text.append(XML_Element.TO_STRING_BRACKET_START_SLASH).append(this.name).append(XML_Element.TO_STRING_BRACKET_END);
		}
		// return text + "\n"
		return text.append(XML_Element.TO_STRING_NEW_LINE).toString();
	}
}