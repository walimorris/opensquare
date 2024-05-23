package com.morris.opensquare.models.converters;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

@WritingConverter
public class ObjectIdToStringConverter implements Converter<ObjectId, String> {

    @Override
    public String convert(@NonNull ObjectId source) {
        return source.toHexString();
    }
}
