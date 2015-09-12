#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include <jni.h>

JNIEXPORT jstring JNICALL Java_com_example_useractivity_MainActivity_firstPass
  (JNIEnv *env , jobject obj, jstring password,jint length){

	char*   Jstring2CStr(JNIEnv*   env,   jstring   jstr)
	{
		 char*   rtn   =   NULL;
		 jclass   clsstring   =   (*env)->FindClass(env,"java/lang/String");
		 jstring   strencode   =   (*env)->NewStringUTF(env,"GB2312");
		 jmethodID   mid   =   (*env)->GetMethodID(env,clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
		 jbyteArray   barr=   (jbyteArray)(*env)->CallObjectMethod(env,jstr,mid,strencode); // String .getByte("GB2312");
		 jsize   alen   =   (*env)->GetArrayLength(env,barr);
		 jbyte*   ba   =   (*env)->GetByteArrayElements(env,barr,JNI_FALSE);
		 if(alen   >   0)
		 {
		  rtn   =   (char*)malloc(alen+1);         //"\0"
		  memcpy(rtn,ba,alen);
		  rtn[alen]=0;
		 }
		 (*env)->ReleaseByteArrayElements(env,barr,ba,0);  //
		 return rtn;
	}

	/**
	 * �ַ�����ת����
	 * @param s ��������Ҫ��ת���ַ���
	 * @return ��ת����ַ���
	 */
	char* strrev(char* s)
	{
	    /* hָ��s��ͷ�� */
	    char* h = s;
	    char* t = s;
	    char ch;
	    /* tָ��s��β�� */
	    while(*t++){};
	    t--;    /* ��t++���� */
	    t--;    /* ������������'\0' */

	    /* ��h��tδ�غ�ʱ������������ָ����ַ� */
	    while(h < t)
	    {
	        ch = *h;
	        *h++ = *t;    /* h��β���ƶ� */
	        *t-- = ch;    /* t��ͷ���ƶ� */
	    }
	    return(s);
	}

	char* cPass= Jstring2CStr(env,password);
	char* revAfter = strrev(cPass);

	jstring firstPass = (*env)->NewStringUTF(env,revAfter);
	return firstPass;
}
