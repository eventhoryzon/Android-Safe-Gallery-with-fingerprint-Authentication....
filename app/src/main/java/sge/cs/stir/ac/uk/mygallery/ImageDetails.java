package sge.cs.stir.ac.uk.mygallery;
import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

/**
 * Created by SHAZIB on 3/21/2017.
 */


//Introduced the sugarORM library to store data in the database.

public class ImageDetails extends SugarRecord  {


    private String title;
    private String path;
    private long Datetime;
    private Float latitude;
    private Float longitude;


//ImageDetails Constructor
    /**
     * Creating a constructor ImageDetails.
     * @param  title
     * @param path
     * @param Datetime
     * @param latitude
     * @param longitude
     */
    public ImageDetails(String title, String path, long Datetime,Float latitude,Float longitude){
         this.title=title;
         this.path=path;
         this.Datetime=Datetime;
         this.latitude=latitude;
         this.longitude=longitude;

     }
     public ImageDetails(){}

    public String Gettitle(){
         return title;
     }
     public void setTitle(String title){
         this.title=title;
     }

     public String getpath(){
         return path;
     }
     public void setpath(String path){
         this.path=path;
     }
     public long getDateTime(long Datetime){
         return Datetime;
     }
     public void setDateTime(long Datetime){
         this.Datetime=Datetime;
     }
     public Float getLatitude(){
         return latitude;
     }
     public void setLatitude(Float latitude){
         this.latitude=latitude;
     }
    public Float getLongitude(){
        return longitude;
    }
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
