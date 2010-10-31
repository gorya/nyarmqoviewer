/* 
 * PROJECT: NyARMqoViewer
 * --------------------------------------------------------------------------------
 * Copyright (c)2008 A虎＠nyatla.jp
 * airmail(at)ebony.plala.or.jp
 * http://nyatla.jp/
 * 
 * オンライン対応のメタセコイアデータARToolkit版ビューアー
 * オンラインの定義XMLに書いてあるメタセコイアデータをARToolkitを
 * 使って表示します。
 * 一番単純な方法でマーカー位置認識させてるからぷるぷると震えます...orz
 */
package jp.nyatla.nyarmqoviewer;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.io.*;
import java.util.*;

import javax.media.Buffer;

import javax.media.opengl.*;
import org.w3c.dom.*;
import com.sun.opengl.util.Animator;

import jp.nyatla.kGLModel.*;
import jp.nyatla.kGLModel.contentprovider.*;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.jmf.utils.*;
import jp.nyatla.nyartoolkit.jogl.utils.*;
import jp.nyatla.nyartoolkit.*;

import javax.xml.parsers.*;
import jp.nyatla.nyarmqoviewer.utils.*;

import java.net.*;
class Logger
{
    public static void logln(String i_message)
    {
	Date d=new Date();
	System.out.println("["+d+"]"+i_message);
    }
}
class NyARMqoViewerParam
{
    public URL    base_url;
    public String version;
    public String main_identifier;	//ContentProviderに渡すメイン識別子(moqファイルのアドレス)
    public String arcode_identifier;	//
    public String cparam_identifier;
    public float marker_size;
    public float scale;
    public int screen_x;
    public int screen_y;
    public float frame_rate;
    public String comment;
    public NyARMqoViewerParam(URL i_url) throws NyARException
    {
	Logger.logln("メタデータに接続中\r\n->"+i_url);
        this.base_url=i_url;
        // URL接続
	try{
	    InputStream in=HttpContentProvider.createInputStream(i_url);
            initByInputStream(in);
            in.close();
	}catch(Exception e){
	    throw new NyARException(e);
	}
    }
    private void initByInputStream(InputStream input_stream) throws NyARException
    {
	try{
	    //DOMの準備
	    DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = dbfactory.newDocumentBuilder();
	    Document doc = builder.parse(input_stream);
	    //XPATHの準備
	    SimpleXPath xpath=new SimpleXPath(doc);

	    //値を読む
	    SimpleXPath root,tmp;
	    root=xpath.select("/root");

	    //Version読み出し
	    version=root.selectString("version");
	    //Config読み出し
	    tmp=root.select("config");
	    this.arcode_identifier=tmp.selectString("ar_code/url");
	    this.marker_size=(float)tmp.selectDouble("ar_code/size");
	    this.cparam_identifier=tmp.selectString("ar_param/url");
	    this.screen_x=tmp.selectInt("ar_param/screen/x");
	    this.screen_y=tmp.selectInt("ar_param/screen/y");
	    this.frame_rate=(float)tmp.selectDouble("frame_rate");

	    //content読み出し
	    tmp=root.select("content");
	    this.scale=(float)tmp.selectDouble("scale");
	    this.main_identifier=tmp.selectString("mqo_file");
	    this.comment=tmp.selectString("comment");

	}catch (Exception e){
	    throw new NyARException(e);
	}	
	//チェック
	this.validationCheck();
    }
    private void validationCheck() throws NyARException
    {
	if(version.compareTo("NyARMqoViewer/0.1")!=0){
	    throw new NyARException("バージョン不一致 NyARMqoViewer/0.1である必要があります。");
	}
	if(this.marker_size<5.0f || this.marker_size>1000.0){
	    throw new NyARException("マーカーサイズが不正 5.0<size<1000.0である必要があります。");
	}
	if(	(this.screen_x==160 && this.screen_y==120) ||
		(this.screen_x==320 && this.screen_y==240) ||
		(this.screen_x==640 && this.screen_y==480)){
	}else{
	    throw new NyARException("スクリーン幅が不正 160x120 320x240 640x480の何れかを指定してください。");
	}

	if(this.frame_rate<10 || this.frame_rate>60){
	    throw new NyARException("フレームレートが不正 15.0 30.0ぐらいを指定して下さい。");
	}
	if(this.frame_rate<0.01 || this.frame_rate>100.0){
	    throw new NyARException("スケールが不正 0.01-100.0ぐらいを指定して下さい。");
	}	
    }
}



