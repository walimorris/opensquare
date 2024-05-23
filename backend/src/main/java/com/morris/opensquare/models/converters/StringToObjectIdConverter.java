package com.morris.opensquare.models.converters;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

@ReadingConverter
public class StringToObjectIdConverter implements Converter<String, ObjectId> {

    @Override
    public ObjectId convert(@NonNull String source) {
        return new ObjectId(source);
    }
}
