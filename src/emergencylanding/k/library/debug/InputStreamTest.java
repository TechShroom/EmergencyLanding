package emergencylanding.k.library.debug;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import emergencylanding.k.library.util.LUtils;

public class InputStreamTest {

	public static void main(String[] args) throws IOException {
		String path = //
		"C:/Users/Kenzie Togami/Dropbox/CrashCourse/test.zip/test/layer.zip/layer/zips.zip/zips/theresult.txt"
				.replace('/', '\\');
		InputStream is = LUtils.getInputStream(path);
		System.err.println("Got " + is + " for " + path);
		if (is == null) {
			System.err.println("No InputStream, no data?");
			return;
		}
		System.err.println("The result text file contains this message:");
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] data = new byte[bis.available()];
		bis.read(data);
		System.err.println(new String(data));
		is.close();
	}
}
