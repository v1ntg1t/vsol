package vsol.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.URL;
import java.net.URLConnection;


import vsol.Const;


public class HttpUtil {

	public static String getHttpContent(String urlPath, String... parameters) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(urlPath);
			URLConnection uc = url.openConnection();
			addParameters(uc, parameters);
			InputStream is = uc.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(is, Const.ENCODING);
			BufferedReader br = new BufferedReader(isr);
			String line;
			sb = new StringBuilder();
			while ( (line = br.readLine()) != null ) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
		} catch(IOException e) {}
		return sb.toString();
	}
	
	private static void addParameters(URLConnection uc, String... parameters) 
			throws IOException {
		uc.setDoOutput(true);
		OutputStream os = uc.getOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(os, Const.ENCODING);
		for(String parameter : parameters) {
	        out.write(parameter);
		}
        out.close();
	}

}