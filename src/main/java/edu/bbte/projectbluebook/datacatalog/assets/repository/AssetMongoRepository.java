package edu.bbte.projectbluebook.datacatalog.assets.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class AssetMongoRepository {

    private static String connection = "mongodb+srv://m001-student:m001-mongodb-basics"
            + "@cluster0.dlhll.mongodb.net/DataCatalog?retryWrites=true&w=majority";
    private static MongoClientURI uri = new MongoClientURI(connection);
    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("DataCatalog");
    private static MongoCollection<Document> assets = database.getCollection("Assets");

    public boolean insert(Document asset) {
        try {
            Date currentTime = new Date();
            asset.append("createdAt", currentTime);
            asset.append("updatedAt", currentTime);
            assets.insertOne(asset);
        } catch (MongoException e) {
            return false;
        }
        return true;
    }

    public Document delete(Document id) {
        return assets.findOneAndDelete(id);
    }

    public Document findOne(Document id) {
        return assets.find(id).first();
    }

    public Document findAndUpdate(Document filter, Document update) {
        return assets.findOneAndUpdate(filter, update);
    }

    public Document findAndUpdateMark(Document filter, Document update) {
        update.append("$currentDate", new Document("updatedAt", true));
        return assets.findOneAndUpdate(filter, update);
    }

    public boolean update(Document id, Document update) {
        try {
            assets.updateOne(id, update);
        } catch (MongoException e) {
            return false;
        }
        return true;
    }

    public FindIterable<Document> findByVisited(Document filter) {
        return assets.find(filter).sort(new Document("visited", - 1));
    }
}
