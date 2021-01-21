package me.theredcat.lib.web.mojangapi;

import com.google.common.base.Preconditions;
import com.google.gson.JsonParser;
import me.theredcat.lib.web.HttpException;

import java.io.IOException;
import java.net.Authenticator;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author TheRedCat
 * @since 1.2
 * A client which access Mojang public rest API.
 * <p>
 * Mojang web api documentation: @link https://wiki.vg/Mojang_API
 */
public class MojangAPIClient {

    private static final Pattern nicknamePattern = Pattern.compile("[a-zA-z0-9_]*");
    private final HttpClient client;

    /**
     * Creates new client.
     *
     * @param timeout Http connection timeout.
     */
    public MojangAPIClient(long timeout) {
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(timeout))
                .authenticator(Authenticator.getDefault())
                .build();
    }

    /**
     * Creates new client which connects using a proxy.
     *
     * @param timeout Http connection timeout.
     * @param proxy   Http proxy.
     */
    public MojangAPIClient(long timeout, ProxySelector proxy) {
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(timeout))
                .authenticator(Authenticator.getDefault())
                .proxy(proxy)
                .build();
    }

    private static boolean checkNickname(String s) {
        final int len = s.length();

        return len >= 3 && len <= 16 && nicknamePattern.matcher(s).matches();
    }

    private static HttpRequest request(String url) throws URISyntaxException {
        return HttpRequest.newBuilder(new URI(url)).build();
    }

    private static UUID UUIDFromString(String s) {
        return UUID.fromString(s.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
    }

    /**
     * Gets unique id of player who is using specified name now.
     *
     * @param currentNickname The name of player whose id we are looking for.
     * @return Unique id of player.
     * @throws IOException          if an I/O error occurs when sending or receiving http request.
     * @throws InterruptedException if the operation is interrupted.
     * @throws HttpException        if http request returns an status code unspecified by API.
     */
    public UUID getUUID(String currentNickname) throws IOException, InterruptedException, HttpException {
        Preconditions.checkArgument(checkNickname(currentNickname), "Player nickname needs to consist of 3-16 characters. The only characters allowed are numbers, letters and underscore.");
        try {
            HttpResponse<String> response = client.send(request("https://api.mojang.com/users/profiles/minecraft/" + currentNickname), HttpResponse.BodyHandlers.ofString());

            final int statusCode = response.statusCode();

            if (statusCode == 200)
                return UUIDFromString(new JsonParser().parse(response.body()).getAsJsonObject().get("id").getAsString());

            if (statusCode == 204)
                return null;

            throw new HttpException(statusCode);

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create valid URI with those parameters.", e);
        }
    }

}
