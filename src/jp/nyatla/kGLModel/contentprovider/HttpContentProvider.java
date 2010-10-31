/* 
 * PROJECT: NyARMqoView
 * --------------------------------------------------------------------------------
 * Copyright (c)2008 A虎＠nyatla.jp
 * airmail@ebony.plala.or.jp
 * http://nyatla.jp/
 * 
 */
package jp.nyatla.kGLModel.contentprovider;

import java.io.InputStream;
import java.net.*;

import jp.nyatla.kGLModel.KGLException;
import jp.nyatla.nyartoolkit.NyARException;




public class HttpContentProvider implements ContentProvider
{
    private URL base_url;
    public HttpContentProvider(String i_base_url) throws KGLException
    {
	try{
	    this.base_url=new URL(i_base_url);
	}catch(Exception e){
	    throw new KGLException(e);
	}
    }
    public static InputStream createInputStream(URL i_url) throws KGLException
    {
        // URL接続
	URLConnection connection;
	try{
	    connection = (URLConnection)i_url.openConnection();
	    if((connection instanceof HttpURLConnection)){
		//HTTPの場合
		HttpURLConnection httpcl=(HttpURLConnection)connection;
		httpcl.setRequestMethod("GET");
                return httpcl.getInputStream();
	    }else{
		//Fileの場合
                return connection.getInputStream();
	    }
	}catch(Exception e){
	    throw new KGLException(e);
	}	
	
    }
    public ContentWrapper CreateStream(String i_identifier) throws KGLException
    {	
	try{
	    URL url=new URL(this.base_url,i_identifier);	
	    return new HttpContentWrapper(createInputStream(url));
	}catch(Exception e){
	    throw new KGLException(e);
	}
    }
    public ContentWrapper CreateMainStream() throws KGLException
    {
        return new HttpContentWrapper(createInputStream(this.base_url));
    }
    public static void main(String[] args)
    {
	try{
	    HttpContentProvider cp=new HttpContentProvider("http://127.0.0.1/model/miku.xml");
	    String s;
	    cp.CreateMainStream();
	    cp.CreateStream("/index.html");
	}catch(Exception e){
	    e.printStackTrace();
	}
    }
}