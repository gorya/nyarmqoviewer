/* 
 * PROJECT: NyARMqoView
 * --------------------------------------------------------------------------------
 * Copyright (c)2008 A虎＠nyatla.jp
 * airmail@ebony.plala.or.jp
 * http://nyatla.jp/
 * 
 * Xpath取り扱い用のツールクラス
 */
package jp.nyatla.nyarmqoviewer.utils;
import org.w3c.dom.*;
import javax.xml.xpath.*;

public class SimpleXPath
{
    private Object doc;
    private XPath xpath;
    public SimpleXPath(Document i_doc)
    {
	this.doc=i_doc;
	XPathFactory factory= XPathFactory.newInstance();
	this.xpath = factory.newXPath();
    }
    public SimpleXPath(Node i_doc)
    {
	this.doc=i_doc;
	XPathFactory factory= XPathFactory.newInstance();
	this.xpath = factory.newXPath();
    }
    public NodeList selectNodeSet(String i_path) throws XPathExpressionException
    {
	XPathExpression expr = this.xpath.compile(i_path);
	Object result = expr.evaluate(doc, XPathConstants.NODESET);
	return (NodeList) result;
    }
    public Node selectNode(String i_path) throws XPathExpressionException
    {
	XPathExpression expr = this.xpath.compile(i_path);
	Object result = expr.evaluate(doc, XPathConstants.NODE);
	return (Node) result;
    }
    public String selectString(String i_path) throws XPathExpressionException
    {
	XPathExpression expr = this.xpath.compile(i_path);
	Object result = expr.evaluate(doc, XPathConstants.STRING);
	return (String) result;
    }
    public double selectDouble(String i_path) throws XPathExpressionException
    {
	XPathExpression expr = this.xpath.compile(i_path);
	Object result = expr.evaluate(doc, XPathConstants.NUMBER);
	return (Double) result;
    }
    public SimpleXPath select(String i_path) throws XPathExpressionException
    {
	return new SimpleXPath(selectNode(i_path));
    }
    public int selectInt(String i_path) throws XPathExpressionException
    {
	XPathExpression expr = this.xpath.compile(i_path);
	Object result = expr.evaluate(doc, XPathConstants.NUMBER);
	return ((Double)result).intValue();
    }
}
