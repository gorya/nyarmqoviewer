Copyright (C)2008 kei

これはMetaseqファイル（.MQO）ファイルをｊａｖａに読み込み＆描画するクラスです。

☆改変・再配布自由です。


☆文字コードはShift-Jisです。
　NyARToolkitは（多分）UTF-8なので
　両方Eclipseで読み込むときは、文字コードの設定をプロジェクト毎にしないと化けると思います。
　（そのうちUTF-8にしちゃうかも…）


java1.5で作ってます。
ＯｐｅｎＧＬを使いますので
jogl-1.1.1-pre-20080328-windows-i586.zip    （←Windowsの場合）
https://jogl.dev.java.net/
が必要です。

2008/04/08修正
・テクスチャの読み込み修正
　・データ並びが上下違う物に対応
　・透明度ファイルの指定に対応
　・TGAファイルの非圧縮モノクロがcom.sun.opengl.util.texture.*;で対応していないので読み込みクラス作成
　　（ほとんど使わないと思うのですが…“某はちゅねみく”のほっぺの渦巻きが表示できないので作りました…）
・描画メソッド(drow())からglEnable,glDisableを外に出しました
　→KGLModelData.enables,KGLModelData.disables
　（必要ない処理があったり、drow()でglDisableされたら困る場合の対策）
・オブジェクト名・マテリアル名で表示のｏｎ／ｏｆｆの切り替え機能追加
・テストソースのJavaSimpleLite.javaをNyARToolkit 0.7.20080406.0版対応にしました。

わかっている問題点
・gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
　だとうまく表示できないものがあることが確認出来ています。
　gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
　なら表示できるのですが…
　MQOファイルのどのパラメータをどう扱えばうまく表示出来るのかサッパリわかっていません…

ディレクトリとファイルの説明
javaMQO
 |
 |-GLMetaseq	Eclipseプロジェクトがあります。
    |
    |-kGLModel		MQO読み込みクラス
    |
    |-test		テストソース（mainクラス入り）
    |
    |-doc		javaDoc まだまだ不完全
 |
 |-Data		テストプログラム用データ

Eclipseプロジェクトの依存関係は配布データを読み込むと壊れているカモ
再設定してください。
kGLModel→joglに依存しています。


・テストソースの説明
test.java　：　ただMQOファイルを読み込んで表示するだけです。
　　　　　　　 読み込むファイルもソースの中に書いちゃってます（filename）。

JavaSimpleLite.java
　　　　　 ：　NyARToolkitに付いてたサンプル。
　　　　　　　 追加したところは「kei add」のコメントを付けています。
　　　　　　　 コレを動かすにはNyARToolkitが必要です。


☆重要☆
・NyARToolkitは…
ARToolkit Java class library NyARToolkit.
Copyright (C)2008 R.Iizuka
http://nyatla.jp/nyartoolkit/wiki/index.php
