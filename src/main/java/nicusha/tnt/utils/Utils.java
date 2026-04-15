package nicusha.tnt.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import nicusha.tnt.data.ModDamageTypeProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static DamageSource nukeDamageSource(Level level, Entity nuke) {
        var registry = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        var nukeType = registry.get(ModDamageTypeProvider.NUKE).orElseGet(() -> registry.getOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC));
        return new DamageSource(nukeType, nuke);
    }

    public static String translate(String langTo, String text) {
        try {
            String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=" + langTo + "&dt=t&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8);
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) response.append(inputLine);
            in.close();
            String result = response.toString();
            return result.substring(4, result.indexOf("\"", 4));
        } catch (Exception e) {
            return text;
        }
    }
}
