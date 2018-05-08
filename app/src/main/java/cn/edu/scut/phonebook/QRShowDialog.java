package cn.edu.scut.phonebook;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class QRShowDialog extends Dialog {
    public QRShowDialog(@NonNull Context context) {
        super(context);
    }
    public QRShowDialog(Context context, int theme){
        super(context,theme);
    }
    public static class Builder{
        private Context context;
        private Bitmap bitmap;
        public Builder(Context context){
            this.context = context;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
        public void setBitmap(Bitmap bitmap){
            this.bitmap = bitmap;
        }
        public QRShowDialog create(){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final QRShowDialog dialog = new QRShowDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.qr_show_dialog,null);
            dialog.addContentView(layout,new WindowManager.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            ImageView imageView = (ImageView) layout.findViewById(R.id.img_qrcode);
            imageView.setImageBitmap(getBitmap());
            return dialog;
        }
    }
}
