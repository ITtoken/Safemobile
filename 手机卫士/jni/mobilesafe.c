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
	 * 字符串反转函数
	 * @param s 给定的需要反转的字符串
	 * @return 翻转后的字符串
	 */
	char* strrev(char* s)
	{
	    /* h指向s的头部 */
	    char* h = s;
	    char* t = s;
	    char ch;
	    /* t指向s的尾部 */
	    while(*t++){};
	    t--;    /* 与t++抵消 */
	    t--;    /* 回跳过结束符'\0' */

	    /* 当h和t未重合时，交换它们所指向的字符 */
	    while(h < t)
	    {
	        ch = *h;
	        *h++ = *t;    /* h向尾部移动 */
	        *t-- = ch;    /* t向头部移动 */
	    }
	    return(s);
	}

	char* cPass= Jstring2CStr(env,password);
	char* revAfter = strrev(cPass);

	jstring firstPass = (*env)->NewStringUTF(env,revAfter);
	return firstPass;
}
