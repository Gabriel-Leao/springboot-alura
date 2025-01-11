package br.com.alura.screenMatch.service.translation;

import br.com.alura.screenMatch.service.ApiConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class QueryMyMemory {
    public static String getTranslation(String text) {
        ObjectMapper mapper = new ObjectMapper();

        ApiConsumer consumer = new ApiConsumer();

        String encodedText = URLEncoder.encode(text);
        String langPair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=" + langPair;

        String json = consumer.getData(url);

        TranslationData translation = null;
        try {
            translation = mapper.readValue(json, TranslationData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return translation.responseData().translatedText();
    }
}
