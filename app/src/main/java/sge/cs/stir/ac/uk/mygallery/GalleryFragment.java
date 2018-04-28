package sge.cs.stir.ac.uk.mygallery;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.orm.SugarContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import static android.app.Activity.RESULT_OK;
import static sge.cs.stir.ac.uk.mygallery.MainActivity.INTENT_PICK_IMAGE;


public class GalleryFragment extends Fragment {

    Intent photoChooser;
    private GridView gridView;
    GridAdapter gridadapter;
    Context mcontext;
    Float Latitude, Longitude;
    private boolean valid = false;


    public GalleryFragment() {
        super();
    }
    /**
     * inflating the rootView and Assigning the gridview layout to the rootview
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SugarContext.init(getContext());
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        mcontext=getActivity().getApplicationContext();
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridadapter = new GridAdapter(rootView.getContext());
        gridView.setAdapter(gridadapter);
        return rootView;
    }
//
    /**
     * Method to start the Intent and send it to OnActivityforResult()
     */
    public void PostImageRequestIntent() {
        Log.wtf("GalFrag", "picking photo");
        if (photoChooser == null) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(intent.ACTION_GET_CONTENT);
            photoChooser = intent.createChooser(intent, "Complete action using?...........");
        }
        startActivityForResult(photoChooser, INTENT_PICK_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.wtf("GalFrag", "Activity on result");

        if (requestCode == INTENT_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            ImageDetails.deleteAll(ImageDetails.class);

            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //add function to display and send the data
            long currentTime = new Date().getTime();
            String imagename = "GALL_" + currentTime + ".jpg";
            String internalpath = saveImageToInternalStorage(bitmap, imagename).getPath();


//         Getting X(Longitude) and Y(Latitude) from EXIF and converting into degree format.
//         CODE REFERENCE:http://android-er.blogspot.co.uk/2010/01/convert-exif-gps-info-to-degree-format.html
//            String path = getPathForV19AndUp(this.context, selectedImage);

            try {
                ExifInterface exif = new ExifInterface(internalpath);
                // extract Exif tags
                // Assuming the image is a JPEG or supported raw format
                String TIMESTAMP = exif.getAttribute(ExifInterface.TAG_DATETIME);
                String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                if ((LATITUDE != null)
                        && (LATITUDE_REF != null)
                        && (LONGITUDE != null)
                        && (LONGITUDE_REF != null)) {

                    if (LATITUDE_REF.equals("N")) {
                        Latitude = convertToDegree(LATITUDE);
                    } else {
                        Latitude = 0 - convertToDegree(LATITUDE);
                    }

                    if (LONGITUDE_REF.equals("E")) {
                        Longitude = convertToDegree(LONGITUDE);
                    } else {
                        Longitude = 0 - convertToDegree(LONGITUDE);
                    }
                }

                if (saveImageToInternalStorage(bitmap, imagename) != null) {
                    ImageDetails imageDetails = new ImageDetails(imagename, internalpath, currentTime, Latitude, Longitude);
                    imageDetails.save();
                    gridadapter.notifyDataSetChanged();
                    gridView.setAdapter(gridadapter);
                    Log.i("Image Saved: Path:", internalpath + "" + Latitude + "" + Longitude +TIMESTAMP);
                } else
                    return;
            } catch (IOException e) {
                e.printStackTrace();
            }
//            gridadapter.notifyDataSetChanged();
//            gridView.invalidateViews();
//            gridView.setAdapter(gridadapter);

        }
    }
    /**
     * Saves the image to Application storage.
     * @param image
     * @param filename
     * @return mypath
     */
    public File saveImageToInternalStorage(Bitmap image, String filename) {
        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());

        File directory = cw.getDir("imageDirectory", Context.MODE_PRIVATE);
//        Create ImageDirectory
        File mypath = new File(directory, filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
//            Using Compress method on the Bitmap to shrink imagesize() and write the image to the output
            // 100 means no compression, the lower you go, the stronger the compression
            //All EXIF METADATA lost because of compression
            image.compress(Bitmap.CompressFormat.JPEG, 70, fos);
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Log.i("ABSOLUTEPATH", directory.getAbsolutePath() + filename);
        return mypath;

    }

    /**
     * Converts DMS to Degree
     * @param stringDMS
     * @return result
     */
    private Float convertToDegree(String stringDMS) {
        Float result;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;


    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return (String.valueOf(Latitude)
                + ", "
                + String.valueOf(Longitude));
    }

    public int getLatitudeE6() {
        return (int) (Latitude * 1000000);
    }

    public int getLongitudeE6() {
        return (int) (Longitude * 1000000);
    }


//Tried to get real Path from Uri to get EXIF data was not able to find the best solution for my device.

//    /**
//     * Handles pre V19 uri's
//     * @param context
//     * @param contentUri
//     * @return
//     */
//    public static String getPathForPreV19(Context context, Uri contentUri) {
//        String res = null;
//
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
//        if(cursor.moveToFirst()){;
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//
//        return res;
//    }
//
//    /**
//     * Handles V19 and up uri's
//     * @param context
//     * @param contentUri
//     * @return path
//     */
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public static String getPathForV19AndUp(Context context, Uri contentUri) {
//        String wholeID = DocumentsContract.getDocumentId(contentUri);
//
//        // Split at colon, use second item in the array
//        String id = wholeID.split(":")[1];
//        String[] column = { MediaStore.Images.Media.DATA };
//
//        // where id is equal to
//        String sel = MediaStore.Images.Media._ID + "=?";
//        Cursor cursor = context.getContentResolver().
//                query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
//                        column, sel, new String[]{ id }, null);
//
//        String filePath = "";
//        int columnIndex = cursor.getColumnIndex(column[0]);
//        if (cursor.moveToFirst()) {
//            filePath = cursor.getString(columnIndex);
//        }
//
//        cursor.close();
//        return filePath;
//    }
//
//    public static String getRealPathFromURI(Context context,
//                                            Uri contentUri) {
//        String uriString = String.valueOf(contentUri);
//        boolean goForKitKat= uriString.contains("com.android.providers");
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && goForKitKat) {
//            Log.i("KIKAT","YES");
//            return getPathForV19AndUp(context, contentUri);
//        } else {
//
//            return getPathForPreV19(context, contentUri);
//        }
//    }
}
