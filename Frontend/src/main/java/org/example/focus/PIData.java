package org.example.focus;

import java.util.ArrayList;
import java.util.List;

//Abstract class for generic PIData to be extended for specific data
public abstract class PIData<T> {

    ArrayList<T> data = new ArrayList<>();
    String name;

    public PIData(String name){
        this.name = name;
    }

    public String getName() { return  name; }

    public void addItem(T item){
        data.add(item);
    }

    public void removeItem(T item){
        data.remove(item);
    }

    public List<T> getData(){
        return data;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(STR."\{name}:\n");
        for (T item : this.data){
            output.append(item.toString());
        }
        return output.toString();
    }
}