public class NyARMqoViewerAPP implements GLEventListener,JmfCaptureListener
{
    private int threshold;
    private NyARMqoViewerParam app_param;
    private KGLModelData model_data; // kei add
    private ContentProvider content_provider;
    private Animator animator;
    private GLNyARRaster_RGB cap_image;
    private JmfCameraCapture capture;
    private GL gl;
    //NyARToolkit関係
    private NyARGLUtil glnya;
    private GLNyARSingleDetectMarker nya;
    private GLNyARParam ar_param;

    /**
     * お手軽HttpInputStream生成関数
     * @return
     */
    private InputStream createHttpStream(URL i_base_url,String i_url) throws NyARException
    {
        // URL接続
	try{
	    URL url;
	    if(i_base_url!=null){
		url=new URL(i_base_url,i_url);
	    }else{
		url=new URL(i_url);		
	    }
	    return HttpContentProvider.createInputStream(url);
	}catch(Exception e){
	    throw new NyARException(e);
	}
    }


    public NyARMqoViewerAPP(NyARMqoViewerParam i_param,int i_threshold) throws NyARException
    {
	Logger.logln("NyARMqoViewerAPPを開始しています...");
	this.threshold=i_threshold;
	this.app_param=i_param;
	int SCR_X=this.app_param.screen_x;
	int SCR_Y=this.app_param.screen_y;
	//キャプチャの準備
	Logger.logln("キャプチャデバイスを準備しています.");
	this.capture=new JmfCameraCapture(SCR_X,SCR_Y,i_param.frame_rate,JmfCameraCapture.PIXEL_FORMAT_RGB);
	this.capture.setCaptureListener(this);
	//NyARToolkitの準備
	Logger.logln("NyARToolkitを準備しています.");
	this.ar_param=new GLNyARParam();
	this.ar_param.loadFromARFile(createHttpStream(this.app_param.base_url,this.app_param.cparam_identifier));
	this.ar_param.changeSize(SCR_X,SCR_Y);
	//検出マーカーの設定
	NyARCode ar_code  =new NyARCode(16,16);
	ar_code.loadFromARFile(createHttpStream(this.app_param.base_url,this.app_param.arcode_identifier));
	//マーカーDetecterの作成
	this.nya=new GLNyARSingleDetectMarker(this.ar_param,ar_code,this.app_param.marker_size);	
	//コンテンツプロバイダを作成
	try{
	    this.content_provider=new HttpContentProvider(this.app_param.main_identifier);
	}catch(Exception e){
	    throw new NyARException(e);
	}
	Logger.logln("ウインドウを作成しています.");
	//ウインドウの準備
	Frame frame = new Frame("NyARMqoViewerAPP");
	GLCanvas canvas = new GLCanvas();
	frame.add(canvas);
	canvas.addGLEventListener(this);
	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	frame.setVisible(true);
	Insets ins=frame.getInsets();
	frame.setSize(SCR_X+ins.left+ins.right,SCR_Y+ins.top+ins.bottom);
	canvas.setBounds(ins.left,ins.top,SCR_X,SCR_Y);
    }
    public void init(GLAutoDrawable drawable)
    {
	Logger.logln("OpenGLを初期化しています.");
	gl = drawable.getGL();
	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

	Logger.logln("モデルデータに接続中です.");
	try {// kei add
	    model_data = KGLModelData.createGLModel(gl,null,this.content_provider,0.015f,
		    KGLExtensionCheck.IsExtensionSupported(gl,"GL_ARB_vertex_buffer_object")) ;
	}
	catch(Exception e) {
	    e.printStackTrace() ;
	}
	//NyARToolkitの準備
	try{
	    //OpenGLユーティリティを作成
	    glnya=new NyARGLUtil(gl,ar_param);
	    //GL対応�のRGBラスタ
	    cap_image=new GLNyARRaster_RGB(gl,ar_param);
	    //キャプチャ開始
	    Logger.logln("キャプチャを開始します.");
	    capture.start();
	}catch(Exception e){
	    e.printStackTrace();
	}
	Logger.logln("拡張現実を開始します.");
	animator = new Animator(drawable);
	animator.start();
	Logger.logln("拡張現実の世界へようこそ！");

    }

