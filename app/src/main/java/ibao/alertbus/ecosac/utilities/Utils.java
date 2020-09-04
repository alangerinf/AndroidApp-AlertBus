package ibao.alertbus.ecosac.utilities;

import android.graphics.Bitmap;
import android.text.format.DateFormat;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static android.graphics.Color.BLACK;



public class Utils {

    public static String getFecha() {
        DateFormat df = new DateFormat();
        return df.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
    }

    public static String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    public static String compress(String str) throws IOException
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            try (GZIPOutputStream gzip = new GZIPOutputStream(out))
            {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            }
            //return new String(out.toByteArray(),"sd");
            return new String((out.toByteArray()), "ISO-8859-1");
            //return out.toString(StandardCharsets.ISO_8859_1);
            // Some single byte encoding
        }
    }

    public static String decompress(String comprimido) throws IOException
    {
        byte[] str = null;
        str = comprimido.getBytes("ISO-8859-1");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str)))
        {
            int b;
            while ((b = gis.read()) != -1) {
                baos.write((byte) b);
            }
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;

    }

}
