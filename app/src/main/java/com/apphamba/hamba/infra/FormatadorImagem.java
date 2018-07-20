package com.apphamba.hamba.infra;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class FormatadorImagem {

    public byte[] getFotoByte(int idResource) {
        byte[] fotoByte = this.bitMapParaByte(gerarBitMap(idResource));
        return fotoByte;
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    private Bitmap gerarBitMap(int idResource) {
        Bitmap bitmap = BitmapFactory.decodeResource(HambaApp.getContext().getResources(), idResource, new BitmapFactory.Options());
        return bitmap;
    }

    private byte[] bitMapParaByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte imagemBytes[] = stream.toByteArray();
        return imagemBytes;
    }

}
