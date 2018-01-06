package com.daksoftwareproducts.kevin.wso;

/**
 * Created by David on 8/17/2017.
 */

public class Profile_Item {

    private String url;
    private String name;
    private String unix;
    private String email;
    private String suBox;
    private String address = "";
    private String tags;
//    private Bitmap pic;

    public String getUrl() {
        return url;
    }
    public void setUrl( String url ) {
        this.url = url;
    }
    public String getName(){
        return name;
    }
    public void setName( String name ){
        this.name = name;
    }
    public String getUnix() {
        return unix;
    }
    public void setUnix( String unix ) {
        this.unix = unix;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail( String email ) {
        this.email = email;
    }
    public String getSuBox(){
        return suBox;
    }
    public void setSuBox( String suBox ){
        this.suBox = suBox;
    }
    public String getAddress(){
        return address;
    }
    public void setAddress( String address ){
        this.address = address;
    }
    public String getTags(){
        return tags;
    }
    public void setTags( String tags ){
        this.tags = tags;
    }
//    public Bitmap getPic() {
//        return pic;
//    }
//    public void setPic( String url ) throws IOException {
//
//        URL obj = new URL(url);
//        HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
//        HttpsObject httpsObject = httpsObject.openConnection(connection,"GET",referer);
//
//        // This is what we want to parse
//        //System.out.println( httpsObject.printResponse(connection) );
//        html = httpsObject.printResponse(connection);
//
//        //connection.disconnect();
//
//        Document doc = Jsoup.parse(html);
//
//        Elements picInfo = doc.getElementsByClass("picture");
//        String pic = picInfo.first().child(0).toString();
//        String picLink = pic.substring(pic.indexOf("\"") + 1, pic.indexOf("\"", pic.indexOf("\"") + 1));
//
//        URL picUrl = new URL("https://wso.williams.edu" + url);
//        HttpURLConnection picConnection = (HttpURLConnection) picUrl.openConnection();
//        picConnection.setDoInput(true);
//        picConnection.connect();
//        InputStream input = picConnection.getInputStream();
//        this.pic = BitmapFactory.decodeStream(input);
//    }

    public String getToString(){
        return "Url: "+getUrl()+"\n"+"Name: "+getName()+"\n"+"Unix: "+getUnix()+"\n"+"Email: "+getEmail()+"\n"+"Address: "+getAddress();
    }

}
