package dao.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {

        if (localDate == null) {
            jsonWriter.value("null");
        }

        jsonWriter.value(localDate.format(dtf));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {

        final String text = jsonReader.nextString();
        if (text.equals("null")) {
            return null;
        }

        return LocalDateTime.parse(jsonReader.nextString(), dtf);
    }

}
