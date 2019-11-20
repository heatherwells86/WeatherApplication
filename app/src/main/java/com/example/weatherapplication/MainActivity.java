/*goal of project is to display an algorithm that pulls information from an API via http connection with JSON, and display the information gathered from JSON
in a format that is represented as strings with specific rules to how the information is displayed within the layout format.
 */

package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weatherapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
//correlates to represent the information as city name, search button to locate the city, and display information as a string result based upon the API JSON data
    TextView CityName;
    Button searchButton;
    TextView result;

/*representation of the class to create an asynchronous task to represent information as a string format to gather information about the input of the city name.  Initialization of the asynchronous task will allow background tasks within the protected
string to pull information from the http connection.
 */

    class Weather extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... address) {
/*try to pull information from the http url address to gather information to display about the city, return content if possible.  Catch IO exception as an error if information cannot be
displayed from the http connection successfully within this protected string
 */
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

// int data is displayed via input stream read to the application//
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();


                }
                return content;

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    //task as seen below to correlate to the layout for information to be displayed from the input task stream asynchronously//
    //code seen within this onCreate method as seen below connects to the background processes as seen above within the application//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    //the processes seen within the public void search conduct the search based upon the parameters stated as input, and the findviewbyid connects this information properly to the android xml document//


    public void search(View view) {
        CityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.result);


        //allocating cName as the representative string to gather information from the API accordingly//
        String cName = CityName.getText().toString();

//for the processes to gather information about the weather based upon the city name entered, the application will try to gather the information from the JSON object to display as an array//
        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+cName+"&appid=b6907d289e10d714a6e88b30761fae22").get();

            Log.i("contentData", content);

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");
            Log.i("weatherData", weatherData);
            JSONArray array = new JSONArray(weatherData);

            //show information displayed as a string in the formats as seen within these lines of code, show as double for visibility//
            String main = "";
            String description = "";
            String temperature = "";
            Double visibility;

//display the information as an array for the content data as in the code seen above.  show information compiled from the weatherPart within the loop.

            for (int i = 0; i < array.length(); i++) {
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }
//get temperature information and display within the "main" part of the weather description for the output of the results//
            JSONObject mainPart = new JSONObject (mainTemperature);
            temperature = mainPart.getString ("temp");

//parse the information from the json object to display the visibility information in the application//
            visibility = Double.parseDouble(jsonObject.getString("visibility"));

//Format to display the information from the results, further associates the string with a concatenation of the string results//

            Log.i("main", main);
            Log.i("description", description);
            Log.i("temperature" , temperature);

            String resultText = "Main : "+main+
                    "\nDescription : "+description+
                    "\nTemperature : "+temperature+
                    "\nVisibility : "+visibility;


            result.setText(resultText);
//results are displayed if successful, the catch is implemented if there is an issue//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

