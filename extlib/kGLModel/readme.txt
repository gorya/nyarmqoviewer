Copyright (C)2008 kei

�����Metaseq�t�@�C���i.MQO�j�t�@�C�������������ɓǂݍ��݁��`�悷��N���X�ł��B

�����ρE�Ĕz�z���R�ł��B


�������R�[�h��Shift-Jis�ł��B
�@NyARToolkit�́i�����jUTF-8�Ȃ̂�
�@����Eclipse�œǂݍ��ނƂ��́A�����R�[�h�̐ݒ���v���W�F�N�g���ɂ��Ȃ��Ɖ�����Ǝv���܂��B
�@�i���̂���UTF-8�ɂ����Ⴄ�����c�j


java1.5�ō���Ă܂��B
�n�������f�k���g���܂��̂�
jogl-1.1.1-pre-20080328-windows-i586.zip    �i��Windows�̏ꍇ�j
https://jogl.dev.java.net/
���K�v�ł��B

2008/04/08�C��
�E�e�N�X�`���̓ǂݍ��ݏC��
�@�E�f�[�^���т��㉺�Ⴄ���ɑΉ�
�@�E�����x�t�@�C���̎w��ɑΉ�
�@�ETGA�t�@�C���̔񈳏k���m�N����com.sun.opengl.util.texture.*;�őΉ����Ă��Ȃ��̂œǂݍ��݃N���X�쐬
�@�@�i�قƂ�ǎg��Ȃ��Ǝv���̂ł����c�g�^�͂���˂݂��h�̂ق��؂̉Q�������\���ł��Ȃ��̂ō��܂����c�j
�E�`�惁�\�b�h(drow())����glEnable,glDisable���O�ɏo���܂���
�@��KGLModelData.enables,KGLModelData.disables
�@�i�K�v�Ȃ���������������Adrow()��glDisable���ꂽ�獢��ꍇ�̑΍�j
�E�I�u�W�F�N�g���E�}�e���A�����ŕ\���̂����^�������̐؂�ւ��@�\�ǉ�
�E�e�X�g�\�[�X��JavaSimpleLite.java��NyARToolkit 0.7.20080406.0�őΉ��ɂ��܂����B

�킩���Ă�����_
�Egl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
�@���Ƃ��܂��\���ł��Ȃ����̂����邱�Ƃ��m�F�o���Ă��܂��B
�@gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
�@�Ȃ�\���ł���̂ł����c
�@MQO�t�@�C���̂ǂ̃p�����[�^���ǂ������΂��܂��\���o����̂��T�b�p���킩���Ă��܂���c

�f�B���N�g���ƃt�@�C���̐���
javaMQO
 |
 |-GLMetaseq	Eclipse�v���W�F�N�g������܂��B
    |
    |-kGLModel		MQO�ǂݍ��݃N���X
    |
    |-test		�e�X�g�\�[�X�imain�N���X����j
    |
    |-doc		javaDoc �܂��܂��s���S
 |
 |-Data		�e�X�g�v���O�����p�f�[�^

Eclipse�v���W�F�N�g�̈ˑ��֌W�͔z�z�f�[�^��ǂݍ��ނƉ��Ă���J��
�Đݒ肵�Ă��������B
kGLModel��jogl�Ɉˑ����Ă��܂��B


�E�e�X�g�\�[�X�̐���
test.java�@�F�@����MQO�t�@�C����ǂݍ���ŕ\�����邾���ł��B
�@�@�@�@�@�@�@ �ǂݍ��ރt�@�C�����\�[�X�̒��ɏ���������Ă܂��ifilename�j�B

JavaSimpleLite.java
�@�@�@�@�@ �F�@NyARToolkit�ɕt���Ă��T���v���B
�@�@�@�@�@�@�@ �ǉ������Ƃ���́ukei add�v�̃R�����g��t���Ă��܂��B
�@�@�@�@�@�@�@ �R���𓮂����ɂ�NyARToolkit���K�v�ł��B


���d�v��
�ENyARToolkit�́c
ARToolkit Java class library NyARToolkit.
Copyright (C)2008 R.Iizuka
http://nyatla.jp/nyartoolkit/wiki/index.php
