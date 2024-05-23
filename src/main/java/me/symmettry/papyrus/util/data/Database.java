package me.symmettry.papyrus.util.data;

import com.ibm.icu.impl.locale.XCldrStub;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.symmettry.papyrus.Papyrus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

@UtilityClass
public class Database {

    private final File file = new File(Papyrus.inst.pluginPath + "data.json");
    private JSONObject data;

    @SneakyThrows
    public void saveDatabase() {
        try(final FileWriter writer = new FileWriter(file)) {
            writer.write(data.toString(4));
        }
    }

    public void loadDatabase() throws IOException {
        if(!file.exists()) {
            file.createNewFile();
            generateEmptyDatabase();
            return;
        }
        final String json = XCldrStub.join(Files.readAllLines(file.toPath()), "\n");
        try {
            data = new JSONObject(json);
            if(!json.equals("{}")) {
                final File fileBackup = new File(Papyrus.inst.pluginPath + "/backups/data_" + now() + ".json");
                fileBackup.createNewFile();
                try (final FileWriter writer = new FileWriter(fileBackup)) {
                    writer.write(json);
                }
            }
        } catch (JSONException e) {
            Papyrus.LOGGER.severe("An error occurred while loading the database, an empty one will be loaded and a backup of the corrupted one will be written.");
            e.printStackTrace();
            final File fileBackup = new File(Papyrus.inst.pluginPath + "/corrupted/data_" + now() + ".json");
            fileBackup.createNewFile();
            try(final FileWriter writer = new FileWriter(fileBackup)) {
                writer.write(json);
            }
            generateEmptyDatabase();
        }
    }

    private String now() {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss"));
    }

    private void generateEmptyDatabase() throws IOException {
        data = new JSONObject("{}");
        try(final FileWriter writer = new FileWriter(file)) {
            writer.write("{}");
        }
    }

    public <T> T saveData(final String id, final T value) {
        data.put(id, value);
        return value;
    }

    public Object getData(final String id, final Object def) {
        return data.has(id) ? data.get(id) : def;
    }

}
