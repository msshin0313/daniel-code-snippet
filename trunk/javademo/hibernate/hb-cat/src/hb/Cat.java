package hb;

import net.sf.hibernate.Validatable;
import net.sf.hibernate.ValidationFailure;

import java.util.Set;

public class Cat implements Validatable {

    private String id;
    private String name;
    private char sex;
    private float weight;

    public Set getMiceBread() {
        return miceBread;
    }

    public void setMiceBread(Set miceBread) {
        this.miceBread = miceBread;
    }

    private Set miceBread;

    public Cat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void validate() throws ValidationFailure {
        if (sex=='f' && weight>60) throw new ValidationFailure("A female cat can weigh more than 60");
    }

}
