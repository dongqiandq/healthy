package com.healthy.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {
	
	 /**
	  * 读取输入流并将其转换为字符串
     * @param is 输入流
     * @return
     */
    public String readInputStreamToString(InputStream is){
        String string = null;
        try {
            int len = 0;
            byte[] bytes = new byte[1024];
            StringBuffer stringBuffer = new StringBuffer();
            while((len=(is.read(bytes)))!=-1){
                stringBuffer.append(new String(bytes,0,len));
            }
            string = stringBuffer.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return string;
    }
    
    /**
       * 关闭输入输出流
     * @param is
     * @param os
     */
    public void closeIO(InputStream is, OutputStream os){
        if(is!=null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(os!=null){
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    

}
