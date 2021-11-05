
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import org.json.JSONArray;

public class App {

    public class AppointmentInfo {
        int doctorId;
        int personId;
        String appointmentTime;
        boolean isNewPatientAppointment;
    }

    public class AppointmentInfoRequest extends AppointmentInfo {
        int requestId;
    }

    public class AppointmentRequest {
        int requestId;
        int personId;
        String[] preferredDays = {};
        String[] preferredDocs = {};
        boolean isNew;
    }

    // private static HttpURLConnection connection;

    public static void main(String[] args) throws Exception {
        // Fetch initial start endpoint.
        fetchStart();
        System.out.println("fetchStartSuccessful");

        // Get list of current appointments and store it in an object
        // TODO figure out how to store it as a type of AppointmentList
        ArrayList<Object> scheduledAppointments = ArrayUtil.convert(fetchInitialSchedule());
        System.out.println(scheduledAppointments);

        // Do-While loop start
        //// TODO Get next appointment

        //// TODO Iterate by date/hour and doctor to find the first available date
        //// closest to requested date

        //// TODO when time is found, call post request to post apopintment to api
        //// endpoint.

        // end Do-While loop once api returns a status code of 204

        // Get final list of apppointmets
        System.out.println(fetchStop());
        System.out.println("fetchStopSuccessful");
    }

    // Calls the start endpoint, throws an exception if there's an issue with the
    // api call.
    public static void fetchStart() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
                "http://scheduling-interview-2021-265534043.us-west-2.elb.amazonaws.com/api/Scheduling/Start?token=ac30b395-491e-4468-8d41-b30edd32ae89"))
                .POST(HttpRequest.BodyPublishers.ofString((""))).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }

    // Posts to stop endpoint and returns a JSONArray of the final schedule
    public static JSONArray fetchStop() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
                "http://scheduling-interview-2021-265534043.us-west-2.elb.amazonaws.com/api/Scheduling/Stop?token=ac30b395-491e-4468-8d41-b30edd32ae89"))
                .POST(HttpRequest.BodyPublishers.ofString((""))).build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
                .thenApply(App::parse).join();
    }

    // Fetches initail schedule and returns a JSONArray of the inital schedule
    public static JSONArray fetchInitialSchedule() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
                "http://scheduling-interview-2021-265534043.us-west-2.elb.amazonaws.com/api/Scheduling/Schedule?token=ac30b395-491e-4468-8d41-b30edd32ae89"))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
                .thenApply(App::parse).join();
    }

    // Converts response object to JSON
    public static JSONArray parse(String responseBody) {
        JSONArray appointments = new JSONArray(responseBody);
        return appointments;
    }
}
