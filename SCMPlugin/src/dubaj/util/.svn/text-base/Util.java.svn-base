package dubaj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

public class Util {
	private static String readFile(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

	public static int contarQuantidadeLinhasArquivo(File file) throws Exception {
		try {
			String fileContent = readFile(file);
			return fileContent.split("\n").length;
		} catch (Exception ex) {
			return 0;
		}
	}

	public static double roundTwoDecimals(double d) {
		return roundToDecimals(d, 2);
	}

	public static double roundToDecimals(double d, int c) {
		int temp = (int) ((d * Math.pow(10, c)));
		return (((double) temp) / Math.pow(10, c));
	}
}
