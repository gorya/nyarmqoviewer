/* 
 * PROJECT: NyARMqoView
 * --------------------------------------------------------------------------------
 * Copyright (c)2008 A虎＠nyatla.jp
 * airmail@ebony.plala.or.jp
 * http://nyatla.jp/
 * 
 */
package jp.nyatla.kGLModel.contentprovider;

import java.io.File;

import jp.nyatla.kGLModel.KGLException;


public class LocalContentProvider implements ContentProvider
{
    private File base_path;
    public LocalContentProvider(String i_base_filepath)
    {
	this.base_path=new File(i_base_filepath);
    }
    public ContentWrapper CreateStream(String i_identifier) throws KGLException
    {
	ContentWrapper result;
	result=new LocalContentWrapper(getFileName(i_identifier));
	return result;
    }
    public ContentWrapper CreateMainStream() throws KGLException
    {
	return CreateStream(base_path.getPath());
    }
    private String getFileName(String i_filename)
    {
	File f=new File(i_filename);
	//子が絶対パス情報を持っていればそのまま使う
	if(f.isAbsolute()){
	    return i_filename;
	}

	//親が絶対パスでなければ、子のパスをそのまま使う。
	if(!this.base_path.isAbsolute()){
	    return i_filename;
	}

	//親が絶対パスで子が絶対パスでなければ、合成する。
	File abs_child_path=new File(this.base_path.getParent(),i_filename);
	return abs_child_path.getAbsolutePath();
    }
    public static void main(String[] args)
    {
	LocalContentProvider cp=new LocalContentProvider("c:\\test\\test2\\2.txt");
	String s;
	s=cp.getFileName("a.txt");
	s=cp.getFileName("d:\\b.txt");
	s=cp.getFileName("..\\..\\a.txt");
    }
}