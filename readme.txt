MQOファイルARToolkit表示プログラム NyARMqoViewer.
Copyright (C)2008 A虎＠nyatla.jp

version 0.1.0.2008.04.16

http://nyatla.jp/
airmail(at)ebony.plala.or.jp
--------------------------------------------------

・NyARMqoViewer

NyARToolkitを使った、メタセコイアデータの表示プログラムです。
カメラから取り込んだ画像にMQOファイルのモデルデータをリアルタイムに合成
して表示します。



・必要なもの

ソフトウェア

　使用前に、JOGL,JMFのインストールが必要です。下記のURLから
　ダウンロードして、システムに導入する必要があります。

　1.JMF
　 http://java.sun.com/products/java-media/jmf/
　2.JOGL
　 https://jogl.dev.java.net/

ハードウェア

　1.ARToolkitに使うパターンファイルを印刷したもの
　2.USBカメラ（なるべく良いもの）




・使い方
　binフォルダに移動して、コマンドラインから以下のように実行します。
　java -cp NyARMqoViewerAPP.jar jp.nyatla.nyarmqoviewer.NyARMqoViewerAPP [設定xmlのURL]

　設定xmlのurlには、例えばhttp://nyatla.jp/nyartoolkit/mqosample/sample.xml等を指定します。
　
　とりあえず実験するにはstart_nya_sample.batを実行してみてください。
　このバッチファイルはnyatla.jpからサンプルデータをダウンロードして表示します。



・ソースの読み方
　エントリポイントは、NyARMqoViewerAPP.javaにあります。
　あとは気合で！



・著作権とライセンス
NyARToolkitがGPLのため、このプログラムもGPLでの配布となります。
ソースコード毎の著作権は、ソースコードの先頭部分を見てください。

パッケージjp.nyatla.kGLModel配下のソースコードは、keiさんの著作物に
改変を加えたものです。この部分のライセンスはkeiさんのそれに従います。


・謝辞

keiさん
メタセコイアデータの読み出し部分kGLModelを提供していただきました。
ありがとうございます。


ARToolkitを開発された加藤博一先生と、Human Interface Technology Lab
に感謝致します。
http://www.hitl.washington.edu/artoolkit/