    public void reshape(GLAutoDrawable drawable,
	    int x, int y,
	    int width, int height)
    {
	float ratio = (float)height / (float)width;
	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	gl.glViewport(0, 0,  width, height);

	//視体積�?設�?
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	gl.glFrustum(-1.0f, 1.0f, -ratio, ratio,5.0f,40.0f);
	//見る位置
	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glLoadIdentity();
	gl.glTranslatef(0.0f, 0.0f, -10.0f);
    }

    public void display(GLAutoDrawable drawable)
    {

	try{
	    if(!cap_image.hasData()){
		return;
	    }    
	    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear the buffers for new frame.          
	    //画像チェ�?��してマ�?カー探して、背景を書�?
	    boolean is_marker_exist;
	    synchronized(cap_image){
		is_marker_exist=nya.detectMarkerLite(cap_image,this.threshold);
		//背景
		glnya.drawBackGround(cap_image, 1.0);
	    }
	    //あったら立方体を書�?
	    if(is_marker_exist){
		// Projection transformation.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadMatrixd(ar_param.getCameraFrustumRH(),0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		// Viewing transformation.
		gl.glLoadIdentity();
		gl.glLoadMatrixd(nya.getCameraViewRH(),0);

		// -------v------ kei add
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		gl.glTranslatef(0.0f,0.0f,0.0f) ;//位置調整
		gl.glRotatef(90.0f,1.0f, 0.0f, 0.0f); //OpenGL座標系→ARToolkit座標系
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_FRONT);
		model_data.enables(app_param.scale) ;
		model_data.draw() ;
		model_data.disables() ;
		// -----^^^------ kei add
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
    }
    public void onUpdateBuffer(Buffer i_buffer)
    {
	try{
	    synchronized(cap_image){
		cap_image.setBuffer(i_buffer, true);
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}        
    }

    public void displayChanged(GLAutoDrawable drawable,
	    boolean modeChanged,
	    boolean deviceChanged)
    {
	
    }
    
    /**
     * 
     * @param args
     * １番目の引数に、XMLのURIを渡します。例えばhttp://127.0.0.1/model/miku.xmlとか。
     */
    public static void main(String[] args)
    {
	System.out.println("NyARMqoViewerAPP Version 0.1 (c)2008 A虎＠nyatla.jp");
	System.out.println("http://nyatla.jp/nyartoolkit/");
	System.out.println("kGLModel (c)2008 kei");
	//System.out.println("");
	System.out.println();
	String target=null;
	int threshold=0;
	switch(args.length){
	case 1:
	    threshold=110;
	    target=args[0];
	    break;
	case 2:
	    target=args[0];
	    threshold=Integer.parseInt(args[1]);
	    break;
	default:
	    System.err.println("引数には設定XMLのURLを設定してください。");
	    System.err.println("#NyARMqoViewerAPP [URL:設定xmlのurl] [カメラ閾値]");
	    System.exit(-1);
	}
	try{
	    // URL接続
	    NyARMqoViewerParam param=new NyARMqoViewerParam(new URL(target));
	    System.out.println("==モデルデータの情報==\r\n"+param.comment);
	    new NyARMqoViewerAPP(param,threshold);
	} catch (Exception ex){
	    System.err.println("エラーになっちゃった。");
	    ex.printStackTrace();
	}
    }
}

