package magicstudio.luckystar;

import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * User: zhou_xiaodan
 * Date: 2004-5-22
 * Time: 21:19:39
 */
public class Alternative {
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int weight;
    private String description;

    public void saveToStream(DataOutputStream outputStream) throws IOException{
        outputStream.writeInt(weight);
        outputStream.writeUTF(description);
    }

    public static Alternative createFromStream(DataInputStream inputStream) throws IOException {
        int weight = inputStream.readInt();
        String description = inputStream.readUTF();
        return new Alternative(weight, description);
    }

    public Alternative() {
        this(1, "");
    }

    public Alternative(String description) {
        this(1, description);
    }

    public Alternative(int weight, String description) {
        setWeight(weight);
        setDescription(description);
    }
}
