package com.yalco.chatapat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectConverter {

    private ObjectConverter () {

    }

    private static final Gson gson = new Gson();
    private static final ModelMapper mapper = new ModelMapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <S, T>T convertObject(S object, Class<T> targetClass) {
        return mapper.map(object, targetClass);
    }

    public static <S, T> List<T> convertList(List<S> list, Class<T> targetClass) {
        List<T> targetList = new ArrayList<>();
        list.forEach(elem -> targetList.add(convertObject(elem, targetClass)));
        return targetList;
    }

    public static <T> String convertObjectToString(T object) {
        return gson.toJson(object);
    }

}
