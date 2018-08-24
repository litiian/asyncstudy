package com.flink.deserializationSchema;

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

/**
 * @author  TL
 * @param <T>
 */
public class ByteArrayDeserializationSchema<T> extends AbstractDeserializationSchema<byte[]>{
    @Override
    public byte[] deserialize(byte[] bytes) throws IOException {
        return bytes;
    }
}
