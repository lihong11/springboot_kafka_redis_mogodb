package uyun.sync.util;
import com.mongodb.*;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;

public class MongodbUtil {
    private static MongoClient mongoClient = null;

    public static void main(String[] args){
//        MongoClient mongoClient = MongodbUtil.connectMongoDB();
//        System.out.println(mongoClient);
//        MongoDatabase database = mongoClient.getDatabase( "alert");
//        MongoCollection<Document> collection = database.getCollection("IncidentLog");
//        Document document = collection.find(eq("incidentId", "5c625fee8212a03135cb9405")).sort(descending("operateTime")).first();

        try {
            MongoClient mongoClient = MongodbUtil.connectMongoDB();
            DB database = mongoClient.getDB( "alert");
            DBCollection collection = database.getCollection("IncidentLog");
            BasicDBObject filter_dbobject = new BasicDBObject();
//            filter_dbobject.put("incidentId", "5c625fee8212a03135cb9405");
            DBCursor cursor = collection.find(filter_dbobject).sort(new BasicDBObject("operateTime", -1)).limit(1);
            DBObject object = null;
            try{
                if(cursor.hasNext()){
                    object = cursor.next();
                }else{
                   System.out.println("123");
                }
            }finally{
                if(cursor != null){
                    cursor.close();
                }
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(((BasicDBObject)object).getDate("operateTime"));
            calendar.add(Calendar.HOUR_OF_DAY, -8);

            System.out.println(calendar.getTime());
            System.out.println(String.valueOf(((BasicDBObject)object).get("operatorName")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 连接数据库 Mongodb3.X版本写法
     */
//    public static MongoClient connectMongoDB(){
//        if(mongoClient != null){
//            return mongoClient;
//        }
//        //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
//        //ServerAddress()两个参数分别为 服务器地址 和 端口
//        ServerAddress serverAddress = new ServerAddress(Config.getInstance().get("kafka.mongodb.host", "localhost"),Config.getInstance().get("kafka.mongodb.port", 27017));
//        List<ServerAddress> addrs = new ArrayList<>();
//        addrs.add(serverAddress);
//
//        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
//        MongoCredential credential = MongoCredential.createScramSha1Credential(
//                Config.getInstance().get("kafka.mongodb.userName", "root"),
//                Config.getInstance().get("kafka.mongodb.source", "admin"),
//                Config.getInstance().get("kafka.mongodb.password", "Root_123").toCharArray());
//        List<MongoCredential> credentials = new ArrayList<>();
//        credentials.add(credential);
//
//        //通过连接认证获取MongoDB连接
//        return new MongoClient(addrs,credentials);
//    }


    /**
     * 连接数据库 Mongodb2.X版本写法
     */
    public static MongoClient connectMongoDB() throws UnknownHostException {
        if(mongoClient != null){
            return mongoClient;
        }

//        MongoCredential credential = MongoCredential.createCredential(
//                Config.getInstance().get("kafka.mongodb.userName", "root"),
//                Config.getInstance().get("kafka.mongodb.source", "admin"),
//                Config.getInstance().get("kafka.mongodb.password", "Root_123").toCharArray()
//        );

//        ServerAddress serverAddress = new ServerAddress(Config.getInstance().get("kafka.mongodb.host", "192.168.0.61"),Config.getInstance().get("kafka.mongodb.port", 27017));


//        MongoCredential credential = MongoCredential.createCredential("root", "admin", "Root_123".toCharArray());
//        ServerAddress serverAddress = new ServerAddress("192.168.0.61",27017);

//        return new MongoClient(serverAddress, Arrays.asList(credential));
        return null;
    }

    /**
     * 关闭服务与mongodb建立的连接
     */
    public static void closeConnection(){
        if(mongoClient != null){
            mongoClient.close();
        }
    }
}
