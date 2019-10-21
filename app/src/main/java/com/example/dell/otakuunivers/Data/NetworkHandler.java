package com.example.dell.otakuunivers.Data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.dell.otakuunivers.Activities.AnimeActivity;
import com.example.dell.otakuunivers.Activities.CharacterActivity;
import com.example.dell.otakuunivers.Activities.EpisodesActivity;
import com.example.dell.otakuunivers.Activities.MangaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NetworkHandler  {

    public NetworkHandler(){}

    public static ArrayList<AnimeInfo> fetchDatafromServer(String link, Context context) throws MalformedURLException

    {
        String json = null;
        ArrayList <AnimeInfo> info ;
        URL url = new URL(link);
        try {
            json = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        info = extractDataFromJson(json,context);
        return info;
    }
    
    public static ArrayList<String> fetchDatafromSingleLink(String attribute,String link) throws MalformedURLException, JSONException {
        String json = null;
        ArrayList <String> info ;
        URL url = new URL(link);
        try {
            json = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (attribute == "company")
        {
            info = extractDataFromSubCategory(attribute,json);
        }

       else if (attribute == "staffInfo")
        {
            info = extractDataFromSubCategory(attribute,json);

        }
        else if (attribute == "character")
        {
            info = extractDataFromSubCategory(attribute,json);

        }
        else
        {
            info = extractDataFromJsonSingleLink(attribute,json);
        }

        return info;
    }

    public static ArrayList<AnimeInfo> fetchDataFromStaffLink(String link) throws MalformedURLException {
        String json = null;
        ArrayList<AnimeInfo> info ;
        URL url = new URL(link);
        try {
            json = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

                info = extractDataFromStaffLink(json);



        return info;

    }





    private static String makeHttpRequest(URL link) throws IOException

    {

        HttpsURLConnection httpsURLConnection = null;
        InputStream inputStream = null;
        String json = null;
        try {
            httpsURLConnection = (HttpsURLConnection) link.openConnection();
            httpsURLConnection.setConnectTimeout(10000);
            httpsURLConnection.setReadTimeout(4000);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();

            if(httpsURLConnection.getResponseCode() == 200)
            {
                inputStream = httpsURLConnection.getInputStream();
                json = inputStreamToStringConverter(inputStream);


            }
            else
            {
                int response = httpsURLConnection.getResponseCode();

            }
        }
        catch (IOException e)
        {
            Log.e("CONNECTION ERROR CODE:", String.valueOf(httpsURLConnection.getResponseCode()));
        }
        finally {
            if(link!=null)
            {
                httpsURLConnection.disconnect();
            }
            if (inputStream!=null)
            {
                inputStream.close();
            }
        }
        return json;

    }

    private static String inputStreamToStringConverter(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader ;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader;
        String json ;
      if(inputStream!= null)
      {
          inputStreamReader  = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
          bufferedReader = new BufferedReader(inputStreamReader);
          json = bufferedReader.readLine();
          while (json!=null)
          {
              stringBuilder.append(json);
              json=bufferedReader.readLine();

          }
      }


        return stringBuilder.toString();

    }

    private static ArrayList<AnimeInfo> extractDataFromStaffLink(String json) throws MalformedURLException {

        ArrayList<AnimeInfo> staff = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray data = root.getJSONArray("data");
            if (data.length() != 0) {

                for (int i = 0; i < data.length(); i++) {
                    JSONObject elements = data.getJSONObject(i);
                    String id = elements.getString("id");
                    String type = elements.getString("type");

                    if(type.equals("mediaCharacters"))
                    {
                        JSONObject attributes = elements.getJSONObject("attributes");
                        String role = hasAValue(attributes, "role");
                        String staffLink = "https://kitsu.io/api/edge/media-characters/"+id+"/character";
                        List<String> staffInfo = fetchDatafromSingleLink("character", staffLink);
                        staff.add(new AnimeInfo(id,staffInfo.get(0),"","", staffInfo.get(3),"",staffInfo.get(1),"",
                                "","","","","","","",
                                "", role,"", staffInfo.get(2),""));
                    }
                    else
                    {
                        JSONObject attributes = elements.getJSONObject("attributes");
                        String role = hasAValue(attributes, "role");
                        String staffLink = "https://kitsu.io/api/edge/media-staff/" + id + "/person";
                        List<String> staffInfo = fetchDatafromSingleLink("staffInfo", staffLink);

                        staff.add(new AnimeInfo(id,staffInfo.get(0),"","","","",staffInfo.get(1),"",
                                "","","","","","","",
                                "", role,"", "",""));

                    }


                }
            }
            else
            {
                /* set a flag to tell the other activity that there is no staff info */
                Log.e("STAFF INFO WARNING ", " NO INFO FOUND");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("STAFF JSON ERROR", "ERROR FETCHING JSON DATA OR JSON IS INVALID");
        }
        return staff;
    }

    private static ArrayList<String> extractDataFromJsonSingleLink(String attribute,String json) throws JSONException, MalformedURLException {

        ArrayList<String> jsonData = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray data = root.getJSONArray("data");
            /*Anime Json*/
            if (attribute == "genres") {

                for (int i = 0; i < data.length(); i++) {

                    // ATTRIBUTES attributes;
                    JSONObject elements = data.getJSONObject(i);
                    JSONObject attributes = elements.getJSONObject("attributes");
                    String name = attributes.getString("name");

                    jsonData.add(name);
                }
            }
            if (attribute == "production")
            {
                String companyName;
                if (data.length()!=0) {


                    for (int i = 0; i < data.length(); i++) {

                        JSONObject elements = data.getJSONObject(i);
                        String id = elements.getString("id");
                        String productionLink = "https://kitsu.io/api/edge/media-productions/" + id + "/company";
                        List<String> companyList = fetchDatafromSingleLink("company", productionLink);
                        companyName = companyList.get(0);

                        jsonData.add(companyName);

                    }
                }
                else
                {
                    companyName = "Unknown";
                    jsonData.add(companyName);
                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.e("SINGLE LINK JSON ERROR", "ERROR FETCHING JSON DATA OR JSON IS INVALID");
        }

        return jsonData;
    }

    private static ArrayList<String> extractDataFromSubCategory(String attribute,String json)  {

        ArrayList<String> subCat_info = new ArrayList<>();
        String name  ;

        try {
            JSONObject root = new JSONObject(json);
            JSONObject dataOneObject = root.getJSONObject("data");

            if (attribute == "company")
            {

                // ATTRIBUTES attributes;
                JSONObject attributes = dataOneObject.getJSONObject("attributes");
                name = hasAValue(attributes,"name" );
                subCat_info.add(name);
            }

            if (attribute == "staffInfo")
            {
                // ATTRIBUTES attributes;
                JSONObject attributes = dataOneObject.getJSONObject("attributes");
                name = hasAValue(attributes,"name" );
                JSONObject imageObject = hasAJsonObject(attributes, "image");
                String imageLink = hasAValue(imageObject, "original");
                subCat_info.add(name);
                subCat_info.add(imageLink);

            }
            if (attribute == "character")
            {
                // ATTRIBUTES attributes;
                JSONObject attributes = dataOneObject.getJSONObject("attributes");
                String id = dataOneObject.getString("id");
                name = hasAValue(attributes,"name" );
                JSONObject imageObject = hasAJsonObject(attributes, "image");
                String imageLink = hasAValue(imageObject, "original");
                String description = hasAValue(attributes,"description" );

                // ATTRIBUTES relationship;
                JSONObject relationship = dataOneObject.getJSONObject("relationships");
                String mediaLink = "https://kitsu.io/api/edge/media-characters/"+id+"/media";


                subCat_info.add(name);
                subCat_info.add(imageLink);
                subCat_info.add(mediaLink);
                subCat_info.add(description);


            }

        }
        catch (JSONException e)
        {
            Log.e("COMPANY JSON ERROR", "ERROR FETCHING JSON DATA OR JSON IS INVALID");
        }



        return subCat_info;
    }

    private static ArrayList<AnimeInfo> extractDataFromJson(String json, Context context) throws MalformedURLException {
        ArrayList<AnimeInfo> jsonData = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray data = root.getJSONArray("data");

            /*Anime Json*/
            if (context.getClass() == AnimeActivity.class) {

              for (int i =0;i<data.length();i++) {

                    // ATTRIBUTES attributes;
                    JSONObject elements = data.getJSONObject(i);
                    JSONObject attributes = elements.getJSONObject("attributes");
                    String id = elements.getString("id");
                    JSONObject titles =  hasAJsonObject(attributes,"titles");
                    String title = hasAValue(titles,"en");
                    if(title=="")
                    {
                        title = hasAValue(titles,"en_jp");
                    }
                    String description = hasAValue(attributes,"synopsis");
                    String rating = hasAValue(attributes,"averageRating");
                    String startDate = hasAValue(attributes,"startDate");
                    String endDate = hasAValue(attributes,"endDate");
                    String ageRating = hasAValue(attributes,"ageRating");
                    String ageRatingGuide = hasAValue(attributes,"ageRatingGuide");
                    String status = hasAValue(attributes,"status");
                    String episodeCount = hasAValue(attributes,"episodeCount");
                    String trailer = hasAValue(attributes,"youtubeVideoId");
//                    String trailer = "https://www.youtube.com/watch?v="+trailerId;
                    String popularity = hasAValue(attributes,"popularityRank");
                    JSONObject posterImageObj = hasAJsonObject(attributes,"posterImage");
                    String posterImage = hasAValue(posterImageObj,"small");// depended on the size of imageView could be changed.
                    JSONObject coverImageObj = hasAJsonObject(attributes,"coverImage");
                    String coverImage = hasAValue(coverImageObj,"small");

                    // RELATIONSHIPS attributes;
                    JSONObject relationships = elements.getJSONObject("relationships");
//                    JSONObject genre = hasAJsonObject(relationships,"genres");
//                    JSONObject genreLinksObj = hasAJsonObject(genre,"links");

                    final String genreLink = "https://kitsu.io/api/edge/anime/"+id+"/genres";

                    List<String> genresList = fetchDatafromSingleLink("genres",genreLink );
                    String genres = listToString(genresList);


                    String charactersLink = "https://kitsu.io/api/edge/anime/"+id+"/characters";

                    String episodesLink = "https://kitsu.io/api/edge/anime/"+id+"/episodes?page[limit]=20&page[offset]=0";
                    //TODO we will add limit and offset variables to load more data if needed.

                    String streamingLink = "https://kitsu.io/api/edge/anime/"+id+"/streaming-links";
                    String productionLink = "https://kitsu.io/api/edge/anime/"+id+"/productions";
                    String staffLink = "https://kitsu.io/api/edge/anime/"+id+"/staff";

                    jsonData.add(new AnimeInfo(id,title, startDate, endDate, description, coverImage, posterImage, rating,
                            charactersLink, popularity, ageRating, ageRatingGuide, status, episodeCount, staffLink,
                            trailer, genres, episodesLink, streamingLink, productionLink));

                }
            }

            /*Manga Json*/
            if (context.getClass() == MangaActivity.class) {

                for (int i = 0; i < data.length(); i++) {
                    // ATTRIBUTES attributes;
                    JSONObject elements = data.getJSONObject(i);
                    JSONObject attributes = elements.getJSONObject("attributes");
                    String id = elements.getString("id");
                    JSONObject titles = hasAJsonObject(attributes, "titles");
                    String title = hasAValue(titles, "en");
                    if (title.equals("null"))
                    {
                        title = hasAValue(titles, "en_jp");
                    }
                    String description = hasAValue(attributes, "synopsis");
                    String rating = hasAValue(attributes, "averageRating");
                    String startDate = hasAValue(attributes, "startDate");
                    String endDate = hasAValue(attributes, "endDate");
                    String ageRating = hasAValue(attributes, "ageRating");
                    String ageRatingGuide = hasAValue(attributes, "ageRatingGuide");
                    String status = hasAValue(attributes, "status");
                    String volumeCount = hasAValue(attributes, "volumeCount");
                    String popularity = hasAValue(attributes, "popularityRank");
                    JSONObject posterImageObj = hasAJsonObject(attributes, "posterImage");
                    String posterImage = hasAValue(posterImageObj, "small");// depended on the size of imageView could be changed.
                    JSONObject coverImageObj = hasAJsonObject(attributes, "coverImage");
                    String coverImage = hasAValue(coverImageObj, "small");
                    String serialization = hasAValue(attributes, "serialization");

                    // RELATIONSHIPS attributes;
                    JSONObject relationships = elements.getJSONObject("relationships");

                    String genreLink = "https://kitsu.io/api/edge/manga/"+id+"/genres";
                    List<String> genresList = fetchDatafromSingleLink("genres",genreLink );
                    String genres = listToString(genresList);

//                    JSONObject characters = hasAJsonObject(relationships, "characters");
//                    JSONObject characterLinksObj = hasAJsonObject(characters, "links");
                    String charactersLink = "https://kitsu.io/api/edge/manga/"+id+"/characters";
//                    JSONObject chapters = hasAJsonObject(relationships, "chapters");
//                    JSONObject chaptersObject = hasAJsonObject(chapters, "links");
                    String chaptersLink = "https://kitsu.io/api/edge/manga/"+id+"/chapters";
//                    JSONObject staff = hasAJsonObject(relationships, "staff");
//                    JSONObject staffObject = hasAJsonObject(staff, "links");
                    String staffLink = "https://kitsu.io/api/edge/manga/"+id+"/staff";
//                    List<Staff> staffList = fetchDataFromStaffLink(staffLink);


                    jsonData.add(new AnimeInfo(id,title, startDate, endDate, description, coverImage, posterImage, rating,
                            charactersLink, popularity, ageRating, ageRatingGuide, status, volumeCount,
                            staffLink, "", genres, chaptersLink, "", serialization));

                }
            }
            /*Characters Json*/

            if (context.getClass() == CharacterActivity.class) {


                for (int i = 0; i < data.length(); i++) {
                    // ATTRIBUTES attributes;
                    JSONObject elements = data.getJSONObject(i);
                    JSONObject attributes = elements.getJSONObject("attributes");
                    String id = elements.getString("id");
                    JSONObject nameObject = hasAJsonObject(attributes, "names");
                    String name = hasAValue(nameObject, "en");
                    if (name == "") {
                        name = hasAValue(nameObject, "en_jp");
                    }
                    else
                    {
                        name = hasAValue(attributes, "name");
                    }

                    String description = hasAValue(attributes, "description");
                    JSONObject imageObj = attributes.getJSONObject("image");
                    String image = imageObj.getString("original");

                    jsonData.add(new AnimeInfo(id,name, "", "", description, "", image, "",
                            "", "", "", "", "", "",
                            null, "", "", "", "", ""));
                }
            }

            /*Episode Json*/

            if (context.getClass() == EpisodesActivity.class) {

                if(data.length()!=0) {
                    for (int i = 0; i < data.length(); i++) {
                        // ATTRIBUTES attributes;
                        JSONObject elements = data.getJSONObject(i);
                        JSONObject attributes = elements.getJSONObject("attributes");
                        String id = elements.getString("id");
                        String name = hasAValue(attributes, "canonicalTitle");
                        String description = hasAValue(attributes, "description");
                        String length = hasAValue(attributes, "length");
                        String number = hasAValue(attributes, "number");
                        String airDate = hasAValue(attributes, "airdate");
                        JSONObject imageObj = hasAJsonObject(attributes,"thumbnail" );
                        String image = hasAValue(imageObj,"original" );

                        jsonData.add(new AnimeInfo(id, name, airDate, "", description, "", image, number,
                                "", "", "", "", "", length,
                                null, "", "", "", "", ""));
                    }
                }
                else
                {
                    jsonData.add(new AnimeInfo("","","", "","", "","","",
                            "", "", "", "", "no data exist","",
                            null, "", "", "", "", ""));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON ERROR","ERROR FETCHING JSON DATA OR JSON IS INVALID");
        }
        return jsonData;

    }


    private static JSONObject hasAJsonObject(JSONObject obj, String objName) throws JSONException {
        if(!obj.isNull(objName))
        {

            return obj.getJSONObject(objName);
        }
        else
        {
            return null;
        }
    }
    private static String hasAValue(JSONObject object,String objName) throws JSONException
    {
       if (object!=null) {
           if (object.has(objName)) {
               if (object.getString(objName).equals(null))
               {
                   return "";
               }

               return object.getString(objName);
           }

           else
           {
               return "";
           }

       }
     return "";

    }
    public static String listToString(List<String> list)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i<list.size();i++)
        {
            stringBuilder.append(list.get(i)+", ");
        }
        return stringBuilder.toString();
    }
}
