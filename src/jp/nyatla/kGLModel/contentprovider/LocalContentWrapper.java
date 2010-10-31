/* 
 * PROJECT: NyARMqoView
 * --------------------------------------------------------------------------------
 * Copyright (c)2008 A虎＠nyatla.jp
 * airmail@ebony.plala.or.jp
 * http://nyatla.jp/
 * 
 */
package jp.nyatla.kGLModel.contentprovider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.nyatla.kGLModel.KGLException;


public class LocalContentWrapper implements ContentWrapper
{
    private FileInputStream file_input;
    private String suffix;
    public LocalContentWrapper(String i_identifier) throws KGLException
    {
	try{
	    file_input=new FileInputStream(i_identifier);
	}catch(IOException e){
	    throw new KGLException(e);
	}	
    }
    public String getFileSuffix()
    {
	return suffix;
    }
    public InputStream getInputStream()
    {
	return file_input;
    }
    public boolean CheckFileExtension(String i_extension)
    {
	//ほんとはここでチェックしなければいけないけど、とりあえず無視！！
	return true;
    }
}