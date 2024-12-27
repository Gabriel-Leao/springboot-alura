package br.com.alura.screenMatch.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> tClass);
}
