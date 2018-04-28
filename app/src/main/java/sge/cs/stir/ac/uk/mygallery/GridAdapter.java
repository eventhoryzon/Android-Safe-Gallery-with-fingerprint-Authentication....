package sge.cs.stir.ac.uk.mygallery;

/**
 * Created by SHAZIB on 3/8/2017.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

//this is the adapter used to fit the grid view
//Adapter is acts as a bridge between data source and adapter views such as ListView, GridView.
//Adapter iterates through the data set from beginning till the end and generate Views for each item in the list.
public class GridAdapter extends BaseAdapter {

    private int layoutResourceId;
    private List<ImageDetails> ArrayofPaths=ImageDetails.listAll(ImageDetails.class);
    private String Image_Path;
    Context mcontext;


    public GridAdapter(Context context) {
        super();
        this.mcontext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {

//   Initialize some attributes
                imageView = new ImageView(mcontext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
            }

         else{
                imageView = (ImageView) convertView;
            }
        Log.i("DATABASE ENTITIES", ArrayofPaths.toString());

              Bitmap bitmap = decodeSampledBitmapFromUri(ArrayofPaths.get(position).getpath(), 300, 300);

//  Using the picasso Libarary was not able to load a bitmap to the imageview.
//              Picasso.with(mcontext)
//                      .load(ArrayofPaths.get(position).getpath())
//                      .placeholder(R.drawable.ic_switch_video_black_48dp)
//                      .into(imageView);

imageView.setImageBitmap(bitmap);
               return imageView;
    }

    /**
     * getting the position of the current item()
     * @param position
     * @return Object
     */
    @Override
    public Object getItem(int position) {
    return ArrayofPaths.get(position);
    }

    /**
     * getting position
     * @param position
     * @return long
     */
    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * getting the Count() from the Array.size() .
     * @return int
     */
    @Override
    public int getCount() {
        return ArrayofPaths.size();
    }

    /**
     * Calculates the size of the bitmap so as to get over memory exceptions.
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return Bitmap
     */
    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm ;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    /**
     * sets the dimensions on based on required height and width of the image
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return int
     */
    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }
}