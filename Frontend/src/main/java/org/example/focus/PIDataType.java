package org.example.focus;

import java.util.ArrayList;
import java.util.List;

//Abstract class to store PIData to be extended for specific data
public abstract class PIDataType {

    ArrayList<PIData> data = new ArrayList<>();
    String name;

    public PIDataType(String name){
        this.name = name;
    }

    public String getName() { return  name; }

    public void addItem(PIData item){
        data.add(item);
    }

    public void removeItem(PIData item){
        data.remove(item);
    }

    public List<PIData> getData(){
        return data;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(STR."\{name}:\n");
        for (PIData item : this.data){
            output.append(item.toString());
        }
        return output.toString();
    }
}
