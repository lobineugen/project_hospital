package com.sumdu.hospital.service;

import com.sumdu.hospital.model.CheckBoxValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckBoxHelper {
    public String parseToString(List<CheckBoxValue> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CheckBoxValue checkBoxValue : list) {
            stringBuilder.append("[");
            if (checkBoxValue.getId().startsWith("c")) {
                stringBuilder.append(checkBoxValue.getId()).append(":").append(checkBoxValue.getLabel());
            } else {
                stringBuilder.append(checkBoxValue.getId());
            }
            stringBuilder.append(":").append(checkBoxValue.isValue()).append("],");
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }


    private Map<String, String> parseFromString(String value) {
        Map<String, String> map = new HashMap<>();
        if (value == null){
            return map;
        }
        String[] arrays = value.split(",");
        for (String b : arrays) {
            map.put(b.substring(b.indexOf("[") + 1, b.indexOf(":")), b.substring(b.indexOf(":") + 1, b.lastIndexOf("]")));
        }
        return map;
    }

    public ObservableList<CheckBoxValue> initListView(String data, Map<String,String> fields) {
        Map<String, String> map = parseFromString(data);
        ObservableList<CheckBoxValue> checkBoxValueList = FXCollections.observableArrayList();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            CheckBoxValue checkBoxValue = new CheckBoxValue(entry.getValue(), entry.getKey());
            if (map.get(entry.getKey()) != null) {
                checkBoxValue.setValue(Boolean.parseBoolean(map.get(entry.getKey())));
            }
            checkBoxValueList.add(checkBoxValue);
        }
        // Add custom check boxes
        for (int i = 1; i <= 3; i++) {
            String id = "c" + i;
            CheckBoxValue checkBoxValue = new CheckBoxValue("", id);
            if (map.get(id) != null) {
                String value = map.get(id);
                checkBoxValue.setLabel(value.substring(0, value.lastIndexOf(":")));
                checkBoxValue.setValue(Boolean.parseBoolean(value.substring(value.lastIndexOf(":") + 1, value.length())));
            }
            checkBoxValueList.add(checkBoxValue);
        }
        return checkBoxValueList;
    }
}
