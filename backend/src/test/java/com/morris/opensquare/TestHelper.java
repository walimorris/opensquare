package com.morris.opensquare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.api.services.customsearch.model.Result;
import com.morris.opensquare.models.DropDownOptions;
import com.morris.opensquare.models.digitalfootprints.NSLookupFootPrint;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The TestHelper class exposes various methods used to help in Unit Tests. Some methods include: converting
 * objects, pulling objects from the file system, String methods, parsing JSON files, and reading files.
 */
public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestHelper.class);

    private static final String ORG_1 = "Graphika";
    private static final String ORG_2 = "RAND Organization";
    private static final String ORG_3 = "Amazon Web Services";
    private static final String PROFESSION_1 = "Software Engineer";
    private static final String PROFESSION_2 = "Data Scientist";
    private static final String PROFESSION_3 = "Intelligence Analyst";
    private static final String AGE_RANGE_1 = "18-25";
    private static final String AGE_RANGE_2 = "26-35";
    private static final String AGE_RANGE_3 = "36-45";
    private static final String AGE_RANGE_4 = "46-55";

    // TODO: add testhelper global context
    private TestHelper() {}

    /**
     * More complicated objects such as objects that are based on a List or Map collection
     * require more class and type checks in order to convert them to its reference type for
     * the object mapper that will convert them. Note: such objects will need to be type cast
     * from the calling client.
     * <br><br>
     * <pre>{@code
     * List<Result> resultsList = (List<Result>) TestHelper.convertModelFromFile(BACK_LINKS_RESULT_JSON, List.class, Result.class);
     * }
     * </pre>
     *
     * @param fileName file path to object json
     * @param clazz the collection class
     * @param type the object type in the collection
     *
     * @return {@link Object}
     *
     * @throws IOException exception
     */
    public static Object convertModelFromFile(String fileName, Class<?> clazz, Class<?> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (clazz.isAssignableFrom(List.class)) {
            if (type == NSLookupFootPrint.class) {
                TypeReference<List<NSLookupFootPrint>> reference = new TypeReference<>() {};
                return mapper.readValue(new File(fileName), reference);
            }
            if (type == Result.class) {
                TypeReference<List<Result>> reference = new TypeReference<>() {};
                return mapper.readValue(new File(fileName), reference);
            }
            if (type == YouTubeTranscribeSegment.class) {
                TypeReference<List<YouTubeTranscribeSegment>> reference = new TypeReference<>() {};
                return mapper.readValue(new File(fileName), reference);
            }
        }
        return mapper.readValue(new File(fileName), clazz);
    }

    /**
     * Converts an object located in a file (in json format) to it's Java Object type.
     * Note: This requires a cast from the calling client.
     *
     * @param fileName filepath to object json
     * @param clazz class
     *
     * @return T
     *
     * @throws IOException exception
     */
    public static <T> T convertModelFromFile(String fileName, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(new File(fileName), clazz);
    }

    /**
     * Writes object value to json using {@link ObjectMapper}.
     *
     * @param object {@link Object}
     *
     * @return {@link JSONObject}
     */
    public static JSONObject getJSONFromObject(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(object, JSONObject.class);
    }

    /**
     * Writes object value to string using {@link ObjectMapper}.
     *
     * @param object {@link Object}
     *
     * @return {@link String}
     * @throws JsonProcessingException processing error
     */
    public static String writeValueAsString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Get {@link BufferedReader} from file.
     *
     * @param filePath path to file
     *
     * @return {@link BufferedReader}
     */
    public static BufferedReader getBufferedReaderFromTxtFile(String filePath) {
        try {
            return new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public static String asJSONString(Object object) throws JsonProcessingException {
        if (object != null) {
            return writeValueAsString(object);
        }
        return null;
    }

    public static DropDownOptions getDropDownOptions() {
        return new DropDownOptions.Builder()
                .ages(getAgeRanges())
                .professions(getProfessions())
                .organizations(getOrganizations())
                .build();
    }

    public static Map<String, Object> getYouTubeVideoObjectIdAndPublishDateMap() {
        Map<String, Object> tertiaryMap = new HashMap<>();

        // create date
        Date date = new Date();
        ObjectId objectIdDate = new ObjectId(date);
        tertiaryMap.put("_id", objectIdDate);

        // create publishDate
        String str = "1986-04-08T12:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        tertiaryMap.put("publishDate", dateTime);

        return tertiaryMap;
    }

    private static List<String> getOrganizations() {
        List<String> organizationsList = new ArrayList<>();
        organizationsList.add(ORG_1);
        organizationsList.add(ORG_2);
        organizationsList.add(ORG_3);
        return organizationsList;
    }

    private static List<String> getProfessions() {
        List<String> professionsList = new ArrayList<>();
        professionsList.add(PROFESSION_1);
        professionsList.add(PROFESSION_2);
        professionsList.add(PROFESSION_3);
        return professionsList;
    }

    private static List<String> getAgeRanges() {
        List<String> ageRangesList = new ArrayList<>();
        ageRangesList.add(AGE_RANGE_1);
        ageRangesList.add(AGE_RANGE_2);
        ageRangesList.add(AGE_RANGE_3);
        ageRangesList.add(AGE_RANGE_4);
        return ageRangesList;
    }
}
