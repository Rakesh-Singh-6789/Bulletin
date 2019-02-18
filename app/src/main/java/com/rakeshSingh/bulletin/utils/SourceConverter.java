package com.rakeshSingh.bulletin.utils;

import com.rakeshSingh.bulletin.models.Source;

import androidx.room.TypeConverter;

public class SourceConverter {
    @TypeConverter
    public static String toString(Source source) {
        return source.getName() + "-!-" + source.getId();
    }

    @TypeConverter
    public static Source toSource(String data) {
        String name = data.split("-!-")[0];
        String id = data.split("-!-")[1];
        return new Source(id, name);
    }
}
